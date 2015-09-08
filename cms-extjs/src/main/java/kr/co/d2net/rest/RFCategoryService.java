package kr.co.d2net.rest;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.xml.Categories;
import kr.co.d2net.dto.xml.Category;
import kr.co.d2net.dto.xml.Episode;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.service.CategoryServices;
import kr.co.d2net.service.EpisodeServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * 카테고리 정보와 관련된 외부 interface 와 통신하는 class.
 * @author Administrator
 *
 */
@Path("/category")
@Service("rfCategoryService")
public class RFCategoryService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CategoryServices categoryServices;
	@Autowired
	private EpisodeServices episodeServices;


	/**
	 * 외부 interface와의 통신을 통해 카테고리LIST를 조회하는 method.
	 * @return
	 */
	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_JSON+";charset=UTF-8", MediaType.APPLICATION_XML})
	public Categories findCategoryAll() {
		Categories categories = new Categories();
		List<CategoryTbl> categoryTbls;
		try {
			categoryTbls = categoryServices.findAll("groupId", "nodes", "orderNum");

			Category category = null;
			for(CategoryTbl categoryTbl : categoryTbls) {
				category = new Category();
				category.setCategoryId(categoryTbl.getCategoryId());
				category.setCategoryNm(categoryTbl.getCategoryNm());
				category.setDepth(categoryTbl.getDepth());
				category.setNodes(categoryTbl.getNodes());
				category.setPreParent(categoryTbl.getPreParent());
				category.setOrderNum(categoryTbl.getOrderNum());
				category.setGroupId(categoryTbl.getGroupId());
				category.setFinalYn(checkSubList(String.valueOf(categoryTbl.getCategoryId())));
				categories.addCategory(category);
			}
		} catch (ServiceException e) {
			return new Categories();
		}
		return categories;
	}


	/**
	 * 외부 interface와의 통신을 통해 카테고리SUBLIST를 조회하는 method.
	 * @param categoryId
	 * @return
	 */
	@GET
	@Path("/find/{categoryId}")
	@Produces({MediaType.APPLICATION_JSON+";charset=UTF-8", MediaType.APPLICATION_XML})
	public Categories findSubCategoryList(@PathParam("categoryId") Integer categoryId) {
		Categories categories = new Categories();

		List<CategoryTbl> categoryTbls;
		try {
			categoryTbls = categoryServices.findSubCategory(categoryId, "orderNum");

			Category category = null;
			for(CategoryTbl categoryTbl : categoryTbls) {
				category = new Category();
				category.setCategoryId(categoryTbl.getCategoryId());
				category.setCategoryNm(categoryTbl.getCategoryNm());
				category.setDepth(categoryTbl.getDepth());
				category.setNodes(categoryTbl.getNodes());
				category.setPreParent(categoryTbl.getPreParent());
				category.setOrderNum(categoryTbl.getOrderNum());
				category.setGroupId(categoryTbl.getGroupId());
				category.setFinalYn(checkSubList(String.valueOf(categoryTbl.getNodes())));
				categories.addCategory(category);
			}
		} catch (ServiceException e) {
			return new Categories();
		}
		return categories;
	}


	/**
	 * 외부 interface와의 통신을 통해 카테고리SUBLIST를 체크하는 method.
	 * @param nodes
	 * @return
	 */
	private String checkSubList(String nodes) {
		List<CategoryTbl> categoryTbls;
		try {
			categoryTbls = categoryServices.findSubNodesList(nodes);

			return (categoryTbls == null || categoryTbls.isEmpty()) ? "Y" : "N";
		} catch (ServiceException e) {

			return "";
		}
	}



	/**
	 * 외부 interface와의 통신을 통해 에피소드LIST를 조회하는 method.
	 * @param categoryId
	 * @return
	 */
	@GET
	@Path("/find/episode/{categoryId}")
	@Produces({MediaType.APPLICATION_JSON+";charset=UTF-8", MediaType.APPLICATION_XML})
	public Categories findEpisodeList(@PathParam("categoryId") Integer categoryId) {
		Categories categories = new Categories();

		if(logger.isInfoEnabled()) 
			logger.info("category_id: "+categoryId);

		Search search = new Search();
		search.setPageNo(1);
		search.setCategoryId(categoryId);
		List<kr.co.d2net.dto.vo.Episode> episodeTbls;

		try {
			episodeTbls = episodeServices.episodeSearchList(search);
		} catch (ServiceException e) {
			logger.error("episode find error", e);
			return new Categories();
		}

		if(logger.isDebugEnabled()) {
			logger.debug("episode size: "+episodeTbls.size());
		}
		
		Episode episode = null;
		for(kr.co.d2net.dto.vo.Episode episodeTbl : episodeTbls) {
			episode = new Episode();
			episode.setEpisodeId(episodeTbl.getEpisodeId());
			episode.setEpisodeNm(episodeTbl.getEpisodeNm());
			categories.addEpisode(episode);
		}

		return categories;
	}
	
	
	/**
	 * 외부 interface와의 통신을 통해 카테고리정보를 조회하는 method.
	 * @param categoryId
	 * @return
	 */
	@GET
	@Path("/get/info/{categoryId}")
	@Produces({MediaType.APPLICATION_JSON+";charset=UTF-8", MediaType.APPLICATION_XML})
	public Category getCategoryInfo(@PathParam("categoryId") Integer categoryId) {
		Category category = new Category();

		if(logger.isInfoEnabled()) 
			logger.info("categoryId: "+categoryId);

		CategoryTbl categoryTbl = null;
		if(categoryId != null && categoryId > 0) {
			try {
				categoryTbl = categoryServices.getCategoryObj(categoryId);
			} catch (ServiceException e) {
				return new Category();
			}
		}

		if(categoryTbl != null) {
			category.setCategoryId(categoryTbl.getCategoryId());
			category.setCategoryNm(categoryTbl.getCategoryNm());
			category.setDepth(categoryTbl.getDepth());
			category.setNodes(categoryTbl.getNodes());
			category.setPreParent(categoryTbl.getPreParent());
			category.setOrderNum(categoryTbl.getOrderNum());
			category.setGroupId(categoryTbl.getGroupId());
		}
		return category;
	}
	
	/**
	 * 
	 * @param parentId
	 * @param categoryNm
	 * @return
	 */
	@POST
	@Path("/create/{parentId}/{categoryNm}")
	@Produces(MediaType.TEXT_PLAIN)
	public String createCategory(@PathParam("parentId") Integer parentId, @PathParam("categoryNm") String categoryNm) {
		return "";
	}
	
	/**
	 * 
	 * @param categoryId
	 * @param categoryNm
	 * @return
	 */
	@PUT
	@Path("/update/{parentId}/{categoryNm}")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCategory(@PathParam("categoryId") Integer categoryId, @PathParam("categoryNm") String categoryNm) {
		return "";
	}
	
	/**
	 * 
	 * @param categoryId
	 * @return
	 */
	@DELETE
	@Path("/delete/{categoryId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteCategory(@PathParam("categoryId") Integer categoryId) {
		return "";
	}

}
