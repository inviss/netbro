package kr.co.d2net.rest;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import kr.co.d2net.dto.EpisodeTbl;
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

@Path("/category")
@Service("rfCategoryService")
public class RFCategoryService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CategoryServices categoryServices;
	@Autowired
	private EpisodeServices episodeServices;

	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
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
	
	@GET
	@Path("/find/{categoryId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Categories findSubCategoryList(@PathParam("categoryId") Integer categoryId) {
		Categories categories = new Categories();
		
		List<CategoryTbl> categoryTbls;
		try {
			categoryTbls = categoryServices.findSubCategory(categoryId);
		
		
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
	
	private String checkSubList(String nodes) {
		List<CategoryTbl> categoryTbls;
		try {
			categoryTbls = categoryServices.findSubNodesList(nodes);
		
		return (categoryTbls == null || categoryTbls.isEmpty()) ? "Y" : "N";
		} catch (ServiceException e) {
			
			return "";
		}
	}
	
	@GET
	@Path("/find/episode/{categoryId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Categories findEpisodeList(@PathParam("categoryId") Integer categoryId) {
		Categories categories = new Categories();
		
		Search search = new Search();
		search.setCategoryId(categoryId);
		List<EpisodeTbl> episodeTbls;
		try {
			episodeTbls = episodeServices.episodeSearchList(search);
		} catch (ServiceException e) {
			return new Categories();
		}
		
		Episode episode = null;
		for(EpisodeTbl episodeTbl : episodeTbls) {
			episode = new Episode();
			episode.setEpisodeId(episodeTbl.getId().getEpisodeId());
			episode.setEpisodeNm(episodeTbl.getEpisodeNm());
			categories.addEpisode(episode);
		}
		
		return categories;
	}
	
	@GET
	@Path("/get/info/{categoryId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Category getCategoryInfo(@PathParam("categoryId") Integer categoryId) {
		Category category = new Category();
		
		if(logger.isDebugEnabled()) {
			logger.debug("categoryId: "+categoryId);
		}
		
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
	
	@POST
	@Path("/create/{parentId}/{categoryNm}")
	@Produces(MediaType.TEXT_PLAIN)
	public String createCategory(@PathParam("parentId") Integer parentId, @PathParam("categoryNm") String categoryNm) {
		
		return "";
	}
	
	@PUT
	@Path("/update/{parentId}/{categoryNm}")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCategory(@PathParam("categoryId") Integer categoryId, @PathParam("categoryNm") String categoryNm) {
		
		return "";
	}
	
	@DELETE
	@Path("/delete/{categoryId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteCategory(@PathParam("categoryId") Integer categoryId) {
		
		return "";
	}
	
}
