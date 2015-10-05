package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.filter.ContentSpecifications;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.springframework.stereotype.Repository;

@Repository("busiPartnerDao")
public class BusiPartnerDaoImpl implements BusiPartnerDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	@Override
	public void save(ContentsTbl contentsTbl)
			throws DaoRollbackException {
		try {
			if(contentsTbl.getCtId() != null && contentsTbl.getCtId() > 0L)
				repository.update(contentsTbl);
			else
				repository.save(contentsTbl);
		} catch (Exception e) {
			throw new DaoRollbackException("", "Content Add Error", e);
		}
	}

	@Override
	public void deleteContent(ContentsTbl contentsTbl)
			throws DaoRollbackException {
		try {
			repository.remove(contentsTbl);
		} catch (Exception e) {
			throw new DaoRollbackException("", "Content Remove Error", e);
		}
	}

	@Override
	public ContentsTbl getContentObj(Long ctId)
			throws DaoNonRollbackException {
		ContentsTbl contentsTbl = null;
		try {
			contentsTbl = repository.find(ContentsTbl.class, ctId);
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "Content Remove Error", e);
		}
		return contentsTbl;
	}

	@Override
	public ContentsTbl getContentObj(Search search)
			throws DaoNonRollbackException {
		ContentsTbl contentsTbl = null;
		try {
			contentsTbl = repository.find(ContentsTbl.class, ContentSpecifications.findContentsOnlyByParams(search)).single();
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "Content find Error", e);
		}
		return contentsTbl;
	}

	@Override
	public List<ContentsTbl> findContentsOnly(Search search,
			Specification<ContentsTbl> specification)
					throws DaoNonRollbackException {
		List<ContentsTbl> contentsTbls = null;
		try {
			SpecificationResult<ContentsTbl> result = repository.find(ContentsTbl.class, ContentSpecifications.findContentsOnlyByParams(search));
			if(search.getPageNo() != null && search.getPageNo() > 0) {
				int startNum = (search.getPageNo()-1) * search.getPageSize();
				int endNum = startNum + search.getPageSize();
				result.from(startNum).size(endNum);
			}
			contentsTbls = result.list();
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "Content findAll Error", e);
		}
		return contentsTbls;
	}

	@Override
	public List<ContentsTbl> findContentsWithSub(Search search,
			Specification<ContentsTbl> specification)
					throws DaoNonRollbackException {
		List<ContentsTbl> contentsTbls = null;
		try {
			SpecificationResult<ContentsTbl> result = repository.find(ContentsTbl.class, ContentSpecifications.findContentsOnlyByParams(search));
			if(search.getPageNo() != null && search.getPageNo() > 0) {
				int startNum = (search.getPageNo()-1) * search.getPageSize();
				int endNum = startNum + search.getPageSize();
				result.from(startNum).size(endNum);
			}
			contentsTbls = result.list();
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "Content findAll Error", e);
		}
		return contentsTbls;
	}

}
