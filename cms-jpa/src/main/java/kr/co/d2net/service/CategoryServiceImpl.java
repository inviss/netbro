package kr.co.d2net.service;

import java.util.Date;
import java.util.List;

import kr.co.d2net.dao.CategoryDao;
import kr.co.d2net.dao.ContentsDao;
import kr.co.d2net.dao.EpisodeDao;
import kr.co.d2net.dao.SegmentDao;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.UseEnum;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.dto.vo.CategoryTreeForExtJs;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("categoryService")
@Transactional(readOnly=true)
public class CategoryServiceImpl implements CategoryService {
	private final static Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	CategoryDao categoryDao;

	@Autowired
	EpisodeDao episodeDao;

	@Autowired
	SegmentDao segmentDao;

	@Autowired
	ContentsDao contentsDao;

	@Override
	@Transactional
	public void insertCategory(Category category) throws ServiceException {

		CategoryTbl categoryTbl = null;
		CategoryTbl newCategoryTbl = null;
		Search search = new Search();
		if(category.getCategoryId() != null){
			search.setCategoryId(category.getCategoryId());
			categoryTbl = categoryDao.getCategoryObj(search);
		} 

		if(categoryTbl != null ){
			newCategoryTbl = new CategoryTbl();
			// 다음노드
			if(category.getType().equals("NEXT")){
				newCategoryTbl.setDepth(categoryTbl.getDepth());
			}else{
				// 하위노드
				newCategoryTbl.setDepth(categoryTbl.getDepth()+1);
			}

			newCategoryTbl.setCategoryNm(category.getNewCategoryNm());
			newCategoryTbl.setOrderNum(categoryTbl.getOrderNum()+1);
			// 하위 OrderNum +1 변경 해야 함.
			categoryDao.updateCategoryOrderNumByNamedQuery(categoryTbl);
		}else{
			//root정보 생성
			newCategoryTbl = new CategoryTbl();

			newCategoryTbl.setCategoryNm(category.getNewCategoryNm());
			newCategoryTbl.setDepth(0);
			newCategoryTbl.setOrderNum(1);
			// 하위 OrderNum +1 변경 해야 함.
			categoryDao.updateCategoryOrderNumByNamedQuery(newCategoryTbl);
		}

		//1차저장
		CategoryTbl newCategory = categoryDao.save(newCategoryTbl);

		//1차저장 이후 생성된 카테고리 id로 node값 저장 root라면 자신의 id를 그이외의 경우에는 node의 값에 자신의 categoryId를 추가한다
		//카테고리id+"." 형태로 자신의 상위 노드를 적어놓는다.
		if(newCategoryTbl.getDepth() ==  0){
			newCategory.setNodes(String.valueOf(newCategoryTbl.getCategoryId())+".");
			newCategory.setGroupId(newCategoryTbl.getCategoryId());
		}else{
			newCategory.setNodes(categoryTbl.getNodes()+newCategoryTbl.getCategoryId()+".");
			String[] groupId = categoryTbl.getNodes().split("\\.");
			logger.debug("groupId[0]        :   "+newCategory.getNodes());
			newCategory.setGroupId(Integer.parseInt(groupId[0]));
		}

		newCategory.setUseYn(UseEnum.Y);
		categoryDao.save(newCategory);


	}

	@Override
	@Transactional
	public void updateCategory(Category category) throws ServiceException {
		Search search = new Search();
		search.setCategoryId(category.getCategoryId());

		CategoryTbl categoryTbl = categoryDao.getCategoryObj(search);

		categoryTbl.setCategoryNm(category.getCategoryNm());

		categoryDao.save(categoryTbl);
	}

