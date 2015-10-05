package kr.co.d2net.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.TrsTbl;
import kr.co.d2net.dto.json.Transfer;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.springframework.stereotype.Repository;

@Repository("trsDao")
public class TrsDaoImpl implements TrsDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}
	
	@Override
	public List<Transfer> findTransferJob(String stat, int size)
			throws DaoNonRollbackException {
		
		List<Transfer> transfers = null;
		List<TrsTbl> trsTbls = null;
		
		try {
			SpecificationResult<TrsTbl> result = repository.find(TrsTbl.class);

//			if(search.getPageNo() != null && search.getPageNo() > 0) {
//				int startNum = (search.getPageNo()-1) * search.getPageSize();
//				int endNum = startNum + search.getPageSize();
//				result.from(startNum).size(endNum);
//			}
			trsTbls = result.list();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return transfers;
	}

	@Override
	public List<TrsTbl> findTrsList(Search search) throws DaoNonRollbackException {

		List<TrsTbl> trsTbls = null;

		try {
			SpecificationResult<TrsTbl> result = repository.find(TrsTbl.class);

			if(search.getPageNo() != null && search.getPageNo() > 0) {
				int startNum = (search.getPageNo()-1) * search.getPageSize();
				int endNum = startNum + search.getPageSize();
				result.from(startNum).size(endNum);
			}
			trsTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findProfileList Error", e);
		}

		return trsTbls;
	}

	
	
	@Override
	public TrsTbl getTrsInfo(Long seq) throws DaoNonRollbackException {
		
		TrsTbl trsTbl = null;
		
		try {
			trsTbl	 = repository.find(TrsTbl.class,seq);
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getTrsInfo Error", e);
		}
		return trsTbl;
	}
	
	
	
	@Override
	public Transfer getTrsInfoForTransferJob(Long trsSeq)
			throws DaoNonRollbackException {
		Transfer transfer = null;
		TrsTbl trsTbl = null;
		
		try {
			SpecificationResult<TrsTbl> result = repository.find(TrsTbl.class);

//			if(search.getPageNo() != null && search.getPageNo() > 0) {
//				int startNum = (search.getPageNo()-1) * search.getPageSize();
//				int endNum = startNum + search.getPageSize();
//				result.from(startNum).size(endNum);
//			}
			trsTbl = result.single();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return transfer;
	}

	@Override
	public void updateTransferState(Transfer transfer)
			throws DaoRollbackException {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	
	
	@Override
	public void retryTrsObj(TrsTbl  tbl) throws DaoRollbackException {
		try {
			
			TrsTbl trsTbl = getTrsInfo(tbl.getSeq());
			
			trsTbl.setModDt(new Date());
			trsTbl.setWorkStatcd("000");
			trsTbl.setPrgrs(0);
			
			repository.update(trsTbl);

		} catch (Exception e) {
			throw new DaoRollbackException("", "retryTrsObj Error", e);
		}

	}



}
