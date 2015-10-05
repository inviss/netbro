package kr.co.d2net.dao;

import java.util.List;

import kr.co.d2net.dto.TrsTbl;
import kr.co.d2net.dto.json.Transfer;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Trs;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

public interface TrsDao {
	public List<Transfer> findTransferJob(String stat,int size) throws DaoNonRollbackException;
	public List<TrsTbl> findTrsList(Search search) throws DaoNonRollbackException;
	public TrsTbl getTrsInfo(Long seq) throws DaoNonRollbackException;
	public Transfer getTrsInfoForTransferJob(Long trsSeq) throws DaoNonRollbackException;
	public void updateTransferState(Transfer transfer) throws DaoRollbackException;
	public void retryTrsObj(TrsTbl tbl) throws DaoRollbackException;
}
