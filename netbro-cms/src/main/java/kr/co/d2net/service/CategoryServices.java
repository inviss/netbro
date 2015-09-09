package kr.co.d2net.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;

import kr.co.d2net.dao.CategoryDao;
import kr.co.d2net.dao.filter.CategorySpecifications;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.EpisodeTbl.EpisodeId;
import kr.co.d2net.dto.SegmentTbl.SegmentId;
import kr.co.d2net.dto.search.Search;
import kr.co.d2net.dto.vo.Category;
import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DaoRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.RequestParamException;
import kr.co.d2net.exception.ServiceException;
import kr.co.d2net.search.config.Configure;
import kr.co.d2net.utils.PropertiesUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 카테고리 정보를 조회하기위한함수
 * @author asura
 *
 */
@Service
@Transactional(readOnly=true)
public class CategoryServices {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private EpisodeServices episodeServices;

	@Autowired
	private SegmentServices segmentServices;
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * category_tbl의 모든 정보를 조회해온다. 정렬순서는 controller에서 정의한 순서
	 * @return
	 */
	public List<CategoryTbl> findAll(String... orders) throws ServiceException  {
		String message = "";
		try{
			return categoryDao.findAll(new Sort(Sort.Direction.ASC, orders));
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}

	public Page<CategoryTbl> findAllCategory(Specification<CategoryTbl> specification, Pageable pageable) throws ServiceException  {
		String message = "";

		try{
			return categoryDao.findAll(specification, pageable);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}
	}

	/**
	 * category_tbl의 총 컬럼수를 구한다.
	 * @return
	 */
	public Long count() throws ServiceException  {
		String message = "";
		try{
			return categoryDao.count();
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}
	}


	/**
	 * 카테고리 정보를 신규로 생성한다. 
	 * @param category
	 */
	@Modifying
	@Transactional
	public void add(CategoryTbl category) throws ServiceException  {
		String message = "";
		try{
			categoryDao.save(category);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}
	}


	/**
	 * 변경된 카테고리 정보를 저장한다. 
	 * @param category
	 * @throws RequestParamException 
	 */
	@Modifying
	@Transactional
	public CategoryTbl updateCategory(Category category) throws ServiceException, RequestParamException  {

		int preOrderNum =0; // 이전 order 번호
		boolean plusMinus = true; // +-여부 판단, order 위치를 지정하는데 사용
		int startOrder =0; //노드 위치 변경 시작점 order
		int endOrder =0; // 노드 위치 변겅 종료점 order
		String message = "";//에러발생시 메세지는 변수
		CategoryTbl preParentsInfo = new CategoryTbl();
		List<CategoryTbl> infos = null;

		//변경할 카테고리의 메타정보를 가져온다.
		CategoryTbl info = getCategoryObj(category.getCategoryId());
		CategoryTbl afterInfo = new CategoryTbl();

		//만약 최상위 노드가 아니라면 부모 카테고리의 정보를 얻어온다.
		//최상위 노드라면 자신의 정보를 부모카테고리 정보로 입력한다.
		if(info != null){
			if(info.getPreParent() != null){

				preParentsInfo = getCategoryObj(info.getPreParent());

			}else{

				preParentsInfo = info;

			}

			//카테고리의 진행 방향 정보를 얻는다. up이면 기존 order에서 -1을 down이면 +1을 한다
			//노드 순서 변경점이 없다면 무시하고 넘어간다.
			if(StringUtils.isNotBlank(category.getDirection())){

				if(category.getDirection().equals("up")){

					category.setOrderNum(info.getOrderNum()-1);

				}else{

					category.setOrderNum(info.getOrderNum()+1);

				}

			}

			//변경하고자하는 정보가 카테고리의 위치정보이면 if문을 단순 명칭 변경이라면 else부분 로직을 수행한다.
			if(category.getOrderNum() != 0 && category.getOrderNum() != info.getOrderNum()){

				preOrderNum = info.getOrderNum();
				info.setOrderNum(category.getOrderNum());

				if(StringUtils.isNotBlank(category.getCategoryNm())){

					info.setCategoryNm(category.getCategoryNm());

				}

				//변경되는 순번정보를 저장한다.
				afterInfo = categoryDao.save(info);

				//변경하고자 하는 위치가 기존 위치보다 하위쪽이면 plusMinus의 값을 false로 변경한다.
				if(preOrderNum < category.getOrderNum()){

					startOrder = preOrderNum;
					endOrder = category.getOrderNum();
					plusMinus = false;

				}else{

					startOrder = category.getOrderNum();
					endOrder = preOrderNum; 

				}

				//최상의 노드가 아니라면 변경 해야하는 depth와 그범위를 조회한다.
				if(info.getPreParent() != null){
					try{
						infos = findOrderNumUpdateListByBetWeen(preParentsInfo.getNodes(), info.getDepth(),startOrder,endOrder);
					}catch(NullPointerException e){
						message = messageSource.getMessage("error.005",null,null);
						throw new RequestParamException("005",message,e);
					}
				}else{

					infos = findOrderNumUpdateListByBetWeen(null, info.getDepth(),startOrder,endOrder);

				}

				//조회된 결과를 가지고 나머지 노드의 순서를 +-1씩 해준다
				if(infos != null){
					for(CategoryTbl updateInfo : infos){

						if(updateInfo.getCategoryId() != category.getCategoryId()){

							if(plusMinus){

								updateInfo.setOrderNum(updateInfo.getOrderNum() + 1);

							}else{

								updateInfo.setOrderNum(updateInfo.getOrderNum() - 1);

							}

							categoryDao.save(updateInfo);

						}

					}
				}

			}else{

				if(StringUtils.isNotBlank(category.getCategoryNm())){

					info.setCategoryNm(category.getCategoryNm());

				}
				try{
					afterInfo =  categoryDao.save(info);
				}catch (PersistenceException e) {
					//e e = e.getCause();
					if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
						message = messageSource.getMessage("error.000",null,null);
						throw new ConnectionTimeOutException("000", message, e);
					} else {
						message = messageSource.getMessage("error.003",null,null);
						throw new DaoRollbackException("003", message, e);
					}
				} catch (Exception e) {
					message = messageSource.getMessage("error.003",null,null);
					throw new DaoRollbackException("003", message, e);
				}

			}
		}
		return afterInfo;

	}

