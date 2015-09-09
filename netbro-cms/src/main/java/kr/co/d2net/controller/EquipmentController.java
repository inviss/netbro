package kr.co.d2net.controller;

import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dao.TraDao;
import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.EquipmentServices;
import kr.co.d2net.service.RoleAuthServices;
import kr.co.d2net.service.TraServices;

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
 * EquipmentTbl 관련된 업무로직이 구현된 class
 * @author vayne
 *
 */

@Controller
public class EquipmentController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private ContentsServices contentsServices;

	@Autowired
	private ContentsInstServices contentsInstServices;

	@Autowired
	private EquipmentServices equipmentServices;

	@Autowired
	private AuthServices authServices;

	@Autowired
	private RoleAuthServices roleAuthServices;

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private AttachServices attachServices;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TraServices traServices;

	@Autowired
	private TraDao traDao;



	/**
	 * 
	 * @param map
	 * @return
	 */

	@RequestMapping(value="/admin/equipment/equipment.ssc", method = RequestMethod.GET)
	public ModelMap equipment(ModelMap map){

		map.addAttribute("search", new Search());
		map.addAttribute("equipTbl", new EquipmentTbl());

		return map;
	}



	/**
	 * Equipment리스트 정보를 가져온다.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/equipment/findEquipmentList.ssc", method = RequestMethod.POST)
	public ModelAndView findEquipmentList(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();
		Long totalCount = null;

		try{

			if(search.getPageNo() == null){
				search.setPageNo(0);
			}


			List<EquipmentTbl> equipmentTbls  =  equipmentServices.findEquipInfos(search);



			totalCount = equipmentServices.count(search);
			if(totalCount == -1){
				view.addObject("result","N");
				view.addObject("reason","DB조회중 오류가 발생하였습니다 담당자에게 문의하십시오");
				view.setViewName("jsonView");
				return view;
			}
			for(EquipmentTbl equipmentTbl : equipmentTbls){
				equipmentTbl.setTra(null);
				equipmentTbl.setTrs(null);
			}

			view.addObject("search", search);
			view.addObject("result","Y");
			view.addObject("equipmentTbls", equipmentTbls);
			view.addObject("totalCount",totalCount);
			view.addObject("pageSize",SearchControls.USER_LIST_COUNT);

			view.setViewName("jsonView");

			return view;

		} catch (ServiceException e) {

			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			return view;
		}

	}


	/**
	 * Equipment리스트 정보를 가져온다.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/equipment/getEquipmentInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getEquipmentInfo(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{

			if (search.getPageNo() == null || search.getPageNo() == 0) {
				search.setPageNo(1);
			}else{
				search.setPageNo(search.getPageNo()+1);
			}

			EquipmentTbl equipmentTbl = new EquipmentTbl();

			equipmentTbl = equipmentServices.findOne(search.getDeviceId());

			equipmentTbl.setTra(null);
			equipmentTbl.setTrs(null);

			view.addObject("search", search);
			view.addObject("result","Y");
			view.addObject("equipmentTbl", equipmentTbl);

			view.setViewName("jsonView");
			
			return view;

		} catch (ServiceException e) {
			
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			return view;
		}
		
	}


	/**
	 * 장비정보를 save한다.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/equipment/saveEquipInfo.ssc", method = RequestMethod.POST)
	public ModelAndView saveEquipInfo(@ModelAttribute("equipTbl")EquipmentTbl eqTbl) {

		ModelAndView view = new ModelAndView();
		try{

			equipmentServices.saveEquipInfo(eqTbl);

			view.addObject("result","Y");
			view.setViewName("jsonView");
			
			return view;
		} catch (ServiceException e) {
			
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			return view;
		}
	
	}


	/**
	 * 장비정보를 save한다.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/equipment/updateEquipInfo.ssc", method = RequestMethod.POST)
	public ModelAndView updateEquipInfo(@ModelAttribute("equipTbl")EquipmentTbl eqTbl) {

		ModelAndView view = new ModelAndView();
		try{
			
			logger.debug("eqTbl : " + eqTbl.getDeviceClfCd());
			

			equipmentServices.updateEquipInfo(eqTbl);

			view.addObject("result","Y");
			view.setViewName("jsonView");

			return view;
		} catch (ServiceException e) {
			
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			return view;
		}
	
	}
}
