package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.TraDao;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Tra;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("traService")
@Transactional(readOnly=true)
public class TraServiceImpl implements TraServices {
	
	@Autowired
	private TraDao traDao;
	

	@Override
	public List<Tra> findTraList(Search search) throws ServiceException {
		
		List<Tra> tras = null;
		try {
			traDao.findTraList(search);
		} catch (Exception e) {
			
		}
		return tras;
	}

	@Override
	@Transactional
	public void updateTraJobStatus(TraTbl traTbl) throws ServiceException {
		try {
			traDao.updateTraJobStatus(traTbl);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	

}
