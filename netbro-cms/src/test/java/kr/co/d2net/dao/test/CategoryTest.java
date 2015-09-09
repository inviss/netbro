package kr.co.d2net.dao.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;


import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.exception.RequestParamException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.CategoryServices;

public class CategoryTest extends BaseDaoConfig {

	@Autowired
	private CategoryServices categoryServices;
	@PersistenceContext
	private EntityManager em;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Ignore
	@Test
	public void findCategory() {
		try {
			Search search = new Search();
			List<CategoryTbl> categoryTbls = categoryServices.findMainCategory(search);
			for(CategoryTbl categoryTbl : categoryTbls) {
				System.out.println(categoryTbl.getCategoryNm());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void addAll() {
		try {

			//for(int i = 0; i < 20; i++){



			Set<CategoryTbl> categories = new HashSet<CategoryTbl>();


			CategoryTbl category = new CategoryTbl();

			categories.add(category);

			categoryServices.add(category);
			//}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	//@Ignore
	@Test
	public void findCategoryId() {

		try {

			CategoryTbl categoryTbl =   new CategoryTbl();
			categoryTbl.setCategoryId(1);
			categoryTbl = categoryServices.getCategoryObj(categoryTbl.getCategoryId());
			int result = categoryTbl.getNodes().indexOf(".");
			boolean test = true;
					
					if(test){
						Exception e = new Exception();
						throw e;
					}
			logger.debug("###############"+categoryTbl.getNodes().substring(0,result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	@SuppressWarnings("unchecked")
	public void findLastCateogrys() {
		//Query query = null;

		try {
			int query = em.createNamedQuery("update category_tbl set lft=lft+2 where lft > :lft").setParameter("lft", 21).executeUpdate();
			System.out.println("asddddddddddddddddddddddddd");
			//query.setParameter("lft", 21);
			//query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Ignore
	@Test
	public void findCategoryTree() {

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<CategoryTbl> node = criteriaQuery.from(CategoryTbl.class);
		Root<CategoryTbl> parent = criteriaQuery.from(CategoryTbl.class);
		Root<CategoryTbl> parent2 = criteriaQuery.from(CategoryTbl.class);

		CriteriaQuery<Object[]> criteriaQuery2 = criteriaBuilder.createQuery(Object[].class);
		Root<CategoryTbl> cate1 = criteriaQuery2.from(CategoryTbl.class);
		Root<CategoryTbl> cate2 = criteriaQuery2.from(CategoryTbl.class);

		//criteriaQuery.multiselect(node.get("categoryId"),node.get("categoryNm"),(criteriaBuilder.count(parent.get("categoryId"))).alias("depth"),node.get("finalYn"),node.get("preParent"),node.get("lft"),node.get("rgt"));
		criteriaQuery2.multiselect(cate1.get("categoryId"),cate1.get("categoryNm"),(criteriaBuilder.count(cate2.get("categoryId"))).alias("depth"),cate1.get("finalYn"),cate1.get("preParent"));

		Path<Integer> cate1Lft = cate1.get("lft");
		Path<Integer> cate2Lft = cate2.get("lft");
		Path<Integer> cate2Rft = cate2.get("rgt");
		criteriaQuery2.where(criteriaBuilder.between(cate1Lft, cate2Lft, cate2Rft)
				,criteriaBuilder.and(criteriaBuilder.equal( cate1.get("useYn"), "Y"),criteriaBuilder.equal( cate2.get("useYn"), "Y")));
		criteriaQuery2.groupBy(cate1.get("categoryId"),cate1.get("categoryNm"),cate1.get("lft"),cate1.get("finalYn"),cate1.get("preParent"));
		criteriaQuery2.having(criteriaBuilder.equal(criteriaBuilder.count(cate2.get("categoryId")), 0));

		Path<Integer> nodeLft = node.get("lft");
		Path<Integer> parentLft = parent.get("lft");
		Path<Integer> parentRft = parent.get("rgt");
		Path<Integer> parent2Lft = parent2.get("lft");
		Path<Integer> parent2Rft = parent2.get("rgt");

		criteriaQuery.where(criteriaBuilder.between(nodeLft, parentLft, parentRft)
				,criteriaBuilder.between(nodeLft, parent2Lft, parent2Rft)
				,criteriaBuilder.and(criteriaBuilder.equal(parent2.get("categoryId"),node.get("categoryId"))
						,criteriaBuilder.equal(node.get("useYn"), "Y")
						,criteriaBuilder.equal(parent.get("useYn"), "Y")	));
		criteriaQuery.groupBy(node.get("categoryId")
				,node.get("categoryNm")
				//,sub.as("depth")
				,node.get("lft")
				,node.get("rgt")
				// ,sub.alias("parent")
				,node.get("finalYn")
				,node.get("preParent"));
		//System.out.println("criteriaQuery  :"+em.createQuery(criteriaQuery).getResultList());

		em.createQuery(criteriaQuery2).getResultList();




	}



	@Ignore
	@Test
	@SuppressWarnings("unchecked")
	public void insertNewCategory() {
		
		Category insertInfo = new Category();
		insertInfo.setType("NEXT");
		insertInfo.setCategoryNm("2뎁스 - 4");
		insertInfo.setCategoryId(76);
		
		try {
			categoryServices.addCategory(insertInfo);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		NewCategoryTbl info = null;
		NewCategoryTbl preInfo = null;
		String tempNodes="";
		List<NewCategoryTbl> infos = null;
		
		if(insertInfo.getCategoryId() != null){
		
			info = categoryServices.getNewCategoryObj(insertInfo.getCategoryId());
		
		}
		
		NewCategoryTbl newCategory = new NewCategoryTbl();
		//newCategory.setCategoryId(1);
		String tempNodse="";

		//info 정보가 null이 아니라면 기존 정보를 베이스로 카테고리 정보 생성
		if(info != null){
		 
			newCategory.setCategoryNm(insertInfo.getCategoryNm());
			
			//하위노드 추가이면 무조건 순번을 1로, 다음노드 추가이면 조회한 값기준으로 +1
			if(insertInfo.getType().equals("SUB")){
				 
				newCategory.setOrderNum(1);
				newCategory.setDepth(info.getDepth() + 1);
				newCategory.setPreParent(info.getCategoryId());

			}else{
				
				newCategory.setOrderNum(info.getOrderNum() + 1);
				newCategory.setDepth(info.getDepth());
				newCategory.setPreParent(info.getPreParent());

			}


			//1차저장
			newCategory = categoryServices.addNewCategory(newCategory);
			
			//1차저장 이후 생성된 카테고리 id로 node값 저장
			
			if(insertInfo.getType().equals("SUB")){
				
				tempNodse = info.getNodes()+"."+newCategory.getCategoryId();
			
			}else{
				
				if(info.getDepth() == 0 ){
				
					tempNodse = String.valueOf(newCategory.getCategoryId());
				
				}else{
					preInfo = categoryServices.getNewCategoryObj(info.getPreParent());
					logger.debug("############################preInfo  " +preInfo.getNodes() );
					tempNodse = preInfo.getNodes()+"."+newCategory.getCategoryId();
					info.setNodes(preInfo.getNodes());
				}
			
			}
			newCategory.setNodes(tempNodse);
			
			categoryServices.addNewCategory(newCategory);
			logger.debug("####################newCategory.getNodes()   " );
			//신규로 업데이트 된 이후 삽입된 곳 아래의 순번을 +1씩 업데이트 한다.
			infos = categoryServices.findOrderNumUpdateList(info.getNodes(), newCategory.getDepth(),newCategory.getOrderNum());
			
		}else{
		
			//root정보 생성
			
			newCategory.setCategoryNm(insertInfo.getCategoryNm());
			newCategory.setDepth(0);
			newCategory.setOrderNum(1);

			
			//1차저장
			newCategory = categoryServices.addNewCategory(newCategory);
			
			//1차저장 이후 생성된 카테고리 id로 node값 저장
			newCategory.setNodes(String.valueOf(newCategory.getCategoryId()));
			
			
			categoryServices.addNewCategory(newCategory);
			
			//신규로 업데이트 된 이후 삽입된 곳 아래의 순번을 +1씩 업데이트 한다.
			infos = categoryServices.findOrderNumUpdateList(null, newCategory.getDepth(),newCategory.getOrderNum());
			
		}
		
		
		logger.debug("####################I`M HERE infos.SIXZE "+infos.size());
		for(NewCategoryTbl updateInfo : infos){
			
			updateInfo.setOrderNum(updateInfo.getOrderNum() + 1);
			
			if(updateInfo.getCategoryId() != newCategory.getCategoryId()){
				categoryServices.addNewCategory(updateInfo);
			}
			
		}*/

	}

	


	@Ignore
	@Test
	@SuppressWarnings("unchecked")
	public void updateNewCategory() {
		
		
		Category insertInfo = new Category();
		insertInfo.setCategoryNm("1-1뎁스 1차");
		insertInfo.setCategoryId(38);
		insertInfo.setOrderNum(3);
		
		try {
			categoryServices.updateCategory(insertInfo);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		
		NewCategory insertInfo = new NewCategory();
		insertInfo.setCategoryNm("1-1뎁스 1차");
		insertInfo.setCategoryId(38);
		insertInfo.setOrderNum(5);

		int preOrderNum =0;
		boolean plusMinus = true;
		int startOrder =0;
		int endOrder =0;
		List<NewCategoryTbl> infos = null;
		NewCategoryTbl info = categoryServices.getNewCategoryObj(insertInfo.getCategoryId());
		NewCategoryTbl preParentsInfo = categoryServices.getNewCategoryObj(info.getPreParent());
		
		if(insertInfo.getOrderNum() != 0 && insertInfo.getOrderNum() != info.getOrderNum()){
			
			
			preOrderNum = info.getOrderNum();
			info.setOrderNum(insertInfo.getOrderNum());
			
			if(StringUtils.isNotBlank(insertInfo.getCategoryNm())){
				
				info.setCategoryNm(insertInfo.getCategoryNm());
				
			}
			
			categoryServices.addNewCategory(info);
			
			if(preOrderNum < insertInfo.getOrderNum()){
				startOrder = preOrderNum;
				endOrder = insertInfo.getOrderNum();
				plusMinus = false;
			}else{
				startOrder = insertInfo.getOrderNum();
				endOrder = preOrderNum; 
			}
			
			
				infos = categoryServices.findOrderNumUpdateListByBetWeen(preParentsInfo.getNodes(), info.getDepth(),startOrder,endOrder);
			
		
			logger.debug("###########preParentsInfo.getNodes()  "+preParentsInfo.getNodes());
			logger.debug("###########info.getDepth()  "+info.getDepth());
			logger.debug("###########insertInfo.getOrderNum()  "+insertInfo.getOrderNum());
			logger.debug("###########infos.size()  "+infos.size());
			for(NewCategoryTbl updateInfo : infos){
				logger.debug("###########updateInfo.getCategoryId()  "+updateInfo.getCategoryId());
				if(plusMinus){
					updateInfo.setOrderNum(updateInfo.getOrderNum() + 1);
				}else{
					updateInfo.setOrderNum(updateInfo.getOrderNum() - 1);
				}
				
				if(updateInfo.getCategoryId() != insertInfo.getCategoryId()){
					logger.debug("###########updateInfo.getOrderNum())  " +updateInfo.getOrderNum());
					categoryServices.addNewCategory(updateInfo);
				
				}
				
			}
			
		}else{
			
		info.setCategoryNm(insertInfo.getCategoryNm());
		
		categoryServices.addNewCategory(info);
		
		}
		
	*/ catch (RequestParamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}

	


	@Ignore
	@Test
	@SuppressWarnings("unchecked")
	public void deleteNewCategory() {
		
		Category insertInfo = new Category();
		
		insertInfo.setCategoryId(38);
		try {
			categoryServices.deleteNewCategory(insertInfo);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*
		
		NewCategory insertInfo = new NewCategory();
		
		insertInfo.setCategoryId(43);
		
		NewCategoryTbl info = categoryServices.getNewCategoryObj(insertInfo.getCategoryId());
		NewCategoryTbl preParentsInfo = categoryServices.getNewCategoryObj(info.getPreParent());
		List<NewCategoryTbl> infos = categoryServices.findSubNodesList(info.getNodes());
		
		if(infos.size() > 1){
			
			logger.debug("하위 노드가 존재합니다");
		
		}else{
			
			categoryServices.deleteNewCategory(info);
			
			List<NewCategoryTbl> updateInfos = categoryServices.findOrderNumUpdateList(preParentsInfo.getNodes(), info.getDepth(),info.getOrderNum());
			
			for(NewCategoryTbl updateInfo : updateInfos){
				
				updateInfo.setOrderNum(updateInfo.getOrderNum() - 1);
				
				categoryServices.addNewCategory(updateInfo);
				
			}
		
		}
		
		
	*/}
	
	
	@Ignore
	@Test
	@SuppressWarnings("unchecked")
	public void getNewCategory() {
		Search search = new Search();
		search.setCategoryId(0);
		search.setDepth(0);
		search.setPreParent(0);
		logger.debug("search.getDepth()       : "+search.getDepth());
		logger.debug("search.getCategoryId()  : "+search.getCategoryId());
		logger.debug("search.getPreParent()   :"+search.getPreParent());
		CategoryTbl info = new CategoryTbl();
	
		if(search.getCategoryId() != 0){
		try {
			info = categoryServices.getCategoryObj(search.getCategoryId());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else{
			info.setCategoryId(0);
			info.setDepth(-1);
			info.setPreParent(0);
		}
		logger.debug("info.getDepth()       : "+info.getDepth());
		logger.debug("info.getCategoryId()  : "+info.getCategoryId());
		logger.debug("info.getPreParent()   :"+info.getPreParent());
		List<CategoryTbl> TempCategorys;
		try {
			TempCategorys = categoryServices.findMainCategory(info, search);
			
			List<Category> categoryTbls = new ArrayList<Category>();
			logger.debug("size()   "+TempCategorys.size());

			for(CategoryTbl addEpisodeTbl : TempCategorys){
				
				Category newCategory = new Category();
				newCategory.setCategoryId(addEpisodeTbl.getCategoryId());
				newCategory.setCategoryNm(addEpisodeTbl.getCategoryNm());
				newCategory.setDepth(addEpisodeTbl.getDepth());
				newCategory.setNodes(addEpisodeTbl.getNodes());
				newCategory.setOrderNum(addEpisodeTbl.getOrderNum());
				newCategory.setPreParent(addEpisodeTbl.getPreParent());
				
				List<CategoryTbl> result = categoryServices.findSubNodesList(newCategory.getNodes());
				
				if(result.size() > 0){
					
					newCategory.setFinalYn("Y");
				
				}else{
					
					newCategory.setFinalYn("N");
					
				}
				logger.debug("newCategory.getFinalYn()  : " +newCategory.getFinalYn());
				categoryTbls.add(newCategory);
				
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
	
	
}
