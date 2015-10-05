package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.dto.vo.CategoryTreeForExtJs;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

public interface CategoryService {
	/**
	 * 카테고리의 메타정보를 신규로 저장하는 함수. 
	 * 
	 * @param categoryTbl
	 * @throws ServiceException
	 */
	public void insertCategory(Category category) throws ServiceException;

	/**
	 * 카테고리의 메타정보를 수정하는 함수,
	 * @param categoryTbl
	 * @throws ServiceException
	 */
	public void updateCategory(Category category) throws ServiceException;

	/**
	 * 카테고리의 순서와 위치정보롤 수정한다.
	 * @param categorys
	 * @throws ServiceException
	 */
	public void updateChangeOrder(List<Category> categorys) throws ServiceException;
	
	/**
	 * 카테고리를 삭제한다
	 * 단 삭제시 contentsTbl에 등록된 메타가 있다면 삭제를 하지 않는다.
	 * @param categoryTbl
	 * @throws ServiceException
	 */
	public String deleteCategory(Category category) throws ServiceException;

	/**
	 * 카테고리 단일건에 대한 정보를 가져온다
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	public Category getCategory(Search search) throws ServiceException;

	/**
	 * ExtJs에서 요구하는 값에 맞춰서 조회하는 함수
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	public List<Category> findCategoryForExtJs(Search search) throws ServiceException;

	/**
	 * 등록된 카테고리의 총 갯수를 구한다
	 * @return
	 * @throws ServiceException
	 */
	public Long countCategory() throws ServiceException;
	
	/**
	 * ExtJs에서 요구하는json 형식으로 변경한다.
	 * @param categorys
	 * @return
	 * @throws ServiceException
	 */
	public List<CategoryTreeForExtJs> makeExtJsJson(List<Category> categorys) throws ServiceException;
	

}
