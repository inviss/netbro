package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.ContentsDao;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("contentService")
@Transactional(readOnly=true)
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private ContentsDao contentsDao;

	@Override
	@Transactional
	public Long addContent(ContentsTbl contentsTbl) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAllContents(List<ContentsTbl> contentsTbls)
			throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public void saveContent(ContentsTbl contentsTbl) throws ServiceException {
		try {
			contentsDao.save(contentsTbl);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}

	@Override
	@Transactional
	public void saveAllContent(List<ContentsTbl> contentsTbls)
			throws ServiceException {
		for(ContentsTbl contentsTbl : contentsTbls) {
			saveContent(contentsTbl);
		}
	}

}
