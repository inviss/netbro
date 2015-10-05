package kr.co.d2net.service;

import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.exception.ServiceException;

public interface ArchiveServices {
	public ArchiveTbl getArchiveJob(String statCd) throws ServiceException;
	
}
