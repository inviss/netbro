package kr.co.d2net.controller;

import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dao.EquipmentDao;
import kr.co.d2net.dao.TraDao;
import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.HighStorageTbl;
import kr.co.d2net.dto.LowStorageTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.CpuAndMem;
import kr.co.d2net.dto.vo.Monitoring;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.CpuAndMemServices;
import kr.co.d2net.service.EquipmentServices;
import kr.co.d2net.service.HighStorageServices;
import kr.co.d2net.service.LowStorageServices;
import kr.co.d2net.service.RoleAuthServices;
import kr.co.d2net.service.TraServices;

import org.hyperic.sigar.SigarException;
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
 * 트렌스코딩과 관련된 업무로직이 구현된 class
 * @author vayne
 *
 */

@Controller
public class MonitoringController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private ContentsServices contentsServices;

	@Autowired
	private ContentsInstServices contentsInstServices;

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

	@Autowired
	private EquipmentDao equipmentDao;

	@Autowired
	private EquipmentServices equipmentServices;

	@Autowired
	private CpuAndMemServices cpuAndMemServices;

	@Autowired
	private HighStorageServices highStorageServices;

	@Autowired
	private LowStorageServices lowStorageServices;

	/**
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/admin/monitoring/monitoring.ssc", method = RequestMethod.GET)
	public ModelMap userAuth(ModelMap map) {

		map.addAttribute("search", new Search());
		map.addAttribute("highStorage", new HighStorageTbl());
		map.addAttribute("lowStorage", new LowStorageTbl());
		map.addAttribute("EquipmentTbl", new EquipmentTbl());

		return map;
	}

	/**
	 * 모니터링 리스트정보를 가져온다.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/monitoring/findMonitoringList.ssc", method = RequestMethod.GET)
	public ModelAndView findMonitoringList(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{

			List<Monitoring> monitorings = equipmentServices.findEquipmentInfos(search);

			view.addObject("monitorings", monitorings);
			view.addObject("search", search);
			view.addObject("result","Y");
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
	 * 모니터링 그래프정보를 가져온다.
	 * sigar를 이용해 가져온다.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/admin/monitoring/findCpuMemMonitoringGraph.ssc", method = RequestMethod.GET)
	public ModelAndView findCpuMemMonitoringGraph(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{

			CpuAndMem cpu = cpuAndMemServices.getCpuObj();   
			CpuAndMem mem = cpuAndMemServices.getMemObj();

			view.addObject("search", search);
			view.addObject("cpu", cpu);
			view.addObject("mem", mem);

			view.addObject("result","Y");
			view.setViewName("jsonView");
			
			return view;

		}catch(ServiceException e){
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
			
			return view;
			
		} catch (SigarException e) {
			view.addObject("result","N");
			view.addObject("reason", "sigar로드중 에러 발생. 관리자에게 문의");
			view.setViewName("jsonView");
			return view;
		} catch (InterruptedException e) {
			view.addObject("result","N");
			view.addObject("reason", "sigar로드중 에러 발생. 관리자에게 문의");
			view.setViewName("jsonView");
			return view;
		}
		
	}
	
	
	
	
	
	@RequestMapping(value = "/admin/monitoring/findStorageMonitoringGraph.ssc", method = RequestMethod.GET)
	public ModelAndView findStorageMonitoringGraph(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{

			HighStorageTbl  highStorageTbl = highStorageServices.getHighStorage(1);
			LowStorageTbl  lowStorageTbl = lowStorageServices.getLowStorage(1);

			view.addObject("search", search);
			view.addObject("highStorageTbl", highStorageTbl);
			view.addObject("lowStorageTbl", lowStorageTbl);
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
	
	
	
	

	@RequestMapping(value = "/admin/monitoring/saveStorageLimit.ssc", method = RequestMethod.POST)
	public ModelAndView saveStorageLimit(@ModelAttribute("search") Search search,@ModelAttribute("highStorage") HighStorageTbl highStorage,@ModelAttribute("lowStorage") LowStorageTbl lowStorage) {

		ModelAndView view = new ModelAndView();

		if(logger.isDebugEnabled()){
			logger.debug("highStorage : "  + highStorage.getHighLimit());
			logger.debug("highStorage : "  + lowStorage.getLowLimit());
		}

		try{

			HighStorageTbl highStorageTbl = highStorageServices.getHighStorage(1);

			if(highStorage.getHighLimit() != 0){
				highStorageTbl.setHighLimit(highStorage.getHighLimit());
				highStorageServices.add(highStorageTbl);
			}

			LowStorageTbl lowStorageTbl = lowStorageServices.getLowStorage(1);

			if(lowStorage.getLowLimit() != 0){

				lowStorageTbl.setLowLimit(lowStorage.getLowLimit());
				lowStorageServices.add(lowStorageTbl);
			}

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


}
