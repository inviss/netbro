package kr.co.d2net.init;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.RoleAuthTbl.RoleAuthId;
import kr.co.d2net.dto.UserAuthTbl.UserAuthId;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.MenuServices;
import kr.co.d2net.service.RoleAuthServices;
import kr.co.d2net.service.UserAuthServices;
import kr.co.d2net.service.UserServices;



public class InitCodes {

	@Autowired
	private CodeServices codeServices;

	@Autowired
	private UserServices userServices;
	@Autowired
	private MenuServices menuServices;
	@Autowired
	private RoleAuthServices roleAuthServices;
	@Autowired
	private UserAuthServices userAuthServices;
	@Autowired
	private AuthServices authServices;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	String[][] ctypList = {
			{"CTYP", "001", "영상유형", "전타이틀"},
			{"CTYP", "002", "영상유형", "전CM"},
			{"CTYP", "003", "영상유형", "본방"},
			{"CTYP", "004", "영상유형", "후CM"},
			{"CTYP", "005", "영상유형", "후타이틀"}
	};
	String[][] cclaList = {
			{"CCLA", "001", "영상구분", "촬영본"},
			{"CCLA", "002", "영상구분", "편집본"},
			{"CCLA", "003", "영상구분", "녹화본"},
			{"CCLA", "004", "영상구분", "방송본"},
			{"CCLA", "005", "영상구분", "클린본"},
			{"CCLA", "006", "영상구분", "복원본"}
	};
	String[][] cfmtList = {
			{"CFMT", "101", "영상포맷", "mxf"},
			{"CFMT", "102", "영상포맷", "mov"},
			{"CFMT", "201", "영상포맷", "mp4"}
	};
	String[][] ristList = {
			{"RIST", "000", "사용제한등급", "설정없음"},
			{"RIST", "002", "사용제한등급", "확인후사용"},
			{"RIST", "003", "사용제한등급", "저작권확인"},
			{"RIST", "004", "사용제한등급", "사용제한"}
	};
	String[][] dataStatList = {
			{"DSCD", "000", "정리상태", "정리전"},
			{"DSCD", "001", "정리상태", "정리중"},
			{"DSCD", "002", "정리상태", "정리완료"},
			{"DSCD", "003", "정리상태", "에러"}
	};
	String[][] authList = {
			{"1", "최고관리자"},
			{"2", "일반관리자"},
			{"3", "제한관리자"},
			{"4", "VOD관리자"},
			{"5", "guest"}
	};
	String[][] userList = {
			{"admin", "bed128365216c019988915ed3add75fb", "1112Z223344","관리자"},
			{"guest", "b59c67bf196a4758191e42f76670ceba", "1112Z223344","게스트"}
	};
	String[][] menuList = {
			{"1", "", "CMS", ""},
			{"2", "clipsrch", "클립검색", "/clip/search.ssc"},
			{"3", "contents", "컨텐츠검색", "/contents/search.ssc"},
			{"4", "del", "폐기검색", "/work/tra/tra.ssc"},
			{"5", "statistic", "통계", "/statistic/statistic.ssc"},
			{"6", "admin", "권한", "/admin/category/category.ssc"},
			{"7", "category", "카테고리", "/admin/category/category.ssc"},
			{"8", "part", "역활", "/admin/category/category.ssc"},
			{"9", "user", "사용자", "/admin/category/category.ssc"},
			{"10", "code", "코드", "/admin/category/category.ssc"},
			{"11", "monitor", "모니터", "/admin/category/category.ssc"},
			{"12", "equip", "장비", "/admin/category/category.ssc"},
			{"13", "notice", "공지사항", "/admin/category/category.ssc"}
	};
	String[][] roleAuthtList = {
			{"1", "1", "RW"},
			{"1", "2", "RW"},
			{"1", "3", "RW"},
			{"1", "4", "RW"},
			{"1", "5", "RW"},
			{"1", "6", "RW"},
			{"1", "7", "RW"},
			{"1", "8", "RW"},
			{"1", "9", "RW"},
			{"1", "10", "RW"},
			{"1", "11", "RW"},
			{"1", "12", "RW"},
			{"1", "13", "RW"},
			{"2", "1", "R"},
			{"2", "2", "R"},
			{"2", "3", "R"},
			{"2", "4", "R"},
			{"2", "5", "R"},
			{"2", "6", "R"},
			{"2", "7", "R"},
			{"2", "8", "R"},
			{"2", "9", "R"},
			{"2", "10", "R"},
			{"2", "11", "R"},
			{"2", "12", "R"},
			{"2", "13", "R"},
			{"3", "1", "R"},
			{"3", "2", "R"},
			{"3", "3", "R"},
			{"3", "4", "R"},
			{"3", "5", "R"},
			{"3", "6", "R"},
			{"3", "7", "R"},
			{"3", "8", "R"},
			{"3", "9", "R"},
			{"3", "10", "R"},
			{"3", "11", "R"},
			{"3", "12", "R"},
			{"3", "13", "R"},
			{"4", "1", "R"},
			{"4", "2", "R"},
			{"4", "3", "R"},
			{"4", "4", "R"},
			{"4", "5", "R"},
			{"4", "6", "R"},
			{"4", "7", "R"},
			{"4", "8", "R"},
			{"4", "9", "R"},
			{"4", "10", "R"},
			{"4", "11", "R"},
			{"4", "12", "R"},
			{"4", "13", "R"},
			{"5", "1", "R"},
			{"5", "2", "L"},
			{"5", "3", "L"},
			{"5", "4", "L"},
			{"5", "5", "L"},
			{"5", "6", "L"},
			{"5", "7", "L"},
			{"5", "8", "L"},
			{"5", "9", "L"},
			{"5", "10", "L"},
			{"5", "11", "L"},
			{"5", "12", "L"},
			{"5", "13", "L"}
	};
	String[][] userAuthList = {
			{"1", "admin"},
			{"2", "guest"}
	};

