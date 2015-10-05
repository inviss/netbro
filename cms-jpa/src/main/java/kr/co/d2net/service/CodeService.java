package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.exception.ServiceException;

public interface CodeService {
	public void addCode(CodeTbl codeTbl) throws ServiceException;
	public void addAllCode(List<CodeTbl> codeTbls) throws ServiceException;
	public void updateCode(CodeTbl codeTbl) throws ServiceException;
	public void updateAllCode(List<CodeTbl> codeTbls) throws ServiceException;
}
