package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.ServiceException;

public interface EquipmentDao {
	
	public List<EquipmentTbl> findEquipList(Search search) throws ServiceException;
	public EquipmentTbl getEquipInfo(Search search) throws ServiceException;
	public void save(EquipmentTbl equipmentTbl) throws DaoRollbackException;
	
}
