package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.filter.ContentSpecifications;
import kr.co.d2net.dao.filter.ContentsInstSpecifications;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.vo.ContentsInst;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

@Repository("contentsInstDao")
public class ContentsInstDaoImpl implements ContentsInstDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	/**
	 * 클립에 대한 메타 정보를 저장한다. ctiId, ctId의 값이 없다면 insert 있다면 update를 시행한다.
	 * @param  contentsInstTbl
	 */
	@Override
	public void save(ContentsInstTbl contentsInstTbl)
			throws DaoRollbackException {
		
		try {
			if((contentsInstTbl.getCtId() != null && contentsInstTbl.getCtiId() != null)&& (contentsInstTbl.getCtId() > 0L && contentsInstTbl.getCtiId() > 0L)){
				System.out.println("update start!!!");
				repository.update(contentsInstTbl);
			}else{
				 System.out.println("insert start!!!"); 
				repository.save(contentsInstTbl);
			}
		} catch (Exception e) {
			throw new DaoRollbackException("", "ContentInst Add Error", e);
		}
		
	}

	/**
	 * 클립정보를 삭제한다. 메타정보 자체를 db에서 완전 삭제하고자 할때 이용한다.
	 * @param contentsInstTbl
	 */
	@Override
	public void deleteContentInst(ContentsInstTbl contentsInstTbl)
			throws DaoRollbackException {

		repository.remove(contentsInstTbl);
		
	}

	/**
	 * contentsInst에 저장되어있는 정보를 조회한다. 
	 * @param ctId
	 * @param ctiFmt
	 */
	@Override
	public ContentsInstTbl getContentsInstObj(ContentsInst contentsInst)
			throws DaoNonRollbackException {
		ContentsInstTbl contentsInstTbl;
		 
			contentsInstTbl = repository.find(ContentsInstTbl.class, ContentsInstSpecifications.findContentInst(contentsInst)).single();
		 
		return contentsInstTbl;
	}

 
	/**
	 * ctId에 소속되어있는 고,저해상도 영상의 정보 리스트를 조회한다. 
	 * @param ctId 
	 */
	@Override
	public List<ContentsInstTbl> findContentsInst(ContentsInst contentsInst) throws DaoNonRollbackException {
		List<ContentsInstTbl> contentsInstTbls = repository.find(ContentsInstTbl.class, ContentsInstSpecifications.findContentInst(contentsInst)).list();
		return contentsInstTbls;
	}

	/**
	 * ctId에 소속되어있는 고,저해상도 영상의 총갯수를 조회한다 
	 * @param ctId
	 * @param ctiFmt
	 */
	@Override
	public Long count(ContentsInst contentsInst) throws DaoNonRollbackException {
		Long count = repository.count(ContentsInstTbl.class, ContentsInstSpecifications.findContentInst(contentsInst));
		return count;
	} }
