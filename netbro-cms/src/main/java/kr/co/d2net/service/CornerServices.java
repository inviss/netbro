package kr.co.d2net.service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

import kr.co.d2net.dao.CornerDao;
import kr.co.d2net.dao.filter.CornerSpecifications;
import kr.co.d2net.dto.CornerTbl;
import kr.co.d2net.dto.vo.StoryBoard;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.search.config.Configure;
import kr.co.d2net.utils.PropertiesUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class CornerServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CornerDao cornerDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;


	public Page<CornerTbl> findAllCorner(Specification<CornerTbl> specification, Pageable pageable) throws ServiceException{
		String message ="";
		try{
			return cornerDao.findAll(specification, pageable);
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
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}

	@Modifying
	@Transactional
	public void addAll(Set<CornerTbl> corners) throws ServiceException{
		String message ="";
		try {
			cornerDao.save(corners);

		} catch (PersistenceException e) {
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
	 * 
	 * @param cornerInfo
	 * @param storyBoard
	 * @throws RuntimeException
	 */
	@Modifying
	@Transactional
	public void updateConrnerInfo(List<CornerTbl> cornerInfo ,StoryBoard storyBoard) throws ServiceException{
		String message ="";
		String[] array = storyBoard.getDividImgs().split(",");
		String[] tempArray = storyBoard.getDividImgs().split(",");
		String[] cnNm = storyBoard.getCnNm().split(",");
		String[] cnCont = storyBoard.getCnCont().split(",");

		try {
			for(int i = 0; i < array.length; i++){
				for(CornerTbl info : cornerInfo){
					if(info.getsDuration() == Long.parseLong(array[i])){
						info.setCnCont(cnCont[i]);
						info.setCnNm(cnNm[i]);
						info.setRegId(storyBoard.getModrId());
						cornerDao.save(info);
						tempArray[i]="none";
					}
				}
			}

			String dividImgs = Arrays.toString(tempArray);
			dividImgs = dividImgs.replace("[", "").replace("]", "");
			storyBoard.setDividImgs(dividImgs);
			add(storyBoard);

		} catch (PersistenceException e) {
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
	 * 
	 * @param storyBoard
	 * @throws RuntimeException
	 */
	@Modifying
	@Transactional
	public void add(StoryBoard storyBoard) throws ServiceException{
		String message ="";
		String[] array = storyBoard.getDividImgs().split(",");
		String[] cnNm = storyBoard.getCnNm().split(",");
		String[] cnCont = storyBoard.getCnCont().split(",");
		try {
			for(int i = 0; i < array.length; i++){

				if(!array[i].trim().equals("none")){
					CornerTbl corner = new CornerTbl();
					corner.setCtId(storyBoard.getCtId());
					corner.setRegDt(new Date());
					corner.setsDuration(Long.parseLong(array[i].trim()));
					corner.setCnCont(cnCont[i]);
					corner.setCnNm(cnNm[i]);
					corner.setsDuration(Long.parseLong(array[i].trim()));
					corner.setRegId(storyBoard.getModrId());
					cornerDao.save(corner);
				}
			}

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
	 * 
	 * @param corner
	 */
	@Modifying
	@Transactional
	public void delete(CornerTbl corner) throws ServiceException{
		String message ="";
		List<CornerTbl> infos = cornerDao.findAll(CornerSpecifications.CtIdEqual(corner.getCtId()));
		if(infos != null){
			try {

				for(CornerTbl delete : infos){
					cornerDao.delete(delete.getCnId());
				}
			} 	catch (PersistenceException e) {
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
	}

	/**
	 * 
	 * @param corner
	 * @return
	 */
	@Modifying	
	public long count(CornerTbl corner) throws ServiceException{
		String message ="";
		try {
			return cornerDao.count(CornerSpecifications.CtIdEqual(corner.getCtId()));
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
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}

	}

	/**
	 * 
	 * @param ctId
	 * @return
	 */
	@Modifying	
	public List<CornerTbl> findCornerList(long ctId) throws ServiceException{
		String message ="";
		try {
			return cornerDao.findAll(CornerSpecifications.CtIdEqual(ctId), new Sort(
					new Order(Direction.ASC, "sDuration")));
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
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}

}
