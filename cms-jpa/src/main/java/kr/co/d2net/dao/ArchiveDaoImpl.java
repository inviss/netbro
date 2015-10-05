package kr.co.d2net.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.filter.ArchiveSpecifications;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.springframework.stereotype.Repository;

@Repository("archiveDao")
public class ArchiveDaoImpl implements ArchiveDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	@Override
	public ArchiveTbl getArchiveInfo(Long seq) throws DaoNonRollbackException {

		ArchiveTbl archiveTbl = null;

		try {
			archiveTbl = repository.find(ArchiveTbl.class, seq);
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getArchiveInfo Error", e);
		}
		return archiveTbl;
	}

	/**
	 * 
	 */
	@Override
	public ArchiveTbl getArchiveJob(String statCd) throws DaoNonRollbackException {

		ArchiveTbl archiveTbl = null;

		try {
			archiveTbl = repository.find(ArchiveTbl.class, statCd);
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getArchiveJob Error", e);
		}
		return archiveTbl;
	}

	@Override
	public void updateRetryArchive(ArchiveTbl archiveTbl) throws DaoRollbackException {

		try {

			ArchiveTbl tbl = repository.find(ArchiveTbl.class, archiveTbl.getSeq());

			tbl.setWorkStatCd("000");

			if (archiveTbl.getSeq() != null && archiveTbl.getSeq() > 0)
				repository.update(archiveTbl);
			else
				repository.save(archiveTbl);

		} catch (Exception e) {
			throw new DaoRollbackException("", "updateRetryArchive Error", e);
		}

	}

	@Override
	public List<ArchiveTbl> findArchiveList(Search search) throws DaoNonRollbackException {

		List<ArchiveTbl> archiveTbls = null;

		try {
			SpecificationResult<ArchiveTbl> result = repository.find(ArchiveTbl.class);
			archiveTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findArchiveList Error", e);
		}
		return archiveTbls;
	}

	/**
	 * getArchiveObj
	 */
	@Override
	public ArchiveTbl getArchiveInfoByCtiId(Long ctiId) throws DaoNonRollbackException {

		ArchiveTbl archiveTbl = new ArchiveTbl();

		try {
			archiveTbl = repository.find(ArchiveTbl.class, ArchiveSpecifications.getArchiveInfoByCtiId(ctiId)).single();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getArchiveInfoByCtiId Error", e);
		}
		return archiveTbl;
	}

	@Override
	public void save(long ctiId, String userId) throws DaoRollbackException {

		ArchiveTbl archiveTbl = new ArchiveTbl();

		try {
			archiveTbl.setCtiId(ctiId);
			archiveTbl.setWorkStatCd("000");
			archiveTbl.setRegDt(new Date());
			archiveTbl.setApproveId(userId);

			repository.save(archiveTbl);

		} catch (Exception e) {
			throw new DaoRollbackException("", "Archive save Error", e);
		}

	}

	@Override
	public Integer checkArchive(Long ctiId) throws DaoNonRollbackException {

		List<ArchiveTbl> archiveTbls = null;

		try {
			SpecificationResult<ArchiveTbl> result = repository.find(ArchiveTbl.class, ArchiveSpecifications.checkCompleteArchiveCount(ctiId));
			archiveTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "checkArchive Error", e);
		}
		return archiveTbls.size();

	}

	@Override
	public Integer checkCompleteArchive(Long ctiId) throws DaoNonRollbackException {

		List<ArchiveTbl> archiveTbls = null;

		try {
			SpecificationResult<ArchiveTbl> result = repository.find(ArchiveTbl.class, ArchiveSpecifications.checkArchiveCount(ctiId));
			archiveTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "checkArchive Error", e);
		}
		return archiveTbls.size();

	}

}
