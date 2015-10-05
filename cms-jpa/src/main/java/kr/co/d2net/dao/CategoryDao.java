package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;


public interface CategoryDao   {
	/**
	 * 카테고리의 depth와 categoryId를 기준으로 조회르란다.
	 * @param search
	 * @return
	 * @throws DaoNonRollbackException
	 */
	public List<CategoryTbl> findCategories(Search search) throws DaoNonRollbackException;
	
	/**
	 * 카테고리 정보를 등록, 
	 * @param categoryTbl
	 * @throws DaoRollbackException
	 */
	public CategoryTbl save(CategoryTbl categoryTbl) throws DaoRollbackException;
	
	/**
	 * 카테고리 정보를 삭제, 
	 * @param categoryTbl
	 * @throws DaoRollbackException
	 */
	public void delete(CategoryTbl categoryTbl) throws DaoRollbackException;
	
	
	/**
	 * category_tbl의 총 컬럼수를 구한다.
	 * @return
	 * @throws DaoNonRollbackException
	 */
	public Long count() throws DaoNonRollbackException;
	
	/**
	 *  ExtJs에 요구한는 값에 맞춰서 조회하는 함수. 
	 * List<CategoryTbl>
	 * @throws DaoRollbackException
	 */
	public List<CategoryTbl> findMainCategoryForExtJs(CategoryTbl categoryTbl,Search search) throws DaoNonRollbackException;
	
	/**
	 *  카테고리단일 건을 조회하는 함수
	 * @param search
	 * @return
	 * @throws DaoRollbackException
	 */
	public CategoryTbl getCategoryObj(Search search) throws DaoNonRollbackException;
	

	/**
	 * namedQuery를 이용하여 신규로 생성되 카테고리 이후의 orderNum을 모두 +1씩 업데이트 해준다.
	 *  name
	 * List<CategoryTbl>
	 * @throws DaoRollbackException
	 */
	public void updateCategoryOrderNumByNamedQuery(CategoryTbl categoryTbl) throws DaoRollbackException;
	
	
}
