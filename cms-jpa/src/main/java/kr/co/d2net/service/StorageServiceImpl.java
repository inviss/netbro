package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.StorageDao;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.StorageTbl;
import kr.co.d2net.exception.ServiceException;

import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("storageService")
@Transactional(readOnly=true)
public class StorageServiceImpl implements StorageSevices{

	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private StorageDao storageDao;
	

	@Override
	public List<StorageTbl> findStorageList() throws ServiceException {
		
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public StorageTbl getStorageInfo(Integer id) throws ServiceException {
		try {
			storageDao.getStorageInfo(id);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public StorageTbl getStorageForStorageWorker(String gubun)
			throws SigarException, ServiceException {
		// Sigar api를 이용.
		Sigar sigar = new Sigar();
		StorageTbl storageTbl = new StorageTbl();

		try {
			String highPrefix = messageSource.getMessage("high.drive", null, null);
			String rowPrefix = messageSource.getMessage("row.drive", null, null);

			FileSystemUsage usage = null;

			if (gubun == "H") {
				usage = sigar.getFileSystemUsage(highPrefix);
			} else {
				usage = sigar.getFileSystemUsage(rowPrefix);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Total =" + usage.getTotal() + "KB");
				logger.debug("Free =" + usage.getFree() + "KB");
				logger.debug("Avail =" + usage.getAvail() + "KB");
				logger.debug("Used =" + usage.getUsed() + "KB");
				logger.debug("DiskReads =" + usage.getDiskReads());
				logger.debug("DiskWrites =" + usage.getDiskWrites());
			}

			if (gubun == "H") {
				//StorageTbl highTbl = getStorageInfoSigar(1);
				StorageTbl highTbl = getStorageInfo(1);

				if (highTbl.getLimit() == null)
					storageTbl.setLimit(100);

				storageTbl.setStorageId(1);
				storageTbl.setStorageGubun("H");
				storageTbl.setStoragePath(highPrefix);
			} else {
				//StorageTbl rowTbl = getStorageInfoSigar(2);
				StorageTbl rowTbl = getStorageInfo(1);

				if (rowTbl.getLimit() == null)
					storageTbl.setLimit(100);

				storageTbl.setStorageId(2);
				storageTbl.setStorageGubun("L");
				storageTbl.setStoragePath(rowPrefix);
			}

			storageTbl.setTotalVolume(usage.getTotal());
			storageTbl.setUseVolume(usage.getUsed());
			storageTbl.setIdleVolume(usage.getFree());
			
			
		} catch (Exception e) {
			logger.error("getStorageObj",e);
		}
		
		return storageTbl;
	}

	@Override
	@Transactional
	public void saveStorageInfo(StorageTbl tbl) throws ServiceException {
		try {
			storageDao.save(tbl);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
