package kr.co.d2net.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dto.BusiPartnerTbl;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.BusiPartner;
import kr.co.d2net.dto.vo.ProBusi;
import kr.co.d2net.dto.vo.Profile;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.BusiPartnerServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ProBusiServices;
import kr.co.d2net.service.ProFlServices;
import kr.co.d2net.service.RoleAuthServices;
import kr.co.d2net.service.UserServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



/**
 * 비지니스파트너와 관련된 업무로직이 구현된 class
 * @author vayne
 *
 */

@Controller
public class BusiPartnerController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private AuthServices authServices;

	@Autowired
	private RoleAuthServices roleAuthServices;

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ProFlServices proFlServices;

	@Autowired
	private UserServices userServices;

	@Autowired
	private BusiPartnerServices busiPartnerServices;

	@Autowired
	private ProBusiServices proBusiServices;



	/**
	 * 비지니스파트너 화면 로딩시 필요한 코드값을 불러온다(사용기능 없음.)
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/admin/busipartner/busipartner.ssc", method = RequestMethod.GET)
	public ModelMap busiPartner(ModelMap map) {

		try {
			List<Profile>profiles = proFlServices.findProfileList();
			
			map.addAttribute("proFlTbl", profiles);
			map.addAttribute("search", new Search());
			map.addAttribute("profile", new ProFlTbl());
			map.addAttribute("business", new BusiPartnerTbl());
			map.addAttribute("result", "Y");
		} catch (ServiceException e) {
			map.addAttribute("result", "N");
			map.addAttribute("reason", e.getMessage());
		}

		return map;
	}



	/**
	 * 권한별 리스트 기본정보를 가져온다.
	 * @param partnerTbl
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/busipartner/findBusiPartnerList.ssc", method = RequestMethod.GET)
	public ModelAndView findBusiPartnerList(@ModelAttribute("business") BusiPartnerTbl partnerTbl,@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{

			if(search.getPageNo() == null){
				search.setPageNo(0);
			}

			//User의 카운터 수를 조회.
			Long totalCount = busiPartnerServices.getAllBusiPartnerCount();
			//비지니스피트너의 리스트를 조회.
			List<BusiPartner> busiPartners = busiPartnerServices.findBusiPartnerList();

			view.addObject("busiPartnerTbls", busiPartners);
			view.addObject("search", search);
			view.addObject("totalCount",totalCount);
			view.addObject("pageSize",SearchControls.USER_LIST_COUNT);
			view.addObject("result", "Y");
			view.setViewName("jsonView");

		}catch(ServiceException e) {
			view.addObject("result", "N");
			view.addObject("reason", e.getMessage());		
			view.addObject("userInfos", Collections.EMPTY_LIST);
			view.addObject("search", search);
			view.addObject("totalCount",0);
			view.addObject("pageSize",SearchControls.USER_LIST_COUNT);
			view.setViewName("jsonView");
			return view;
		}
		return view;
	}


	/**
	 * 비지니스파트너의 기본정보를 가져온다
	 * @param partnerTbl
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/business/getBusipartnerInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getBusipartnerInfo(@ModelAttribute("business") BusiPartnerTbl partnerTbl,@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{

			List<Profile> profiles = proFlServices.findProfileList();
			List<BusiPartner> busiPartners = busiPartnerServices.findBusiPartnerInfo(partnerTbl.getBusiPartnerId());
			List<ProBusi> proBusiTbls = proBusiServices.findProBusiInfo(partnerTbl.getBusiPartnerId());

			view.addObject("result","Y");
			view.addObject("proFlTbl", profiles);
			view.addObject("proBusiTbls", proBusiTbls);
			view.addObject("busiPartnerTbls", busiPartners);
			view.setViewName("jsonView");
			return view;

		} catch (ServiceException e) {
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			return view;
		}

	}


	/**
	 * 권한정보를 저장한다
	 * @param partnerTbl
	 * @param proFlTbl
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/business/saveBusiInfo.ssc", method = RequestMethod.POST)
	public ModelAndView saveBusiInfo(@ModelAttribute("business") BusiPartnerTbl partnerTbl)  {

		ModelAndView view = new ModelAndView();

		try{
			busiPartnerServices.saveBusiPartner(partnerTbl);

			Long tmp_maxId = busiPartnerServices.getBusiPartnerMaxId();
			partnerTbl.setBusiPartnerId(tmp_maxId);

			proBusiServices.saveBusiInfo(partnerTbl);

			view.setViewName("jsonView");
			view.addObject("result","Y");
			return view;

		} catch (ServiceException e) {
			view.setViewName("jsonView");
			view.addObject("result","N");
			
			
			if(logger.isErrorEnabled()){
				logger.error("saveBusiInfo",e);
			}
			
			return view;
		}

	}

	/**
	 * 권한정보를 업데이트한다
	 * @param partnerTbl
	 * @param proFlTbl
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/business/updateBusiInfo.ssc", method = RequestMethod.POST)
	public ModelAndView updateBusiInfo(@ModelAttribute("business") BusiPartnerTbl partnerTbl,@ModelAttribute("profile") ProFlTbl proFlTbl,@ModelAttribute("search") Search search)  {

		ModelAndView view = new ModelAndView();

		try{
			
			if(logger.isDebugEnabled())
				logger.debug("partnerTbl.getBusiPartnerId() : " + partnerTbl.getBusiPartnerId());
			//비지니스파트너 정보를 업데이트.
			busiPartnerServices.updateBusiPartner(partnerTbl);
			//proBusiTbl의 비지니스 파트너 정보 삭제.
			proBusiServices.deleteBusiInfo(partnerTbl, proFlTbl, search);
			//proBusiTbl의 비지니스 파트너 정보 저장.
			proBusiServices.saveBusiInfo(partnerTbl);

			view.setViewName("jsonView");
			view.addObject("result","Y");
			return view;

		} catch (ServiceException e) {
			view.setViewName("jsonView");
			view.addObject("result","N");
			return view;
		}

	}


}
