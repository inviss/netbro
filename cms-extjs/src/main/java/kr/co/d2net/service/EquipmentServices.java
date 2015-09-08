package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.EquipmentDao;
import kr.co.d2net.dao.filter.EquipSpecifications;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.EquipmentTbl.EquipmentId;
import kr.co.d2net.dto.EquipmentTbl_;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.RoleAuthTbl.RoleAuthId;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.TraTbl_;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Equip;
import kr.co.d2net.dto.vo.Monitoring;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;
import kr.co.d2net.utils.Utility;

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
	public void add(EquipmentTbl equip) throws ServiceException{
		equipmentDao.save(equip);
	}

	/**
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List<EquipmentTbl> findAll() throws ServiceException{
		Sort sort = new Sort(new Order(Direction.DESC, "deviceIp"));
		return equipmentDao.findAll(sort);
	}

	/**
	 * 장비list를가져온다.
	 * @param search
	 * @return

	public List<EquipmentTbl> findEquipInfos(Search search) throws ServiceException{

		PageRequest pageRequest = new PageRequest(search.getPageNo()-1, SearchControls.CODE_LIST_COUNT);

		Page<EquipmentTbl> equipInfo;

		if(StringUtils.isBlank(search.getSearchEquipObj())){
			equipInfo = equipmentDao.findAll(pageRequest);
		}else{
			equipInfo = equipmentDao.findAll(EquipSpecifications.equipFilterSearchByNm(search.getEquipSelectBox(),search.getSearchEquipObj()), pageRequest);
		}
		return equipInfo.getContent();
	}
	 */


	public EquipmentTbl findOne(String deviceId, Integer deviceNum) throws ServiceException{
		return equipmentDao.findOne(EquipSpecifications.getEquipInfo(deviceId,deviceNum));
	}

	/**
	 * 장비 list의 카운트를 가져온다.
	 * @param search
	 * @return
	 */
	public Long count(Search search) throws ServiceException{

		if(StringUtils.isEmpty(search.getSearchEquipObj())){
			return equipmentDao.count();
		}else{
			return equipmentDao.count(EquipSpecifications.equipFilterSearchByNm(search.getEquipSelectBox(),search.getSearchEquipObj()));
		}
	}


	/**
	 * 장비정보를 save한다.
	 * @param equipment
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void updateEquipInfo(Equip equipment) throws ServiceException{

		if(logger.isInfoEnabled()){
			logger.info("equipment.getDeviceId() : " + equipment.getDeviceId());
			logger.info("equipment.getDeviceNum() : " + equipment.getDeviceNum());
		}

		try {
			EquipmentTbl equipmentTbl = findOne(equipment.getDeviceId(),equipment.getDeviceNum());

			equipmentTbl.setDeviceIp(equipment.getDeviceIp());
			equipmentTbl.setDeviceNm(equipment.getDeviceNm());
			equipmentTbl.setUseYn(equipment.getUseYn());
			//equipmentTbl.setDeviceClfCd(equipment.getDeviceClfCd());
			equipmentTbl.setRegDt(new Date());

			equipmentDao.save(equipmentTbl);
		} catch (Exception e) {
			// TODO: handle exception
		}


	}

	/**
	 * 장비정보를 저장하는 method.
	 * @param equip
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void saveEquipInfo(Equip equip) throws ServiceException {

		EquipmentTbl equipmentTbl = new EquipmentTbl();

		String tmpEquipKinds = null;

		/*장비 선택 값이 트렌스코더,트랜스퍼 확인
		 *TC = 트랜스코드, TS = 트랜스퍼
		 */
		if(logger.isInfoEnabled()){
			logger.info("equip.getEquipKinds() : " + equip.getEquipKinds());
		}

		if(equip.getEquipKinds().equals("TC")){
			tmpEquipKinds = "TC";
		}else{
			tmpEquipKinds = "TS";
		}

		/* deviceId를 자동 생성하기 위함.
		 * 
		 */
		List<EquipmentTbl> equipmentTbls = equipmentDao.findAll(EquipSpecifications.equipKinds(tmpEquipKinds));

		String tmpSize = Integer.toString(equipmentTbls.size() + 1);

		/*
		 * 장비 추가 인지 인스턴스 추가인지 구분
		 * E = 장비추가, I = 인스턴스추가
		 */

		EquipmentId id = new EquipmentId();

		id.setDeviceId(tmpEquipKinds + tmpSize);

		if(equip.getEquipAdd().equals("E")){
			id.setDeviceNum(1);
		}else{
			id.setDeviceNum(equip.getInstanceAdd());
		}
		//equipmentTbl.setDeviceId(tmpEquipKinds + tmpSize);
		equipmentTbl.setId(id);
		equipmentTbl.setDeviceNm(equip.getDeviceNm() + "#" + tmpSize +"_"+ id.getDeviceNum());
		equipmentTbl.setDeviceIp(equip.getDeviceIp());
		equipmentTbl.setDevicePort(equip.getDevicePort());
		equipmentTbl.setUseYn(equip.getUseYn());
		equipmentTbl.setRegDt(Utility.getTimestamp());

		equipmentDao.save(equipmentTbl);

	}


	/**
	 * 장비 인스턴스를 저장하는 method.
	 * @param equipment
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void saveEquipInstance(Equip equipment) throws ServiceException{

		EquipmentTbl equipmentTbl = findOne(equipment.getDeviceId(),1);
		EquipmentTbl tbl = new EquipmentTbl();
		EquipmentId id = new EquipmentId();

		id.setDeviceId(equipment.getDeviceId());
		id.setDeviceNum(equipment.getInstanceAdd());

		tbl.setId(id);
		tbl.setDeviceIp(equipment.getDeviceIp());

		String tmpDeviceNm = equipment.getDeviceNm().substring(0,equipment.getDeviceNm().lastIndexOf("_")+1);

		tbl.setDeviceNm(tmpDeviceNm + equipment.getInstanceAdd());
		tbl.setDevicePort(equipment.getDevicePort());
		tbl.setUseYn(equipmentTbl.getUseYn());
		tbl.setRegDt(Utility.getTimestamp());

		equipmentDao.save(tbl);

	}


	/**
	 * 변환정보 쪽 UI.
	 * 장비리스트 정보를 가져온다.
	 * @param search
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Monitoring> findEquipmentInfos(Search search) throws ServiceException{

		String[] monitoringFields = {"workStatCd","deviceNm","deviceIp","deviceId","prgrs"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<EquipmentTbl> root = cq.from(EquipmentTbl.class);		
		Root<ContentsTbl> root2 = cq.from(ContentsTbl.class);

		Path<String> deviceId = root.get("id").get("deviceId");
		Path<String> deviceNm = root.get(EquipmentTbl_.deviceNm);
		Path<String> deviceIp = root.get(EquipmentTbl_.deviceIp);
		Path<String> workStatcd = root.get(EquipmentTbl_.workStatcd);
		Path<Integer> prgrs = root.get(EquipmentTbl_.prgrs);

		Path<String> ctNm = root2.get(ContentsTbl_.ctNm);

		Join<EquipmentTbl, TraTbl> userAuth = root.join("tra", JoinType.INNER);
		Join<ContentsTbl, ContentsInstTbl> userAuth1 = root2.join("contentsInst", JoinType.INNER);

		cq.multiselect(workStatcd,deviceNm,deviceIp,deviceId,prgrs);
		cq.distinct(true);
		cq.orderBy(cb.asc(deviceId));

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
	}



	/**
	 * 장비 리스트를 보여주는 method.
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Equip> findEquipList(Search search) throws ServiceException{

		String[] equipFields = {"tmpDeviceId","deviceId","deviceNm","deviceIp","deviceNum","useYn"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<EquipmentTbl> root = cq.from(EquipmentTbl.class);

		Path<String> deviceId = root.get("id").get("deviceId");
		Path<String> tmpDeviceId = root.get("id").get("deviceId");
		Path<String> deviceNm = root.get(EquipmentTbl_.deviceNm);
		Path<String> deviceIp = root.get(EquipmentTbl_.deviceIp);
		Path<Integer> deviceNum = root.get("id").get("deviceNum");
		Path<String> useYn = root.get(EquipmentTbl_.useYn);

		cq.multiselect(tmpDeviceId,deviceId,deviceNm,deviceIp,deviceNum,useYn);
		cq.orderBy(cb.asc(deviceId));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);
		List<Object[]> MonitoringList =  typedQuery.getResultList();
		List<Equip> equips = new ArrayList<Equip>();

		for(Object[] list : MonitoringList) {
			Equip equip = new Equip();

			int i = 0;
			for(int j = 0; j<equipFields.length; j++){
				ObjectUtils.setProperty(equip, equipFields[j], list[i]);			
				i++;
			}
			equips.add(equip);
		}
		return equips;
	}



	/**
	 * List에서 클릭한 장보의 정보를 가져오는 method.
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public Equip getEquipInfo(Search search) throws ServiceException{

		String[] equipFields = {"tmpDeviceId","deviceId","deviceNm","deviceIp","devicePort","deviceNum","useYn"};

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<EquipmentTbl> root = cq.from(EquipmentTbl.class);

		Path<String> deviceId = root.get("id").get("deviceId");
		Path<String> tmpDeviceId = root.get("id").get("deviceId");
		Path<Integer> deviceNum = root.get("id").get("deviceNum");
		Path<String> deviceNm = root.get(EquipmentTbl_.deviceNm);
		Path<String> deviceIp = root.get(EquipmentTbl_.deviceIp);
		Path<Integer> devicePort = root.get(EquipmentTbl_.devicePort);
		Path<String> useYn = root.get(EquipmentTbl_.useYn);


		cq.multiselect(deviceId,tmpDeviceId,deviceNm,deviceIp,devicePort,deviceNum,useYn);
		cq.where(cb.and(cb.equal(root.get("id").get("deviceId"), search.getDeviceId())
				,cb.equal(root.get("id").get("deviceNum"), search.getDeviceNum())));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);
		Object[] tmpEquip =  typedQuery.getSingleResult();

		Equip equip = new Equip();

		for(int j = 0; j<equipFields.length; j++){
			ObjectUtils.setProperty(equip, equipFields[j], tmpEquip[j]);
		}
		return equip;
	}

}
