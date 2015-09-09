package kr.co.d2net.controller;

import java.util.Date;

import javax.servlet.ServletContext;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.search.index.SearchMetaIndex;
import kr.co.d2net.service.AttachServices;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.CodeServices;
import kr.co.d2net.service.ContentsInstServices;
import kr.co.d2net.service.ContentsServices;
import kr.co.d2net.service.EpisodeServices;
import kr.co.d2net.service.RoleAuthServices;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class DisuseController {

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

	@Autowired
	private EpisodeServices episodeServices;

	@RequestMapping(value="/disuse/disuse.ssc", method = RequestMethod.GET)
	public ModelMap code(ModelMap map) {

		//List<CategoryTbl>category = categoryServices.findMainCategory();

		//map.addAttribute("categories", category);
		map.addAttribute("search", new Search());

		return map;
	}




	@RequestMapping(value="/disuse/insertDisUse.ssc", method = RequestMethod.POST)
	public ModelAndView insertDisUse(@RequestParam(value = "ctIds", required = false) String ctIds) {
		ModelAndView result = new ModelAndView();

		if(StringUtils.isNotBlank(ctIds)) {

			String[] aryCtId = null;
			if(ctIds.indexOf(",") > -1) {
				aryCtId = ctIds.split(",");
			} else {
				aryCtId = new String[] {ctIds};
			}

			for(String ctId : aryCtId) {
				if(StringUtils.isNotBlank(ctId)) {
					logger.debug("isNotBlank~~~~!!!!" + ctId);
					ContentsTbl contentsTbl;
					try {
						contentsTbl = contentsServices.getContentObj(Long.valueOf(ctId));

						if(contentsTbl.getDelDd() == null){
							try {
								contentsTbl.setDelDd(new Date());
								contentsServices.saveContentObj(contentsTbl);
							} catch (ServiceException e) {
								result.addObject("result","N");
								result.addObject("errorCont","저장중 오류가 발생하였습니다.");
								result.setViewName("jsonView");
								return result;
							}
							logger.debug("saveContentObj~~~~!!!!" + ctId);
							SearchMetaIndex searchMetaIndex = new SearchMetaIndex();
							searchMetaIndex.deleteSearchMeta(Long.valueOf(ctId));
							logger.debug("searchMetaIndex~~~~!!!!" + ctId);
						}else{
							result.addObject("result","N");
							result.addObject("errorCont","이미 폐기된 영상입니다.");
							result.setViewName("jsonView");
							return result;
						}

					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ServiceException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}

		result.addObject("result","Y");
		result.setViewName("jsonView");

		return result;
	}



	@RequestMapping(value="/disuse/cancleDisUse.ssc", method = RequestMethod.POST)
	public ModelAndView cancleDisUse(@RequestParam(value = "ctId", required = false) Long ctId) {
		ModelAndView result = new ModelAndView();

		if(ctId !=0) {
			logger.debug("isNotBlank~~~~!!!!" + ctId);
			ContentsTbl contentsTbl  = new ContentsTbl();
			ContentsInstTbl contentsInstTbl = new ContentsInstTbl(); 

			try {
				contentsInstTbl =	contentsInstServices.getContentInstObj(Long.valueOf(ctId));
				contentsTbl = contentsServices.getContentObj(Long.valueOf(ctId));

			}  catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			contentsTbl.setDelDd(null);
			try {
				contentsServices.saveContentObj(contentsTbl);
			} catch (ServiceException e1) {
				result.addObject("result","Y");
				result.setViewName("jsonView");

				return result;
			}

			//카테고리 정보검색
			CategoryTbl cateogryTbl;
			try {
				cateogryTbl = categoryServices.getCategoryObj(contentsTbl.getCategoryId());
				contentsTbl.setCategoryNm(cateogryTbl.getCategoryNm());
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			//에피소드 정보 검색

			EpisodeId id = new EpisodeId();
			EpisodeTbl episodeTbl = new EpisodeTbl();
			id.setCategoryId(contentsTbl.getCategoryId());
			id.setEpisodeId(contentsTbl.getEpisodeId());
			try {
				episodeTbl = episodeServices.episodeInfo(id);
			} catch (ServiceException e) {
				e.getMessage();
				result.addObject("result","N");
				result.setViewName("jsonView");

				return result;
			}
			contentsTbl.setEpisodeId(episodeTbl.getEpisodeId());
			contentsTbl.setEpisodeNm(episodeTbl.getEpisodeNm());


			//세그먼트 정보 검색
			contentsTbl.setSegmentId(1);

			SearchMetaIndex searchMetaIndex = new SearchMetaIndex();

			searchMetaIndex.addSearchMeta(contentsTbl, contentsInstTbl);
			logger.debug("searchMetaIndex~~~~!!!!" + ctId);

		}

		result.addObject("result","Y");
		result.setViewName("jsonView");

		return result;
	}


}
