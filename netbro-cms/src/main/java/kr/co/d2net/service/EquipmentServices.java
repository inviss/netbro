package kr.co.d2net.service;

import java.io.InputStream;
import java.util.ArrayList;
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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.EquipmentDao;
import kr.co.d2net.dao.filter.EquipSpecifications;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.EquipmentTbl_;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.TraTbl_;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Monitoring;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.search.config.Configure;
import kr.co.d2net.utils.ObjectUtils;
import kr.co.d2net.utils.PropertiesUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class EquipmentServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private EquipmentDao equipmentDao;

	@PersistenceContext
	private EntityManager em;


	@Modifying
	@Transactional
	public void addAll(Set<EquipmentTbl> equips) throws ServiceException{
		equipmentDao.save(equips);
	}

	@Modifying
	@Transactional
	public void add(EquipmentTbl equip) throws ServiceException{
		equipmentDao.save(equip);
	}


	public List<EquipmentTbl> findAll() throws ServiceException{
		String message = "";
		try {
			Sort sort = new Sort(new Order(Direction.DESC, "deviceIp"));
			return equipmentDao.findAll(sort);
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
	 * 장비list를가져온다.
	 * @param search
	 * @return
	 */
	public List<EquipmentTbl> findEquipInfos(Search search) throws ServiceException{

		String message = "";

		try {
			PageRequest pageRequest = new PageRequest(search.getPageNo(), SearchControls.CODE_LIST_COUNT);

			Page<EquipmentTbl> equipInfo;

			if(StringUtils.isBlank(search.getSearchEquipObj())){
				equipInfo = equipmentDao.findAll(pageRequest);
			}else{
				equipInfo = equipmentDao.findAll(EquipSpecifications.equipFilterSearchByNm(search.getEquipSelectBox(),search.getSearchEquipObj()), pageRequest);
			}
			return equipInfo.getContent();
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


	public EquipmentTbl findOne(String deviceId) throws ServiceException{

		String message = "";

		try {
			return equipmentDao.findOne(deviceId);
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
	 * 장비 list의 카운트를 가져온다.
	 * @param search
	 * @return
	 */
	public Long count(Search search) throws ServiceException{

		String message = "";

		try {
			if(StringUtils.isEmpty(search.getSearchEquipObj())){
				return equipmentDao.count();
			}else{
				return equipmentDao.count(EquipSpecifications.equipFilterSearchByNm(search.getEquipSelectBox(),search.getSearchEquipObj()));
			}
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
	 * 장비정보를 save한다.
	 * @param equipment
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void updateEquipInfo(EquipmentTbl equipment) throws ServiceException{

		String message = "";

		try {
			EquipmentTbl equipmentTbl = findOne(equipment.getDeviceId());
			
			logger.debug("equipment.getDeviceclfCd() : " + equipment.getDeviceClfCd());

			equipmentTbl.setDeviceIp(equipment.getDeviceIp());
			equipmentTbl.setDeviceNm(equipment.getDeviceNm());
			equipmentTbl.setDeviceIp(equipment.getDeviceIp());
			equipmentTbl.setUseYn(equipment.getUseYn());
			equipmentTbl.setDeviceClfCd(equipment.getDeviceClfCd());
			equipmentTbl.setRegDt(new Date());

			equipmentDao.save(equipmentTbl);

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


	@Modifying
	@Transactional
	public void saveEquipInfo(EquipmentTbl equipment) throws ServiceException {

		EquipmentTbl equipmentTbl = new EquipmentTbl();

		String message = "";

		try {
			equipmentTbl.setDeviceId(equipment.getDeviceId());
			equipmentTbl.setDeviceIp(equipment.getDeviceIp());
			equipmentTbl.setDeviceNm(equipment.getDeviceNm());
			equipmentTbl.setDeviceIp(equipment.getDeviceIp());
			equipmentTbl.setUseYn(equipment.getUseYn());
			equipmentTbl.setRegDt(new Date());

			equipmentDao.save(equipmentTbl);

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
	 * 변환정보 쪽 UI.
	 * 장비리스트 정보를 가져온다.
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Monitoring> findEquipmentInfos(Search search) throws ServiceException{

		String message = "";

		String[] monitoringFields = {"workStatCd","deviceNm","deviceIp","deviceId","deviceClfCd"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<EquipmentTbl> root = cq.from(EquipmentTbl.class);
		//Root<ContentsInstTbl> root1 = cq.from(ContentsInstTbl.class);
		//Root<ContentsTbl> root2 = cq.from(ContentsTbl.class);
		//Root<TraTbl> root3 = cq.from(TraTbl.class);

		Path<String> deviceId = root.get(EquipmentTbl_.deviceId);
		Path<String> deviceClfCd = root.get(EquipmentTbl_.deviceClfCd);
		Path<String> deviceNm = root.get(EquipmentTbl_.deviceNm);
		Path<String> deviceIp = root.get(EquipmentTbl_.deviceIp);
		Path<String> workStatcd = root.get(EquipmentTbl_.workStatcd);

		//Path<String> ctNm = root2.get(ContentsTbl_.ctNm);

		//Path<Integer> prgrs = root3.get(TraTbl_.prgrs);

		//cq.multiselect(workStatcd,deviceNm,deviceIp,deviceId,deviceclfCd,ctNm,prgrs);
		cq.multiselect(workStatcd,deviceNm,deviceIp,deviceId,deviceClfCd);
		//cq.where(cb.and(cb.equal(root.get("ctiId"), root1.get("ctiId")),cb.equal(root1.get("ctId"), root2.get("ctId")),cb.equal(root2.get("ctId"), root3.get("ctId"))));


		try {
			TypedQuery<Object[]> typedQuery = em.createQuery(cq);
			List<Object[]> MonitoringList =  typedQuery.getResultList();
			List<Monitoring> monitorings = new ArrayList<Monitoring>();

			for(Object[] list : MonitoringList) {
				Monitoring monitoring = new Monitoring();

				int i = 0;
				for(int j = 0; j<monitoringFields.length; j++){
					ObjectUtils.setProperty(monitoring, monitoringFields[j], list[i]);			
					i++;
				}
				monitorings.add(monitoring);
			}

			return monitorings;
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
