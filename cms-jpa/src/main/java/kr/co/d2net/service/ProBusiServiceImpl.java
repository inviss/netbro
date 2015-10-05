package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.ProBusiDao;
import kr.co.d2net.dto.ProBusiTbl;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("proBusiService")
@Transactional(readOnly=true)
public class ProBusiServiceImpl implements ProBusiServices {

	@Autowired
	private ProBusiDao proBusiDao;

	@Override
	@Transactional
	public void saveProBusiInfo(ProBusiTbl proBusiTbl) throws ServiceException {
		try {
			proBusiDao.save(proBusiTbl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		
	}

	@Override
	public List<ProBusiTbl> findProBusiInfo(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteBusiInfo(ProBusiTbl proBusiTbl, ProFlTbl proFlTbl,
			Search search) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

}
