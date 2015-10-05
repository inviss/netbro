package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.ContentsDao;
import kr.co.d2net.dao.ContentsInstDao;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.vo.ContentsInst;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("contentsInstService")
@Transactional(readOnly=true)
public class ContentsInstServiceImpl implements ContentsInstService {

	@Autowired
	private ContentsInstDao contentsInstDao;
	
	@Override
	public void saveContentsInst(ContentsInstTbl contentsInstTbl)
			throws ServiceException {
		contentsInstDao.save(contentsInstTbl);
	}

	@Override
	public ContentsInstTbl getContentsInst(ContentsInst contentsInst)
			throws ServiceException {
		
		return contentsInstDao.getContentsInstObj(contentsInst);
	}

	@Override
	public List<ContentsInstTbl> findContentsInst(ContentsInst contentsInst)
			throws ServiceException {
		 
		return contentsInstDao.findContentsInst(contentsInst);
	}

	@Override
	public void deleteContentsInst(ContentsInstTbl contentsInstTbl)
			throws ServiceException {
		contentsInstDao.deleteContentInst(contentsInstTbl);
	}

	@Override
	public Long countContentsInst(ContentsInst contentsInst)
			throws ServiceException {
		
		return contentsInstDao.count(contentsInst).longValue();
	}
	
	
}
