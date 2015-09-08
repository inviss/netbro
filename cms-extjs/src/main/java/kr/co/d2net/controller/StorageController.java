package kr.co.d2net.controller;

import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.StorageTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Storage;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.CpuAndMemServices;
import kr.co.d2net.service.StorageServices;

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
 * 스토리지 용량체크,저장과 관련된 업무로직이 구현된 class
 * @author vayne
 *
 */

@Controller
public class StorageController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private CpuAndMemServices cpuAndMemServices;

	@Autowired
	private StorageServices storageServices;



	/**
	 * 스토리지 화면 로딩시 필요한 코드값을 불러온다(사용기능 없음.)
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/admin/storage/storage.ssc", method = RequestMethod.GET)
	public ModelMap storage(ModelMap map) {

		map.addAttribute("search", new Search());
		map.addAttribute("EquipmentTbl", new EquipmentTbl());

		return map;
	}




	/**
	 * 스토리지 임계치값을 저장한다.
	 * @param search
	 * @param storageTbl
	 * @return
	 */

	@RequestMapping(value = "/admin/storage/saveStorageInfo.ssc", method = RequestMethod.POST)
	public ModelAndView saveStorageLimit(@ModelAttribute("search") Search search,@ModelAttribute("storageTbl") StorageTbl storageTbl) {

		ModelAndView view = new ModelAndView();

		try{
			storageServices.saveStorageInfo(storageTbl);

			view.setViewName("jsonView");
			view.addObject("result","Y");
			return view;
		}catch(ServiceException e){
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			return view;
		}

	}



	/**
	 * 스토리지 리스트정보를 불러온다.
	 * @param search
	 * @param storage
	 * @return
	 */
	@RequestMapping(value = "/admin/storage/findStorageList.ssc", method = RequestMethod.GET)
	public ModelAndView findStorageList(@ModelAttribute("search") Search search,@ModelAttribute("highStorage") StorageTbl storage) {

		ModelAndView view = new ModelAndView();

		try{
			List<Storage>storages = storageServices.findStorageList();

			view.addObject("result","N");
			view.addObject("storageTbls",storages);
			view.setViewName("jsonView");
			return view;

		}catch(ServiceException e){
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			return view;
		}

	}


	/**
	 * 스토리지 리스트에서 클릭한 정보값을 가져온다.
	 * @param search
	 * @param storage
	 * @return
	 */
	@RequestMapping(value = "/admin/storage/getStorageInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getStorageList(@ModelAttribute("search") Search search,@ModelAttribute("storage") StorageTbl storage) {

		ModelAndView view = new ModelAndView();

		if(logger.isInfoEnabled()){
			logger.info("storage.getStorageId() : "  + storage.getStorageId());
		}

		try{
			//List<Storage> listStorage = storageServices.getStorageInfo(storage.getStorageId());
			Storage listStorage = storageServices.getStorageInfo(storage.getStorageId());

			view.addObject("result","N");
			view.addObject("storageTbl",listStorage);
			view.setViewName("jsonView");
			return view;

		}catch(ServiceException e){
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			return view;
		}

	}


}