	public void init(){
		
		if(SystemUtils.IS_OS_WINDOWS)
			System.load(new File("./sigar-amd64-winnt.dll").getAbsolutePath());
		else
			System.load(new File("./libsigar-amd64-linux.so").getAbsolutePath());

		/* 공통코드 테이블의 데이타가 존재하는지 확인하여 없으면 신규등록 */
		/* CTYP, CFMT, RIST, CCLA 별로 각각 체크해서 없으면 등록 */
		/* 공통코드 서비스를 이용하여 List 조회 후 없으면 개별 등록 */

		/* 공통코드 체크 */
		logger.debug("####################");
		/*CTYP 체크 */
		CodeTbl codeTbl = new CodeTbl();
		CodeId id = new CodeId();
		try {

			id.setClfCD("CTYP");
			codeTbl.setId(id);

			long codeCount = codeServices.countClfCount(codeTbl);

			if(codeCount == 0){
				codeInitService(ctypList);
			}
		} catch (ServiceException e) {
			logger.error("error is "+e.getMessage());
		}

		/*CCLA 체크 */
		try {

			id.setClfCD("CCLA");
			codeTbl.setId(id);

			long codeCount = codeServices.countClfCount(codeTbl);

			if(codeCount == 0){
				codeInitService(cclaList);
			}
		} catch (ServiceException e) {
			logger.error("error is "+e);
		}

		/*CFMT 체크 */
		try {

			id.setClfCD("CFMT");
			codeTbl.setId(id);

			long codeCount = codeServices.countClfCount(codeTbl);

			if(codeCount == 0){
				codeInitService(cfmtList);
			}
		} catch (ServiceException e) {
			logger.error("error is "+e);
		}

		/*RIST 체크 */
		try {

			id.setClfCD("RIST");
			codeTbl.setId(id);

			long codeCount = codeServices.countClfCount(codeTbl);

			if(codeCount == 0){
				codeInitService(ristList);
			}
		} catch (ServiceException e) {
			logger.error("error is "+e);
		}

		/*DATASTATCD 체크 */
		try {

			id.setClfCD("DSCD");
			codeTbl.setId(id);

			long codeCount = codeServices.countClfCount(codeTbl);

			if(codeCount == 0){
				codeInitService(dataStatList);
			}
		} catch (ServiceException e) {
			logger.error("error is "+e);
		}
		
		/*USER_TBL 체크 */
		try {

			long codeCount = userServices.findUserCount();

			if(codeCount == 0){
				userInitService(userList);
			}
		} catch (ServiceException e) {
			logger.error("error is "+e);
		}

		/*MENU 체크 */
		try {

			long codeCount = menuServices.findMenuCount();

			if(codeCount == 0){
				menuInitService(menuList);
			}
		} catch (ServiceException e) {
			logger.error("error is "+e);
		}

		/*AUTH 체크 */
		try {

			long codeCount = authServices.findAuthCount();

			if(codeCount == 0){
				authInitService(authList);
			}
		} catch (ServiceException e) {
			logger.error("error is "+e);
		}

		/*ROLE_AUTH_TBL 체크 */
		try {

			long codeCount = roleAuthServices.findRoleAuthCount();

			if(codeCount == 0){
				roleAuthInitService(roleAuthtList);
			}
		} catch (ServiceException e) {
			logger.error("error is "+e);
		}

		/*USER_AUTH_TBL 체크 */
		try {

			long codeCount = userAuthServices.findUserAuthCount();

			if(codeCount == 0){
				userAuthInitService(userAuthList);
			}
		} catch (ServiceException e) {
			logger.error("error is "+e);
		}

	}

