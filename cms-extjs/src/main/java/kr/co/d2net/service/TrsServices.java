package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.TrsDao;
import kr.co.d2net.dao.filter.ObjectLikeSpecifications;
import kr.co.d2net.dao.filter.TrsSpecifications;
import kr.co.d2net.dto.BusiPartnerTbl;
import kr.co.d2net.dto.BusiPartnerTbl_;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CategoryTbl_;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsInstTbl_;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl_;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.ProFlTbl_;
import kr.co.d2net.dto.TrsTbl;
import kr.co.d2net.dto.TrsTbl_;
import kr.co.d2net.dto.json.Transfer;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Trs;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;
import kr.co.d2net.utils.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Administrator
 *
 */
@Service
@Transactional(readOnly=true)
public class TrsServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TrsDao trsDao;

	@PersistenceContext
	private EntityManager em;


	public Page<TrsTbl> findAllTrs(Specification<TrsTbl> specification, Pageable pageable) {
		return trsDao.findAll(specification, pageable);
	}

	@Modifying
	@Transactional
	public void addAll(Set<TrsTbl> trsTbls) {
		trsDao.save(trsTbls);
	}

	@Modifying
	@Transactional
	public void add(TrsTbl trs) {
		trsDao.save(trs);
	}


	/**
	 * 
	 * @param seq
	 * @return
	 * @throws ServiceException
	 */
	public TrsTbl getTrsInfo(Long seq) throws ServiceException{
		return trsDao.findOne(TrsSpecifications.formatLike(seq));
	}



	/**
	 * 
	 * @param trsSeq
	 * @return
	 * @throws ServiceException
	 */
	public Transfer getTrsObj(Long trsSeq) throws ServiceException{
		//return trsDao.findOne(seq);

		String[] trsFields = {"ctId","priority","retryCnt","ctNm","ctiId","flPath","orgFileNm"
				,"wrkFileNm","flExt","busiPartnerId","proFlid","company"
				,"ftpId","ip","port","password","remoteDir","seq","workStatcd","proFlnm"
				,"categoryNm","episodeNm","brdDd","ctLeng","vdoBitRate"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<ContentsTbl> root = cq.from(ContentsTbl.class);
		Root<ContentsInstTbl> root1 = cq.from(ContentsInstTbl.class);
		Root<BusiPartnerTbl> root2 = cq.from(BusiPartnerTbl.class);
		Root<ProFlTbl> root3 = cq.from(ProFlTbl.class);
		Root<TrsTbl> root4 = cq.from(TrsTbl.class);
		Root<CategoryTbl> root5 = cq.from(CategoryTbl.class);
		Root<EpisodeTbl> root6 = cq.from(EpisodeTbl.class);


		Path<Long> ctId = root.get(ContentsTbl_.ctId);
		Path<String> priority = root4.get(TrsTbl_.priority);
		Path<Integer> retryCnt = root4.get(TrsTbl_.retryCnt);
		Path<Long> seq = root4.get(TrsTbl_.seq);
		Path<String> workStatcd = root4.get(TrsTbl_.workStatcd);
		Path<String> ctNm = root.get(ContentsTbl_.ctNm);
		Path<Long> ctiId = root1.get(ContentsInstTbl_.ctiId);
		Path<String> flPath = root1.get(ContentsInstTbl_.flPath);
		Path<String> orgFileNm = root1.get(ContentsInstTbl_.orgFileNm);
		Path<String> wrkFileNm = root1.get(ContentsInstTbl_.wrkFileNm);
		Path<String> flExt = root1.get(ContentsInstTbl_.flExt);
		Path<Long> busiPartnerId = root2.get(BusiPartnerTbl_.busiPartnerId);
		Path<Long> proFlid = root3.get(ProFlTbl_.proFlId);
		Path<String> company = root2.get(BusiPartnerTbl_.company);
		Path<String> ftpId = root2.get(BusiPartnerTbl_.ftpId);
		Path<String> ip = root2.get(BusiPartnerTbl_.ip);
		Path<String> port = root2.get(BusiPartnerTbl_.port);
		Path<String> password = root2.get(BusiPartnerTbl_.password);
		Path<String> remoteDir = root2.get(BusiPartnerTbl_.remoteDir);
		Path<String> proFlnm = root3.get(ProFlTbl_.proFlnm);
		Path<String> categoryNm = root5.get(CategoryTbl_.categoryNm);
		Path<String> episodeNm = root6.get(EpisodeTbl_.episodeNm);
		Path<Date> brdDd = root.get(ContentsTbl_.brdDd);
		Path<String> ctLeng = root.get(ContentsTbl_.ctLeng);
		Path<String> vdoBitRate = root3.get(ProFlTbl_.vdoBitRate);


		cq.multiselect(ctId,priority,retryCnt,ctNm,ctiId,flPath
				,orgFileNm,wrkFileNm,flExt,busiPartnerId,proFlid,company
				,ftpId,ip,port,password,remoteDir,seq,workStatcd,proFlnm
				,categoryNm,episodeNm,brdDd,ctLeng,vdoBitRate);

		cq.where(cb.and(cb.equal(root.get("ctId"), root1.get("ctId"))
				,cb.equal(root1.get("ctiId"), root4.get("ctiId"))
				,cb.equal(root4.get("seq"),trsSeq)
				,cb.equal(root4.get("busiPartnerId"), root2.get("busiPartnerId"))
				,cb.equal(root4.get("proFlId"), root3.get("proFlId"))
				,cb.equal(root.get("categoryId"), root5.get("categoryId"))
				,cb.equal(root.get("episodeId"), root6.get("id").get("episodeId"))
				,cb.equal(root5.get("categoryId"), root6.get("id").get("categoryId"))));

		//cq.orderBy(cb.asc(root4.get("regDt")),cb.asc(root4.get("priority")));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		//typedQuery.setMaxResults(2);
		Object[] traList =  typedQuery.getSingleResult();
		Transfer transfer = new Transfer();

		for(int j = 0; j<trsFields.length; j++){
			ObjectUtils.setProperty(transfer, trsFields[j], traList[j]);			

		}

		return transfer;
	}




	/**
	 * 예약전송이 적용되는 프로파일 영상 조회(TRS_TBL에서 workStatcd가 '001')
	 * ->전송하다 서버가 죽었을 경우 workStatcd 001 부터 조회해서 다시 queue에 등록해야함
	 * @param string
	 * @param object
	 * @param transferGB
	 * @return
	 */
	public List<Transfer> findTransferJob(String stat,int size) throws ServiceException{

		String[] trsFields = {"ctId","priority","retryCnt","ctNm","ctiId","flPath","orgFileNm"
				,"wrkFileNm","flExt","busiPartnerId","proFlId","company"
				,"ftpId","ip","port","password","remoteDir","seq","workStatcd","proFlnm"
				,"categoryNm","episodeNm","brdDd","ctLeng","vdoBitRate"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<ContentsTbl> root = cq.from(ContentsTbl.class);
		Root<ContentsInstTbl> root1 = cq.from(ContentsInstTbl.class);
		Root<BusiPartnerTbl> root2 = cq.from(BusiPartnerTbl.class);
		Root<ProFlTbl> root3 = cq.from(ProFlTbl.class);
		Root<TrsTbl> root4 = cq.from(TrsTbl.class);
		Root<CategoryTbl> root5 = cq.from(CategoryTbl.class);
		Root<EpisodeTbl> root6 = cq.from(EpisodeTbl.class);


		Path<Long> ctId = root.get(ContentsTbl_.ctId);
		Path<String> priority = root4.get(TrsTbl_.priority);
		Path<Integer> retryCnt = root4.get(TrsTbl_.retryCnt);
		Path<Long> seq = root4.get(TrsTbl_.seq);
		Path<String> workStatcd = root4.get(TrsTbl_.workStatcd);
		Path<String> ctNm = root.get(ContentsTbl_.ctNm);
		Path<Long> ctiId = root1.get(ContentsInstTbl_.ctiId);
		Path<String> flPath = root1.get(ContentsInstTbl_.flPath);
		Path<String> orgFileNm = root1.get(ContentsInstTbl_.orgFileNm);
		Path<String> wrkFileNm = root1.get(ContentsInstTbl_.wrkFileNm);
		Path<String> flExt = root1.get(ContentsInstTbl_.flExt);
		Path<Long> busiPartnerId = root2.get(BusiPartnerTbl_.busiPartnerId);
		Path<Long> proFlId = root3.get(ProFlTbl_.proFlId);
		Path<String> company = root2.get(BusiPartnerTbl_.company);
		Path<String> ftpId = root2.get(BusiPartnerTbl_.ftpId);
		Path<String> ip = root2.get(BusiPartnerTbl_.ip);
		Path<String> port = root2.get(BusiPartnerTbl_.port);
		Path<String> password = root2.get(BusiPartnerTbl_.password);
		Path<String> remoteDir = root2.get(BusiPartnerTbl_.remoteDir);
		Path<String> proFlnm = root3.get(ProFlTbl_.proFlnm);
		Path<String> categoryNm = root5.get(CategoryTbl_.categoryNm);
		Path<String> episodeNm = root6.get(EpisodeTbl_.episodeNm);
		Path<Date> brdDd = root.get(ContentsTbl_.brdDd);
		Path<String> ctLeng = root.get(ContentsTbl_.ctLeng);
		Path<String> vdoBitRate = root3.get(ProFlTbl_.vdoBitRate);


		cq.multiselect(ctId,priority,retryCnt,ctNm,ctiId,flPath
				,orgFileNm,wrkFileNm,flExt,busiPartnerId,proFlId,company
				,ftpId,ip,port,password,remoteDir,seq,workStatcd,proFlnm
				,categoryNm,episodeNm,brdDd,ctLeng,vdoBitRate);

		cq.where(cb.and(cb.equal(root.get(ContentsTbl_.ctId), root1.get(ContentsInstTbl_.ctId))
				,cb.equal(root1.get(ContentsInstTbl_.ctiId), root4.get(TrsTbl_.ctiId))
				,cb.equal(root4.get(TrsTbl_.workStatcd), stat)
				,cb.equal(root.get(ContentsTbl_.ctTyp), "003")
				,cb.equal(root1.get(ContentsInstTbl_.useYn), "Y")
				,cb.equal(root4.get(TrsTbl_.busiPartnerId), root2.get(BusiPartnerTbl_.busiPartnerId))
				,cb.equal(root4.get(TrsTbl_.proFlId), root3.get(ProFlTbl_.proFlId))
				,cb.equal(root.get("categoryId"), root5.get("categoryId"))
				,cb.equal(root.get("episodeId"), root6.get("id").get("episodeId"))
				,cb.equal(root5.get("categoryId"), root6.get("id").get("categoryId"))
				,cb.like((root1.<String>get(ContentsInstTbl_.ctiFmt)), "1%")));

		cq.orderBy(cb.asc(root4.get(TrsTbl_.priority)),cb.asc(root4.get(TrsTbl_.regDt)));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		int tmpSize = 0;

		switch(size){
		case 4:
			tmpSize = 0;
			break;
		case 3:
			tmpSize = 1;
			break;
		case 2:
			tmpSize = 2;
			break;
		case 1:
			tmpSize = 3;
			break;
		case 0:
			tmpSize = 4;
			break;
		}

		logger.debug("tmpSize : " + tmpSize);

		typedQuery.setMaxResults(tmpSize);

		List<Object[]> traList =  typedQuery.getResultList();

		if(logger.isDebugEnabled()){
			logger.debug("traList.size : " + traList.size());
			logger.debug("tmpSize.size : " + tmpSize);
		}

		List<Transfer> transfers = new ArrayList<Transfer>();

		for(Object[] list : traList) {
			Transfer transfer = new Transfer();

			int i = 0;
			for(int j = 0; j<trsFields.length; j++){
				ObjectUtils.setProperty(transfer, trsFields[j], list[i]);			
				i++;
			}
			transfers.add(transfer);
		}
		return transfers;
	}

	/**
	 * 컨텐츠 전송 리스트를 조회하는 method.
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Trs> findTrsInfos(Search search) throws ServiceException{

		String[] trsFields = {"ctId","priority","retryCnt","ctNm","ctiId","flPath","orgFileNm"
				,"wrkFileNm","flExt","busiPartnerId","proFlId","company"
				,"ftpId","ip","port","password","remoteDir","seq","workStatcd","proFlnm"
				,"categoryNm","episodeNm","brdDd","ctLeng","vdoBitRate","regDt","reqDt","modDt","trsStrDt","trsEndDt","prgrs"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<ContentsTbl> root = cq.from(ContentsTbl.class);
		Root<ContentsInstTbl> root1 = cq.from(ContentsInstTbl.class);
		Root<BusiPartnerTbl> root2 = cq.from(BusiPartnerTbl.class);
		Root<ProFlTbl> root3 = cq.from(ProFlTbl.class);
		Root<TrsTbl> root4 = cq.from(TrsTbl.class);
		Root<CategoryTbl> root5 = cq.from(CategoryTbl.class);
		Root<EpisodeTbl> root6 = cq.from(EpisodeTbl.class);


		Path<Long> ctId = root.get(ContentsTbl_.ctId);
		Path<String> priority = root4.get(TrsTbl_.priority);
		Path<Integer> retryCnt = root4.get(TrsTbl_.retryCnt);
		Path<Long> seq = root4.get(TrsTbl_.seq);
		Path<String> workStatcd = root4.get(TrsTbl_.workStatcd);
		Path<String> ctNm = root.get(ContentsTbl_.ctNm);
		Path<Long> ctiId = root1.get(ContentsInstTbl_.ctiId);
		Path<String> flPath = root1.get(ContentsInstTbl_.flPath);
		Path<String> orgFileNm = root1.get(ContentsInstTbl_.orgFileNm);
		Path<String> wrkFileNm = root1.get(ContentsInstTbl_.wrkFileNm);
		Path<String> flExt = root1.get(ContentsInstTbl_.flExt);
		Path<Long> busiPartnerId = root2.get(BusiPartnerTbl_.busiPartnerId);
		Path<Long> proFlId = root3.get(ProFlTbl_.proFlId);
		Path<String> company = root2.get(BusiPartnerTbl_.company);
		Path<String> ftpId = root2.get(BusiPartnerTbl_.ftpId);
		Path<String> ip = root2.get(BusiPartnerTbl_.ip);
		Path<String> port = root2.get(BusiPartnerTbl_.port);
		Path<String> password = root2.get(BusiPartnerTbl_.password);
		Path<String> remoteDir = root2.get(BusiPartnerTbl_.remoteDir);
		Path<String> proFlnm = root3.get(ProFlTbl_.proFlnm);
		Path<String> categoryNm = root5.get(CategoryTbl_.categoryNm);
		Path<String> episodeNm = root6.get(EpisodeTbl_.episodeNm);
		Path<Date> brdDd = root.get(ContentsTbl_.brdDd);
		Path<String> ctLeng = root.get(ContentsTbl_.ctLeng);
		Path<String> vdoBitRate = root3.get(ProFlTbl_.vdoBitRate);
		Path<Date> regDt = root4.get(TrsTbl_.regDt);
		Path<Date> reqDt = root4.get(TrsTbl_.reqDt);
		Path<Date> modDt = root4.get(TrsTbl_.modDt);
		Path<Date> trsStrDt = root4.get(TrsTbl_.trsStrDt);
		Path<Date> trsEndDt = root4.get(TrsTbl_.trsEndDt);
		Path<Integer> prgrs = root4.get(TrsTbl_.prgrs);


		cq.multiselect(ctId,priority,retryCnt,ctNm,ctiId,flPath
				,orgFileNm,wrkFileNm,flExt,busiPartnerId,proFlId,company
				,ftpId,ip,port,password,remoteDir,seq,workStatcd,proFlnm
				,categoryNm,episodeNm,brdDd,ctLeng,vdoBitRate,regDt,reqDt,modDt,trsStrDt,trsEndDt,prgrs);

		//		cq.where(cb.and(cb.equal(root.get("ctId"), root1.get("ctId"))
		//				,cb.equal(root1.get("ctiId"), root4.get("ctiId"))
		//				,cb.equal(root4.get("busiPartnerId"), root2.get("busiPartnerId"))
		//				,cb.equal(root4.get("proFlid"), root3.get("proFlid"))
		//				,cb.equal(root.get("categoryId"), root5.get("categoryId"))
		//				,cb.equal(root.get("episodeId"), root6.get("id").get("episodeId"))
		//				,cb.equal(root5.get("categoryId"), root6.get("id").get("categoryId"))));

		cq.where(ObjectLikeSpecifications.trsFilterSearch(cb,cq,search,root,root1,root2,root3,root4,root5,root6));

		//		cq.where(cb.and(cb.equal(root.get("ctId"), root1.get("ctId"))
		//				,cb.equal(root1.get("ctiId"), root4.get("ctiId"))
		//				,cb.equal(root4.get("busiPartnerId"), root2.get("busiPartnerId"))
		//				,cb.equal(root4.get("proFlid"), root3.get("proFlid"))
		//				,cb.equal(root.get("categoryId"), root5.get("categoryId"))
		//				,cb.equal(root.get("episodeId"), root6.get("id").get("episodeId"))
		//				,cb.equal(root5.get("categoryId"), root6.get("id").get("categoryId"))));

		cq.orderBy(cb.desc(root4.get("seq")));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		int startPage = 0;
		int endPage = 0;

		startPage = (search.getPageNo()-1) * SearchControls.TRA_LIST_COUNT;
		endPage = startPage+SearchControls.TRA_LIST_COUNT;

		typedQuery.setFirstResult(startPage);
		typedQuery.setMaxResults(endPage);

		List<Object[]> trsList =  typedQuery.getResultList();

		if(trsList != null){
			List<Trs> tras = new ArrayList<Trs>();

			for(Object[] list : trsList) {
				Trs trs = new Trs();

				int i = 0;
				for(int j = 0; j<trsFields.length; j++){
					ObjectUtils.setProperty(trs, trsFields[j], list[i]);			
					i++;
				}
				tras.add(trs);
			}
			return tras;
		}else{
			return Collections.EMPTY_LIST;
		}
	}


	/**
	 * 
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public Integer getTrsCount(Search search) throws ServiceException{

		String[] trsFields = {"ctId","priority","retryCnt","ctNm","ctiId","flPath","orgFileNm"
				,"wrkFileNm","flExt","busiPartnerId","proFlId","company"
				,"ftpId","ip","port","password","remoteDir","seq","workStatcd","proFlnm"
				,"categoryNm","episodeNm","brdDd","ctLeng","vdoBitRate","regDt","prgrs"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<ContentsTbl> root = cq.from(ContentsTbl.class);
		Root<ContentsInstTbl> root1 = cq.from(ContentsInstTbl.class);
		Root<BusiPartnerTbl> root2 = cq.from(BusiPartnerTbl.class);
		Root<ProFlTbl> root3 = cq.from(ProFlTbl.class);
		Root<TrsTbl> root4 = cq.from(TrsTbl.class);
		Root<CategoryTbl> root5 = cq.from(CategoryTbl.class);
		Root<EpisodeTbl> root6 = cq.from(EpisodeTbl.class);


		Path<Long> ctId = root.get(ContentsTbl_.ctId);
		Path<String> priority = root4.get(TrsTbl_.priority);
		Path<Integer> retryCnt = root4.get(TrsTbl_.retryCnt);
		Path<Long> seq = root4.get(TrsTbl_.seq);
		Path<String> workStatcd = root4.get(TrsTbl_.workStatcd);
		Path<String> ctNm = root.get(ContentsTbl_.ctNm);
		Path<Long> ctiId = root1.get(ContentsInstTbl_.ctiId);
		Path<String> flPath = root1.get(ContentsInstTbl_.flPath);
		Path<String> orgFileNm = root1.get(ContentsInstTbl_.orgFileNm);
		Path<String> wrkFileNm = root1.get(ContentsInstTbl_.wrkFileNm);
		Path<String> flExt = root1.get(ContentsInstTbl_.flExt);
		Path<Long> busiPartnerId = root2.get(BusiPartnerTbl_.busiPartnerId);
		Path<Long> proFlId = root3.get(ProFlTbl_.proFlId);
		Path<String> company = root2.get(BusiPartnerTbl_.company);
		Path<String> ftpId = root2.get(BusiPartnerTbl_.ftpId);
		Path<String> ip = root2.get(BusiPartnerTbl_.ip);
		Path<String> port = root2.get(BusiPartnerTbl_.port);
		Path<String> password = root2.get(BusiPartnerTbl_.password);
		Path<String> remoteDir = root2.get(BusiPartnerTbl_.remoteDir);
		Path<String> proFlnm = root3.get(ProFlTbl_.proFlnm);
		Path<String> categoryNm = root5.get(CategoryTbl_.categoryNm);
		Path<String> episodeNm = root6.get(EpisodeTbl_.episodeNm);
		Path<Date> brdDd = root.get(ContentsTbl_.brdDd);
		Path<String> ctLeng = root.get(ContentsTbl_.ctLeng);
		Path<String> vdoBitRate = root3.get(ProFlTbl_.vdoBitRate);
		Path<Date> regDt = root4.get(TrsTbl_.regDt);
		Path<Integer> prgrs = root4.get(TrsTbl_.prgrs);


		cq.multiselect(ctId,priority,retryCnt,ctNm,ctiId,flPath
				,orgFileNm,wrkFileNm,flExt,busiPartnerId,proFlId,company
				,ftpId,ip,port,password,remoteDir,seq,workStatcd,proFlnm
				,categoryNm,episodeNm,brdDd,ctLeng,vdoBitRate,regDt,prgrs);

		//		cq.where(cb.and(cb.equal(root.get("ctId"), root1.get("ctId"))
		//				,cb.equal(root1.get("ctiId"), root4.get("ctiId"))
		//				,cb.equal(root4.get("busiPartnerId"), root2.get("busiPartnerId"))
		//				,cb.equal(root4.get("proFlid"), root3.get("proFlid"))
		//				,cb.equal(root.get("categoryId"), root5.get("categoryId"))
		//				,cb.equal(root.get("episodeId"), root6.get("id").get("episodeId"))
		//				,cb.equal(root5.get("categoryId"), root6.get("id").get("categoryId"))));

		cq.where(ObjectLikeSpecifications.trsFilterSearch(cb,cq,search,root,root1,root2,root3,root4,root5,root6));

		//		cq.where(cb.and(cb.equal(root.get("ctId"), root1.get("ctId"))
		//				,cb.equal(root1.get("ctiId"), root4.get("ctiId"))
		//				,cb.equal(root4.get("busiPartnerId"), root2.get("busiPartnerId"))
		//				,cb.equal(root4.get("proFlid"), root3.get("proFlid"))
		//				,cb.equal(root.get("categoryId"), root5.get("categoryId"))
		//				,cb.equal(root.get("episodeId"), root6.get("id").get("episodeId"))
		//				,cb.equal(root5.get("categoryId"), root6.get("id").get("categoryId"))));

		cq.orderBy(cb.desc(root4.get("seq")));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		List<Object[]> trsList =  typedQuery.getResultList();

		if(trsList != null){
			List<Trs> tras = new ArrayList<Trs>();

			for(Object[] list : trsList) {
				Trs trs = new Trs();

				int i = 0;
				for(int j = 0; j<trsFields.length; j++){
					ObjectUtils.setProperty(trs, trsFields[j], list[i]);			
					i++;
				}
				tras.add(trs);
			}
			return (Integer) tras.size();
		}else{
			return 0;
		}
	}


	/**
	 * Queue에 있는 전송job의 작업상태를 변경한다.
	 * @param transfer
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void updateTransferHisState(Transfer transfer) throws ServiceException{

		TrsTbl trsTbl = getTrsInfo(transfer.getSeq());

		trsTbl.setModDt(Utility.getTimestamp());
		trsTbl.setWorkStatcd("001");// 대기상태[00] -> 요청상태 변경[01]

		trsDao.save(trsTbl);
	}


	/**
	 * 컨텐츠 전송관리의 재요청 작업을 하는 method.
	 * 진행률과 작업상태, 수정일시를 update한다.
	 * @param search
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void retryTrsObj(Search search) throws ServiceException{

		TrsTbl trsTbl = getTrsInfo(search.getTrsSeq());

		trsTbl.setModDt(Utility.getTimestamp());
		trsTbl.setWorkStatcd("000");
		trsTbl.setPrgrs(0);

		trsDao.save(trsTbl);
	}

}
