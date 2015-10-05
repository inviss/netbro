package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.ArchiveDao;
import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("archiveService")
@Transactional(readOnly=true)
public class ArchiveServiceImpl implements ArchiveServices {

	@Autowired
	private ArchiveDao archiveDao;


	@Override
	public ArchiveTbl getArchiveJob(String statCd) throws ServiceException {

		ArchiveTbl archiveTbl = null;
		try {
			archiveTbl = archiveDao.getArchiveJob(statCd);
		} catch (Exception e) {
			throw new ServiceException("",e);
		}
		

		return archiveTbl;
	}




	/*
	 * 

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
	 */

}
