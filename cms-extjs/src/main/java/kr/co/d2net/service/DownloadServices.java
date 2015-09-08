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

import kr.co.d2net.dao.DownloadDao;
import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.DownloadTbl;
import kr.co.d2net.dto.DownloadTbl_;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Download;
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
 * 다운로드(DOWNLOAD_TBL) 관련 정보를 조회하기위한 class.
 * @author vayne
 *
 */
@Service
@Transactional(readOnly=true)
public class DownloadServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private DownloadDao downloadDao;

	@PersistenceContext
	private EntityManager em;



	/**
	 * 다운로드 데이터 1건을 조회한다.
	 * @param statCd
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Download> getDownloadJob(String statCd) throws ServiceException{

		String[] downloadFields = {"seq", "ctiId", "workStatCd","errorCd","reason","regDt","approveId"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<DownloadTbl> root = cq.from(DownloadTbl.class);

		Path<Long> seq = root.get(DownloadTbl_.seq);
		Path<Long> ctiId = root.get(DownloadTbl_.ctiId);
		Path<String> workStatCd = root.get(DownloadTbl_.workStatCd);
		Path<String> errorCd = root.get(DownloadTbl_.errorCd);
		Path<String> reason = root.get(DownloadTbl_.reason);
		Path<Date> regDt = root.get(DownloadTbl_.regDt);
		Path<String> approveId = root.get(DownloadTbl_.approveId);

		cq.multiselect(seq,ctiId,workStatCd,errorCd,reason,regDt,approveId);
		cq.where(cb.and(cb.equal(root.get(DownloadTbl_.workStatCd),statCd)));
		cq.orderBy(cb.asc(root.get(DownloadTbl_.seq)));

		em.createQuery(cq).setMaxResults(1);


		/* Query Cache 추가 */
		List<Object[]> list2 = em.createQuery(cq).setHint("org.hibernate.cacheable", true).getResultList();

		List<Download> archives = new ArrayList<Download>();

		for(Object[] list : list2) {
			Download download = new Download();
			for(int j = 0; j<downloadFields.length; j++){
				ObjectUtils.setProperty(download, downloadFields[j], list[j]);			
			}

			archives.add(download);
		}
		
		return  archives;
	}


	/**
	 * 다운로드 정보를 get하는 method.
	 * @param seq
	 * @return
	 * @throws ServiceException
	 */
	public DownloadTbl getDownloadInfo(Long seq) throws ServiceException{
		return downloadDao.findOne(seq);
	}


	/**
	 * 다운로드 하는 파일의 정보를 update하는 method.(진행률, 작업상태,다운로드 경로, 등록시간,완료시간,에러코드)
	 * @param download
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void updateDownloadHisState(Download download) throws ServiceException{
		
		try {
			DownloadTbl downloadTbl = getDownloadInfo(download.getSeq());

			downloadTbl.setPrgrs(download.getPrgrs());
			downloadTbl.setWorkStatCd(download.getWorkStatCd());

			if(StringUtils.isNotBlank(download.getErrorCd()))
				download.setErrorCd(download.getErrorCd());
			if(download.getRegDt() != null)
				downloadTbl.setRegDt(Utility.getTimestamp());
			if(download.getDnStrDt() != null)
				downloadTbl.setDnStrDt(download.getDnStrDt());
			if(download.getDnEndDt() != null)
				downloadTbl.setDnEndDt(download.getDnEndDt());
			if(StringUtils.isNotBlank(download.getDownloadPath()))
				downloadTbl.setDownloadPath(download.getDownloadPath());

			downloadDao.save(downloadTbl);
		} catch (Exception e) {
			logger.error("updateDownloadHisState",e);
		}
		
	}

	/**
	 * where 조건에 따른 조회결과를 반환한다.
	 * 
	 * @param search
	 * @return List<SearchMeta>
	 */
	public List<Download> findDownloadList(Search search)throws ServiceException{

		String[] downloadFields = {"seq","ctiId", "errorCd","regDt","workStatCd","prgrs"};
		String[] episodeFields = {"episodeNm"};
		String[] categoryFields = {"categoryNm"};
		String[] userFields = {"userNm"};
		String[] contentsFields = {"ctNm","ctLeng"};
		String[] codeFields = {"sclNm"};
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<DownloadTbl> from = criteriaQuery.from(DownloadTbl.class);
		Root<UserTbl> user = criteriaQuery.from(UserTbl.class);	
		Root<CodeTbl> code = criteriaQuery.from(CodeTbl.class);	
		Join<ContentsInstTbl, DownloadTbl> archive = from.join("contentsInstTbls", JoinType.INNER);
		Join<ContentsInstTbl, ContentsTbl> inst = archive.join("contentsTbl", JoinType.INNER);
		Join<ContentsTbl, SegmentTbl> segment = inst.join("segmentTbl", JoinType.INNER);
		Join<EpisodeTbl, SegmentTbl> episode = segment.join("episodeTbl", JoinType.INNER);
		Join<CategoryTbl, EpisodeTbl> category = episode.join("categoryTbl", JoinType.INNER);

		criteriaQuery.where(ObjectLikeSpecifications.downloadsFilterSearch(criteriaBuilder,criteriaQuery,from,user,code,archive,inst,segment,episode,category ,search));

		Selection[] s = new Selection[downloadFields.length + episodeFields.length +categoryFields.length +userFields.length +contentsFields.length+codeFields.length];

		int i=0;

		for(int j=0; j<downloadFields.length; j++) {
			s[i] = from.get(downloadFields[j]);
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
				).orderBy(criteriaBuilder.desc(from.get(DownloadTbl_.regDt)));

		TypedQuery<Object[]> typedQuery = em.createQuery(select);

		/**
		 * 페이징 시작. 
		 * searchTyp가 imgage면 한페이지에 25개씩
		 * searchTyp가 list면 한페이지에 50개씩
		 */
		int startPage = 0;
		int endPage = 0;

		if(logger.isDebugEnabled())
			logger.debug(search.getSearchTyp());

		startPage = (search.getPageNo()-1) * SearchControls.ARCHIVE_lIST_COUNT;
		endPage = startPage+SearchControls.ARCHIVE_lIST_COUNT;

		if(logger.isDebugEnabled()){
			logger.debug("startPage :"+startPage);
			logger.debug("endPage :"+endPage);
		}

		typedQuery.setMaxResults(SearchControls.ARCHIVE_lIST_COUNT);
		typedQuery.setFirstResult(startPage);
		List<Object[]> list2 = typedQuery.setHint("org.hibernate.cacheable", true).getResultList();

		List<Download> downloads = new ArrayList<Download>();

		for(Object[] list : list2) {
			Download info = new Download();

			i=0;
			for(int j=0; j<downloadFields.length; j++) {
				ObjectUtils.setProperty(info, downloadFields[j], list[i]);
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
			
			downloads.add(info);
		}
		
		if(logger.isDebugEnabled())
			logger.debug("downloads : " + downloads.size());
		
		return downloads;
	}

	
	/**
	 * 다운로드 정보 한건을 조회하는 method.
	 * @param ctiId
	 * @return
	 * @throws ServiceException
	 */
	public Download getDownloadObj(Long ctiId) throws ServiceException{

		String[] downloadFields = {"ctiId", "reason", "errorCd","regDt","workStatCd","prgrs"};
		String[] episodeFields = {"episodeNm"};
		String[] categoryFields = {"categoryNm"};
		String[] userFields = {"userNm"};
		String[] contentsFields = {"ctNm","ctLeng"};
		
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

		Root<DownloadTbl> from = criteriaQuery.from(DownloadTbl.class);
		Root<UserTbl> user = criteriaQuery.from(UserTbl.class);		
		Root<CodeTbl> code = criteriaQuery.from(CodeTbl.class);	
		Join<ContentsInstTbl, DownloadTbl> archive = from.join("contentsInstTbls", JoinType.INNER);
		Join<ContentsInstTbl, ContentsTbl> inst = archive.join("contentsTbl", JoinType.INNER);
		Join<ContentsTbl, SegmentTbl> segment = inst.join("segmentTbl", JoinType.INNER);
		Join<EpisodeTbl, SegmentTbl> episode = segment.join("episodeTbl", JoinType.INNER);
		Join<CategoryTbl, EpisodeTbl> category = episode.join("categoryTbl", JoinType.INNER);
		
		Search search = new Search();
		search.setCtiId(ctiId);
		
		criteriaQuery.where(ObjectLikeSpecifications.downloadsFilterSearch(criteriaBuilder,criteriaQuery,from,user,code,archive,inst,segment,episode,category ,search));

		Selection[] s = new Selection[downloadFields.length + episodeFields.length +categoryFields.length +userFields.length +contentsFields.length];

		int i=0;

		for(int j=0; j<downloadFields.length; j++) {
			s[i] = from.get(downloadFields[j]);
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
				).orderBy(criteriaBuilder.desc(from.get(DownloadTbl_.regDt)));

		TypedQuery<Object[]> typedQuery = em.createQuery(select);


		List<Object[]> list2 = typedQuery.setHint("org.hibernate.cacheable", true).getResultList();

		Download info = new Download();
		
		for(Object[] list : list2) {
			i=0;
			
			for(int j=0; j<downloadFields.length; j++) {
				ObjectUtils.setProperty(info, downloadFields[j], list[i]);
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
	 * 다운로드 실패건 재요청
	 * @param archive2
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void updateRetryDownload(DownloadTbl download) throws ServiceException{
		DownloadTbl downloadTbl = getDownloadInfo(download.getSeq());
		downloadTbl.setWorkStatCd("000");
		downloadDao.save(downloadTbl);
	}


	/**
	 * 다운로드 요청정보를 저장하는 method.
	 * @param ctiId
	 * @param userId
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void add(long ctiId,String userId) throws ServiceException {
		
		DownloadTbl downloadTbl = new DownloadTbl();

		downloadTbl.setCtiId(ctiId);
		downloadTbl.setWorkStatCd("000");
		downloadTbl.setRegDt(new Date());
		downloadTbl.setReqId(userId);
		downloadTbl.setApproveId(userId);

		downloadDao.save(downloadTbl);
	}

}
