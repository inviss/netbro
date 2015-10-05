package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.ContentsModDao;
import kr.co.d2net.dto.ContentsModTbl;
import kr.co.d2net.dto.vo.Content;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("contentsModService")
@Transactional(readOnly=true)
public class ContentsModServiceImpl implements ContentsModService {

	@Autowired
	private ContentsModDao contentsModDao;
	
	@Override
	public void insertContentsModInfo(ContentsModTbl contentsModTbl)
			throws ServiceException {
		contentsModDao.save(contentsModTbl);
	}

	@Override
	public List<Content> findContentsModList(Search search)
			throws ServiceException {
		List<ContentsModTbl> contentsModTbls = contentsModDao.findContentsModList(search);
		List<Content> contents = null;
		
		for(ContentsModTbl contentsModTbl : contentsModTbls){
			Content content = new Content();
			
			content.setDataStatCd(contentsModTbl.getDataStatcd());
			content.setCtId(contentsModTbl.getCtId());
			content.setModDt(contentsModTbl.getModDt());
			content.setModrId(contentsModTbl.getModId()); 
			
			contents.add(content);
		}
		return contents;
	}

	@Override
	public Long countContentsMod(Search search) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}}