	private void codeInitService(String[][] codeList) {
		for(String[] list : codeList) {
			CodeTbl codeTbl = new CodeTbl();

			CodeId codeId = new CodeId();
			codeId.setClfCD(list[0]);
			codeId.setSclCd(list[1]);

			codeTbl.setId(codeId);
			codeTbl.setClfNM(list[2]);
			codeTbl.setSclNm(list[3]);
			codeTbl.setUseYn("Y");
			codeTbl.setClfGubun("S");
			codeTbl.setRegDt(new Date());

			try {

				codeServices.add(codeTbl);

			} catch (ServiceException e) {				
				logger.error("error is "+e);
			}
		}
	}

	private void userInitService(String[][] userList) {
		for(String[] list : userList) {
			UserTbl userTbl = new UserTbl();

			userTbl.setUserId(list[0]);
			userTbl.setUserPass(list[1]);
			userTbl.setUserPhone(list[2]);
			userTbl.setRegDt(new Date());
			userTbl.setUserNm(list[3]);
			userTbl.setUseYn("Y");
			try {

				userServices.add(userTbl);

			} catch (ServiceException e) {				
				logger.error("error is "+e);
			}
		}
	}


	private void menuInitService(String[][] menuList) {
		for(String[] list : menuList) {
			MenuTbl menuTbl = new MenuTbl();
			menuTbl.setMenuId(Integer.parseInt(list[0]));
			menuTbl.setMenuEnNm(list[1]);
			menuTbl.setMenuNm(list[2]);
			menuTbl.setUrl(list[3]);
			menuTbl.setUseYn("Y");
			menuTbl.setRegDt(new Date());
			try {

				menuServices.add(menuTbl);

			} catch (ServiceException e) {				
				logger.error("error is "+e);
			}
		}
	}

	private void roleAuthInitService(String[][] rolAuthList) {
		for(String[] list : rolAuthList) {
			RoleAuthTbl roleAuthTbl = new RoleAuthTbl();
			RoleAuthId id = new RoleAuthId();
			id.setAuthId(Integer.parseInt(list[0]));
			id.setMenuId(Integer.parseInt(list[1]));
			roleAuthTbl.setId(id);
			roleAuthTbl.setControlGubun(list[2]);
			try {

				roleAuthServices.add(roleAuthTbl);

			} catch (ServiceException e) {				
				logger.error("error is "+e);
			}
		}
	}

	private void userAuthInitService(String[][] userAuthList) {
		for(String[] list : userAuthList) {
			UserAuthTbl userAuthTbl = new UserAuthTbl();
			UserAuthId id = new UserAuthId();
			id.setAuthId(Integer.parseInt(list[0]));
			id.setUserId(list[1]);
			userAuthTbl.setId(id);
			userAuthTbl.setModDt(new Date());
			try {

				userAuthServices.add(userAuthTbl);

			} catch (ServiceException e) {				
				logger.error("error is "+e);
			}
		}
	}

	private void authInitService(String[][] lists) {
		for(String[] list : lists) {
			AuthTbl authTbl = new AuthTbl();

			authTbl.setAuthId(Integer.parseInt(list[0]));
			authTbl.setAuthNm(list[1]);
			authTbl.setUseYn("Y");
			authTbl.setRegDt(new Date());
			try {
				authServices.add(authTbl);
			} catch (ServiceException e) {				
				logger.error("error is "+e);
			}
		}
	}

}
