package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.exception.ServiceException;

public interface ContentService {
	public Long addContent(ContentsTbl contentsTbl) throws ServiceException;
	
	public void addAllContents(List<ContentsTbl> contentsTbls) throws ServiceException;
	
	public void saveContent(ContentsTbl contentsTbl) throws ServiceException;
	
	public void saveAllContent(List<ContentsTbl> contentsTbls) throws ServiceException;
}
