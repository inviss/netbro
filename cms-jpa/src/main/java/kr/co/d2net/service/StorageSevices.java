package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.StorageTbl;
import kr.co.d2net.exception.ServiceException;

import org.hyperic.sigar.SigarException;


public interface StorageSevices {
	public List<StorageTbl> findStorageList() throws ServiceException;
	public StorageTbl getStorageInfo(Integer id) throws ServiceException;
	public StorageTbl getStorageForStorageWorker(String gubun) throws SigarException,ServiceException;
	public void saveStorageInfo(StorageTbl tbl) throws ServiceException;
}
