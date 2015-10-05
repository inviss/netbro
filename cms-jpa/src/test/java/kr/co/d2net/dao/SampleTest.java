package kr.co.d2net.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dao.api.Repository;
import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dao.support.SpecificationBuilder;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.TrsTbl;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class SampleTest {
	private static Repository repository;
	private static EntityManagerFactory factory;
	private static EntityManager em;
	private static EntityTransaction tx;

	private static Map<String, Object> props = new HashMap<String, Object>();

	protected EntityManager getEntityManager() {
		if (em == null) {
			throw new NullPointerException("entityManager must be set before executing any operations");
		}
		return em;
	}

	@BeforeClass
	public static void before() {
		factory = Persistence.createEntityManagerFactory("netbro_hsql");
		em = factory.createEntityManager();
		repository = new JpaRepository(em);
		tx = em.getTransaction();
		tx.begin();
		createCategory("aaa", 0, "1");
		createCategory("bbb", 0, "2");
		createCategory("ccc", 0, "3");

		createSampleTest("aaaa", 0L, "4");
		createSampleTest("cccc", 0L, "6");
		createSampleTest("bbbb", 0L, "5");
		createSampleTest("dddd", 0L, "7");

		tx.commit();
		props.put("javax.persistence.cache.storeMode", "REFRESH");
	}

	private static void createCategory(String cateNm, Integer depth, String node) {
		CategoryTbl categoryTbl = new CategoryTbl();
		categoryTbl.setCategoryNm(cateNm);
		categoryTbl.setDepth(depth);
		categoryTbl.setNodes(node);
		repository.save(categoryTbl);
	}

	private static void createSampleTest(String cateNm, Long depth, String node) {
		TrsTbl trsTbl = new TrsTbl();
		trsTbl.setBusiPartnerId(depth);
		trsTbl.setDeviceId(cateNm);
		repository.save(trsTbl);
	}



	@Test
	@Ignore
	public void test(){

		//3
		Specification<TrsTbl> depth1 = SpecificationBuilder.forProperty("busiPartnerId").equal(0).build();
		//1
		Specification<TrsTbl> depth2 = SpecificationBuilder.forProperty("busiPartnerId").like("1%").build();
		//3
		Specification<TrsTbl> depth3 = SpecificationBuilder.forProperty("busiPartnerId").equal(0).build();
		//		List<CategoryTbl> categoryTbls = repository.find(CategoryTbl.class, depth1).list();
		List<TrsTbl> trsTbls1 = repository.find(TrsTbl.class,depth1).descending("deviceId").list();
		//List<TrsTbl> trsTbls1 = repository.find(TrsTbl.class,depth1).list();
		List<TrsTbl> trsTbls2 = repository.find(TrsTbl.class,depth2).list();

		Specification<TrsTbl> depth4 = SpecificationBuilder.forProperty("busiPartnerId").equal(0).build();


		System.out.println("depth4 : " + depth4);


		Specification<TrsTbl> depth5 = SpecificationBuilder.forProperty("busiPartnerId").equal(0).build();

		Specification<TrsTbl> depth66 = SpecificationBuilder.forProperty("busiPartnerId").equal(0).and().equal("TC1").build();
		List<TrsTbl> trsTbls22 = repository.find(TrsTbl.class,depth66).list();
		System.out.println("trsTbls22 : " + trsTbls22.size());
		//		List<TrsTbl> trsTbls3 = repository.find(TrsTbl.class,depth4).list();
		//		System.out.println("trsTbls3 : " + trsTbls3.size());

		List<TrsTbl> trsTbls4 = repository.find(TrsTbl.class,depth5).list();
		System.out.println("trsTbls4 : " + trsTbls4.size());

		depth1.toString();


		SpecificationResult<TrsTbl> a4 =  repository.find(TrsTbl.class,depth1).size(555);
		List<TrsTbl>a44 = a4.list();
		System.out.println("a4.list().size() : " +  a4.list().size());

		for(TrsTbl a444 : a44){
			System.out.println("getDeviceId : " + a444.getDeviceId());
			System.out.println("getBusiPartnerId : " + a444.getBusiPartnerId());

		}


		TrsTbl tbl3 = new TrsTbl();

		tbl3 = repository.find(TrsTbl.class,depth1).single();
		System.out.println("tbl3 : " + tbl3.getSeq());

		//Assert.assertEquals(3, repository.count(TrsTbl.class, depth1));

		//레파지토리 순번에 따라 find함.
		//		TrsTbl trsTbl = repository.find(TrsTbl.class,4L);
		//		System.out.println("trsTbl : " + trsTbl.getDeviceId());


		for(TrsTbl t : trsTbls1){
			System.out.println("tbl.getDeviceId() : " + t.getDeviceId());
			System.out.println("tbl.getDeviceId() : " + t.getBusiPartnerId());
		}


		//System.out.println(trsTbls3.size());
		//		for(TrsTbl tbl : trsTbls3){
		//			System.out.println("tbl.getDeviceId() : " + tbl.getDeviceId());
		//		}
		//		


		//		Specification<CategoryTbl> depth0 = SpecificationBuilder.forProperty("depth").equal(0).build();


		//		Specification<CategoryTbl> depth0 = SpecificationBuilder.forProperty("depth").equal(0).build();
		//		Assert.assertEquals(3, repository.count(CategoryTbl.class, depth0));
		//		List<CategoryTbl> categoryTbls = repository.find(CategoryTbl.class, depth0).list();
		//		for(CategoryTbl categoryTbl : categoryTbls) {
		//			System.out.println("findCategories >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
		//		}



	}
	//
	//	@Test
	//	public void findCategories() {
	//		Specification<CategoryTbl> depth0 = SpecificationBuilder.forProperty("depth").equal(0).build();
	//		Assert.assertEquals(3, repository.count(CategoryTbl.class, depth0));
	//		List<CategoryTbl> categoryTbls = repository.find(CategoryTbl.class, depth0).list();
	//		for(CategoryTbl categoryTbl : categoryTbls) {
	//			System.out.println("findCategories >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
	//		}
	//	}
	//
	//	@Test
	//	public void likeCategories1() {
	//		Specification<CategoryTbl> like1 = SpecificationBuilder.forProperty("nodes").like("1%").build();
	//		Assert.assertEquals(1, repository.count(CategoryTbl.class, like1));
	//		List<CategoryTbl> categoryTbls = repository.find(CategoryTbl.class, like1).list();
	//		for(CategoryTbl categoryTbl : categoryTbls) {
	//			System.out.println("likeCategories1 >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
	//		}
	//	}
	//
	@Test
	@Ignore
	public void getCategory1() {
		CategoryTbl categoryTbl = repository.find(CategoryTbl.class, 2);
		System.out.println("getCategory1 >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
	}

	//	@Test
	//	public void likeCategories2() {
	//		Specification<CategoryTbl> like2 = SpecificationBuilder.forProperty("nodes").like("2%").build();
	//		List<CategoryTbl> categoryTbls = repository.find(CategoryTbl.class, like2).list();
	//		for(CategoryTbl categoryTbl : categoryTbls) {
	//			System.out.println("likeCategories2 >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
	//		}
	//		System.out.println("===============================================================================");
	//		List<CategoryTbl> categoryTbls2 = repository.find(CategoryTbl.class, like2).list();
	//		for(CategoryTbl categoryTbl : categoryTbls2) {
	//			System.out.println("likeCategories2 >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
	//		}
	//	}
	//	
	//	@Test
	//	public void insertAfterGet4() {
	//		createCategory("ddd", 0, "4");
	//		CategoryTbl categoryTbl = repository.find(CategoryTbl.class, 4);
	//		System.out.println("insertAfterGet4 >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
	//		
	//		System.out.println("===============================================================================");
	//		Specification<CategoryTbl> like2 = SpecificationBuilder.forProperty("nodes").like("2%").build();
	//		List<CategoryTbl> categoryTbls2 = repository.find(CategoryTbl.class, like2).list();
	//		for(CategoryTbl categoryTbl2 : categoryTbls2) {
	//			System.out.println("insertAfterGet4 list >>>> category_id: "+categoryTbl2.getCategoryId()+", category_nm: "+categoryTbl2.getCategoryNm());
	//		}
	//	}


	@SuppressWarnings("deprecation")
	@Test
	@Ignore
	public void test2(){

		Specification<TrsTbl> depth1 = SpecificationBuilder.forProperty("busiPartnerId").equal(0).or().equal(1).build();
		List<TrsTbl> trsTbls22 = repository.find(TrsTbl.class,depth1).ascending("deviceId").list();

		System.out.println("trsTbls22 : " + trsTbls22.size());

		for(TrsTbl t : trsTbls22){
			System.out.println("t.getDeviceId() : " + t.getDeviceId());
		}

	}

	@Test
	@Ignore
	public void findWithOp() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);


		String[] codeFields = {"deviceId"};
		String[] ctiFields = {"seq"};


		Selection[] s = new Selection[codeFields.length + ctiFields.length];


		Root<TrsTbl> root = cq.from(TrsTbl.class);
		Root<TraTbl> root1 = cq.from(TraTbl.class);


		int i=0;

		//cq.multiselect(test1,test2);
		for(int j=0; j<codeFields.length; j++) {
			s[i] = root.get(codeFields[j]);
			i++;
		}

		for(int j=0; j<ctiFields.length; j++) {
			s[i] = root1.get(ctiFields[j]);
			i++;
		}

		CriteriaQuery<Object[]> select = cq.select(cb.array(s));

		//System.out.println("aaaa : " + cq.);
		TypedQuery<Object[]> typedQuery = em.createQuery(select);
		typedQuery.getResultList();




	}

	@Test
	@Ignore
	public void test222(){

		String[] ctFields = {"ctId","aspRtoCd","brdDd","categoryId","cont","ctCla","ctId","ctLeng","ctNm","ctTyp","dataStatCd","delDd"
				,"duration","episodeId","keyWords","kfrmPath","lockStatcd","rpimgKfrmSeq","spcInfo","vdQlty","ristClfCd"};

		String[] codeFields = {"sclNm"};

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<ContentsTbl> from = criteriaQuery.from(ContentsTbl.class);
		Root<CodeTbl> code = criteriaQuery.from(CodeTbl.class);

		//criteriaQuery.where(ObjectLikeSpecifications.contentFilterSearch(criteriaBuilder,criteriaQuery,from,code,ctId));

		Selection[] s = new Selection[ctFields.length + codeFields.length];

		int i=0;
		for(int j=0; j<ctFields.length; j++) {
			s[i] = from.get(ctFields[j]);
			i++;
		}

		for(int j=0; j<codeFields.length; j++) {
			s[i] = code.get(codeFields[j]);
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				);


		//쿼리 캐시를 적용한다
		TypedQuery<Object[]> typedQuery = em.createQuery(select).setHint("org.hibernate.cacheable", true);

		List<Object[]> list2 = typedQuery.getResultList();


		//		  TypedQuery<TrsTbl> query = em.createNamedQuery("Country.findAll", TrsTbl.class);		 
		//		  
		//		  query.getResultList();

	}



	@Test
	@Ignore
	public void equal(){

		Specification<TrsTbl> specification = SpecificationBuilder.forProperty("busiPartnerId").equal(0).build();

		List<TrsTbl> trsTbls = repository.find(TrsTbl.class,specification).descending("deviceId").list();

		for(TrsTbl tbl : trsTbls){
			System.out.println("tbl : " + tbl.getDeviceId());
		}

	}


	@Test
	@Ignore
	public void like(){

		Specification<TrsTbl> specification = SpecificationBuilder.forProperty("busiPartnerId").like("0%").build();

		List<TrsTbl> trsTbls = repository.find(TrsTbl.class,specification).descending("deviceId").list();

		for(TrsTbl tbl : trsTbls){
			System.out.println("tbl : " + tbl.getDeviceId());
		}

	}


	@Test
	@Ignore
	public void greaterThan(){

		Specification<TrsTbl> specification = SpecificationBuilder.forProperty("busiPartnerId").greaterThanOrEqualTo(0).build();

		List<TrsTbl> trsTbls = repository.find(TrsTbl.class,specification).descending("deviceId").list();

		for(TrsTbl tbl : trsTbls){
			System.out.println("tbl : " + tbl.getDeviceId());
		}

	}



	@Test
	@Ignore
	public void isNotNull(){

		Specification<TrsTbl> specification = SpecificationBuilder.forProperty("busiPartnerId").isNotNull().build();

		List<TrsTbl> trsTbls = repository.find(TrsTbl.class,specification).descending("deviceId").list();

		for(TrsTbl tbl : trsTbls){
			System.out.println("tbl : " + tbl.getDeviceId());
		}

	}
	
	@Test
	public void or(){

		Specification<TrsTbl> specification = SpecificationBuilder.forProperty("busiPartnerId").isNotNull().or().greaterThanOrEqualTo(0).build();

		List<TrsTbl> trsTbls = repository.find(TrsTbl.class,specification).descending("deviceId").list();

		for(TrsTbl tbl : trsTbls){
			System.out.println("tbl : " + tbl.getDeviceId());
		}

	}
	
	
	


}
