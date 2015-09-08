package kr.co.d2net.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Code;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.CodeServices;

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
	private CodeServices codeServices;



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

		if(logger.isInfoEnabled()){
			logger.info("codeTbl.getClfCd() : "+codeId.getClfCD());
			logger.info("codeTbl.getClfNM() : "+codeTbl.getClfNM());
			logger.info("codeTbl.getClfGubun() : "+codeTbl.getClfGubun());
			logger.info("codeTbl.getCreateWay() : "+codeTbl.getCreateWay());
		}
		
		ModelAndView result = new ModelAndView();
		codeTbl.setId(codeId);

		try {
			
			//clfcd에 소속된 코드정보의 총건수를 조회한다
			long count = codeServices.countClfCount(codeTbl);
			
			if(logger.isDebugEnabled()){
				logger.debug("count : " + count);
			}

			//sclcd의 생성방식에 따라 저장 방식을 달리한다
			if(codeTbl.getCreateWay().equals("auto")){
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
	@RequestMapping(value = "/admin/code/findCodeList.ssc", method = RequestMethod.GET)
	public ModelAndView findCodeList(@ModelAttribute("codeTbl") CodeTbl codeTbl,@ModelAttribute("search") Search search,@ModelAttribute("codeId") CodeId codeId)  {

		if(logger.isDebugEnabled()){
			logger.debug("getGubun "+codeTbl.getGubun());
			logger.debug("getKeyWord "+codeTbl.getKeyWord());
			logger.debug("search.getPageNo() : "  + search.getPageNo());
		}

		ModelAndView result = new ModelAndView();
		
		if(search.getPageNo() == null){
			search.setPageNo(0);
		}

		codeTbl.setClfCd(codeId.getClfCD());

		try {
			
			//clfcd에 소속되어있는 코드 정보의 갯수를 조회한다
			Long totalCount = codeServices.CountCodeInfo(codeTbl);

			//조회값이 -이면 오류로 리턴
			if(totalCount == -1){
				result.addObject("result","N");
				result.addObject("reason",messageSource.getMessage("error.004", null, null));
				result.setViewName("jsonView");
		
				return result;
			}
			
			//clfcd에 소속된 코드의 정보를 조회한다
			List<CodeTbl> codeInfos  =  codeServices.findCodeInfos(codeTbl,search);
			List<Code> codes = new ArrayList<Code>();
			
			for(CodeTbl list : codeInfos){
				Code code = new Code();
				code.setClfCd(list.getId().getClfCD());
				code.setSclCd(list.getId().getSclCd());
				code.setSclNm(list.getSclNm());
				code.setClfGubun(list.getClfGubun());
				code.setClfNm(list.getClfNM());
				code.setCodeCont(list.getCodeCont());
				code.setCreateWay(list.getCreateWay());
				code.setGubun(list.getGubun());
				code.setRmk1(list.getRmk1());
				code.setRmk2(list.getRmk2());
				code.setUseYn(list.getUseYn());
				code.setRegDt(list.getRegDt());
				code.setCodeCont(list.getCodeCont());
				codes.add(code);
			}
			
			result.addObject("totalCount",totalCount);
			result.addObject("codeInfos",codes);
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
	 * 코드관리에서 분류코드명 콤보박스 데이터를 불러오는 메서드.
	 * @param codeTbl
	 * @param search
	 * @param codeId
	 * @return
	 */
	@RequestMapping(value = "/admin/code/findCodeClfList.ssc", method = RequestMethod.GET)
	public ModelAndView findCodeClfList(@ModelAttribute("codeTbl") CodeTbl codeTbl,@ModelAttribute("search") Search search,@ModelAttribute("codeId") CodeId codeId)  {

		ModelAndView result = new ModelAndView();

		try {
			//clfcd의 정보를 모두 조회한다
			List<Code> codes = codeServices.findCodeClfList();

			result.addObject("codes",codes);
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
	 * 단일 코드 정보를 조회한다.
	 * @param clfCd
	 * @param sclCd
	 * @return
	 */
	@RequestMapping(value = "/admin/code/getCodeInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getCodeInfo(@RequestParam(value = "clfCd", required = false) String clfCd, @RequestParam(value = "sclCd", required = false) String sclCd)  {

		ModelAndView result = new ModelAndView();
		CodeTbl codeTbl = new CodeTbl();
		Code code = new Code();
		CodeId id = new CodeId();
		CodeTbl codeinfo = new CodeTbl();

		id.setClfCD(clfCd);
		id.setSclCd(sclCd);

		codeTbl.setId(id);

		try {
			
			//clfcd, sclcd의 메타 정보를 조회한다
			codeinfo = codeServices.getCodeInfo(codeTbl);
			
			code.setClfCd(codeinfo.getId().getClfCD());
			code.setSclCd(codeinfo.getId().getSclCd());
			code.setSclNm(codeinfo.getSclNm());
			code.setClfGubun(codeinfo.getClfGubun());
			code.setClfNm(codeinfo.getClfNM());
			code.setCodeCont(codeinfo.getCodeCont());
			code.setCreateWay(codeinfo.getCreateWay());
			code.setGubun(codeinfo.getGubun());
			code.setRmk1(codeinfo.getRmk1());
			code.setRmk2(codeinfo.getRmk2());
			code.setUseYn(codeinfo.getUseYn());
			code.setRegDt(codeinfo.getRegDt());
			code.setCodeCont(codeinfo.getCodeCont());

			if(logger.isDebugEnabled()){
				logger.debug("codeinfo.getCodeCont() : "+codeinfo.getCodeCont());
			}

			result.addObject("codeinfo",code);
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

		if(logger.isInfoEnabled()){
			logger.info("codeId.getClfCD() : "+codeId.getClfCD());
			logger.info("codeId.getSclCd() : "+codeId.getSclCd());
		}
		
		ModelAndView result = new ModelAndView();

		codeTbl.setId(codeId);
		
		try {

			//코드정보 수정
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

			//코드리스트 조회
			List<Code> clfInfos = codeServices.findClfInfoList();

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

		if(logger.isInfoEnabled()){
			logger.info("codeId.getClfCD() : "+codeId.getClfCD());
			logger.info("codeId.getSclCd() : "+codeId.getSclCd());
		}
		
		ModelAndView result = new ModelAndView();

		codeTbl.setId(codeId);
		
		try {

			//신규등록
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


}