	@Override
	@Transactional
	public void updateChangeOrder(List<Category> categorys)
			throws ServiceException {

		int orderNum = 1;
		CategoryTbl newInfo = new CategoryTbl();
		Search search = new Search();
		for(Category changeOrder : categorys){
			search.setCategoryId(changeOrder.getCategoryId());
			CategoryTbl info = categoryDao.getCategoryObj(search);
			newInfo.setCategoryNm(info.getCategoryNm()); 
			newInfo.setOrderNum(orderNum);
			newInfo.setPreParent(changeOrder.getParents());
			newInfo.setCategoryId(info.getCategoryId());
			newInfo.setDepth(changeOrder.getDepth()-1);
			newInfo.setUseYn(UseEnum.Y);
			//depth가 1이라면 자신의 id를 nodes컬럼에 입력하고
			//2이상이라면 자신의 부모categoryId의 node정보에 자신의id를 '.'을 추가하여 덧붙인다.
			if(changeOrder.getDepth() ==1){
				newInfo.setNodes(String.valueOf(changeOrder.getCategoryId()));
				newInfo.setGroupId(changeOrder.getCategoryId());
			}else{
				//부모카테고리의 nodes 정보를 조회한다.
				search.setCategoryId(changeOrder.getParents());
				CategoryTbl parentInfo = categoryDao.getCategoryObj(search); 
				int sub = parentInfo.getNodes().indexOf(".");

				newInfo.setNodes(parentInfo.getNodes()+"."+changeOrder.getCategoryId());

				//sub의 값이 -1이면 nodes값을 그대로 넣어주고 그렇지않다면 sub의 값만큼 잘라서 넣어준다.
				if(sub == -1){
					newInfo.setGroupId(Integer.parseInt(parentInfo.getNodes()));
				}else{
					String groupId = parentInfo.getNodes().substring(0, parentInfo.getNodes().indexOf("."));
					newInfo.setGroupId(Integer.parseInt(groupId));
				}

			}

			try{
				categoryDao.save(newInfo); 
			}catch(Exception e){
				logger.error("save err : "+e);

			}

			//순서를 1씩 증가시킨다.
			orderNum++;
		}



	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public String deleteCategory(Category category) throws ServiceException {

		Search search = new Search();
		search.setCategoryId(category.getCategoryId());
		//삭제하고자하는 카테고리의 정보를 조회한다.
		CategoryTbl categoryTbl = categoryDao.getCategoryObj(search);
		CategoryTbl preParentsInfo = new CategoryTbl();

		//삭제하고자하는 카테고리가  최상위 노드라면 자신을 부모 카테고리에 집어놓고 아니라면 부모노드의 정보를 조회한다
		if(categoryTbl != null) {
			if(categoryTbl.getPreParent() != null){
				search = new Search();
				search.setCategoryId(categoryTbl.getPreParent());
				preParentsInfo = categoryDao.getCategoryObj(search);
			}else{
				preParentsInfo = categoryTbl;
			}

			search = new Search();
			search.setNodes(categoryTbl.getNodes());

			List<CategoryTbl> infos = categoryDao.findCategories(search);


			//삭제하고자하는 카테고리의 하위 노드가 있는지 확인한다 하위 노드가 있다면 로직을 중단하고 나간다,
			 if(infos != null){
				if(infos.size() > 1){
					return "N";
				}else{
					//2014.07.15 삭제하고자하는 카테고리ID가 컨텐츠로 등록된 적이 없다면 에피소드까지 삭제 가능하도록 변경
					search = new Search();
					search.setCategoryId(categoryTbl.getCategoryId());
					List<EpisodeTbl> deleteEpisodes = episodeDao.findEpisodeInfoList(search);

					//검색된 에피소드의 정보를 모두 삭제 처리한다.
					if(deleteEpisodes.size() > 0){
						for(EpisodeTbl episode :  deleteEpisodes){
							episodeDao.delete(episode);
						}
					}

					//하위노드가 없다면 해당 카테고리를 삭제하고 동일 깊이에 있고
					categoryDao.delete(categoryTbl);

					return "Y";
				}
			} 
		}

		return "N";
	}

	@Override
	public Category getCategory(Search search) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> findCategoryForExtJs(Search search)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countCategory() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CategoryTreeForExtJs> makeExtJsJson(List<Category> categorys)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}


}