package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface OptDao {
	public void save(ContentsTbl contentsTbl) throws DaoRollbackException;
	public void deleteContent(ContentsTbl contentsTbl) throws DaoRollbackException;
	public ContentsTbl getContentObj(Long ctId) throws DaoNonRollbackException;
	public ContentsTbl getContentObj(Search search) throws DaoNonRollbackException;
	public List<ContentsTbl> findContentsOnly(Search search, Specification<ContentsTbl> specification) throws DaoNonRollbackException;
	public List<ContentsTbl> findContentsWithSub(Search search, Specification<ContentsTbl> specification) throws DaoNonRollbackException;
}
