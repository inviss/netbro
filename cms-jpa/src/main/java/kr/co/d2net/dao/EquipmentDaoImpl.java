package kr.co.d2net.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kr.co.d2net.dao.api.SpecificationResult;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dao.support.JpaRepository;
import kr.co.d2net.dao.support.SpecificationBuilder;
import kr.co.d2net.dto.EquipmentTbl;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

@Repository("equipmentDao")
public class EquipmentDaoImpl implements EquipmentDao {

	@PersistenceContext
	private EntityManager em;
	private kr.co.d2net.dao.api.Repository repository;

	@PostConstruct
	public void setup() {
		repository = new JpaRepository(em);
	}

	@Override
	public void save(EquipmentTbl equipmentTbl) throws DaoRollbackException {
		try {
			if (StringUtils.isNotBlank(equipmentTbl.getDeviceId()))
				repository.update(equipmentTbl);
			else
				repository.save(equipmentTbl);
		} catch (Exception e) {
			throw new DaoRollbackException("", "Equipment Add Error", e);
		}
	}

	@Override
	public List<EquipmentTbl> findEquipList(Search search)
			throws ServiceException {
		List<EquipmentTbl> equipmentTbls = null;

		try {
			SpecificationResult<EquipmentTbl> result = repository
					.find(EquipmentTbl.class);

			if (search.getPageNo() != null && search.getPageNo() > 0) {
				int startNum = (search.getPageNo() - 1) * search.getPageSize();
				int endNum = startNum + search.getPageSize();
				result.from(startNum).size(endNum);
			}
			equipmentTbls = result.list();

		} catch (Exception e) {
			throw new DaoNonRollbackException("", "findEquipList Error", e);
		}

		return equipmentTbls;
	}

	@Override
	public EquipmentTbl getEquipInfo(Search search) throws ServiceException {
		EquipmentTbl equipmentTbl = null;

		try {
			Specification<EquipmentTbl> specification = SpecificationBuilder
					.forProperty("deviceId").equal(search.getDeviceId()).and()
					.forProperty("deviceNum").equal(search.getDeviceNum()).build();
			
			equipmentTbl = repository.find(EquipmentTbl.class, specification).single();
			
			return equipmentTbl;
			
		} catch (Exception e) {
			throw new DaoNonRollbackException("", "getEquipInfo Error", e);
		}
	}

}