	/**
	 * 카테고리 정보를 신규로 등록한다.
	 * @param category
	 */
	@Modifying
	@Transactional
	public CategoryTbl addCategory(Category category) throws ServiceException  {

		CategoryTbl info = null;
		CategoryTbl preInfo = null;
		String message = "";//에러발생시 메세지는 변수
		String preNodes = "";
		List<CategoryTbl> infos = null;



		if(category.getCategoryId() != null){
			info = getCategoryObj(category.getCategoryId());
		}

		CategoryTbl categoryTbl = new CategoryTbl();

		String tempNodse="";

		//info 정보가 null이 아니라면 기존 정보를 베이스로 카테고리 정보 생성
		if(info != null ){

			categoryTbl.setCategoryNm(category.getCategoryNm());

			//노드의 최상위 값을 찾아 넣는다.
			int result = info.getNodes().indexOf(".");

			if(result == -1){
				result = 1;
			}
			categoryTbl.setGroupId(Integer.parseInt(info.getNodes().substring(0,result)));

			//하위노드 추가이면 무조건 순번을 1로, 다음노드 추가이면 조회한 값기준으로 +1
			if(category.getType().equals("SUB")){

				categoryTbl.setOrderNum(1);
				categoryTbl.setDepth(info.getDepth() + 1);
				categoryTbl.setPreParent(info.getCategoryId());

			}else{

				if(info.getPreParent() != null){

					categoryTbl.setOrderNum(info.getOrderNum() + 1);
					categoryTbl.setDepth(info.getDepth());
					categoryTbl.setPreParent(info.getPreParent());

				}else{

					categoryTbl.setCategoryNm(category.getCategoryNm());
					categoryTbl.setDepth(0);
					categoryTbl.setOrderNum(info.getOrderNum() + 1);

				}

			}


			//1차저장
			try{
				categoryTbl = categoryDao.save(categoryTbl);
			}catch (PersistenceException e) {
				//e e = e.getCause();
				if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
					message = messageSource.getMessage("error.000",null,null);
					throw new ConnectionTimeOutException("000", message, e);
				} else {
					message = messageSource.getMessage("error.003",null,null);
					throw new DaoRollbackException("003", message, e);
				}
			} catch (Exception e) {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
			//1차저장 이후 생성된 카테고리 id로 node값 저장

			if(category.getType().equals("SUB")){

				tempNodse = info.getNodes()+"."+categoryTbl.getCategoryId();
				categoryTbl.setGroupId(info.getCategoryId());

			}else{

				if(info.getDepth() == 0 ){

					tempNodse = String.valueOf(categoryTbl.getCategoryId());
					categoryTbl.setGroupId(categoryTbl.getCategoryId());

				}else{

					preInfo =  getCategoryObj(info.getPreParent());
					tempNodse = preInfo.getNodes()+"."+categoryTbl.getCategoryId();
					preNodes = preInfo.getNodes();

				}

			}

			categoryTbl.setNodes(tempNodse);

			try{
				categoryTbl = categoryDao.save(categoryTbl);
			}catch (PersistenceException e) {
				//e e = e.getCause();
				if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
					message = messageSource.getMessage("error.000",null,null);
					throw new ConnectionTimeOutException("000", message, e);
				} else {
					message = messageSource.getMessage("error.003",null,null);
					throw new DaoRollbackException("003", message, e);
				}
			} catch (Exception e) {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}

			//신규로 업데이트 된 이후 삽입된 곳 아래의 순번을 +1씩 업데이트 한다.
			infos = findOrderNumUpdateList(preNodes, categoryTbl.getDepth(),categoryTbl.getOrderNum());

		}else{

			//root정보 생성

			categoryTbl.setCategoryNm(category.getCategoryNm());
			categoryTbl.setDepth(0);
			categoryTbl.setOrderNum(1);


			//1차저장
			categoryTbl = categoryDao.save(categoryTbl);

			//1차저장 이후 생성된 카테고리 id로 node값 저장
			categoryTbl.setNodes(String.valueOf(categoryTbl.getCategoryId()));
			categoryTbl.setGroupId(categoryTbl.getCategoryId());

			categoryDao.save(categoryTbl);

			//신규로 업데이트 된 이후 삽입된 곳 아래의 순번을 +1씩 업데이트 한다.
			infos = findOrderNumUpdateList(null, categoryTbl.getDepth(),categoryTbl.getOrderNum());

		}

