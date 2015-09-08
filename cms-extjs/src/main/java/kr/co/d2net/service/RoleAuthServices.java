package kr.co.d2net.service;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.AuthDao;
import kr.co.d2net.dao.RoleAuthDao;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.RoleAuthTbl.RoleAuthId;
import kr.co.d2net.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class RoleAuthServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RoleAuthDao roleAuthDao;

	@Autowired
	private AuthDao authDao;

	@PersistenceContext
	private EntityManager em;


	@Autowired
	private MessageSource messageSource;

	/**
	 * 
	 * @param roleauths
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void addAll(Set<RoleAuthTbl> roleauths) throws ServiceException{
		roleAuthDao.save(roleauths);
	}

	/**
	 * 
	 * @param roleauth
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void add(RoleAuthTbl roleauth) throws ServiceException{
		roleAuthDao.save(roleauth);
	}


	/**
	 * authId를 이용해 RoleAuth정보를 가져온다.
	 * @param id
	 * @return
	 */
	public RoleAuthTbl getRoleAuthObj(RoleAuthId authId) throws ServiceException{
		if(logger.isDebugEnabled()){
			logger.debug("id.getAuthid() : "+authId.getAuthId());
			logger.debug("id.getMenuId() : "+authId.getMenuId());
		}
		return roleAuthDao.findOne(authId);
	}


	/**
	 * authId를 이용해 RoleAuthTbl의 데이터를 delete한다.
	 * @param authId
	 */
	public void deleteRoleAuth(Integer authId) throws ServiceException{

		AuthTbl authTbl = authDao.findOne(authId);
		Set<RoleAuthTbl> authTbls = authTbl.getRoleAuth();

		//AuthTbl,RoleAuthTbl의 데이터를 일괄 삭제한다.
		roleAuthDao.deleteInBatch(authTbls);
	}



	/**
	 * roleAuthTbl에 데이터가 있는지 여부를 파악한다
	 * @param id
	 * @return
	 */
	public long findRoleAuthCount() throws ServiceException{
		return roleAuthDao.count();
	}

}
