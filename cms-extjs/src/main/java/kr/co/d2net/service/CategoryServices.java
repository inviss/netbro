package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import kr.co.d2net.dao.CategoryDao;
import kr.co.d2net.dao.filter.CategorySpecifications;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CategoryTbl_;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.SegmentTbl.SegmentId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.dto.vo.CategoryTreeForExtJs;
import kr.co.d2net.exception.RequestParamException;
import kr.co.d2net.exception.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 카테고리 정보를 조회하기위한함수
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class CategoryServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private EpisodeServices episodeServices;

	@Autowired
	private SegmentServices segmentServices;
	@Autowired
	private MessageSource messageSource;

	/**
	 * category_tbl의 모든 정보를 조회해온다. 정렬순서는 controller에서 정의한 순서
	 * @return
	 */
	public List<CategoryTbl> findAll(String...  orders) throws ServiceException  {
		return categoryDao.findAll(new Sort(Sort.Direction.ASC, orders));
	}

	/**
	 * category_tbl의 총 컬럼수를 구한다.
	 * @return
	 */
	public Long count() throws ServiceException  {
		return categoryDao.count();
	}


	/**
	 * 카테고리 정보를 신규로 생성한다. 
	 * @param category
	 */
	@Modifying
	@Transactional
	public void add(CategoryTbl category) throws ServiceException  {
		categoryDao.save(category);
	}


	/**
	 * 변경된 카테고리 정보를 저장한다. 
	 * @param category
	 * @throws RequestParamException 
	 */
	@Modifying
	@Transactional
	public CategoryTbl updateCategory(Category category) throws ServiceException, RequestParamException  {

		int preOrderNum =0; // 이전 order 번호
		boolean plusMinus = true; // +-여부 판단, order 위치를 지정하는데 사용
		int startOrder =0; //노드 위치 변경 시작점 order
		int endOrder =0; // 노드 위치 변겅 종료점 order
		String message = "";//에러발생시 메세지는 변수
		CategoryTbl preParentsInfo = new CategoryTbl();
		List<CategoryTbl> infos = null;

		//변경할 카테고리의 메타정보를 가져온다.
		CategoryTbl info = getCategoryObj(category.getCategoryId());
		CategoryTbl afterInfo = new CategoryTbl();

		//만약 최상위 노드가 아니라면 부모 카테고리의 정보를 얻어온다.
		//최상위 노드라면 자신의 정보를 부모카테고리 정보로 입력한다.
		if(info != null){
		
			if(info.getPreParent() != null){
				preParentsInfo = getCategoryObj(info.getPreParent());
			}else{
				preParentsInfo = info;
			}

			//카테고리의 진행 방향 정보를 얻는다. up이면 기존 order에서 -1을 down이면 +1을 한다
			//노드 순서 변경점이 없다면 무시하고 넘어간다.
			if(StringUtils.isNotBlank(category.getDirection())){

				if(category.getDirection().equals("up")){
					category.setOrderNum(info.getOrderNum()-1);
				}else if(category.getDirection().equals("down")){
					category.setOrderNum(info.getOrderNum()+1);
				}
				
			}

			//변경하고자하는 정보가 카테고리의 위치정보이면 if문을 단순 명칭 변경이라면 else부분 로직을 수행한다.
			if(category.getOrderNum() != 0 && category.getOrderNum() != info.getOrderNum()){

				preOrderNum = info.getOrderNum();
				info.setOrderNum(category.getOrderNum());

				if(StringUtils.isNotBlank(category.getCategoryNm())){
					info.setCategoryNm(category.getCategoryNm());
				}

				//변경되는 순번정보를 저장한다.
				afterInfo = categoryDao.save(info);

				//변경하고자 하는 위치가 기존 위치보다 하위쪽이면 plusMinus의 값을 false로 변경한다.
				if(preOrderNum < category.getOrderNum()){
					startOrder = preOrderNum;
					endOrder = category.getOrderNum();
					plusMinus = false;
				}else{
					startOrder = category.getOrderNum();
					endOrder = preOrderNum; 
				}

				//최상의 노드가 아니라면 변경 해야하는 depth와 그범위를 조회한다.
				if(info.getPreParent() != null){
					try{
						infos = findOrderNumUpdateListByBetWeen(preParentsInfo.getNodes(), info.getDepth(),startOrder,endOrder);
					}catch(NullPointerException e){
						message = messageSource.getMessage("error.005",null,null);
						throw new RequestParamException("005",message,e);
					}
				}else{
					infos = findOrderNumUpdateListByBetWeen(null, info.getDepth(),startOrder,endOrder);
				}

				//조회된 결과를 가지고 나머지 노드의 순서를 +-1씩 해준다
				if(infos != null){
					for(CategoryTbl updateInfo : infos){
						if(updateInfo.getCategoryId() != category.getCategoryId()){
							if(plusMinus){
								updateInfo.setOrderNum(updateInfo.getOrderNum() + 1);
							}else{
								updateInfo.setOrderNum(updateInfo.getOrderNum() - 1);
							}
							categoryDao.save(updateInfo);
						}
					}
				}

			}else{
				if(StringUtils.isNotBlank(category.getCategoryNm())){
					info.setCategoryNm(category.getCategoryNm());
				}
				
				afterInfo =  categoryDao.save(info);
			}
		}
		return afterInfo;

	}

	/**
	 * 카테고리 정보를 신규로 등록한다.
	 * @param category
	 */
	@Modifying
	@Transactional
	public CategoryTbl addCategory(Category category) throws ServiceException  {

		CategoryTbl info = null;
		CategoryTbl preInfo = null;
		String message = "";//에러발생시 메세지는 변수
		String preNodes = "";//직전노드
		List<CategoryTbl> infos = null;
		CategoryTbl categoryTbl = new CategoryTbl();
		String tempNodse="";

		if(category.getCategoryId() != null){
			info = getCategoryObj(category.getCategoryId());
		}

		//info 정보가 null이 아니라면 기존 정보를 베이스로 카테고리 정보 생성
		if(info != null ){

			categoryTbl.setCategoryNm(category.getCategoryNm());

			//노드의 최상위 값을 찾아 넣는다.
			int result = info.getNodes().indexOf(".");

			if(result == -1){
				result = 1;
			}
			
			categoryTbl.setGroupId(Integer.parseInt(info.getNodes().substring(0,result)));

			//하위노드 추가이면 무조건 순번을 1로, 다음노드 추가이면 조회한 값기준으로 +1
			if(category.getType().equals("SUB")){
				categoryTbl.setOrderNum(1);
				categoryTbl.setDepth(info.getDepth() + 1);
				categoryTbl.setPreParent(info.getCategoryId());
			}else{
				if(info.getPreParent() != null){
					categoryTbl.setOrderNum(info.getOrderNum() + 1);
					categoryTbl.setDepth(info.getDepth());
					categoryTbl.setPreParent(info.getPreParent());
				}else{
					categoryTbl.setCategoryNm(category.getCategoryNm());
					categoryTbl.setDepth(0);
					categoryTbl.setOrderNum(info.getOrderNum() + 1);
				}
			}

			//1차저장
			categoryTbl = categoryDao.save(categoryTbl);

			//1차저장 이후 생성된 카테고리 id로 node값 저장
			if(category.getType().equals("SUB")){
				tempNodse = info.getNodes()+"."+categoryTbl.getCategoryId();
				categoryTbl.setGroupId(info.getCategoryId());
			}else{
				if(info.getDepth() == 0 ){
					tempNodse = String.valueOf(categoryTbl.getCategoryId());
					categoryTbl.setGroupId(categoryTbl.getCategoryId());
				}else{
					preInfo =  getCategoryObj(info.getPreParent());
					tempNodse = preInfo.getNodes()+"."+categoryTbl.getCategoryId();
					preNodes = preInfo.getNodes();
				}
			}

			categoryTbl.setNodes(tempNodse);
			categoryTbl = categoryDao.save(categoryTbl);

			//신규로 업데이트 된 이후 삽입된 곳 아래의 순번을 +1씩 업데이트 한다.
			infos = findOrderNumUpdateList(preNodes, categoryTbl.getDepth(),categoryTbl.getOrderNum());

		}else{

			//root정보 생성

			categoryTbl.setCategoryNm(category.getCategoryNm());
			categoryTbl.setDepth(0);
			categoryTbl.setOrderNum(1);


			//1차저장
			categoryTbl = categoryDao.save(categoryTbl);

			//1차저장 이후 생성된 카테고리 id로 node값 저장
			categoryTbl.setNodes(String.valueOf(categoryTbl.getCategoryId()));
			categoryTbl.setGroupId(categoryTbl.getCategoryId());

			categoryDao.save(categoryTbl);

			//신규로 업데이트 된 이후 삽입된 곳 아래의 순번을 +1씩 업데이트 한다.
			infos = findOrderNumUpdateList(null, categoryTbl.getDepth(),categoryTbl.getOrderNum());

		}

		if(infos != null){
			for(CategoryTbl updateInfo : infos){
				if(updateInfo.getCategoryId() != categoryTbl.getCategoryId()){
					updateInfo.setOrderNum(updateInfo.getOrderNum() + 1);
					categoryDao.save(updateInfo);
				}
			}
		}

		/*카테고리 생성후 에피소드 세그먼트 정보 모두를 생성해준다.
		 * */

		//카테고리가 생기면 자동으로 기본 회차 기본 세그먼트를 생성해준다.
		EpisodeTbl newEpisode = new EpisodeTbl();
		EpisodeId id = new EpisodeId();

		id.setCategoryId(categoryTbl.getCategoryId());
		Integer maxEpisodeId = episodeServices.MaxCount(id.getCategoryId());

		if(maxEpisodeId != null){
			id.setEpisodeId(maxEpisodeId+1);
		}else{
			id.setEpisodeId(1);
		}

		newEpisode.setId(id);
		newEpisode.setEpisodeNm("기본회차");
		newEpisode.setRegDt(new Date());
		newEpisode.setUseYn("Y");

		episodeServices.add(newEpisode);

		SegmentTbl segment = new SegmentTbl();
		SegmentId segid = new SegmentId();
		
		segid.setCategoryId(id.getCategoryId());
		segid.setEpisodeId(maxEpisodeId);

		if(segid.getEpisodeId() != null){
			segid.setEpisodeId(maxEpisodeId+1);
			segid.setSegmentId(1);
		}else{
			segid.setEpisodeId(1);
			segid.setSegmentId(1);
		}

		segment.setId(segid);
		segment.setSegmentNm("기본값");

		segmentServices.add(segment);

		return categoryTbl;
	}

	/**
	 * 카테고리id의 정보를 테이블에서 삭제한다.
	 * @param category
	 */
	@Modifying
	@Transactional
	public void delete(CategoryTbl category) throws ServiceException  {
		categoryDao.delete(category.getCategoryId());
	}

	@Modifying
	@Transactional
	public String deleteNewCategory(Category category) throws ServiceException  {

		String message="";

		//삭제하고자하는 카테고리의 정보를 조회한다.
		CategoryTbl info = getCategoryObj(category.getCategoryId());
		CategoryTbl preParentsInfo = new CategoryTbl();

		//삭제하고자하는 카테고리가  최상위 노드라면 자신을 부모 카테고리에 집어놓고 아니라면 부모노드의 정보를 조회한다
		if(info != null){
			if(info.getPreParent() != null){
				preParentsInfo = getCategoryObj(info.getPreParent());
			}else{
				preParentsInfo = info;
			}

			List<CategoryTbl> infos = findSubNodesList(info.getNodes());

			//삭제하고자하는 카테고리의 하위 노드가 있는지 확인한다 하위 노드가 있다면 로직을 중단하고 나간다,
			if(infos != null){
				if(infos.size() > 1){
					return "N";
				}else{
					//2014.07.15 삭제하고자하는 카테고리ID가 컨텐츠로 등록된 적이 없다면 에피소드까지 삭제 가능하도록 변경
					List<EpisodeTbl> deleteEpisodes = episodeServices.findEpisodeList(info.getCategoryId());

					//검색된 에피소드의 정보를 모두 삭제 처리한다.
					if(deleteEpisodes.size() > 0){
						for(EpisodeTbl episode :  deleteEpisodes){
							episodeServices.deleteEpisodeInfo(episode);
						}
					}
					
					//하위노드가 없다면 해당 카테고리를 삭제하고 동일 깊이에 있고, 삭제카테고리보다 order가 높은 노드들의 orderNum을 1씩 마이너스해준다.
					categoryDao.delete(info.getCategoryId());
					
					List<CategoryTbl> updateInfos;
					updateInfos = findOrderNumUpdateList(preParentsInfo.getNodes(), info.getDepth(),info.getOrderNum());

					if(updateInfos != null){
						for(CategoryTbl updateInfo : updateInfos){
							updateInfo.setOrderNum(updateInfo.getOrderNum() - 1);
							add(updateInfo);
							categoryDao.save(updateInfo);
						}
					}

					return "Y";
				}
			}
		}

		return "N";
	}

	
	/**
	 * 카테고리 정보를 조회하는 함수  depth가  0일 경우에는 최초 카테고리 정보만 조회한다.
	 * @param search
	 * @return
	 */
	public List<CategoryTbl> findMainCategory(Search search) throws ServiceException  {

		if(search.getDepth() == 0 && search.getCategoryId() == 0){// 최초 카테고리 전체조회
			return categoryDao.findAll(CategorySpecifications.DepthEqual(search.getDepth()),new Sort(Sort.Direction.ASC, "orderNum"));
		}else if(search.getDepth() != 0 && search.getCategoryId() != 0){//카테고리의 하위 카테고리 조회
			CategoryTbl newCategoryTbl = getCategoryObj(search.getCategoryId());
			return categoryDao.findAll(CategorySpecifications.findSubList(newCategoryTbl.getNodes(),search.getDepth()),new Sort(Sort.Direction.ASC, "orderNum"));
		}else{// 카테고리 아이디 조회
			List<CategoryTbl> infos = new ArrayList<CategoryTbl>();
			CategoryTbl info = getCategoryObj(search.getCategoryId());
			info.setEpisodeTbl(null);
			infos.add(info);

			return infos;
		}
	}	


	/**
	 * ExtJs에 요구한는 값에 맞춰서 조회하는 함수. 
	 * @param search
	 * @return List<CategoryTbl>
	 */
	public List<CategoryTbl> findMainCategoryForExtJs(CategoryTbl categoryTbl,Search search) throws ServiceException  {

		if(categoryTbl.getDepth() == 0 && search.getNode().equals("total")){// 최초 카테고리 전체조회
			return categoryDao.findAll(CategorySpecifications.DepthEqual(categoryTbl.getDepth()), new Sort(Sort.Direction.ASC, "orderNum"));

		}else if(categoryTbl.getCategoryId() != 0){//카테고리의 하위 카테고리 조회
			//조회하고자 하는 카테고리의 depth보다 +1해서 조회한다.
			categoryTbl.setDepth(categoryTbl.getDepth()+1);
			return categoryDao.findAll(CategorySpecifications.findSubList(categoryTbl.getNodes(),categoryTbl.getDepth()),new Sort(Sort.Direction.ASC, "orderNum"));
		}
		return null;
	}	

	/**
	 * 카테고리단일 건을 조회하는 함수
	 * @param categoryId
	 * @return
	 */
	public CategoryTbl getCategoryObj(Integer categoryId) throws ServiceException  {
		return categoryDao.findOne(categoryId);
	}


	/**
	 * 입력된 카테고리id를 부모키로 하는 모든 카테고리 정보를 조회한다.
	 * @param categoryId
	 * @return List<CategoryTbl>
	 */
	public List<CategoryTbl> findSubCategory(Integer categoryId, String... orders) throws ServiceException  {
		return categoryDao.findAll(CategorySpecifications.findSubList(categoryId), new Sort(Sort.Direction.ASC, orders));
	}


	/**
	 * nodes, depth, orderNum의 컬럼을 가지고
	 * @param categoryId
	 * @return List<CategoryTbl>
	 */
	public List<CategoryTbl> findOrderNumUpdateList(String nodes,Integer depth,Integer orderNum) throws ServiceException  {
		return categoryDao.findAll(CategorySpecifications.findListForOderNum(nodes,depth,orderNum));
	}


	/**
	 * nodes컬럼을 이용해 하위 카테고리 리스트를 조회한다.
	 * @param categoryId
	 * @return List<CategoryTbl>
	 */

	public List<CategoryTbl> findSubNodesList(String nodes) throws ServiceException  {
		return categoryDao.findAll(CategorySpecifications.NodesLike(nodes));
	}


	/**
	 * 카테고리id, depth를 이용해orderNum 사이의 리스트를 불러온다
	 * @param nodes
	 * @param depth
	 * @param orderNum
	 * @return List<NewCategoryTbl>
	 */

	public List<CategoryTbl> findOrderNumUpdateListByBetWeen(String nodes,Integer depth,Integer startOrder,Integer endOrder) throws ServiceException  {
		return categoryDao.findAll(CategorySpecifications.findListForOderNumByBetWeen(nodes,depth, startOrder, endOrder));
	}
	public List<CategoryTbl> findListByOrderNum(Integer startOrder) throws ServiceException  {
		 Sort sort = new Sort(new Order(Direction.ASC, "orderNum"));
		return categoryDao.findAll(CategorySpecifications.findListForOderNumByGreaterThan(startOrder),sort);
	}

	/**
	 * Extjs TreeStore에서 요구하는 형식으로 값을 치환한다.
	 * @param nodes
	 * @param depth
	 * @param orderNum
	 * @return List<NewCategoryTbl>
	 */

	public List<CategoryTreeForExtJs> makeExtJsJson(List<Category> categorys)   {
		List<CategoryTreeForExtJs> lists = new ArrayList<CategoryTreeForExtJs>();

		//단순  category bean에서 extjs가 요구하는 형식의 beans에 담에서 json으로 변환 시킨다
		if(categorys.size() != 0){
			for(Category info : categorys){
				CategoryTreeForExtJs store = new CategoryTreeForExtJs();
				store.setId(info.getCategoryId());
				
				if(logger.isDebugEnabled()){
					logger.debug("final yn   :   "+info.getFinalYn());
				}

				if(info.getFinalYn().equals("N")){
					store.setCls("folder");
					store.setLeaf(false);
				}else{
					store.setCls("file");
					store.setLeaf(true);
				}
				store.setText(info.getCategoryNm());
				lists.add(store);
			}
		}
		return lists;

	}
	
	
	
	
	
	/**
	 * 카테고리의 순서와 order방식 수정
	 * @param nodes
	 * @param depth
	 * @param orderNum
	 * @return List<NewCategoryTbl>
	 */
	@Modifying
	@Transactional
	public String updateChangeOrder(List<Category> categorys)   {
		 
		int orderNum = 1;
		CategoryTbl newInfo = new CategoryTbl();
		for(Category changeOrder : categorys){
			
			CategoryTbl info = categoryDao.findOne(changeOrder.getCategoryId());
			newInfo.setCategoryNm(info.getCategoryNm()); 
			newInfo.setOrderNum(orderNum);
			newInfo.setPreParent(changeOrder.getParents());
			newInfo.setCategoryId(info.getCategoryId());
			newInfo.setDepth(changeOrder.getDepth()-1);
			
			//depth가 1이라면 자신의 id를 nodes컬럼에 입력하고
			//2이상이라면 자신의 부모categoryId의 node정보에 자신의id를 '.'을 추가하여 덧붙인다.
			if(changeOrder.getDepth() ==1){
				newInfo.setNodes(String.valueOf(changeOrder.getCategoryId()));
				newInfo.setGroupId(changeOrder.getCategoryId());
			}else{
				CategoryTbl parentInfo = categoryDao.findOne(changeOrder.getParents()); 
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
				return "N";
			}
			 
			//순서를 1씩 증가시킨다.
			orderNum++;
		 }
		logger.debug("i`m here");
		return "Y";

	}
	
	/**
	 * 자식노드가 없는 노드의 순서변경
	 * @param nodes
	 * @param depth
	 * @param orderNum
	 * @return List<NewCategoryTbl>
	 */

	public String updateChangeByNoDE(List<Category> categorys)   {
	 
		return "";

	}
}
