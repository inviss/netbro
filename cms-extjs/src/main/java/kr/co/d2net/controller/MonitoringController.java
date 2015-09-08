package kr.co.d2net.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dao.EquipmentDao;
import kr.co.d2net.dao.TraDao;
import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.StorageTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.CpuAndMem;
import kr.co.d2net.dto.vo.Monitoring;
import kr.co.d2net.dto.vo.Storage;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.AuthServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.CpuAndMemServices;
import kr.co.d2net.service.EquipmentServices;
import kr.co.d2net.service.RoleAuthServices;
import kr.co.d2net.service.StorageServices;
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
 * 모니터링과 관련된 업무로직이 구현된 class
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
	private StorageServices storageServices;

	/**
	 * 모니터링 화면 로딩시 필요한 코드값을 불러온다(사용기능 없음.)
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/admin/monitoring/monitoring.ssc", method = RequestMethod.GET)
	public ModelMap monitoring(ModelMap map) {

		map.addAttribute("search", new Search());
		map.addAttribute("EquipmentTbl", new EquipmentTbl());

		return map;
	}

	/**
	 * 모니터링 리스트정보를 가져온다.
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/monitoring/findMonitoringList.ssc", method = RequestMethod.GET)
	public ModelAndView findMonitoringList(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{
			//모니터링 리스트를 가져오는 함수.
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
	@RequestMapping(value = "/monitoring/findCpuMemMonitoringGraph.ssc", method = RequestMethod.GET)
	public ModelAndView findCpuMemMonitoringGraph(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{
			CpuAndMem cpuAndMem = new CpuAndMem();
			//sigar lib를 이용해 cpu,mem 정보를 가져옴.
			cpuAndMem = cpuAndMemServices.getCpuObj(cpuAndMem);   
			cpuAndMem = cpuAndMemServices.getMemObj(cpuAndMem);

			view.addObject("search", search);
			view.addObject("cpuAndMem", cpuAndMem);

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


	/**
	 * 스토리지 용량을 그래프로 나타낸다.(EXTJS전용)
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "/monitoring/findStorageMonitoringGraph.ssc", method = RequestMethod.GET)
	public ModelAndView findStorageMonitoringGraph(@ModelAttribute("search") Search search) {

		ModelAndView view = new ModelAndView();

		try{
			//extjs에 맞도록 총용량, 남은 용량, 사용 용량을 모두 리스트로 각각 담는다
			List<Storage> storages = new ArrayList<Storage>();

			//스토리지에서 고해상도, 저해상도 데이터를 가져온다.
			StorageTbl storageTbl = storageServices.getStorageInfoSigar(search.getStorageId());

			//사용량을 집어넣는다.
			Storage useStorage = new Storage();
			useStorage.setPartNm("스토리지 사용용량");
			useStorage.setVolume(storageTbl.getUseVolume());
			storages.add(useStorage);

			//남은용량을 넣는다.
			Storage idleStorage = new Storage();
			idleStorage.setPartNm("스토리지 남은용량");
			idleStorage.setVolume(storageTbl.getIdleVolume());
			storages.add(idleStorage);

			view.addObject("search", search);
			view.addObject("storageInfo", storages); 
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
	 * 서버 리소스를 조회한다.(EXTJS 전용) 
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/monitor/search.ssc", method = RequestMethod.GET)
	public ModelAndView findServerResource() {
		ModelAndView view = new ModelAndView();
		try {
			CpuAndMem cpuAndMem = new CpuAndMem();
			//sigar lib를 이용해 cpu,mem 정보를 가져옴.
			cpuAndMem = cpuAndMemServices.getCpuObj(cpuAndMem);   
			cpuAndMem = cpuAndMemServices.getMemObj(cpuAndMem);

			view.addObject("cpuAndMem", cpuAndMem);
		}catch(ServiceException e){
			view.addObject("result","N");
			view.addObject("reason", e.getMessage());
			view.setViewName("jsonView");
		} catch (SigarException e) {
			view.addObject("result","N");
			view.addObject("reason", "서버 리소스를 조회 중 오류가 발생 했습니다.");
			view.setViewName("jsonView");
		} catch (InterruptedException e) {
			view.addObject("result","N");
			view.addObject("reason", "서버 리소스를 조회 중 오류가 발생 했습니다.");
			view.setViewName("jsonView");
		}

		return view;
	}

}
