package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dao.CategoryDao;
import kr.co.d2net.dao.ContentsDao;
import kr.co.d2net.dao.ContentsInstDao;
import kr.co.d2net.dao.ContentsModDao;
import kr.co.d2net.dao.filter.CategorySpecifications;
import kr.co.d2net.dao.filter.ContentsLikeSpecifications;
import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Content;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import kr.co.d2net.schedule.delection.BatchDeleteQueue;

/**
 * 컨텐츠 관련 정보를 조회하기위한 함수.
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class ContentsServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ContentsDao contentsDao;

	@Autowired
	private ContentsModDao contentsModDao;

	@Autowired
	private CategoryDao categoryDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private ContentsInstDao contentsInstDao;

	@Autowired
	private MessageSource messageSource;

	/**
	 * 삭제대상 컨텐츠 정보를 모두 조회한다
	 * */
	public List<ContentsTbl> findAll(Search search) throws ServiceException{
		return contentsDao.findAll(ContentsLikeSpecifications.deleteRangeSearch(search));
	}


	/**
	 * 신규저장을 하거나 기존에 정보가 존재한다면 update 성공한다면 키값을 리턴한다.
	 * @param contentsTbl
	 * @return ct_id
	 */
	@Modifying
	@Transactional
	public Long add(ContentsTbl contentsTbl) throws ServiceException{
		ContentsTbl tbl = contentsDao.save(contentsTbl);
		return tbl.getCtId();
	}

	/**
	 * 전체조회시 조회된 조회 갯수를 반환한다.(paging 용도)
	 * @param search
	 * @return list.size()
	 */
	public int countAllContents(Search search) throws ServiceException{

		String[] ctFields = {"ctId"};

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<ContentsTbl> from = criteriaQuery.from(ContentsTbl.class);
		Root<CodeTbl> code = criteriaQuery.from(CodeTbl.class);

		Join<ContentsTbl, ContentsInstTbl> inst = from.join("contentsInst", JoinType.INNER);
		Join<ContentsTbl, SegmentTbl> segment = from.join("segmentTbl", JoinType.INNER);
		Join<EpisodeTbl, SegmentTbl> episode = segment.join("episodeTbl", JoinType.INNER);
		Join<EpisodeTbl, CategoryTbl> category = episode.join("categoryTbl", JoinType.INNER);

		criteriaQuery.where(ObjectLikeSpecifications.contentsFilterSearch(criteriaBuilder,criteriaQuery,from,code,inst,segment,episode,category ,search));

		Selection[] s = new Selection[ctFields.length];

		int i=0;
		for(int j=0; j<ctFields.length; j++) {
			s[i] = from.get(ctFields[j]);
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				);
		TypedQuery<Object[]> typedQuery = em.createQuery(select).setHint("org.hibernate.cacheable", true);

		List<Object[]> list2 = typedQuery.getResultList();

		//list의 size를 반환하다.
		return list2.size();
	}



	/**
	 * contents_tbl 에서 ct_id 단일 건을 조회한다.(criteria 쿼리사용)
	 * @param ctId
	 * @return Content
	 */

	public Content getConObj(Long ctId) throws ServiceException{

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


		//쿼리 캐시를 적용한다
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
		return content;
	}

	/**
	 * contents_tbl 에서 ct_id 단일 건을 조회한다.(jpa기능사용)
	 * @param ctId
	 * @return Content
	 */
	public ContentsTbl getContentObj(Long ctId) throws ServiceException{
		return   contentsDao.findOne(ctId);
	}

	/**
	 * 컨텐츠 정보를 업데이트 한다.
	 * 대표화면 키프레임 정보를 수정한다.
	 * @param contentsTbl
	 */
	@Modifying
	@Transactional
	public void updateCotentObj(ContentsTbl contentsTbl) throws ServiceException{
		ContentsTbl old = contentsDao.findOne(contentsTbl.getCtId());
		old.setRpimgKfrmSeq(contentsTbl.getRpimgKfrmSeq());
		contentsDao.save(old);
	}

	/**
	 * contents_tbl에 값을 저장(수정)한다.
	 * 단일건에 대해한 메타정보를 수정하고, 그수정기록을 contentsModTbl에 남기도록 한다
	 * 
	 * @param contentsTbl
	 * @return ContentsTbl
	 */
	@Modifying
	@Transactional
	public ContentsTbl saveContentObj(ContentsTbl contentsTbl) throws ServiceException{
		ContentsTbl result = contentsDao.save(contentsTbl);
		saveContentHistory(contentsTbl);
		return result;
	}



	/**
	 * where 조건에 따른 조회결과를 반환한다.
	 * 
	 * @param search
	 * @return List<SearchMeta>
	 */
	public List<Content> findAllContentsInfo(Search search)throws ServiceException{

		String[] ctFields = {"ctId", "ctNm", "categoryId","brdDd","regDt","vdQlty","delDd","rpimgKfrmSeq","ctLeng","dataStatCd","delDd","cont","aspRtoCd"};
		String[] ctiFields = {"flPath", "wrkFileNm","vdHresol","vdVresol"};
		String[] codeFields = {"sclNm"};

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<ContentsTbl> from = criteriaQuery.from(ContentsTbl.class);
		Root<CodeTbl> code = criteriaQuery.from(CodeTbl.class);

		Join<ContentsTbl, ContentsInstTbl> inst = from.join("contentsInst", JoinType.INNER);
		Join<ContentsTbl, SegmentTbl> segment = from.join("segmentTbl", JoinType.INNER);
		Join<EpisodeTbl, SegmentTbl> episode = segment.join("episodeTbl", JoinType.INNER);
		Join<EpisodeTbl, CategoryTbl> category = episode.join("categoryTbl", JoinType.INNER);

		criteriaQuery.where(ObjectLikeSpecifications.contentsFilterSearch(criteriaBuilder,criteriaQuery,from,code,inst,segment,episode,category ,search));

		Selection[] s = new Selection[ctFields.length + ctiFields.length +codeFields.length];

		int i=0;

		for(int j=0; j<ctFields.length; j++) {
			s[i] = from.get(ctFields[j]);
			i++;
		}

		for(int j=0; j<ctiFields.length; j++) {	
			s[i] = inst.get(ctiFields[j]);				
			i++;
		}

		for(int j=0; j<codeFields.length; j++) {	
			s[i] = code.get(codeFields[j]);				
			i++;
		}
		
		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.desc(from.get("ctId")));

		TypedQuery<Object[]> typedQuery = em.createQuery(select);

		/**
		 * 페이징 시작. 
		 * searchTyp가 imgage면 한페이지에 25개씩
		 * searchTyp가 list면 한페이지에 50개씩
		 */

		int startPage = 0;
		int endPage = 0;

		if(search.getSearchTyp().equals("image")){

			startPage = (search.getPageNo()-1) * SearchControls.CLIP_IMAGE_COUNT;
			endPage = startPage+SearchControls.CLIP_IMAGE_COUNT;

		}else{
			
			if(logger.isDebugEnabled()){
				logger.debug(search.getSearchTyp());
			}

			startPage = (search.getPageNo()-1) * SearchControls.CLIP_LIST_COUNT;
			endPage = startPage+SearchControls.CLIP_LIST_COUNT;
		}

		if(logger.isDebugEnabled()){
			logger.debug("startPage :"+startPage);
			logger.debug("endPage :"+endPage);
		}

		typedQuery.setMaxResults(SearchControls.CLIP_IMAGE_COUNT);
		typedQuery.setFirstResult(startPage);
		
		//캐쉬를 적용한다
		List<Object[]> list2 = typedQuery.setHint("org.hibernate.cacheable", true).getResultList();

		List<Content> contents = new ArrayList<Content>();

		for(Object[] list : list2) {
			Content content = new Content();

			i=0;
			for(int j=0; j<ctFields.length; j++) {
				ObjectUtils.setProperty(content, ctFields[j], list[i]);
				i++;
			}

			for(int j=0; j<ctiFields.length; j++) {
				ObjectUtils.setProperty(content, ctiFields[j], list[i]);
				i++;
			}

			for(int j=0; j<codeFields.length; j++) {
				ObjectUtils.setProperty(content, codeFields[j], list[i]);
				i++;
			}
			contents.add(content);

		}
		return contents;
	}

	/**
	 * 컨텐츠를 삭제요청을 한다.
	 * @param contentsTbl
	 */
	@Modifying
	@Transactional
	public void deleteContent(ContentsTbl contentsTbl) throws ServiceException{

		contentsTbl.setUseYn("N");
		contentsDao.save(contentsTbl);

		List<ContentsInstTbl> contentsInstTbls = contentsInstDao.findAll(ContentsLikeSpecifications.findContentIntByCtId(contentsTbl.getCtId()));

		if(contentsInstTbls != null){
			for(ContentsInstTbl contentsInstTbl : contentsInstTbls) {
				// DB정보는 USE_YN = 'N'로 변경
				contentsInstTbl.setUseYn("N");
				contentsInstDao.save(contentsInstTbl);

				// 물리적인 파일은 삭제 Queue에 등록하여 별도로 처리
				//BatchDeleteQueue.putJob(contentsInstTbl);

			}
		}
	}


	/**
	 * 데이터 정리상태의 값을 저장한다. contents_mod_tbl에 값을 저장.
	 * @param contentsTbl
	 */
	@Modifying
	@Transactional
	public void saveContentHistory(ContentsTbl contentsTbl) throws ServiceException{

		ContentsModTbl contentsModTbl = new ContentsModTbl();

		contentsModTbl.setModDt(new Date());
		contentsModTbl.setModId(contentsTbl.getModrId());
		contentsModTbl.setDataStatcd(contentsTbl.getDataStatCd());
		contentsModTbl.setContentsTbl(contentsTbl);

		contentsModDao.save(contentsModTbl);

	}


	/**
	 * EPISODE_ID로 조회하여 등록된 컨텐츠가 존재하는지를 확인한다.
	 * @return
	 */
	public Long findContentsForEpisode(EpisodeId id) throws ServiceException{
		long count = contentsDao.count(ContentsLikeSpecifications.equalEpisode(id));

		if(logger.isDebugEnabled()){
			logger.debug("count :"+count);
		}
		
		return count;
	}


	/**
	 * CATEGORY_ID로 조회하여 등록된 컨텐츠가 존재하는지를 확인한다.
	 * 카테고리id뿐만 아니라 트리구조상 해당 카테고리id 하위로 등록된 카테고리id로 등록된 컨텐츠까지 동시에 조회한다.
	 * @return
	 */
	public Long findContentsForCategoryId(Integer categoryId) throws ServiceException{
		
		//입력 카테고리id를 node 값으로 가지는 하위 카테고리를 모두 조회한다
		List<CategoryTbl> categoryTbls = categoryDao.findAll(CategorySpecifications.NodesLike(String.valueOf(categoryId)));
		long count=0l;

		//각각의 카테고리id에 등록된 컨텐츠를 합한다.
		for(CategoryTbl subCategorys : categoryTbls){
			count += contentsDao.count(ContentsLikeSpecifications.equalCategory(subCategorys.getCategoryId()));			
		}

		if(logger.isDebugEnabled()){
			logger.debug("count :"+count);
		}
		
		return count;
	}

}

