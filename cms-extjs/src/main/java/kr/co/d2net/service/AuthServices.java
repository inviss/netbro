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

import kr.co.d2net.dao.AuthDao;
import kr.co.d2net.dao.RoleAuthDao;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.AuthTbl_;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.RoleAuthTbl.RoleAuthId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Auth;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

import org.hibernate.Hibernate;
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
public class AuthServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;


	@Autowired
	private AuthDao authDao;

	@Autowired
	private RoleAuthDao roleAuthDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private RoleAuthServices roleAuthServices;
	
	/**
	 * 
	 * @param auths
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void addAll(Set<AuthTbl> auths) throws ServiceException{
		authDao.save(auths);
	}

	
	/**
	 * 
	 * @param auth
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void add(AuthTbl auth) throws ServiceException{
		authDao.save(auth);
	}

	
	
	
	/**
	 * 권한목록의 list를 조회하는 method.
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Auth> findAuthList() throws ServiceException{

		String[] authFields = {"authId","authNm", "authSubNm", "regrId","regDt","modrId","modDt","useYn"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<AuthTbl> root = cq.from(AuthTbl.class);

		Path<Integer> authId = root.get(AuthTbl_.authId);
		Path<String> authNm = root.get(AuthTbl_.authNm);
		Path<String> authSubNm = root.get(AuthTbl_.authSubNm);
		Path<String> regrId = root.get(AuthTbl_.regrId);
		Path<Date> regDt = root.get(AuthTbl_.regDt);
		Path<String> modrId = root.get(AuthTbl_.modrId);
		Path<Date> modDt = root.get(AuthTbl_.modDt);
		Path<String> useYn = root.get(AuthTbl_.useYn);

		cq.multiselect(authId,authNm,authSubNm,regrId,regDt,modrId,modDt,useYn);
		cq.orderBy(cb.desc(authId));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		List<Object[]> authList =  typedQuery.getResultList();

		if(authList != null){
			List<Auth> auths = new ArrayList<Auth>();

			for(Object[] list : authList) {
				Auth auth = new Auth();

				int i = 0;
				for(int j = 0; j<authFields.length; j++){
					ObjectUtils.setProperty(auth, authFields[j], list[i]);			
					i++;
				}
				auths.add(auth);
			}
			return auths;
		}else{
			return Collections.EMPTY_LIST;
		}
	}





	/**
	 * authId를 AuthTbl정보를 가져온다.
	 * @param id
	 * @return
	 */
	public AuthTbl getAuthObj(Integer authId) throws ServiceException{
		AuthTbl authTbl = authDao.findOne(authId);
		Hibernate.initialize(authTbl.getRoleAuth());
		return authTbl;
	}
	
	
	/**
	 * authId를 AuthTbl정보를 가져온다.
	 * @param id
	 * @return
	 */
	public Auth getAuthInfo(Integer authId) throws ServiceException{
		AuthTbl authTbl = authDao.findOne(authId);
		Hibernate.initialize(authTbl.getRoleAuth());
		
		Auth auth = new Auth();
		
		auth.setAuthId(authTbl.getAuthId());
		auth.setAuthNm(authTbl.getAuthNm());
		auth.setAuthSubNm(authTbl.getAuthSubNm());
		auth.setUseYn(authTbl.getUseYn());
		return auth;
	}


	/**
	 * authTbl에서 MaxId값을 가져온다.
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Integer getAuthMaxId() throws ServiceException{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<AuthTbl> root = cq.from(AuthTbl.class);
		Path<Integer> authId = root.get(AuthTbl_.authId);

		cq.select(cb.max(authId));

		return (Integer) em.createQuery(cq).getSingleResult();

	}


	/**
	 * AuthTbl,RoleAuthTbl 의 데이터를 일괄 insert한다.
	 * @param search
	 * @throws Exception 
	 */
	@Modifying
	@Transactional
	public void updateRoleAuth(Search search) throws Exception{

		if(logger.isDebugEnabled()){
			logger.debug("search.getControlGubun() : "+search.getControlGubun());
		}
		
		try{
			AuthTbl delAuthTbl = authDao.findOne(Integer.parseInt(search.getAuthId()));
			Set<RoleAuthTbl> authTbls = delAuthTbl.getRoleAuth();

			//AuthTbl,RoleAuthTbl의 데이터를 일괄 삭제한다.
			//roleAuthDao.deleteInBatch(authTbls);
		}catch(Exception e){
			throw new Exception("",e);
		}

		String controlGubun = search.getControlGubun();

		//ex) controlGubuns = R(읽기),RW(쓰기),L(제한)
		String[] controlGubuns = controlGubun.split(",");

		AuthTbl authTbl = getAuthObj(Integer.parseInt(search.getAuthId()));

		authTbl.setAuthNm(search.getAuthNm());
		authTbl.setAuthSubNm(search.getAuthSubNm());
		authTbl.setUseYn(search.getUseYn());
		authTbl.setModDt(new Date());

		//menuTbl에 menu id값은 2,3,4,5,6
		//ex)menuid =  2 클립검색, 3	컨텐츠관리, 4	작업관리, 5	통계 6 관리
		for(int i = 0; i <= (controlGubuns.length -1); i++){

			RoleAuthTbl roleAuthTbl = new RoleAuthTbl();
			RoleAuthId roleAuthId = new RoleAuthId();

			roleAuthId.setAuthId(Integer.parseInt(search.getAuthId()));
			roleAuthId.setMenuId(i + 2);
			roleAuthTbl.setId(roleAuthId);
			roleAuthTbl.setControlGubun(controlGubuns[i]);	

			roleAuthServices.add(roleAuthTbl);
			authDao.save(authTbl);
		}
	}
	
	
	/**
	 * 권한정보와 권한 설정을 저장한다.
	 * @param search
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void saveRoleAuth(Search search) throws ServiceException{

		AuthTbl authTbl = new AuthTbl();

		if(logger.isDebugEnabled()){
			logger.debug("search.getControlGubun() : "+search.getControlGubun());
		}

		String controlGubun = search.getControlGubun();
		String[] controlGubuns = controlGubun.split(",");

		authTbl.setUseYn(search.getUseYn());
		authTbl.setAuthNm(search.getAuthNm());
		authTbl.setAuthSubNm(search.getAuthSubNm());
		authTbl.setRegDt(new Date());
		authTbl.setUseYn(search.getUseYn());

		add(authTbl);

		//authTbl에서 MaxId값을 가져온다.
		Integer authMaxId = getAuthMaxId();

		if(logger.isDebugEnabled()){
			logger.debug(controlGubun);
			logger.debug("authMaxId : "+authMaxId);
		}
		
		//menuTbl에 menu id값은 2,3,4,5,6
		//ex)menuid =  2 클립검색, 3	컨텐츠관리, 4	작업관리, 5	통계,6 관리 

		for(int i = 0; i <= (controlGubuns.length -1); i++){

			RoleAuthTbl roleAuthTbl = new RoleAuthTbl();
			RoleAuthId roleAuthId = new RoleAuthId();
			
			roleAuthTbl.setControlGubun(controlGubuns[i]);	

			roleAuthId.setAuthId(authMaxId);
			//menuId는 2부터 시작.
			roleAuthId.setMenuId(i + 2);
			roleAuthTbl.setId(roleAuthId);
			roleAuthServices.add(roleAuthTbl);
		}
	}


	/**
	 * auth테이블이 총 갯수를 얻어온다
	 * @param id
	 * @return
	 */
	public long findAuthCount() throws ServiceException{
		return authDao.count();
	}
}
