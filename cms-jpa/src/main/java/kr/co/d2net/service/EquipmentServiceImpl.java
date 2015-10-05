package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.EquipmentDao;
import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("equipmentService")
@Transactional(readOnly=true)
public class EquipmentServiceImpl implements EquipmentServices {

	@Autowired
	private EquipmentDao equipmentDao;


	@Override
	@Transactional
	public void saveEquipInfo(EquipmentTbl equipmentTbl) throws ServiceException {
		try {
			equipmentDao.save(equipmentTbl);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	@Override
	@Transactional
	public void saveEquipInstance(EquipmentTbl equipmentTbl) throws ServiceException {
		try {
			equipmentDao.save(equipmentTbl);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	@Override
	public List<EquipmentTbl> findEquipList(Search search)
			throws ServiceException {
		try {
			equipmentDao.findEquipList(search);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public EquipmentTbl getEquipInfo(Search search) throws ServiceException {
		try {
			equipmentDao.getEquipInfo(search);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
