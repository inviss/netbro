package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import kr.co.d2net.dao.ArchiveDao;
import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.ArchiveTbl_;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Archive;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;
import kr.co.d2net.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 권한(AUTH_TBL) 관련 정보를 조회하기위한 class.
 * @author vayne
 *
 */
@Service
@Transactional(readOnly=true)
public class ArchiveServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ArchiveDao archiveDao;

	@PersistenceContext
	private EntityManager em;




	/**
	 * 아카이브 데이터 1건을 조회한다
	 * Archive Job에 넣을 데이터.
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Archive> getArchiveJob(String statCd) throws ServiceException{

		String[] archiveFields = {"seq", "ctiId", "workStatCd","errorCd","cont","regDt","approveId"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();


		Root<ArchiveTbl> root = cq.from(ArchiveTbl.class);

		Path<Long> seq = root.get(ArchiveTbl_.seq);
		Path<Long> ctiId = root.get(ArchiveTbl_.ctiId);
		Path<String> workStatCd = root.get(ArchiveTbl_.workStatCd);
		Path<String> errorCd = root.get(ArchiveTbl_.errorCd);
		Path<String> cont = root.get(ArchiveTbl_.cont);
		Path<Date> regDt = root.get(ArchiveTbl_.regDt);
		Path<String> approveId = root.get(ArchiveTbl_.approveId);

		cq.multiselect(seq,ctiId,workStatCd,errorCd,cont,regDt,approveId);
		cq.where(cb.and(cb.equal(root.get(ArchiveTbl_.workStatCd),statCd)));
		cq.orderBy(cb.asc(root.get(ArchiveTbl_.seq)));

		em.createQuery(cq).setMaxResults(1);


		/* Query Cache 추가 */
		List<Object[]> list2 = em.createQuery(cq).setHint("org.hibernate.cacheable", true).getResultList();

		List<Archive> archives = new ArrayList<Archive>();

		for(Object[] list : list2) {
			Archive archive = new Archive();
			for(int j = 0; j<archiveFields.length; j++){
				ObjectUtils.setProperty(archive, archiveFields[j], list[j]);			
			}

			archives.add(archive);
		}
		return  archives;
	}


	/**
	 * 아카이브 정보 한 건을 가져오는 method.
	 * updateArchiveHisState에서 기본 정보를 가져오기 위함.
	 * @param seq
	 * @return
	 * @throws ServiceException
	 */
	public ArchiveTbl getArchiveInfo(Long seq) throws ServiceException{
		return archiveDao.findOne(seq);
	}


	/**
	 * 아카이브 진행현황을 저장하는 method.
	 * @param archive
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void updateArchiveHisState(Archive archive) throws ServiceException{

		if(logger.isInfoEnabled()){
			logger.info("archive.getSeq() : "  + archive.getSeq());
		}

		ArchiveTbl archiveTbl = getArchiveInfo(archive.getSeq());

		archiveTbl.setRegDt(Utility.getTimestamp());
		archiveTbl.setPrgrs(archive.getPrgrs());
		archiveTbl.setWorkStatCd(archive.getWorkStatCd());

		if(StringUtils.isNotBlank(archive.getArchivePath()))
			archiveTbl.setArchivePath(archive.getArchivePath());
		
		if(StringUtils.isNotBlank(archive.getErrorCd()))
			archiveTbl.setErrorCd(archive.getErrorCd());

		archiveDao.save(archiveTbl);
	}

	/**
	 * where 조건에 따른 조회결과를 반환한다.
	 * 
	 * @param search
	 * @return List<SearchMeta>
	 */
	public List<Archive> findArchiveList(Search search)throws ServiceException{

		String[] archiveFields = {"seq","ctiId", "cont", "errorCd","regDt","workStatCd","prgrs"};
		String[] episodeFields = {"episodeNm"};
		String[] categoryFields = {"categoryNm"};
		String[] userFields = {"userNm"};
		String[] contentsFields = {"ctNm"};
		String[] codeFields = {"sclNm"};
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<ArchiveTbl> from = criteriaQuery.from(ArchiveTbl.class);
		Root<UserTbl> user = criteriaQuery.from(UserTbl.class);	
		Root<CodeTbl> code = criteriaQuery.from(CodeTbl.class);	
		Join<ContentsInstTbl, ArchiveTbl> archive = from.join("contentsInstTbls", JoinType.INNER);
		Join<ContentsInstTbl, ContentsTbl> inst = archive.join("contentsTbl", JoinType.INNER);
		Join<ContentsTbl, SegmentTbl> segment = inst.join("segmentTbl", JoinType.INNER);
		Join<EpisodeTbl, SegmentTbl> episode = segment.join("episodeTbl", JoinType.INNER);
		Join<CategoryTbl, EpisodeTbl> category = episode.join("categoryTbl", JoinType.INNER);

		criteriaQuery.where(ObjectLikeSpecifications.archivesFilterSearch(criteriaBuilder,criteriaQuery,from,user,code,archive,inst,segment,episode,category ,search));

		Selection[] s = new Selection[archiveFields.length + episodeFields.length +categoryFields.length +userFields.length +contentsFields.length+codeFields.length];

		int i=0;

		for(int j=0; j<archiveFields.length; j++) {
			s[i] = from.get(archiveFields[j]);
			i++;
		}
		for(int j=0; j<episodeFields.length; j++) {	
			s[i] = episode.get(episodeFields[j]);				
			i++;
		}
		for(int j=0; j<categoryFields.length; j++) {	
			s[i] = category.get(categoryFields[j]);				
			i++;
		} 
		for(int j=0; j<userFields.length; j++) {	
			s[i] = user.get(userFields[j]);				
			i++;
		}
		for(int j=0; j<contentsFields.length; j++) {	
			s[i] = inst.get(contentsFields[j]);				
			i++;
		}
		for(int j=0; j<codeFields.length; j++) {	
			s[i] = code.get(codeFields[j]);				
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.desc(from.get(ArchiveTbl_.regDt)));

		TypedQuery<Object[]> typedQuery = em.createQuery(select);

		/**
		 * 페이징 시작. 
		 * searchTyp가 imgage면 한페이지에 25개씩
		 * searchTyp가 list면 한페이지에 50개씩
		 */
		int startPage = 0;
		int endPage = 0;

		startPage = (search.getPageNo()-1) * SearchControls.ARCHIVE_lIST_COUNT;
		endPage = startPage+SearchControls.ARCHIVE_lIST_COUNT;

		if(logger.isDebugEnabled()){
			logger.debug("search.getSearchTyp() : " + search.getSearchTyp());
			logger.debug("startPage :"+startPage);
			logger.debug("endPage :"+endPage);
		}

		typedQuery.setMaxResults(SearchControls.ARCHIVE_lIST_COUNT);
		typedQuery.setFirstResult(startPage);
		List<Object[]> list2 = typedQuery.setHint("org.hibernate.cacheable", true).getResultList();

		List<Archive> archives = new ArrayList<Archive>();

		for(Object[] list : list2) {
			Archive info = new Archive();

			i=0;
			for(int j=0; j<archiveFields.length; j++) {
				ObjectUtils.setProperty(info, archiveFields[j], list[i]);
				i++;
			}
			for(int j=0; j<episodeFields.length; j++) {
				ObjectUtils.setProperty(info, episodeFields[j], list[i]);
				i++;
			}
			for(int j=0; j<categoryFields.length; j++) {
				ObjectUtils.setProperty(info, categoryFields[j], list[i]);
				i++;
			} 
			for(int j=0; j<userFields.length; j++) {
				ObjectUtils.setProperty(info, userFields[j], list[i]);
				i++;
			}
			for(int j=0; j<contentsFields.length; j++) {
				ObjectUtils.setProperty(info, contentsFields[j], list[i]);
				i++;
			}
			for(int j=0; j<codeFields.length; j++) {
				ObjectUtils.setProperty(info, codeFields[j], list[i]);
				i++;
			}
			archives.add(info);

		}
		return archives;
	}


	/**
	 * ctiId값을 이용해 archive정보를 얻어오는 method.
	 * @param ctiId
	 * @return
	 * @throws ServiceException
	 */
	public Archive getArchiveObj(Long ctiId) throws ServiceException{

		Search search = new Search();
		search.setCtiId(ctiId);
		String[] archiveFields = {"seq","ctiId", "cont", "errorCd","regDt","workStatCd","prgrs"};
		String[] episodeFields = {"episodeNm"};
		String[] categoryFields = {"categoryNm"};
		String[] userFields = {"userNm"};
		String[] contentsFields = {"ctNm","ctLeng"};
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<ArchiveTbl> from = criteriaQuery.from(ArchiveTbl.class);
		Root<UserTbl> user = criteriaQuery.from(UserTbl.class);		
		Root<CodeTbl> code = criteriaQuery.from(CodeTbl.class);	
		Join<ContentsInstTbl, ArchiveTbl> archive = from.join("contentsInstTbls", JoinType.INNER);
		Join<ContentsInstTbl, ContentsTbl> inst = archive.join("contentsTbl", JoinType.INNER);
		Join<ContentsTbl, SegmentTbl> segment = inst.join("segmentTbl", JoinType.INNER);
		Join<EpisodeTbl, SegmentTbl> episode = segment.join("episodeTbl", JoinType.INNER);
		Join<CategoryTbl, EpisodeTbl> category = episode.join("categoryTbl", JoinType.INNER);

		criteriaQuery.where(ObjectLikeSpecifications.archivesFilterSearch(criteriaBuilder,criteriaQuery,from,user,code,archive,inst,segment,episode,category ,search));

		Selection[] s = new Selection[archiveFields.length + episodeFields.length +categoryFields.length +userFields.length +contentsFields.length];

		int i=0;

		for(int j=0; j<archiveFields.length; j++) {
			s[i] = from.get(archiveFields[j]);
			i++;
		}
		for(int j=0; j<episodeFields.length; j++) {	
			s[i] = episode.get(episodeFields[j]);				
			i++;
		}
		for(int j=0; j<categoryFields.length; j++) {	
			s[i] = category.get(categoryFields[j]);				
			i++;
		} 
		for(int j=0; j<userFields.length; j++) {	
			s[i] = user.get(userFields[j]);				
			i++;
		}
		for(int j=0; j<contentsFields.length; j++) {	
			s[i] = inst.get(contentsFields[j]);				
			i++;
		}

		CriteriaQuery<Object[]> select = criteriaQuery.select(
				criteriaBuilder.array(s)
				).orderBy(criteriaBuilder.desc(from.get(ArchiveTbl_.regDt)));

		TypedQuery<Object[]> typedQuery = em.createQuery(select);

		List<Object[]> list2 = typedQuery.setHint("org.hibernate.cacheable", true).getResultList();

		Archive info = new Archive();
		for(Object[] list : list2) {

			i=0;
			for(int j=0; j<archiveFields.length; j++) {
				ObjectUtils.setProperty(info, archiveFields[j], list[i]);
				i++;
			}
			for(int j=0; j<episodeFields.length; j++) {
				ObjectUtils.setProperty(info, episodeFields[j], list[i]);
				i++;
			}
			for(int j=0; j<categoryFields.length; j++) {
				ObjectUtils.setProperty(info, categoryFields[j], list[i]);
				i++;
			} 
			for(int j=0; j<userFields.length; j++) {
				ObjectUtils.setProperty(info, userFields[j], list[i]);
				i++;
			}
			for(int j=0; j<contentsFields.length; j++) {
				ObjectUtils.setProperty(info, contentsFields[j], list[i]);
				i++;
			}
		}
		return info;

	}

	/**
	 * 아카이브 기본정보를 저장하는 method.
	 * @param ctiId
	 * @param userId
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void add(long ctiId,String userId) throws ServiceException {
		ArchiveTbl archiveTbl = new ArchiveTbl();

		archiveTbl.setCtiId(ctiId);
		archiveTbl.setWorkStatCd("000");
		archiveTbl.setRegDt(new Date());
		archiveTbl.setApproveId(userId);

		archiveDao.save(archiveTbl);
	}

	/**
	 * 아카이브 재요청을 한다.
	 * @param archive
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void updateRetryArchive(ArchiveTbl archive) throws ServiceException{

		if(logger.isInfoEnabled()){
			logger.info("archive.getSeq() : "  + archive.getSeq());
		}

		ArchiveTbl archiveTbl = archiveDao.findOne(archive.getSeq());
		archiveTbl.setWorkStatCd("000");

		ArchiveTbl resutl = archiveDao.save(archiveTbl); 

		if(logger.isDebugEnabled())
			logger.debug("resutl.getWorkStatCd()       : "  + resutl.getWorkStatCd() );

	}


	/**
	 * 아카이브 요청건이 있는지 조회한다.
	 * Archive Job에 넣을 데이터.
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public Integer checkArchive(Long Id) throws ServiceException{

		String[] archiveFields = {"seq", "ctiId"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<ArchiveTbl> root = cq.from(ArchiveTbl.class);

		Path<Long> seq = root.get(ArchiveTbl_.seq);
		Path<Long> ctiId = root.get(ArchiveTbl_.ctiId);

		cq.multiselect(seq,ctiId);
		cq.where(cb.and(cb.equal(root.get(ArchiveTbl_.ctiId),Id)));
		cq.orderBy(cb.asc(root.get(ArchiveTbl_.seq)));

		em.createQuery(cq).setMaxResults(1);

		/* Query Cache 추가 */
		List<Object[]> list2 = em.createQuery(cq).setHint("org.hibernate.cacheable", true).getResultList();

		return  list2.size();
	}



	/**
	 * 아카이브 요청건이 있는지 조회한다.
	 * Archive Job에 넣을 데이터.
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public Integer checkCompleteArchive(Long Id) throws ServiceException{

		String[] archiveFields = {"seq", "ctiId"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<ArchiveTbl> root = cq.from(ArchiveTbl.class);

		Path<Long> seq = root.get(ArchiveTbl_.seq);
		Path<Long> ctiId = root.get(ArchiveTbl_.ctiId);

		cq.multiselect(seq,ctiId);
		cq.where(cb.and(cb.equal(root.get(ArchiveTbl_.ctiId),Id),cb.equal(root.get(ArchiveTbl_.workStatCd),"004")));
		cq.orderBy(cb.asc(root.get(ArchiveTbl_.seq)));

		em.createQuery(cq).setMaxResults(1);

		/* Query Cache 추가 */
		List<Object[]> list2 = em.createQuery(cq).setHint("org.hibernate.cacheable", true).getResultList();

		return  list2.size();
	}


}