		if(infos != null){

			try{
				for(CategoryTbl updateInfo : infos){

					if(updateInfo.getCategoryId() != categoryTbl.getCategoryId()){

						updateInfo.setOrderNum(updateInfo.getOrderNum() + 1);
						categoryDao.save(updateInfo);

					}

				}
			}catch (PersistenceException e) {
				//e e = e.getCause();
				if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
					message = messageSource.getMessage("error.000",null,null);
					throw new ConnectionTimeOutException("000", message, e);
				} else {
					message = messageSource.getMessage("error.003",null,null);
					throw new DaoRollbackException("003", message, e);
				}
			} catch (Exception e) {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		}

		/*카테고리 생성후 에피소드 세그먼트 정보 모두를 생성해준다.
		 * */

		//카테고리가 생기면 자동으로 기본 회차 기본 세그먼트를 생성해준다.
		EpisodeTbl newEpisode = new EpisodeTbl();
		EpisodeId id = new EpisodeId();

		id.setCategoryId(categoryTbl.getCategoryId());
		Integer maxEpisodeId = episodeServices.MaxCount(id.getCategoryId());

		if(maxEpisodeId != null){

			id.setEpisodeId(maxEpisodeId+1);

		}else{

			id.setEpisodeId(1);

		}


		newEpisode.setId(id);
		newEpisode.setEpisodeNm("기본회차");
		newEpisode.setRegDt(new Date());
		newEpisode.setUseYn("Y");

		try{
			episodeServices.add(newEpisode);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}

		SegmentTbl segment = new SegmentTbl();
		SegmentId segid = new SegmentId();
		segid.setCategoryId(id.getCategoryId());
		segid.setEpisodeId(maxEpisodeId);

		logger.debug("segid.getEpisodeId()    "+segid.getEpisodeId());
		if(segid.getEpisodeId() != null){

			segid.setEpisodeId(maxEpisodeId+1);
			segid.setSegmentId(1);

		}else{

			segid.setEpisodeId(1);
			segid.setSegmentId(1);

		}

