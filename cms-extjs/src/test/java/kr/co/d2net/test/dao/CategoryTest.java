package kr.co.d2net.test.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dao.CategoryDao;
import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Archive;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.dto.vo.Content;
import kr.co.d2net.dto.vo.Download;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.ArchiveServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.DownloadServices;
import kr.co.d2net.utils.ObjectUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class CategoryTest extends BaseDaoConfig {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private ArchiveServices archiveServices;
	@Autowired
	private CategoryServices categoryServices;
	@Autowired
	private CategoryDao categoryDao;
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private DownloadServices downloadServices;
	@Ignore
	@Test
	public void cacheTest() {

		Statistics stats = sessionFactory.getStatistics();
		System.out.println("Stats enabled="+stats.isStatisticsEnabled());
		stats.setStatisticsEnabled(true);
		System.out.println("Stats enabled="+stats.isStatisticsEnabled());

		Session session = sessionFactory.openSession();
		Session otherSession = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Transaction otherTransaction = otherSession.beginTransaction();

		printStats(stats, 0);

		CategoryTbl categoryTbl = (CategoryTbl) session.load(CategoryTbl.class, 1);
		printData(categoryTbl, stats, 1);

		categoryTbl = (CategoryTbl) session.load(CategoryTbl.class, 1);
		printData(categoryTbl, stats, 2);

		// clear first level cache, so that second level cache is used
		session.evict(categoryTbl);

		categoryTbl = (CategoryTbl) session.load(CategoryTbl.class, 1);
		printData(categoryTbl, stats, 3);

		categoryTbl = (CategoryTbl) session.load(CategoryTbl.class, 3);
		printData(categoryTbl, stats, 4);

		categoryTbl = (CategoryTbl) otherSession.load(CategoryTbl.class, 1);
		printData(categoryTbl, stats, 5);

		// Release resources
		transaction.commit();
		otherTransaction.commit();
		sessionFactory.close();
	}

	@Ignore
	@Test
	public void archiveList() {
		Search search = new Search();
		try {

			search.setPageNo(1);
			search.setKeyword("");
			search.setCategoryId(0);
			List<Download> infos = downloadServices.findDownloadList(search);
			logger.debug(""+infos.size());
			for(Download info : infos){
				logger.debug(info.getEpisodeNm());
				logger.debug(info.getWorkStatCd());
				logger.debug(info.getCtNm());
			}

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void printStats(Statistics stats, int i) {
		System.out.println("***** "+i+"*****");
		System.out.println("Fetch Count= "+stats.getEntityFetchCount());
		System.out.println("Second Level Hit Count="+stats.getSecondLevelCacheHitCount());
		System.out.println("Second Level Miss Count="+stats.getSecondLevelCacheMissCount());
		System.out.println("Second Level Put Count="+stats.getSecondLevelCachePutCount());
	}

	private void printData(CategoryTbl categoryTbl, Statistics stats, int count) {
		System.out.println(count+":: Name="+categoryTbl.getCategoryNm());
		printStats(stats, count);
	}

	@Ignore
	@Test
	public void test(){


		long ctId= 487;
		String[] ctFields = {"ctId","aspRtoCd","brdDd","categoryId","cont","ctCla","ctId","ctLeng","ctNm","ctTyp","dataStatCd","delDd"
				,"duration","episodeId","keyWords","kfrmPath","lockStatcd","rpimgKfrmSeq","spcInfo","vdQlty","ristClfCd"};

		String[] codeFields = {"sclNm"};
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<ContentsTbl> from = criteriaQuery.from(ContentsTbl.class);
		Root<CodeTbl> code = criteriaQuery.from(CodeTbl.class);

		criteriaQuery.where(ObjectLikeSpecifications.contentFilterSearch(criteriaBuilder,criteriaQuery,from,code,ctId));

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

		TypedQuery<Object[]> typedQuery = em.createQuery(select).setHint("org.hibernate.cacheable", true);

		List<Object[]> list2 = typedQuery.getResultList();

		Content content = new Content();
		for(Object[] list : list2) {

			i=0;
			for(int j=0; j<ctFields.length; j++) {
				ObjectUtils.setProperty(content, ctFields[j], list[i]);
				i++;
			}


			for(int j=0; j<codeFields.length; j++) {
				ObjectUtils.setProperty(content, "ristClfNm", list[i]);

				i++;
			}
		}

		logger.debug("content.getCtNm()"  + content.getCtNm());
		logger.debug("content.getRistClfNm()"  + content.getRistClfNm());
		//return content;

	}


	@Test
	public void orderChange(){
		Category newCategory = new Category();

		newCategory.setCategoryIds("1,45,6,82,83,75,77,76,3,4,10,9,11,2,5,7,8,100,101,102,104,105,103,106");
		newCategory.setParentsIds("total,1,1,total,82,total,75,75,total,3,4,4,4,total,2,total,7,total,total,101,total,104,total,total");
		newCategory.setDepths("1,2,2,1,2,1,2,2,1,2,3,3,3,1,2,1,2,1,1,2,1,2,1,1");

		String[] categoryIds = newCategory.getCategoryIds().split(",");
		String[] getParentsIds = newCategory.getParentsIds().split(",");
		String[] getDepths = newCategory.getDepths().split(",");


		List<Category> categorys = new ArrayList<Category>();

		//배열에 담은 카테고리 정보를 list로 변환하여 담는다.
		for(int i = 0; i < categoryIds.length; i++){

			Category category = new Category();
			category.setCategoryId(Integer.parseInt(categoryIds[i]));
			//extjs의 경우 root가 total이므로 null으로 치환해준다.
			if(getParentsIds[i].equals("total")){
				category.setParents(null);
			}else{
				category.setParents(Integer.parseInt(getParentsIds[i]));
			}
			
			category.setDepth(Integer.parseInt(getDepths[i]));

			categorys.add(category);

		}

		categoryServices.updateChangeOrder(categorys);


	}
}
