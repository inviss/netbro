package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ProBusiTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

@Repository("proBusiDao")
public class ProBusiDaoImpl implements ProBusiDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}



	@Override
	public void save(ProBusiTbl proBusiTbl) throws ServiceException {
		try {

			if(StringUtils.isNotBlank(proBusiTbl.getTmpProflId())){
				String[] tmp = proBusiTbl.getTmpProflId().split(",");

				for(int i = 0; i<tmp.length;i++){

					ProBusiTbl tbl = new ProBusiTbl();

					tbl.setBusiPartnerId(proBusiTbl.getBusiPartnerId());
					tbl.setProFlId(Long.parseLong(tmp[i]));

					if(proBusiTbl.getBusiPartnerId() != null && proBusiTbl.getBusiPartnerId() > 0L)
						repository.update(proBusiTbl);
					else
						repository.save(proBusiTbl);
				}
			}
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "saveProBusiInfo Error", e);
		}
	}



	@Override
	public List<ProBusiTbl> findProBusiList(Long id, Search search) throws ServiceException {

		List<ProBusiTbl> proBusiTbls = null;

		try {
			SpecificationResult<ProBusiTbl> result = repository.find(ProBusiTbl.class);

			if(search.getPageNo() != null && search.getPageNo() > 0) {
				int startNum = (search.getPageNo()-1) * search.getPageSize();
				int endNum = startNum + search.getPageSize();
				result.from(startNum).size(endNum);
			}
			proBusiTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findProBusiList Error", e);
		}

		return proBusiTbls;
	}



}
