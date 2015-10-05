package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.List;

import kr.co.d2net.dao.DisuseDao;
import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.vo.Disuse;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("disuseService")
@Transactional(readOnly=true)
public class DisuseServiceImpl implements DisuseService {

	@Autowired
	private DisuseDao disuseDao;
	
	@Override
	public void saveDisuse(DisuseInfoTbl disuseInfoTbl) throws ServiceException {
		disuseDao.save(disuseInfoTbl);
	}

	@Override
	public void deleteDisuse(DisuseInfoTbl disuseInfoTbl) throws ServiceException {
		 
	}

	@Override
	public Disuse getDisuse(Search search) throws ServiceException {
		DisuseInfoTbl disuseInfoTbl = disuseDao.getDisuserInfo(search.getCtId());
		Disuse disuse = new Disuse();
		
		disuse.setCtId(disuseInfoTbl.getCtId());
		return disuse;
	}

	@Override
	public List<Disuse> findDisuse(Search search)
			throws ServiceException {
		List<DisuseInfoTbl> disuseInfoTbls = disuseDao.findDisuseInfoList(search); 
		List<Disuse> results = new ArrayList<Disuse>();
		
		/**
		 * dto에서 얻어온 정보를 vo로 옮겨놓는다
		 */
		for(DisuseInfoTbl disuseInfoTbl : disuseInfoTbls){
			Disuse disuse = new Disuse();
			
			disuse.setCtId(disuseInfoTbl.getCtId());
			results.add(disuse);
		}
		return results;
		
	}

	@Override
	public Long countDisuse(Search search) throws ServiceException {
		
		return disuseDao.count(search);
	}}
