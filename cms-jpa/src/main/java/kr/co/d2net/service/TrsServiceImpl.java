package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dao.TraDao;
import kr.co.d2net.dao.TrsDao;
import kr.co.d2net.dto.TrsTbl;
import kr.co.d2net.dto.json.Transfer;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Trs;
import kr.co.d2net.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("trsService")
@Transactional(readOnly=true)
public class TrsServiceImpl implements TrsServices {
	
	@Autowired
	private TrsDao trsDao;

	@Override
	public List<Transfer> findTransferJob(String stat, int size)
			throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public List<Trs> findTrsList(Search search) throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public TrsTbl getTrsInfo(Long seq) throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Transfer getTrsInfoForTransferJob(Long trsSeq)
			throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public void updateTransferState(Transfer transfer) throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void retryTrsObj(Search search) throws ServiceException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
