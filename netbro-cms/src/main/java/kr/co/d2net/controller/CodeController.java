package kr.co.d2net.controller;

import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.RoleAuthServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 코드관련 제어함수가 모여있는 class
 * @author asura
 *
 */
@Controller
public class CodeController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private ContentsServices contentsServices;

	@Autowired
	private ContentsInstServices contentsInstServices;

	@Autowired
	private CodeServices codeServices;

	@Autowired
	private RoleAuthServices roleAuthServices;

	@Autowired
	private CategoryServices categoryServices;

	@Autowired
	private AttachServices attachServices;

	@Autowired
	private MessageSource messageSource;


	/**
	 * 코드 화면 로딩시 필요한 코드값을 불러온다(사용기능 없음.)
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/admin/code/code.ssc", method = RequestMethod.GET)
	public ModelMap code(ModelMap map)  {

		map.addAttribute("search", new Search());
		map.addAttribute("codeTbl", new CodeTbl());
		map.addAttribute("codeId", new CodeId());

		return map;

	}



	/**
	 * 분류코드를 신규로 생성한다. 분류코드는 자동, 생성여부에 따라 각각 생성한다.
	 * @param clfNm
	 * @return
	 */
	@RequestMapping(value = "/admin/code/insertClfInfo.ssc", method = RequestMethod.POST)
	public ModelAndView insertClfInfo(@ModelAttribute("codeTbl") CodeTbl codeTbl,@ModelAttribute("codeId") CodeId codeId)  {

		ModelAndView result = new ModelAndView();

		if(logger.isDebugEnabled()){

			logger.debug("codeTbl.getClfCd() : "+codeId.getClfCD());
			logger.debug("codeTbl.getClfNM() : "+codeTbl.getClfNM());
			logger.debug("codeTbl.getClfGubun() : "+codeTbl.getClfGubun());
			logger.debug("codeTbl.getCreateWay() : "+codeTbl.getCreateWay());

		}
		codeTbl.setId(codeId);
		try {

			long count = codeServices.findClfCdInfos(codeTbl);

			if(codeTbl.getCreateWay().equals("auto")){
				codeServices.insertClfInfo(codeTbl);
				result.addObject("result","Y");
			}else if( codeTbl.getCreateWay().equals("manual") && count == 0 ){
				codeServices.insertClfInfo(codeTbl);
				result.addObject("result","Y");
			}else if(count == -1){
				result.addObject("result","N");
				result.addObject("reason",messageSource.getMessage("error.004", null, null));

			}else{
				result.addObject("result","N");
				result.addObject("reason",messageSource.getMessage("error.012", null, null));
			}



			result.setViewName("jsonView");

			return result;
		} catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}

	}



	/**
	 * 코드 정보를 조회한다.
	 * @param clfCd
	 * @param sclCd
	 * @return
	 */
	@RequestMapping(value = "/admin/code/findCodeList.ssc", method = RequestMethod.POST)
	public ModelAndView findCodeList(@ModelAttribute("codeTbl") CodeTbl codeTbl,@ModelAttribute("search") Search search,@ModelAttribute("codeId") CodeId codeId)  {

		ModelAndView result = new ModelAndView();

		if(logger.isDebugEnabled()){

			logger.debug("getGubun "+codeTbl.getGubun());
			logger.debug("getKeyWord "+codeTbl.getKeyWord());

		}

		CodeId id = new CodeId();

		codeTbl.setClfCd(codeId.getClfCD());

		try {

			Long totalCount = codeServices.CountCodeInfo(codeTbl);

			if(search.getPageNo() == null){

				search.setPageNo(0);

			}

			//조회값이 -이면 오류로 리턴
			if(totalCount == -1){
				result.addObject("result","N");
				result.addObject("reason",messageSource.getMessage("error.004", null, null));
				result.setViewName("jsonView");
				return result;
			}
			List<CodeTbl> codeInfos  =  codeServices.getCodeInfos(codeTbl,search);

			result.addObject("totalCount",totalCount);
			result.addObject("codeInfos",codeInfos);
			result.addObject("pageSize",SearchControls.CODE_LIST_COUNT);
			result.addObject("result","Y");
			result.setViewName("jsonView");
			logger.debug("result  "+result);
			return result;
		}  catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}


	}



	/**
	 * 단일 코드 정보를 조회한다.
	 * @param clfCd
	 * @param sclCd
	 * @return
	 */
	@RequestMapping(value = "/admin/code/getCodeInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getCodeInfo(@RequestParam(value = "clfCd", required = false) String clfCd,
			@RequestParam(value = "sclCd", required = false) String sclCd)  {

		ModelAndView result = new ModelAndView();
		CodeTbl code = new CodeTbl();
		CodeId id = new CodeId();

		id.setClfCD(clfCd);
		id.setSclCd(sclCd);

		code.setId(id);
		CodeTbl codeinfo = new CodeTbl();
		try {

			codeinfo = codeServices.getCodeInfo(code);
			if(codeinfo == null) {
				result.addObject("result", "N");
				result.addObject("reason", messageSource.getMessage("error.001", null, null));
				result.setViewName("jsonView");	
				return result;
			}
			if(logger.isDebugEnabled()){

				logger.debug("codeinfo.getCodeCont() : "+codeinfo.getCodeCont());

			}

			result.addObject("codeinfo",codeinfo);
			result.addObject("result", "Y");
			result.setViewName("jsonView");

			return result;
		}  catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}


	}


	/**
	 * 분류코드 상세 정보를 업데이트한다.
	 * @param codeTbl
	 * @param codeId
	 * @return
	 */
	@RequestMapping(value = "/admin/code/updateCodeInfo.ssc", method = RequestMethod.POST)
	public ModelAndView updateCodeInfo(@ModelAttribute("codeTbl") CodeTbl codeTbl,@ModelAttribute("codeId") CodeId codeId)  {

		ModelAndView result = new ModelAndView();

		codeTbl.setId(codeId);
		try {

			codeServices.updateCodeInfo(codeTbl);

			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}  catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}


	}




	/**
	 * 분류코드 정보를 조회한다.
	 * @return
	 */
	@RequestMapping(value="/admin/code/findClfInfoList.ssc", method = RequestMethod.GET)
	public ModelAndView getClfInfoList()  {

		ModelAndView result = new ModelAndView();
		try {

			List<CodeTbl> clfInfos = codeServices.getClfInfoList();

			result.addObject("clfInfos",clfInfos);

			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}  catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}

	}



	/**
	 * 분류코드 상세 정보를 생성한다.
	 * @param codeTbl
	 * @param codeId
	 * @return
	 */
	@RequestMapping(value = "/admin/code/insertCodeInfo.ssc", method = RequestMethod.POST)
	public ModelAndView insertCodeInfo(@ModelAttribute("codeTbl") CodeTbl codeTbl,@ModelAttribute("codeId") CodeId codeId) 
	{

		ModelAndView result = new ModelAndView();

		codeTbl.setId(codeId);
		try {

			codeServices.insertCodeInfo(codeTbl);

			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}  catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}

	}

	
	/**
	 * 코드 정보를 조회한다.
	 * @param clfCd
	 * @param sclCd
	 * @return
	 */
	@RequestMapping(value = "/admin/code/findSclListForClf.ssc", method = RequestMethod.POST)
	public ModelAndView findSclListForClf(@ModelAttribute("codeId") CodeId codeId)  {

		ModelAndView result = new ModelAndView();

		if(logger.isDebugEnabled()){

			logger.debug("getClfCD "+codeId.getClfCD());

		}

		CodeTbl codeTbl = new CodeTbl();

		codeTbl.setClfCd(codeId.getClfCD());

		try {

			List<CodeTbl> codeInfos  =  codeServices.findSclListForClf(codeTbl);

			result.addObject("codeInfos",codeInfos);
			result.addObject("result","Y");
			result.setViewName("jsonView");
			logger.debug("result  "+result);
			return result;
		}  catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}


	}

}
