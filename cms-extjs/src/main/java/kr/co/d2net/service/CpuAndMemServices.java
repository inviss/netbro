package kr.co.d2net.service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;

import kr.co.d2net.dto.vo.CpuAndMem;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * Sigar lib를 이용한 cpu & mem 사용량을 조회하는 class.
 * @author Administrator
 *
 */
@Service
@Transactional(readOnly=true)
public class CpuAndMemServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());


	@Autowired
	private MessageSource messageSource;

	@PersistenceContext
	private EntityManager em;

	/**
	 * cpu 사용량을 조회한다.
	 * @param cpuAndMem
	 * @return
	 * @throws SigarException
	 * @throws InterruptedException
	 * @throws ServiceException
	 */
	@SuppressWarnings("static-access")
	public CpuAndMem getCpuObj(CpuAndMem cpuAndMem) throws SigarException, InterruptedException,ServiceException { 

		String message = "";

		//Sigar api를 이용.
		Sigar sigar = new Sigar (); 

		try {
			CpuPerc cpu = sigar.getCpuPerc();

			cpuAndMem.setUseCpu(cpu.format(cpu.getUser()).substring(cpu.format(cpu.getUser()).indexOf(""), cpu.format(cpu.getUser()).lastIndexOf("%")));
			cpuAndMem.setIdleCpu(cpu.format(cpu.getIdle()).substring(cpu.format(cpu.getIdle()).indexOf(""), cpu.format(cpu.getIdle()).lastIndexOf("%")));
			
			if(logger.isDebugEnabled()) {
				logger.debug("use cpu: "+cpuAndMem.getUseCpu());
				logger.debug("idle cpu: "+cpuAndMem.getIdleCpu());
			}
			
		} catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.001",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}
		return cpuAndMem; 
	 
		}
	
	/**
	 * mem 사용량을 조회한다.
	 * @param cpuAndMem
	 * @return
	 * @throws SigarException
	 * @throws InterruptedException
	 * @throws ServiceException
	 */
	@SuppressWarnings("static-access")
	public CpuAndMem getMemObj(CpuAndMem cpuAndMem) throws SigarException, InterruptedException,ServiceException  { 

		//Sigar api를 이용.
		Sigar sigar = new Sigar (); 
		String message = "";

		try {
			Mem mem = sigar.getMem(); 
			cpuAndMem.setTotalMem(mem.getTotal () / 1024L);
			cpuAndMem.setUseMem(mem.getUsed () / 1024L);
			cpuAndMem.setIdleMem(mem.getFree () / 1024L);

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
		return cpuAndMem; 
 
	}
}
