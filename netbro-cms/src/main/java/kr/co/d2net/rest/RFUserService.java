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
import kr.co.d2net.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Path("/member")
@Service("rfUserService")
public class RFUserService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserServices userServices;
	@Autowired
	private UserAuthServices userAuthServices;
	
	@GET
	@Path("/valid/{userId}/{userPass}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public User checkMember(@PathParam("userId") String userId, @PathParam("userPass") String userPass) throws ServiceException {
		
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(userPass)) {
			return null;
		}
		
		String useYn = "Y";
		
		UserTbl userTbl = userServices.getLoginUserInfoYn(userId, userPass,useYn);
		if(userTbl != null && StringUtils.isNotBlank(userTbl.getUserId())) {
			User user = new User();
			user.setUserId(userId);
			user.setUserPass(userPass);
			user.setAuthIds( userTbl.getUserAuths());
			return user;
		}
		
		return null;
	}
}
