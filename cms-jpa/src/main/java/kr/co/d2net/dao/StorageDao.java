package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.StorageTbl;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface StorageDao {
	public List<StorageTbl> findStorageList() throws DaoNonRollbackException;
	public StorageTbl getStorageInfo(Integer id) throws DaoNonRollbackException;
	public void save(StorageTbl tbl) throws DaoRollbackException;
}
