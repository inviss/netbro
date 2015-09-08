package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.StorageDao;
import kr.co.d2net.dto.StorageTbl;
import kr.co.d2net.dto.StorageTbl_;
import kr.co.d2net.dto.vo.Storage;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Administrator
 *
 */
@Service
@Transactional(readOnly = true)
public class StorageServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private StorageDao storageDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;

	@Modifying
	@Transactional
	public void add(StorageTbl StorageTbl) throws ServiceException {
		storageDao.save(StorageTbl);
	}


	/**
	 * 스토리지 리스트 정보를 가져온다.
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Storage> findStorageList() throws ServiceException {

		String[] storageFields = { "storageId", "totalVolume", "useVolume",
				"idleVolume", "limit", "storagePath", "storageGubun" };

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<StorageTbl> root = cq.from(StorageTbl.class);

		Path<Integer> storageId = root.get(StorageTbl_.storageId);
		Path<Long> totalVolume = root.get(StorageTbl_.totalVolume);
		Path<Long> useVolume = root.get(StorageTbl_.useVolume);
		Path<Long> idleVolume = root.get(StorageTbl_.idleVolume);
		Path<Integer> limit = root.get(StorageTbl_.limit);
		Path<String> storagePath = root.get(StorageTbl_.storagePath);
		Path<String> storageGubun = root.get(StorageTbl_.storageGubun);

		cq.multiselect(storageId, totalVolume, useVolume, idleVolume, limit,
				storagePath, storageGubun);
		cq.orderBy(cb.asc(storageId));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		List<Object[]> storageList = typedQuery.getResultList();

		if (storageList != null) {
			List<Storage> storages = new ArrayList<Storage>();

			for (Object[] list : storageList) {
				Storage storage = new Storage();

				int i = 0;
				for (int j = 0; j < storageFields.length; j++) {
					ObjectUtils.setProperty(storage, storageFields[j], list[i]);
					i++;
				}
				storages.add(storage);
			}
			return storages;
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * Storage리스트에서 선택된 값을 가져온다.
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public StorageTbl getStorageInfoSigar(Integer id) throws ServiceException {
		return storageDao.findOne(id);
	}




	/**
	 * 스토리지 정보를 가져오는 method.
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Storage getStorageInfo(Integer id) throws ServiceException {

		String[] storageFields = { "storageId", "totalVolume", "useVolume",
				"idleVolume", "limit", "storagePath", "storageGubun" };

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<StorageTbl> root = cq.from(StorageTbl.class);

		Path<Integer> storageId = root.get(StorageTbl_.storageId);
		Path<Long> totalVolume = root.get(StorageTbl_.totalVolume);
		Path<Long> useVolume = root.get(StorageTbl_.useVolume);
		Path<Long> idleVolume = root.get(StorageTbl_.idleVolume);
		Path<Integer> limit = root.get(StorageTbl_.limit);
		Path<String> storagePath = root.get(StorageTbl_.storagePath);
		Path<String> storageGubun = root.get(StorageTbl_.storageGubun);

		cq.multiselect(storageId, totalVolume, useVolume, idleVolume, limit,
				storagePath, storageGubun);
		cq.where(cb.equal(storageId, id));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		//List<Object[]> storageList = typedQuery.getResultList();
		Object[] storageList = typedQuery.getSingleResult();
		Storage storage = new Storage();
		if (storageList != null) {
			List<Storage> storages = new ArrayList<Storage>();

			for (int j = 0; j < storageFields.length; j++) {
				ObjectUtils.setProperty(storage, storageFields[j], storageList[j]);
			}
			storages.add(storage);
		} 
		return storage;
	}



	/**
	 * 
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void saveStorageInfo(StorageTbl tbl) throws ServiceException {

		if(logger.isInfoEnabled()){
			logger.debug("tbl.getLimit() : " + tbl.getLimit());
			logger.debug("tbl.getStorageId() : " + tbl.getStorageId());
		}

		StorageTbl storageTbl = storageDao.findOne(tbl.getStorageId());
		storageTbl.setLimit(tbl.getLimit());
		storageDao.save(storageTbl);
	}

	/**
	 * 스토리지 리스트에서 클릭된 데이터 값을 가져온다.
	 * 
	 * @param gubun
	 * @return
	 * @throws SigarException
	 * @throws ServiceException
	 */
	public StorageTbl getStorageObj(String gubun) throws SigarException,ServiceException {

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
				// Total size of the file system
				logger.debug("Total =" + usage.getTotal() + "KB");
				// Remaining size of the file system
				logger.debug("Free =" + usage.getFree() + "KB");
				// Size of the file system is available
				logger.debug("Avail =" + usage.getAvail() + "KB");
				// Use the file system has been
				logger.debug("Used =" + usage.getUsed() + "KB");
				double usePercent = usage.getUsePercent() * 100D;
				// File system resource utilization
				logger.debug("Usage =" + usePercent + "%");

				logger.debug("DiskReads =" + usage.getDiskReads());
				logger.debug("DiskWrites =" + usage.getDiskWrites());
			}

			if (gubun == "H") {
				StorageTbl highTbl = getStorageInfoSigar(1);

				if (highTbl.getLimit() == null)
					storageTbl.setLimit(100);

				storageTbl.setStorageId(1);
				storageTbl.setStorageGubun("H");
				storageTbl.setStoragePath(highPrefix);
			} else {
				StorageTbl rowTbl = getStorageInfoSigar(2);

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
}