		segment.setId(segid);
		segment.setSegmentNm("기본값");
		try{
			segmentServices.add(segment);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}
		return categoryTbl;


	}

	/**
	 * 카테고리id의 정보를 테이블에서 삭제한다.
	 * @param category
	 */
	@Modifying
	@Transactional
	public void delete(CategoryTbl category) throws ServiceException  {
		String message ="";
		try{
			categoryDao.delete(category.getCategoryId());
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			} else {
				message = messageSource.getMessage("error.003",null,null);
				throw new DaoRollbackException("003", message, e);
			}
		} catch (Exception e) {
			message = messageSource.getMessage("error.003",null,null);
			throw new DaoRollbackException("003", message, e);
		}

	}

	@Modifying
	@Transactional
	public String deleteNewCategory(Category category) throws ServiceException  {

		String message="";

		//삭제하고자하는 카테고리의 정보를 조회한다.
		CategoryTbl info = getCategoryObj(category.getCategoryId());
		CategoryTbl preParentsInfo = new CategoryTbl();


		//삭제하고자하는 카테고리가  최상위 노드라면 자신을 부모 카테고리에 집어놓고 아니라면 부모노드의 정보를 조회한다

		if(info != null){

			if(info.getPreParent() != null){

				preParentsInfo = getCategoryObj(info.getPreParent());

			}else{

				preParentsInfo = info;

			}

			List<CategoryTbl> infos = findSubNodesList(info.getNodes());

			//삭제하고자하는 카테고리의 하위 노드가 있는지 확인한다 하위 노드가 있다면 로직을 중단하고 나간다,
			if(infos != null){

				if(infos.size() > 1){

					return "N";

				}else{

					//하위노드가 없다면 해당 카테고리를 삭제하고 동일 깊이에 있고, 삭제카테고리보다 order가 높은 노드들의 orderNum을 1씩 마이너스해준다.
					categoryDao.delete(info.getCategoryId());
					List<CategoryTbl> updateInfos;
					try{
						updateInfos = findOrderNumUpdateList(preParentsInfo.getNodes(), info.getDepth(),info.getOrderNum());
					}catch (PersistenceException e) {
						//e e = e.getCause();
						if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
							message = messageSource.getMessage("error.000",null,null);
							throw new ConnectionTimeOutException("000", message, e);
						}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
								|| e instanceof NoResultException ) {
							message = messageSource.getMessage("error.001",null,null);
							throw new DataNotFoundException("001", message, e);
						} else {
							message = messageSource.getMessage("error.004",null,null);
							throw new DaoNonRollbackException("004", message, e);
						}
					}
					if(updateInfos != null){

						for(CategoryTbl updateInfo : updateInfos){

							updateInfo.setOrderNum(updateInfo.getOrderNum() - 1);
							add(updateInfo);
							try{
								categoryDao.save(updateInfo);
							}catch (PersistenceException e) {
								//e e = e.getCause();
								if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
									message = messageSource.getMessage("error.000",null,null);
									throw new ConnectionTimeOutException("000", message, e);
								} else {
									message = messageSource.getMessage("error.003",null,null);
									throw new DaoRollbackException("003", message, e);
								}
							} catch (Exception e) {
								message = messageSource.getMessage("error.003",null,null);
								throw new DaoRollbackException("003", message, e);
							}
						}

					}

					return "Y";

				}
			}
		}
		return "N";

	}

	/**
	 * 카테고리 정보를 조회하는 함수 dept가 0일 경우에는 최초 카테고리 정보만 조회한다.
	 * @param search
	 * @return
	 */


	public List<CategoryTbl> findMainCategory(CategoryTbl categoryTbl, Search search) throws ServiceException  {
		String message = "";
		try {
			if(categoryTbl.getDepth() == -1 && categoryTbl.getCategoryId() == 0){// 최초 카테고리 전체조회
				return categoryDao.findAll(CategorySpecifications.DepthEqual(search.getDepth()),new Sort(Sort.Direction.ASC, "orderNum"));
			}else if(categoryTbl.getDepth() != -1 && categoryTbl.getCategoryId() != 0){//카테고리의 하위 카테고리 조회
				return categoryDao.findAll(CategorySpecifications.findSubList(categoryTbl.getNodes(),search.getDepth()),new Sort(Sort.Direction.ASC, "orderNum"));
			}else{

				// 카테고리 아이디 조회
				List<CategoryTbl> infos = new ArrayList<CategoryTbl>();

				if(categoryTbl.getCategoryId() != null){

					CategoryTbl info = getCategoryObj(categoryTbl.getCategoryId());

					if(info != null){
						infos.add(info);
						return infos;
					}
				}else{
					return Collections.emptyList();
				}
			}

			return Collections.emptyList();

		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}	

	/**
	 * 카테고리 정보를 조회하는 함수 dept가 0일 경우에는 최초 카테고리 정보만 조회한다.
	 * @param search
	 * @return
	 */
	public List<CategoryTbl> findMainCategory(Search search) throws ServiceException  {

		String message = "";

		try {
			if(search.getDepth() == 0 && search.getCategoryId() == 0){// 최초 카테고리 전체조회

				return categoryDao.findAll(CategorySpecifications.DepthEqual(search.getDepth()),new Sort(Sort.Direction.ASC, "orderNum"));

			}else if(search.getDepth() != 0 && search.getCategoryId() != 0){//카테고리의 하위 카테고리 조회

				CategoryTbl newCategoryTbl = getCategoryObj(search.getCategoryId());

				return categoryDao.findAll(CategorySpecifications.findSubList(newCategoryTbl.getNodes(),search.getDepth()),new Sort(Sort.Direction.ASC, "orderNum"));

			}else{// 카테고리 아이디 조회

				List<CategoryTbl> infos = new ArrayList<CategoryTbl>();
				CategoryTbl info = getCategoryObj(search.getCategoryId());
				info.setEpisodeTbl(null);
				infos.add(info);

				return infos;

			}
		} catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}	

	/**
	 * 카테고리단일 건을 조회하는 함수
	 * @param categoryId
	 * @return
	 */
	public CategoryTbl getCategoryObj(Integer categoryId) throws ServiceException  {
		String message = "";
		try {
			 
		
			return categoryDao.findOne(categoryId);
		}catch (PersistenceException e) {
			//e e = e.getCause();
			logger.error("error is "+e);
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
		
	}


	/**
	 * 카테고리 정보를 모두 조회하는 함수.(이 조회건의 결과에는 dept정보가 들어있지 않다.)
	 * @return
	 */
	public List<CategoryTbl> getLastCategoryObj() throws ServiceException  {
		String message = "";
		try {
			return categoryDao.findAll();
		}catch (PersistenceException e) {
		//	e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}



	/**
	 * category_id, depth의 정보를 가지고 하위 카테고리 정보를 조회한다.
	 * @param categoryId
	 * @param depth
	 * @return List<CategoryTbl>
	 */
	public List<CategoryTbl> findChildCategory(Integer categoryId, Integer depth) throws ServiceException  {
		String message = "";
		try {
			return categoryDao.findAll(CategorySpecifications.findSubList(categoryId, depth));
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}	

	/**
	 * 입력된 카테고리id를 부모키로 하는 모든 카테고리 정보를 조회한다.
	 * @param categoryId
	 * @return List<CategoryTbl>
	 */
	public List<CategoryTbl> findSubCategory(Integer categoryId) throws ServiceException  {
		String message = "";
		try {
			return categoryDao.findAll(CategorySpecifications.findSubList(categoryId));
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}

	}


	/**
	 * root카테고리중 최대값을 찾는다
	 * @param categoryId
	 * @return List<CategoryTbl>
	 */

	public CategoryTbl getMaxCategory() throws ServiceException  {
		String message = "";
		categoryDao.findOne(CategorySpecifications.DepthEqual(0));
		return null;
	}

	/**
	 * nodes, depth, orderNum의 컬럼을 가지고
	 * @param categoryId
	 * @return List<CategoryTbl>
	 */

	public List<CategoryTbl> findOrderNumUpdateList(String nodes,Integer depth,Integer orderNum) throws ServiceException  {
		String message = "";
		try {
			return categoryDao.findAll(CategorySpecifications.findListForOderNum(nodes,depth,orderNum));
		} catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}


	/**
	 * nodes컬럼을 이용해 하위 카테고리 리스트를 조회한다.
	 * @param categoryId
	 * @return List<CategoryTbl>
	 */

	public List<CategoryTbl> findSubNodesList(String nodes) throws ServiceException  {
		String message = "";
		try {
			return categoryDao.findAll(CategorySpecifications.NodesLike(nodes));

		} catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}


	/**
	 * 카테고리id, depth를 이용해orderNum 사이의 리스트를 불러온다
	 * @param nodes
	 * @param depth
	 * @param orderNum
	 * @return List<NewCategoryTbl>
	 */

	public List<CategoryTbl> findOrderNumUpdateListByBetWeen(String nodes,Integer depth,Integer startOrder,Integer endOrder) throws ServiceException  {
		String message = "";
		try {
			return categoryDao.findAll(CategorySpecifications.findListForOderNumByBetWeen(nodes,depth, startOrder, endOrder));
		}catch (PersistenceException e) {
			//e e = e.getCause();
			if(e instanceof LockTimeoutException || e instanceof QueryTimeoutException) {
				message = messageSource.getMessage("error.000",null,null);
				throw new ConnectionTimeOutException("000", message, e);
			}else if(e instanceof EntityExistsException || e instanceof EntityNotFoundException
					|| e instanceof NoResultException ) {
				message = messageSource.getMessage("error.001",null,null);
				throw new DataNotFoundException("001", message, e);
			} else {
				message = messageSource.getMessage("error.004",null,null);
				throw new DaoNonRollbackException("004", message, e);
			}
		}catch (Exception e) {
			message = messageSource.getMessage("error.004",null,null);
			throw new DaoNonRollbackException("004", message, e);
		}
	}
}
