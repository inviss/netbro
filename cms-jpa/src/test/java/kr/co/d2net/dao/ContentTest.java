package kr.co.d2net.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import kr.co.d2net.dao.api.Repository;
import kr.co.d2net.dao.filter.CodeSpecifications;
import kr.co.d2net.dao.filter.ContentSpecifications;
import kr.co.d2net.dao.filter.EpisodeSpecifications;
import kr.co.d2net.dao.spec.AbstractSpecification;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dao.support.SpecificationBuilder;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CategoryTbl_;
import kr.co.d2net.dto.CodePK;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.EpisodePK;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.UseEnum;
import kr.co.d2net.dto.vo.Search;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class ContentTest {
	private static Repository repository;
	private static EntityManagerFactory factory;
	private static EntityManager em;
	private static EntityTransaction tx;

	private static Map<String, Object> props = new HashMap<String, Object>();

	@BeforeClass
	public static void before() {
		factory = Persistence.createEntityManagerFactory("netbro_hsql");
		em = factory.createEntityManager();
		repository = new JpaRepository(em);
		/*
		tx = em.getTransaction();
		tx.begin();
		createCategory("aaa", 0, "1");
		createCategory("bbb", 0, "2");
		createCategory("ccc", 0, "3");
		tx.commit();
		*/
		props.put("javax.persistence.cache.storeMode", "REFRESH");
	}

	@SuppressWarnings("unused")
	private static void createCategory(String cateNm, Integer depth, String node) {
		CategoryTbl categoryTbl = new CategoryTbl();
		categoryTbl.setCategoryNm(cateNm);
		categoryTbl.setDepth(depth);
		categoryTbl.setNodes(node);
		repository.save(categoryTbl);
	}

	@Ignore
	@Test
	public void findCategories() {
		Specification<CategoryTbl> depth0 = SpecificationBuilder.forProperty("depth").equal(0).build();
		Assert.assertEquals(3, repository.count(CategoryTbl.class, depth0));
		List<CategoryTbl> categoryTbls = repository.find(CategoryTbl.class, depth0).list();
		for(CategoryTbl categoryTbl : categoryTbls) {
			System.out.println("findCategories >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
		}
	}

	@Ignore
	@Test
	public void likeCategories1() {
		Specification<CategoryTbl> like1 = SpecificationBuilder.forProperty("nodes").like("1%").build();
		Assert.assertEquals(1, repository.count(CategoryTbl.class, like1));
		List<CategoryTbl> categoryTbls = repository.find(CategoryTbl.class, like1).list();
		for(CategoryTbl categoryTbl : categoryTbls) {
			System.out.println("likeCategories1 >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
		}
	}

	@Ignore
	@Test
	public void getCategory1() {
		CategoryTbl categoryTbl = repository.find(CategoryTbl.class, 1);
		System.out.println("getCategory1 >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
	}

	@Ignore
	@Test
	public void likeCategories2() {
		Specification<CategoryTbl> like2 = SpecificationBuilder.forProperty("nodes").like("2%").build();
		List<CategoryTbl> categoryTbls = repository.find(CategoryTbl.class, like2).list();
		for(CategoryTbl categoryTbl : categoryTbls) {
			System.out.println("likeCategories2 >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
		}
		System.out.println("===============================================================================");
		List<CategoryTbl> categoryTbls2 = repository.find(CategoryTbl.class, like2).list();
		for(CategoryTbl categoryTbl : categoryTbls2) {
			System.out.println("likeCategories2 >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
		}
	}

	@Ignore
	@Test
	public void findContentTest() {
		
		Search search = new Search();
		search.setCategoryId(83);
		//search.setEpisodeId(1);
		//search.setSegmentId(1);
		
		//search.setKeyword("SBS 영상      롯데");
		
		//search.setStartDt(new Date());
		//search.setEndDt(new Date());
		
		//search.setCtiFmt("2");
		
		//search.setCtCla("001");
		//search.setCtTyp("003");
		
		//search.setNodes("104");
		
		search.setDataStatCd("001");
		
		List<ContentsTbl> categoryTbls3 = repository.find(ContentsTbl.class, 
				ContentSpecifications.findContentsWithSubByParams(search)).list();
		
		for(ContentsTbl contentsTbl : categoryTbls3) {
			System.out.println("ctNm: "+contentsTbl.getCtNm());
		}
		
		search.count();

		long size = repository.count(ContentsTbl.class, ContentSpecifications.findContentsWithSubByParams(search));
		System.out.println("size: "+size);
	}
	
	@Ignore
	@Test
	public void findExistsTest() {
		repository.find(ContentsTbl.class, ContentSpecifications.findContentEqualsCategory(83)).list();
	}
	
	@Ignore
	@Test
	public void findCode() {
		Search search = new Search();
		search.setClfCd("CTYP");
		
		CodeTbl codeTbl = new CodeTbl();
		codeTbl.setClfCd("CTYP");
		codeTbl.setSclCd("000");
		codeTbl.setSclNm("test");
		codeTbl.setUseYn("Y");
		
		repository.saveAndFlush(codeTbl);
		
		CodePK pk = new CodePK("CTYP", "000");
		CodeTbl codeTbl2 = repository.find(CodeTbl.class, pk);
		System.out.println("pk clf_cd: "+codeTbl2.getClfCd()+", scl_cd: "+codeTbl2.getSclCd()+", use_yn: "+codeTbl2.getUseYn());
		
		List<CodeTbl> codeTbls = repository.find(CodeTbl.class, CodeSpecifications.findCodesOnlyByParams(search)).list();
		System.out.println("list size: "+codeTbls.size());
		for(CodeTbl codeTbl3 : codeTbls) {
			System.out.println("list clf_cd: "+codeTbl3.getClfCd());
		}
	}
	
	//@Ignore
	@Test
	public void findEpisode() {
		
		
		CategoryTbl category = new CategoryTbl();
		category.setCategoryNm("test category");
		repository.saveAndFlush(category);
		System.out.println("category_id: "+category.getCategoryId());
		
		EpisodeTbl episodeTbl = new EpisodeTbl();
		episodeTbl.setCategoryId(category.getCategoryId());
		episodeTbl.setEpisodeId(1);
		episodeTbl.setEpisodeNm("test2");
		episodeTbl.setUseYn(UseEnum.Y);
		
		repository.saveAndFlush(episodeTbl);
		
		EpisodePK pk = new EpisodePK(category.getCategoryId(), 1);
		EpisodeTbl episodeTbl2 = repository.find(EpisodeTbl.class, pk);
		System.out.println("pk category_id: "+episodeTbl2.getCategoryId()+", episode_id: "+episodeTbl2.getEpisodeId()+", episode_nm: "+episodeTbl2.getEpisodeNm());
		
		SegmentTbl segmentTbl = new SegmentTbl();
		segmentTbl.setCategoryId(category.getCategoryId());
		segmentTbl.setEpisodeId(1);
		segmentTbl.setSegmentId(1);
		segmentTbl.setSegmentNm("segment test1");
		segmentTbl.setUseYn(UseEnum.Y);
		
		repository.saveAndFlush(segmentTbl);
		
		ContentsTbl contentsTbl = new ContentsTbl();
		contentsTbl.setCtNm("ct_nm test");
		contentsTbl.setCategoryId(category.getCategoryId());
		contentsTbl.setEpisodeId(1);
		contentsTbl.setSegmentId(1);
		contentsTbl.setUseYn(UseEnum.N);
		repository.saveAndFlush(contentsTbl);
		
		Search search = new Search();
		search.setCategoryId(category.getCategoryId());
		search.setKeyword("test");
		
		List<EpisodeTbl> episodeTbls = repository.find(EpisodeTbl.class, EpisodeSpecifications.findEpisodeOnlyByParams(search)).list();
		System.out.println("list size: "+episodeTbls.size());
		for(EpisodeTbl episodeTbl3 : episodeTbls) {
			System.out.println("list episode_id: "+episodeTbl3.getEpisodeId());
		}
		
		ContentsTbl contentsTbl2 = repository.find(ContentsTbl.class, 1L);
		if(contentsTbl2.getUseYn() == UseEnum.N) {
			
		}
		System.out.println("get content: "+contentsTbl2.getCtNm()+", use_yn: "+contentsTbl2.getUseYn());
	}

	@SuppressWarnings({ "serial", "unused" })
	private Specification<CategoryTbl> findCategoryFilter() {
		return new AbstractSpecification<CategoryTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<CategoryTbl> root) {
				SetJoin<CategoryTbl, EpisodeTbl> join = root.join(CategoryTbl_.episodeTbl);
				return cb.equal(root.get(CategoryTbl_.categoryId), 10);
			}
		};
	}
}
