package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

public interface EquipmentServices {
	public void saveEquipInfo(EquipmentTbl equip) throws ServiceException;
	public void saveEquipInstance(EquipmentTbl equip) throws ServiceException;
	public List<EquipmentTbl> findEquipList(Search search) throws ServiceException;
	public EquipmentTbl getEquipInfo(Search search) throws ServiceException;
	
}
