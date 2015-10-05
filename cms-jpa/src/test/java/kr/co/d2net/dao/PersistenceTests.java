package kr.co.d2net.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.Repository;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dao.support.SpecificationBuilder;
import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.UseEnum;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.dto.vo.Content;
import kr.co.d2net.dto.vo.ContentsInst;
import kr.co.d2net.dto.vo.Disuse;
import kr.co.d2net.dto.vo.Episode;
import kr.co.d2net.dto.vo.Notice;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Segment;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.CategoryService;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentService;
import kr.co.d2net.service.ContentsInstService;
import kr.co.d2net.service.ContentsModService;
import kr.co.d2net.service.DisuseService;
import kr.co.d2net.service.EpisodeService;
import kr.co.d2net.service.NoticeService;
import kr.co.d2net.service.SegmentService;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PersistenceTests extends BaseDaoConfig{

	@Autowired
	private EpisodeService episodeService;
	@Autowired
	private SegmentService segmentService;
	@Autowired
	private ContentsModService contentsModService;
	@Autowired
	private CategoryServices categoryServices;
	@Autowired
	private ContentsInstService contentsInstService;
	@Autowired
	private DisuseService disuseService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private ContentService contentService;
	@Autowired
	private CategoryService categoryService; 
	
	@Autowired
	private CategoryDao categoryDao; 
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private EntityManagerFactory factory;
	private EntityManager em;
	private EntityTransaction tx;
	private Repository repository;
	
	/*
	@BeforeClass
	public static void before() {
		createCategory("aaa", 0, "1", 1);
		createCategory("bbb", 0, "2", 2);
		createCategory("ccc", 0, "3", 3);
	}
	*/
	
	private void createCategory(String cateNm, Integer depth, String node, Integer order) {
		CategoryTbl  category = new CategoryTbl();
		category.setCategoryNm(cateNm);
		category.setDepth(depth);
		 category.setNodes(node);
		category.setOrderNum(order);
		
		  try {
			//categoryService.insertCategory(category);
			categoryServices.saveCategory(category);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//  @Ignore
	@Test
	//@Transactional
	public void testOne() {
		//createCategory("SBS", 0, "1", 1);
	 //	createCategory("TVN", 0, "2", 2);
	 // 	createCategory("MBC", 0, "3", 3);
		 categoryTest();
	}
	
	@Ignore
	@Test
	public void findCategories() {
		factory = Persistence.createEntityManagerFactory("netbro_hsql");
		em = factory.createEntityManager();
		repository = new JpaRepository(em);
		
		Specification<CategoryTbl> depth0 = SpecificationBuilder.forProperty("depth").equal(0).build();
		Assert.assertEquals(3, repository.count(CategoryTbl.class, depth0));
		List<CategoryTbl> categoryTbls = repository.find(CategoryTbl.class, depth0).list();
		
		tx = em.getTransaction();
		tx.begin();
		for(CategoryTbl categoryTbl : categoryTbls) {
			System.out.println("findCategories >>>> category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm());
			
			EpisodeTbl episodeTbl = new EpisodeTbl();
			episodeTbl.setCategoryId(categoryTbl.getCategoryId());
			episodeTbl.setEpisodeId(1);
			episodeTbl.setEpisodeNm("aa_1");
			//episodeTbl.setCategoryTbl(categoryTbl);
			
			categoryTbl.addEpisodeTbl(episodeTbl);
			
			repository.update(categoryTbl);
			
			System.out.println("update category_id: "+categoryTbl.getCategoryId()+", category_nm: "+categoryTbl.getCategoryNm()+", depth: "+categoryTbl.getDepth());
		}
		tx.commit();
	}
	
	@Ignore
	@Test
	public void findContents() {
		ArchiveTbl archiveTbl = null;

		archiveTbl = repository.find(ArchiveTbl.class,"001");
		System.out.println("archiveTbl.getWorkStatCd() : " + archiveTbl.getWorkStatCd());
	}
	
	
	@Test
	@Ignore
	public void contentsInstSaveTest(){
		 ContentsInstTbl contentsInstTbl = new ContentsInstTbl();
			contentsInstTbl.setCtiFmt("101");
			contentsInstTbl.setUseYn(UseEnum.Y);
			contentsInstTbl.setBitRt("29.97");
			contentsInstTbl.setFlSz(1234l);
			contentsInstTbl.setOrgFileNm("1234");
		//	contentsInstTbl.setCtId(23l);
		//	contentsInstTbl.setCtiId(12l); 
			 
				try {
					contentsInstService.saveContentsInst(contentsInstTbl);
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
	}
	
	@Test
	@Ignore
	public void contentsInstDeleteTest(){
		 ContentsInstTbl contentsInstTbl = new ContentsInstTbl();
			 
		 	contentsInstTbl.setCtId(23l);
		
			 
				try {
					contentsInstService.deleteContentsInst(contentsInstTbl);
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
	}
	
	
	@Test
	@Ignore
	public void contentsInstGetTest(){
		 ContentsInst contentsInst = new ContentsInst();
		 contentsInst.setCtId(1l);
		 contentsInst.setCtiFmt("1");
			 
				try {
					Long count = contentsInstService.countContentsInst(contentsInst);
					System.out.println("count :  "+ count);
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
	}
	
	@Test
	@Ignore
	public void disuseGetTest(){
	 Search search = new Search();
				try {
					search.setCtId(3l);
					Disuse  disuses = disuseService.getDisuse(search);
					System.out.println("getCtNm :  "+disuses.getCtNm());
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			 
	}
	
	@Test
	@Ignore
	public void NoticeGetTest(){
	 Search search = new Search();
				try {
					search.setNoticeId(1l);
					List<Notice> notice = noticeService.findNoticeList(search);
					System.out.println("getCtNm :  "+notice.size());
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			 
	}
	
	@Test
	@Ignore
	public void ContentsModGetTest(){
	 Search search = new Search();
				try {
					search.setCtId(30l);
					List<Content> notice = contentsModService.findContentsModList(search);
					 System.out.println("getCtNm :  " );
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			 
	}
	
	@Test
	@Ignore
	public void SegMentGetTest(){
	 Search search = new Search();
				try {
					search.setEpisodeId(10);
					search.setCategoryId(10);
					List<Segment> notice = segmentService.findSegmentList(search);
					 System.out.println("getCtNm :  " );
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			 
	}
	
	@Test
	@Ignore
	public void episodeGetTest(){
	 Search search = new Search();
	 System.out.println("test 추가요망");
				try {
					 
					search.setCategoryId(10);
					search.list();
					 List<Episode> notice = episodeService.findEpisodeList(search);
					 System.out.println("getCtNm :  " );
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			 
	}
	
//	@Test
//	 @Ignore
	public void categoryTest(){ 
		Category category = new Category();
		category.setCategoryId(145); 
		try {
			String result = categoryService.deleteCategory(category);
			System.out.println("result : "+result);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
}
