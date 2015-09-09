package kr.co.d2net.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.CodeDao;
import kr.co.d2net.dao.filter.CodeSpecifications;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.CodeTbl_;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.search.config.Configure;
import kr.co.d2net.utils.PropertiesUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 코드관련 정보를 조회하는 함수
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class CodeServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CodeDao codeDao;

	@PersistenceContext
	private EntityManager em;


	@Autowired
	private MessageSource messageSource;

	

	@Modifying
	@Transactional
	public void addAll(Set<CodeTbl> codes) throws ServiceException {
		String message ="";
		try {
			codeDao.save(codes);
		} catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.001",null,null);
				throw new ConnectionTimeOutException("001", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}


	}

	@Modifying
	@Transactional
	public void add(CodeTbl code) throws ServiceException {
		String message ="";
		try {
			codeDao.save(code);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}
	}

	/**
	 * 분류코드를 신규로 생성한다. 
	 * @param clfNm
	 * @param clfCd
	 * @return result
	 */
	@Modifying
	@Transactional
	public String insertClfInfo(CodeTbl codeTbl) throws ServiceException {
		String message ="";
		CodeTbl code = new CodeTbl();


		List<CodeTbl> codeinfos = codeDao.findAll(CodeSpecifications.ClfLike("U%"));
		CodeId id = new CodeId();

		if(codeinfos.size() == 0){//만약 코드테이블의 최초생성 시도라면

			code.setClfNM(codeTbl.getClfNM());
			code.setRegDt(new Date());
			code.setSclNm("기본값");
			id.setClfCD("U001");
			id.setSclCd("001");
			code.setId(id);
			code.setUseYn("Y");
			code.setRegrId("user");

		}else{
			if(StringUtils.isBlank(codeTbl.getId().getClfCD())){

				int maxNum = 0;

				//clfcd의 값중에서 가장 높은값을 구한다.
				for(CodeTbl info : codeinfos){
					String a = info.getId().getClfCD();
					int tempId = Integer.parseInt(a.substring(1));

					if(tempId > maxNum){
						maxNum = tempId;
					}		
				}

				maxNum += 1;

				code.setClfNM(codeTbl.getClfNM());

				//자리수에 따라 0의 갯수를 달리 정해준다.
				if(maxNum <= 9){
					id.setClfCD("U00" + maxNum);
				}else if(maxNum >=10 && maxNum <= 99){
					id.setClfCD("U0" + maxNum);	
				}else{
					id.setClfCD("U" + maxNum);	
				}
			}else{
				id.setClfCD(codeTbl.getId().getClfCD());
			}

			id.setSclCd("001");
			code.setRegDt(new Date());
			code.setClfNM(codeTbl.getClfNM());
			code.setSclNm("기본값");
			code.setId(id);
			code.setUseYn("Y");
			code.setRegrId("user");
			code.setClfGubun(codeTbl.getClfGubun());
		}

		try{
			codeDao.save(code);
			return "Y";
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}


	}

	/**
	 * 단일건 코드정보 조회
	 * @param code
	 * @return CodeTbl
	 */
	public CodeTbl getCodeInfo(CodeTbl code) throws ServiceException {
		String message ="";
		try {
			return codeDao.findOne(CodeSpecifications.CodeInfo(code.getId().getClfCD(), code.getId().getSclCd()));
		}catch (PersistenceException e) {
			// e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}

	public CodeTbl getCodeInfo(String clfCd, String sclCd) throws ServiceException {
		String message ="";
		try {
			return codeDao.findOne(CodeSpecifications.CodeInfo(clfCd, sclCd));
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}

	}

	/**
	 * code_Tbl에 등록된 총rows수를 구한다.
	 * @param code
	 * @return Long
	 */
	public Long CountCodeInfo(CodeTbl code) throws ServiceException {
		String message ="";
		try {
			if(StringUtils.isBlank(code.getKeyWord())){
				return codeDao.count();
			}else{
				return codeDao.count(CodeSpecifications.codeFilterSearchByNm(code.getGubun(),code.getKeyWord()));	
			}
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}

	}

	public List<CodeTbl> getCodeInfos(String clfCd) throws ServiceException {
		String message ="";
		try {
			if(StringUtils.isBlank(clfCd)) return Collections.emptyList();
			else
				return codeDao.findAll(CodeSpecifications.dataStatCdSearch(clfCd, null), new Sort(Direction.ASC, "id.sclCd"));
		} catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}

	}

	/**
	 * 분류코드에 소속된 정보를 구한다.
	 * @param code
	 * @return List<CodeTbl>
	 */

	public List<CodeTbl> getCodeInfos(CodeTbl code,Search search) throws ServiceException {
		String message ="";
		try {
			PageRequest pageRequest = new PageRequest(
					search.getPageNo(), SearchControls.CODE_LIST_COUNT, new Sort(
							new Order(Direction.DESC, "regDt"),new Order(Direction.DESC, "id.clfCD"),new Order(Direction.DESC, "id.sclCd")
							)
					);

			Page<CodeTbl> codeInfo;

			if(StringUtils.isBlank(code.getKeyWord())){
				codeInfo = codeDao.findAll(pageRequest);
			}else{
				codeInfo = codeDao.findAll(CodeSpecifications.codeFilterSearchByNm(code.getGubun(),code.getKeyWord()), pageRequest);
			}

			return codeInfo.getContent();
		} catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}

	/**
	 * 분류코드명 분류상세코드명으로 정보를 구한다.
	 * @param code
	 * @return List<CodeTbl>
	 */
	public List<CodeTbl> findCodeInfos(CodeTbl code,Search search)throws ServiceException  {
		String message = "";
		try {
			PageRequest pageRequest = new PageRequest(
					search.getPageNo(), SearchControls.CODE_LIST_COUNT, new Sort(
							new Order(Direction.DESC, "regDt"),new Order(Direction.DESC, "id.clfCD"),new Order(Direction.DESC, "id.sclCd")
							)
					);

			Page<CodeTbl> codeInfo;

			if(StringUtils.isBlank(code.getClfNM()) && StringUtils.isBlank(code.getSclNm())){
				codeInfo = codeDao.findAll(pageRequest);
			}else{
				codeInfo = codeDao.findAll(CodeSpecifications.codeFilterSearchByNm(code.getClfCd(),code.getSclNm()), pageRequest);
			}

			return codeInfo.getContent();
		}  catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}


	}

	/**
	 * 분류코드 값을 구한다.(group by 를 사용하여 clf_nm과 clf_Cd만을 사용)
	 * @return List<CodeTbl>
	 * @throws DaoNonRollbackException
	 */

	public List<CodeTbl> getClfInfoList()throws ServiceException {
		String message = "";
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
		Root<CodeTbl> from = criteriaQuery.from(CodeTbl.class);

		criteriaQuery.multiselect(from.get("id").get("clfCD"),from.get(CodeTbl_.clfNM));
		//criteriaQuery.where(criteriaBuilder.notLike(from.<String>get("id").<String>get("clfCD"), "S%"));
		criteriaQuery.groupBy(from.get("id").get("clfCD"),from.get(CodeTbl_.clfNM));

		criteriaQuery.orderBy(criteriaBuilder.desc(from.get("id").get("clfCD")));

		try {
			List<Tuple> result = em.createQuery(criteriaQuery).getResultList();

			List<CodeTbl> list2 = new ArrayList();

			for(Tuple t : result) {

				CodeTbl code = new CodeTbl();
				//CodeId id = new CodeId();

				code.setClfCd((String)t.get(0));
				code.setClfNM((String)t.get(1));

				list2.add(code);
			}
			return list2;	
		}  catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}


	}

	/**
	 * 
	 * @param clfCd
	 * @param sclNm
	 * @return
	 */
	public CodeTbl getClfInfo(String clfCd, String sclNm)  throws ServiceException {

		CodeTbl codeInfo = new CodeTbl();
		String message = "";
		try {
			codeInfo = codeDao.findOne(CodeSpecifications.codeFilterSearchByNm(clfCd,sclNm));
			return codeInfo;
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}



	/**
	 * 신규로 분류코드를 생성한다.
	 * @param code
	 * @return result
	 */
	@Modifying
	@Transactional
	public String insertCodeInfo(CodeTbl code) throws ServiceException {


		//분류코드에 소속된 모든 정보를 조회한다.
		List<CodeTbl> codeinfos = codeDao.findAll(CodeSpecifications.CodeInfo(code.getId().getClfCD(), ""));
		String message = "";
		CodeId id = new CodeId();

		if(codeinfos.size()!=0){

			int maxNum=0;

			for(CodeTbl info : codeinfos){

				String a = info.getId().getSclCd();
				int tempId = Integer.parseInt(a.substring(1));

				if(tempId>maxNum){
					maxNum = tempId;
				}		

				code.setClfNM(info.getClfNM());

			}

			maxNum+=1;

			code.setRegDt(new Date());
			code.setSclNm(code.getSclNm());
			id.setClfCD(code.getId().getClfCD());

			//자릿수에 따라서0을 채워준다
			if(maxNum <= 9){
				id.setSclCd("00"+maxNum);
			}else if(maxNum >= 10){
				id.setSclCd("0"+maxNum);
			}else{
				id.setSclCd(""+maxNum);	
			}

			code.setId(id);
			code.setUseYn("Y");
			code.setRegrId("user");

		}

		String result = "";

		try{
			codeDao.save(code);

			return result;
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}


	}


	/**
	 * 코드정보를 저장한다.
	 * @param code
	 * @return result
	 */
	@Modifying
	@Transactional
	public String updateCodeInfo(CodeTbl code) throws ServiceException {

		//저장전에 기존정보를 조회하여 beans에 담는다.
		CodeTbl codeinfo = codeDao.findOne(code.getId());
		String message = "";
		CodeId id = new CodeId();

		codeinfo.setModDt(new Date());
		codeinfo.setSclNm(code.getSclNm());
		codeinfo.setDesc(code.getDesc());
		codeinfo.setUseYn(code.getUseYn());

		try{
			codeDao.save(codeinfo);

			return "Y";
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}
	}


	/**
	 * 중복코드 여부를 조회한다.
	 * @param code
	 * @return List<CodeTbl>
	 */
	public Long findClfCdInfos(CodeTbl code) throws ServiceException {
		String message = "";
		try {
			Long count = 0l;

			count = codeDao.count(CodeSpecifications.ClfEqual(code.getClfCd()));

			return count;
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}

	}
	
	/**
	 * 중복코드 여부를 조회한다.
	 * @param code
	 * @return List<CodeTbl>
	 */
	public List<CodeTbl> findSclListForClf(CodeTbl code) throws ServiceException {
		String message = "";
		try {
			Long count = 0l;

			return codeDao.findAll(CodeSpecifications.ClfEqual(code.getClfCd()));

		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}

	}

	
	/**
	 * 중복코드 여부를 조회한다.
	 * @param code
	 * @return List<CodeTbl>
	 */
	public long findClfCount(CodeTbl code) throws ServiceException {
		String message = "";
		try {
			Long count = 0l;
			List<CodeTbl> lists = codeDao.findAll(CodeSpecifications.ClfEqual(code.getId().getClfCD()));
			
			count = (long) lists.size();
			return count;

		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}

	}
	
}
