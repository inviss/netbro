package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.CodeDao;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="codeService")
public class CodeServiceImpl implements CodeService {
	
	@Autowired
	private CodeDao codeDao;

	@Override
	public void addCode(CodeTbl codeTbl) throws ServiceException {
		codeDao.save(codeTbl);
	}

	@Override
	public void addAllCode(List<CodeTbl> codeTbls) throws ServiceException {
		for(CodeTbl codeTbl : codeTbls) {
			addCode(codeTbl);
		}
	}

	@Override
	public void updateCode(CodeTbl codeTbl) throws ServiceException {
		// 명시적으로 update라고 선언함.
		codeTbl.update();
		codeDao.save(codeTbl);
	}

	@Override
	public void updateAllCode(List<CodeTbl> codeTbls) throws ServiceException {
		for(CodeTbl codeTbl : codeTbls) {
			updateCode(codeTbl);
		}
	}

}
