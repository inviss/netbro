package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface ArchiveDao {
	public ArchiveTbl getArchiveInfo(Long seq) throws DaoNonRollbackException;
	public ArchiveTbl getArchiveJob(String statCd) throws DaoNonRollbackException;
	public void updateRetryArchive(ArchiveTbl archive) throws DaoRollbackException;
	public List<ArchiveTbl> findArchiveList(Search search)throws DaoNonRollbackException;
	public ArchiveTbl getArchiveInfoByCtiId(Long ctiId) throws DaoNonRollbackException;
	public void save(long ctiId,String userId) throws DaoRollbackException;
	public Integer checkArchive(Long ctiId) throws DaoNonRollbackException;
	public Integer checkCompleteArchive(Long ctiId) throws DaoNonRollbackException;
	
	
}
