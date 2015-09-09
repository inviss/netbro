package kr.co.d2net.service;


import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;

import kr.co.d2net.dao.LowStorageDao;
import kr.co.d2net.dto.LowStorageTbl;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.PropertiesUtils;

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

@Service
@Transactional(readOnly=true)
public class LowStorageServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private LowStorageDao lowStorageDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;

	@Modifying
	@Transactional
	public void addAll(Set<LowStorageTbl> lowStorageTbls) {
		lowStorageDao.save(lowStorageTbls);
	}

	@Modifying
	@Transactional
	public void add(LowStorageTbl lowStorageTbl) throws ServiceException{

		String message = "";
		try {
			lowStorageDao.save(lowStorageTbl);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}
	}


	/**
	 * lowStroge 용량 조회.
	 * @param seq
	 * @return
	 */
	public LowStorageTbl getLowStorage(Integer seq) throws ServiceException{

		String message = "";
		try {
			return lowStorageDao.findOne(seq);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}
	}

	/**
	 * 저용량 스토리지 용량 정보를 가져온다.
	 * sigar api를 이용해 해당정보를 가져온다.
	 * 해당 정보는 DB에 저장.
	 * @return
	 */
	public LowStorageTbl getStorageObj() throws SigarException,ServiceException{
		String message = "";
		//Sigar api를 이용.
		Sigar sigar = new Sigar (); 
		LowStorageTbl lowStorageTbl = new LowStorageTbl();

		String lowPrefix = messageSource.getMessage("row.drive", null, null);
		//String limit = messageSource.getMessage("row.limit", null, null);

		FileSystemUsage usage = null; 

		try {
			usage = sigar.getFileSystemUsage (lowPrefix);

			if(logger.isDebugEnabled()){
				// Total size of the file system 
				logger.debug("Total =" + usage.getTotal () + "KB"); 
				// Remaining size of the file system 
				logger.debug("Free =" + usage.getFree () + "KB"); 
				// Size of the file system is available 
				logger.debug("Avail =" + usage.getAvail () + "KB"); 
				// Use the file system has been 
				logger.debug("Used =" + usage.getUsed () + "KB"); 
				double usePercent = usage.getUsePercent () * 100D; 
				// File system resource utilization 
				logger.debug("Usage =" + usePercent + "%"); 

				logger.debug("DiskReads =" + usage.getDiskReads ()); 
				logger.debug("DiskWrites =" + usage.getDiskWrites ());
			}

			lowStorageTbl.setStorageId(1);
			lowStorageTbl.setTotalVolume(usage.getTotal());
			lowStorageTbl.setUseVolume(usage.getUsed());
			lowStorageTbl.setIdleVolume(usage.getFree());
			lowStorageTbl.setStoragePath(lowPrefix);

			LowStorageTbl tmp = getLowStorage(1);

			Integer tmp2 = tmp.getLowLimit();
			lowStorageTbl.setLowLimit(tmp2);

			return lowStorageTbl; 

		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}

	}

}
