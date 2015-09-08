package kr.co.d2net.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import kr.co.d2net.dao.ProFlDao;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.ProFlTbl_;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.search.SearchControls;
import kr.co.d2net.dto.vo.Profile;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.utils.ObjectUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProFlServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProFlDao proFlDao;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;

	public Page<ProFlTbl> findAllProFl(Specification<ProFlTbl> specification,
			Pageable pageable) {
		return proFlDao.findAll(specification, pageable);
	}

	@Modifying
	@Transactional
	public void addAll(Set<ProFlTbl> profls) {
		proFlDao.save(profls);
	}

	/**
	 * 프로파일 정보를 저장한다.
	 * 
	 * @param profl
	 * @throws ServiceException
	 */
	@Modifying
	@Transactional
	public void add(ProFlTbl profl) throws ServiceException {
		profl.setRegDt(new Date());
		profl.setModrId(null);
		proFlDao.save(profl);
	}

	/**
	 * profile 전체리스트를 가져온다.
	 * 
	 * @param proFlTbl
	 * @param search
	 * @return
	 * @throws ServiceException
	 */
	public List<ProFlTbl> findProFlInfos(ProFlTbl proFlTbl, Search search)
			throws ServiceException {
		PageRequest pageRequest = new PageRequest(search.getPageNo() - 1,
				SearchControls.CODE_LIST_COUNT, new Sort(new Order(
						Direction.DESC, "regDt")));

		Page<ProFlTbl> proFlInfo;

		if (StringUtils.isBlank(search.getKeyword())) {
			proFlInfo = proFlDao.findAll(pageRequest);
		} else {
			proFlInfo = proFlDao.findAll(pageRequest);
		}

		return proFlInfo.getContent();
	}





	/**
	 * profileId를 이용해 프로파일 정보를 가져온다.
	 * 
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public ProFlTbl getProFlObj(Long proId) throws ServiceException {
		return proFlDao.findOne(proId);
	}

	/**
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List<ProFlTbl> findAll() throws ServiceException {
		return proFlDao.findAll();
	}

	/**
	 * 
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Profile> findProfileList() throws ServiceException {

		String[] profileFields = { "proFlId", "regrId", "modrId", "servBit",
				"ext", "vdoCodec", "vdoBitRate", "vdoHori", "vdoVert", "vdoFS",
				"vdoSync", "keyFrame", "chanPriority", "priority", "proFlnm",
				"picKind", "flNameRule", "regDt", "modDt", "useYn" };

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<ProFlTbl> root = cq.from(ProFlTbl.class);

		Path<Long> proFlId = root.get(ProFlTbl_.proFlId);
		Path<String> regrId = root.get(ProFlTbl_.regrId);
		Path<String> modrId = root.get(ProFlTbl_.modrId);
		Path<String> servBit = root.get(ProFlTbl_.servBit);
		Path<String> ext = root.get(ProFlTbl_.ext);
		Path<String> vdoCodec = root.get(ProFlTbl_.vdoCodec);
		Path<String> vdoBitRate = root.get(ProFlTbl_.vdoBitRate);
		Path<String> vdoHori = root.get(ProFlTbl_.vdoHori);
		Path<String> vdoVert = root.get(ProFlTbl_.vdoVert);
		Path<String> vdoFS = root.get(ProFlTbl_.vdoFS);
		Path<String> vdoSync = root.get(ProFlTbl_.vdoSync);
		Path<String> keyFrame = root.get(ProFlTbl_.keyFrame);
		Path<Integer> chanPriority = root.get(ProFlTbl_.chanPriority);
		Path<Integer> priority = root.get(ProFlTbl_.priority);
		Path<String> proFlnm = root.get(ProFlTbl_.proFlnm);
		Path<String> picKind = root.get(ProFlTbl_.picKind);
		Path<String> flNameRule = root.get(ProFlTbl_.flNameRule);
		Path<Date> regDt = root.get(ProFlTbl_.regDt);
		Path<Date> modDt = root.get(ProFlTbl_.modDt);
		Path<String> useYn = root.get(ProFlTbl_.useYn);

		cq.multiselect(proFlId, regrId, modrId, servBit, ext, vdoCodec,
				vdoBitRate, vdoHori, vdoVert, vdoFS, vdoSync, keyFrame,
				chanPriority, priority, proFlnm, picKind, flNameRule, regDt,
				modDt, useYn);
		cq.orderBy(cb.asc(proFlId));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		List<Object[]> authList = typedQuery.getResultList();

		if (authList != null) {
			List<Profile> profiles = new ArrayList<Profile>();

			for (Object[] list : authList) {
				Profile profile = new Profile();

				int i = 0;
				for (int j = 0; j < profileFields.length; j++) {
					ObjectUtils.setProperty(profile, profileFields[j], list[i]);
					i++;
				}
				profiles.add(profile);
			}
			return profiles;
		} else {
			return Collections.EMPTY_LIST;
		}
	}


	/**
	 * 
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Profile getProfileInfo(Long profileId) throws ServiceException {

		String[] profileFields = { "proFlId", "regrId", "modrId", "servBit",
				"ext", "vdoCodec", "vdoBitRate", "vdoHori", "vdoVert", "vdoFS",
				"vdoSync", "keyFrame", "chanPriority", "priority", "proFlnm",
				"picKind", "flNameRule", "regDt", "modDt", "useYn" };

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery cq = cb.createQuery();

		Root<ProFlTbl> root = cq.from(ProFlTbl.class);

		Path<Long> proFlId = root.get(ProFlTbl_.proFlId);
		Path<String> regrId = root.get(ProFlTbl_.regrId);
		Path<String> modrId = root.get(ProFlTbl_.modrId);
		Path<String> servBit = root.get(ProFlTbl_.servBit);
		Path<String> ext = root.get(ProFlTbl_.ext);
		Path<String> vdoCodec = root.get(ProFlTbl_.vdoCodec);
		Path<String> vdoBitRate = root.get(ProFlTbl_.vdoBitRate);
		Path<String> vdoHori = root.get(ProFlTbl_.vdoHori);
		Path<String> vdoVert = root.get(ProFlTbl_.vdoVert);
		Path<String> vdoFS = root.get(ProFlTbl_.vdoFS);
		Path<String> vdoSync = root.get(ProFlTbl_.vdoSync);
		Path<String> keyFrame = root.get(ProFlTbl_.keyFrame);
		Path<Integer> chanPriority = root.get(ProFlTbl_.chanPriority);
		Path<Integer> priority = root.get(ProFlTbl_.priority);
		Path<String> proFlnm = root.get(ProFlTbl_.proFlnm);
		Path<String> picKind = root.get(ProFlTbl_.picKind);
		Path<String> flNameRule = root.get(ProFlTbl_.flNameRule);
		Path<Date> regDt = root.get(ProFlTbl_.regDt);
		Path<Date> modDt = root.get(ProFlTbl_.modDt);
		Path<String> useYn = root.get(ProFlTbl_.useYn);

		cq.multiselect(proFlId, regrId, modrId, servBit, ext, vdoCodec,
				vdoBitRate, vdoHori, vdoVert, vdoFS, vdoSync, keyFrame,
				chanPriority, priority, proFlnm, picKind, flNameRule, regDt,
				modDt, useYn);
		cq.where(cb.equal(proFlId, profileId));

		TypedQuery<Object[]> typedQuery = em.createQuery(cq);

		Object[] tmpProfile = typedQuery.getSingleResult();
		Profile profile = new Profile();
		if (tmpProfile != null) {
			for (int j = 0; j < profileFields.length; j++) {
				ObjectUtils.setProperty(profile, profileFields[j], tmpProfile[j]);
			}
		} 
		return profile;
	}


}
