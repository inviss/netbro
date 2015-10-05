package kr.co.d2net.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import kr.co.d2net.dao.filter.CategorySpecifications;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("categoryDao")
public class CategoryDaoImpl implements CategoryDao {

	private final static Logger logger = LoggerFactory.getLogger(CategoryDaoImpl.class);
	
	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;
	
	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}
	
	@Cacheable("categories")
	@Override
	public List<CategoryTbl> findCategories(Search search) throws DaoNonRollbackException {
		return repository.find(CategoryTbl.class,CategorySpecifications.findCategoryOnlyByParams(search)).list();
	}

	@Override
	@Transactional
	public CategoryTbl save(CategoryTbl categoryTbl) throws DaoRollbackException {
		if(categoryTbl.getCategoryId() != null && categoryTbl.getCategoryId() > 0){
			 repository.update(categoryTbl);
		}else{ 
			 repository.save(categoryTbl);
		}
		return categoryTbl;
	}

	@Override
	public Long count() throws DaoNonRollbackException {
		return 	repository.count(CategoryTbl.class);
	}

	@Override
	public List<CategoryTbl> findMainCategoryForExtJs(CategoryTbl categoryTbl,Search search)
			throws DaoNonRollbackException {
		List<CategoryTbl> categoryTbls = new ArrayList<CategoryTbl>();
			if(categoryTbl.getDepth() == 0 && search.getNode().equals("total")){// 최초 카테고리 전체조
				categoryTbls = repository.find(CategoryTbl.class,CategorySpecifications.findCategoryOnlyByParams(search)).list();
			}else if(categoryTbl.getCategoryId() != 0){//카테고리의 하위 카테고리 조회
				//조회하고자 하는 카테고리의 depth보다 +1해서 조회한다.
				Search tmp = new Search();
				tmp.setDepth(categoryTbl.getDepth()+1);
				tmp.setNodes(categoryTbl.getNodes());
				categoryTbls = repository.find(CategoryTbl.class, CategorySpecifications.findCategoryOnlyByParams(tmp)).list();
			}
		return categoryTbls;
	}

	@Override
	public CategoryTbl getCategoryObj(Search search)
			throws DaoNonRollbackException {
		return repository.find(CategoryTbl.class,search.getCategoryId());
	}

	@Override
	public void updateCategoryOrderNumByNamedQuery(CategoryTbl categoryTbl) throws DaoRollbackException {
		Query query = em.createNamedQuery("Category.updateMaxOrderNum").setParameter("orderNum", categoryTbl.getOrderNum());
	 
		query.executeUpdate();
		
	}

	@Override
	@Transactional
	public void delete(CategoryTbl categoryTbl) throws DaoRollbackException {
		logger.debug("category id is :"+ categoryTbl.getCategoryId());
		repository.remove(categoryTbl);
	}

 

	 

}
