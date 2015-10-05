package kr.co.d2net.service;

import java.util.List;

import kr.co.d2net.dto.TrsTbl;
import kr.co.d2net.dto.json.Transfer;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Trs;
import kr.co.d2net.exception.ServiceException;

public interface TrsServices {
	public List<Transfer> findTransferJob(String stat,int size) throws ServiceException;
	public List<Trs> findTrsList(Search search) throws ServiceException;
	public TrsTbl getTrsInfo(Long seq) throws ServiceException;
	public Transfer getTrsInfoForTransferJob(Long trsSeq) throws ServiceException;
	public void updateTransferState(Transfer transfer) throws ServiceException;
	public void retryTrsObj(Search search) throws ServiceException;
}
