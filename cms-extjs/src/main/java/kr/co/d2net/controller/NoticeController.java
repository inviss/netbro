package kr.co.d2net.controller;

import java.util.List;

import javax.servlet.ServletContext;

import kr.co.d2net.dao.UserDao;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Notice;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.NoticeServices;

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
 * 공지사항관련 제어 함수가 들어있는 class
 * @author asura
 *
 */
@Controller
public class NoticeController {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private NoticeServices noticeServices;


	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserDao userDao;

	/**
	 * 코드 화면 로딩시 필요한 코드값을 불러온다(사용기능 없음.)
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/admin/notice/notice.ssc", method = RequestMethod.GET)
	public ModelMap notice(ModelMap map)  {

		map.addAttribute("search", new Search());
		map.addAttribute("codeTbl", new CodeTbl());
		map.addAttribute("codeId", new CodeId());
		map.addAttribute("notice", new Notice());

		return map;

	}

	/**
	 * 신규로 공지사항을 생성한다.
	 * @param notice
	 * @return
	 */
	@RequestMapping(value = "/admin/notice/insertNoticeInfo.ssc", method = RequestMethod.POST)
	public ModelAndView insertNoticeInfo(@ModelAttribute("notice") Notice notice)  {


		if(logger.isInfoEnabled()){
			logger.info("notice.getCont() : "+notice.getCont());
			logger.info("notice.getPopUpYn() : "+notice.getPopUpYn());
			logger.info("notice.getRegId() : "+notice.getRegId());
			logger.info("notice.getStartDd() : "+notice.getStartDd());
			logger.info("notice.getEndDd() : "+notice.getEndDd());
			logger.info("notice.getTitle() : "+notice.getTitle());
		}
		
		ModelAndView result = new ModelAndView();

		try {
			//공지사항 신규등록
			noticeServices.insertNotice(notice);

			result.addObject("result","Y");
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
	 * 공지사항을 수정한다.
	 * @param notice
	 * @return
	 */
	@RequestMapping(value = "/admin/notice/updateNoticeInfo.ssc", method = RequestMethod.POST)
	public ModelAndView updateNoticeInfo(@ModelAttribute("notice") Notice notice){

		if(logger.isInfoEnabled()){
			logger.info("notice.getCont() : "+notice.getCont());
			logger.info("notice.getPopUpYn() : "+notice.getPopUpYn());
			logger.info("notice.getRegId() : "+notice.getRegId());
			logger.info("notice.getStartDd() : "+notice.getStartDd());
			logger.info("notice.getEndDd() : "+notice.getEndDd());
			logger.info("notice.getTitle() : "+notice.getTitle());
			logger.info("notice.getNoticeId() : "+notice.getNoticeId());
		}

		ModelAndView result = new ModelAndView();

		try {
			//공지사항 수정사항적용
			noticeServices.updateNotice(notice);

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
	 * 공지사항상세 정보를 조회한다.
	 * @param notice
	 * @return
	 */
	@RequestMapping(value = "/admin/notice/getNoticeInfo.ssc", method = RequestMethod.GET)
	public ModelAndView getNoticeInfo(@ModelAttribute("notice") Notice notice)  {

		ModelAndView result = new ModelAndView();

		try {
			//공지사항 리스트 조회
			//List<Notice> notice1 = noticeServices.getNoticeInfo(notice);
			Notice notice1 = noticeServices.getNoticeInfo(notice);

			if(notice1 == null) {
				result.addObject("result", "N");
				result.addObject("reason", messageSource.getMessage("error.001", null, null));
				result.setViewName("jsonView");	
				
				return result;
			}

			result.addObject("noticeTbl",notice1);
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
	 * 공지사항 리스트조회를 한다.
	 * @param codeTbl
	 * @param codeId
	 * @return
	 */
	@RequestMapping(value = "/admin/notice/findNoticeList.ssc", method = RequestMethod.GET)
	public ModelAndView findNoticeList(@ModelAttribute("notice") Notice notice,@ModelAttribute("search") Search search)  {

		if(logger.isInfoEnabled()){
			logger.info("search.getPageNo() : " + search.getPageNo());
			logger.info("notice.getPageNo() : " + notice.getPageNo());
		}

		if (notice.getPageNo() == null){
			notice.setPageNo(0);
		}
		
		ModelAndView result = new ModelAndView();

		try {

			//공지사항 건을 조회한다
			List<Notice> noticeTbls = noticeServices.findNoticeList(notice,search);

			//총 조회건수를 구한다
			long totalCount = noticeServices.countNotice(notice);

			result.addObject("totalCount",totalCount);			
			result.addObject("noticeTbls",noticeTbls);
			result.addObject("pageSize",SearchControls.NOTICE_LIST_COUNT);
			result.addObject("result","Y");
			result.setViewName("jsonView");

			return result;
		}catch (ServiceException e) {
			result.addObject("result","N");
			result.addObject("reason",e.getMessage());
			result.setViewName("jsonView");

			return result;
		}
	}


	/**
	 * 공지사항을 삭제한다.
	 * @param notice
	 * @return
	 */
	@RequestMapping(value = "/admin/notice/deleteNoticeInfo.ssc", method = RequestMethod.POST)
	public ModelAndView deleteNoticeInfo(@ModelAttribute("notice") Notice notice){

		if(logger.isInfoEnabled()){
			logger.info("notice.getNoticeId() : "+notice.getNoticeId());
		}

		ModelAndView result = new ModelAndView();

		try {
			//공지사항 삭제
			noticeServices.deleteNotice(notice);

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
