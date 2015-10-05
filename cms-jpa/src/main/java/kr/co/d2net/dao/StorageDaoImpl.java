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
import kr.co.d2net.dto.StorageTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Repository;

@Repository("storageDao")
public class StorageDaoImpl implements StorageDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	@Override
	public List<StorageTbl> findStorageList() throws DaoNonRollbackException {
		List<StorageTbl> storageTbls = null;

		try {
			SpecificationResult<StorageTbl> result = repository.find(StorageTbl.class);
			storageTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findProfileList Error", e);
		}
		return storageTbls;
	}

	@Override
	public StorageTbl getStorageInfo(Integer id) throws DaoNonRollbackException {
		
		StorageTbl storageTbl = null;

		try {
			storageTbl = repository.find(StorageTbl.class,id);
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getProfileInfo Error", e);
		}
		return storageTbl;
	}

	@Override
	public void save(StorageTbl storageTbl) throws DaoRollbackException {
		try {
			if(storageTbl.getStorageId() != null && storageTbl.getStorageId() > 0L)
				repository.update(storageTbl);
			else
				repository.save(storageTbl);
		} catch (Exception e) {
			throw new DaoRollbackException("", "Storage save Error", e);
		}
	}
	
	
}
