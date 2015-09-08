package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.CodeDao;
import kr.co.d2net.dao.filter.CodeSpecifications;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.CodeTbl_;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Code;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

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
	public void add(CodeTbl code) throws ServiceException {
		codeDao.save(code);
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

		codeDao.save(code);
		return "Y";

	}

	/**
	 * 단일건 코드정보 조회
	 * @param code
	 * @return CodeTbl
	 */
	public CodeTbl getCodeInfo(CodeTbl code) throws ServiceException {
		return codeDao.findOne(CodeSpecifications.CodeInfo(code.getId().getClfCD(), code.getId().getSclCd()));
	}
 

	/**
	 * code_Tbl에 등록된 총rows수를 구한다.
	 * @param code
	 * @return Long
	 */
	public Long CountCodeInfo(CodeTbl code) throws ServiceException {
		if(StringUtils.isBlank(code.getKeyWord())){
			return codeDao.count();
		}else{
			return codeDao.count(CodeSpecifications.codeFilterSearchByNm(code.getGubun(),code.getKeyWord()));	
		}
	}

	public List<CodeTbl> findCodeInfos(String clfCd) throws ServiceException {
		if(StringUtils.isBlank(clfCd)) return Collections.emptyList();
		else
			return codeDao.findAll(CodeSpecifications.dataStatCdSearch(clfCd, null), new Sort(Direction.ASC, "id.sclCd"));

	}


	/**
	 * 분류코드명 분류상세코드명으로 정보를 구한다.
	 * @param code
	 * @return List<CodeTbl>
	 */
	public List<CodeTbl> findCodeInfos(CodeTbl code,Search search)throws ServiceException  {
		PageRequest pageRequest = new PageRequest(
				search.getPageNo()-1, SearchControls.CODE_LIST_COUNT, new Sort(
						new Order(Direction.DESC, "regDt"),new Order(Direction.DESC, "id.clfCD"),new Order(Direction.DESC, "id.sclCd")
						)
				);

		Page<CodeTbl> codeInfo;

		codeInfo = codeDao.findAll(pageRequest);
		
		return codeInfo.getContent();

	}

	/**
	 * 분류코드 값을 구한다.(group by 를 사용하여 clf_nm과 clf_Cd만을 사용)
	 * @return List<CodeTbl>
	 * @throws DaoNonRollbackException
	 */

	public List<Code> findClfInfoList()throws ServiceException {

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
		
		Root<CodeTbl> from = criteriaQuery.from(CodeTbl.class);

		criteriaQuery.multiselect(from.get("id").get("clfCD"),from.get(CodeTbl_.clfNM));
		criteriaQuery.groupBy(from.get("id").get("clfCD"),from.get(CodeTbl_.clfNM));
		criteriaQuery.orderBy(criteriaBuilder.desc(from.get("id").get("clfCD")));

		List<Tuple> result = em.createQuery(criteriaQuery).setHint("org.hibernate.cacheable", true).getResultList();
		List<Code> codes = new ArrayList();

		for(Tuple t : result) {
			Code code = new Code();

			code.setClfCd((String)t.get(0));
			code.setClfNm((String)t.get(1));
			
			codes.add(code);
		}
		return codes;	
	}


	/**
	 * 분뷰코드 정보를 리스트형태로 조회한다
	 * @return Code<Li
	 * @throws ServiceException
	 */
	public List<Code> findCodeClfList()throws ServiceException {

		String[] codeClfFields = {"clfCd", "clfNm"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<CodeTbl> root = cq.from(CodeTbl.class);
		Path<String> clfCD = root.get(CodeTbl_.id).get("clfCD");
		Path<String> clfNM = root.get(CodeTbl_.clfNM);

		cq.multiselect(clfCD,clfNM).distinct(true);

		TypedQuery<Object[]> typedQuery = em.createQuery(cq).setHint("org.hibernate.cacheable", true);
		
		List<Object[]> objects =  typedQuery.getResultList();
		
		List<Code> codes = new ArrayList<Code>();

		for(Object[] list : objects) {
			Code monitoring = new Code();

			int i = 0;
			for(int j = 0; j<codeClfFields.length; j++){
				ObjectUtils.setProperty(monitoring, codeClfFields[j], list[i]);			
				i++;
			}
			codes.add(monitoring);
		}
		return codes;
	}	




	/**
	 * 
	 * @param clfCd
	 * @param sclNm
	 * @return
	 */
	public CodeTbl getClfInfo(String clfCd, String rmk1)  throws ServiceException {
		CodeTbl codeInfo = new CodeTbl();
		codeInfo = codeDao.findOne(CodeSpecifications.Rmk1Equal(clfCd, rmk1));
		return codeInfo;
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
		codeDao.save(code);
		return result;

	}


	/**
	 * 코드정보를 저장한다.
	 * @param code
	 * @return result
	 */
	@Modifying
	@Transactional
	public String updateCodeInfo(CodeTbl code) throws ServiceException {


		logger.debug("code.getId() : " + code.getId());
		logger.debug("code.getId() : " + code.getUseYn());


		//저장전에 기존정보를 조회하여 beans에 담는다.
		CodeTbl codeinfo = codeDao.findOne(code.getId());
		String message = "";

		codeinfo.setModDt(new Date());
		codeinfo.setSclNm(code.getSclNm());
		codeinfo.setDesc(code.getDesc());
		codeinfo.setUseYn(code.getUseYn());

		codeDao.save(codeinfo);

		return "Y";
	}


 

	/**
	 * 구분코드에 소속되어있는 구분상세콬드 정보를 조회한다
	 * @param code
	 * @return List<CodeTbl>
	 */
	public List<CodeTbl> findSclListForClf(CodeTbl code) throws ServiceException {
		Long count = 0l;
		return codeDao.findAll(CodeSpecifications.ClfEqual(code.getClfCd()));
	}


	/**
	 * 구분코드에 소속되어있는 구분 상세코드의 갯수를 구한다
	 * @param code
	 * @return List<CodeTbl>
	 */
	public long countClfCount(CodeTbl code) throws ServiceException {
		Long count = 0l;
		count = codeDao.count(CodeSpecifications.ClfEqual(code.getId().getClfCD()));
 
		return count;

	}

}
