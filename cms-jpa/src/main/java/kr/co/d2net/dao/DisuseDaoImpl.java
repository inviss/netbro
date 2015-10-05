package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.filter.DisuseSpecifications;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.DisuseInfoTbl;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Disuse;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.springframework.stereotype.Repository;

@Repository("disuseDao")
public class DisuseDaoImpl implements DisuseDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	/**
	 * 폐기상태를 입력, 수정한다. 폐기신청,폐기취소,기간연장을 정보를 저장할수있다.
	 * 작성자 : asura
	 */
	@Override
	public void save(DisuseInfoTbl disuseInfoTbl) throws DaoRollbackException {
		 
		if(disuseInfoTbl.getCtId() != null && disuseInfoTbl.getCtId() > 0){
			repository.update(disuseInfoTbl);
		}else{
			repository.save(disuseInfoTbl);			
		}
	}

	/**
	 * 폐기 현환에 대해서 조회를 한다.
	 * search 조건에 따라 조회 조건이 동적으로 추가된다.
	 * 작성자 : asura
	 */
	@Override
	public List<DisuseInfoTbl> findDisuseInfoList(Search search)
			throws DaoNonRollbackException {
		List<DisuseInfoTbl> disuseInfoTbls = null;
		SpecificationResult<DisuseInfoTbl> result =  repository.find(DisuseInfoTbl.class, DisuseSpecifications.findDisuseList(search));
		
		if(search.getPageNo() != null && search.getPageNo() > 0){
			int startNum = (search.getPageNo()-1) * SearchControls.DISCARD_LIST_COUNT;
			int endNum = startNum + SearchControls.DISCARD_LIST_COUNT;
			result.from(startNum).size(endNum);
		}
		
		disuseInfoTbls = result.list();
		return disuseInfoTbls;
	}

	
/**
 * 총 폐기건수의 갯수를 구한다.
 * findDisuseInfoList과 조회 조건을 동일하나 paging 조건이 없으며, 조회 대상 컬럼도 없고 long으로 값을 리턴한다.
 * 작성자 : asura
 */
	@Override
	public Long count(Search search) throws DaoNonRollbackException {
		search.count();
		return repository.count(DisuseInfoTbl.class,DisuseSpecifications.findDisuseList(search));
	}

	/**
	 * 단일 폐기건에 대해서 정보를 조회한다. disuseInfoTbl을 대상으로 조회를 한다.
	 * 작성자 : asura
	 */
	@Override
	public DisuseInfoTbl getDisuserInfo(Long ctId) throws DaoNonRollbackException {
		DisuseInfoTbl disuseInfoTbl;
		try {
			disuseInfoTbl = repository.find(DisuseInfoTbl.class, ctId);
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "Disuse get Error", e);
		}
		
		return disuseInfoTbl;
	}}
