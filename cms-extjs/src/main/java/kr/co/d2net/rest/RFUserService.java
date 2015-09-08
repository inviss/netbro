package kr.co.d2net.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.json.User;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.UserAuthServices;
import kr.co.d2net.service.UserServices;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 유저 정보와 관련된 외부 interface 와 통신하는 class.
 * @author Administrator
 *
 */
@Path("/member")
@Service("rfUserService")
public class RFUserService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserServices userServices;
	@Autowired
	private UserAuthServices userAuthServices;
	
	
	/**
	 * 외부 interface와의 통신을 통해(userId,userPasswd) 사용자정보를 조회하는 method.
	 * @param userId
	 * @param userPass
	 * @return
	 * @throws ServiceException
	 */
	@GET
	@Path("/valid/{userId}/{userPass}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public User checkMember(@PathParam("userId") String userId, @PathParam("userPass") String userPass) throws ServiceException {
		
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(userPass)) {
			return null;
		}
		
		if(logger.isInfoEnabled()){
			logger.info("userId : "  + userId);
			logger.info("userPass : "  + userPass);
		}
		
		String useYn = "Y";
		
		UserTbl userTbl = userServices.getLoginUserInfoYn(userId, userPass, useYn);
		
		if(userTbl != null && StringUtils.isNotBlank(userTbl.getUserId())) {
			User user = new User();
			user.setUserId(userId);
			user.setUserPass(userPass);
			user.setAuthIds(userTbl.getUserAuths());
			return user;
		}
		
		return null;
	}
}
