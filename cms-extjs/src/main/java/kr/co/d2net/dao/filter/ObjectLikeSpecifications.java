package kr.co.d2net.dao.filter;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import kr.co.d2net.dto.ArchiveTbl;
import kr.co.d2net.dto.ArchiveTbl_;
import kr.co.d2net.dto.AuthTbl;
import kr.co.d2net.dto.BusiPartnerTbl;
import kr.co.d2net.dto.BusiPartnerTbl_;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CategoryTbl_;
import kr.co.d2net.dto.CodeTbl;
import kr.co.d2net.dto.CodeTbl.CodeId;
import kr.co.d2net.dto.CodeTbl_;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsInstTbl_;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.DownloadTbl;
import kr.co.d2net.dto.DownloadTbl_;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl_;
import kr.co.d2net.dto.MenuTbl;
import kr.co.d2net.dto.ProFlTbl;
import kr.co.d2net.dto.ProFlTbl_;
import kr.co.d2net.dto.RoleAuthTbl;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.TraTbl;
import kr.co.d2net.dto.TraTbl_;
import kr.co.d2net.dto.TrsTbl;
import kr.co.d2net.dto.TrsTbl_;
import kr.co.d2net.dto.UserAuthTbl;
import kr.co.d2net.dto.UserTbl;
import kr.co.d2net.dto.UserTbl_;
import kr.co.d2net.dto.search.Search;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectLikeSpecifications {

	final static Logger logger = LoggerFactory.getLogger(ObjectLikeSpecifications.class);

	/**
	 * 컨텐츠 검색집합. 컨텐츠 조회에 필요한 where 조건이 모두 존재하며 각 beans의 값유무에 따라 조회조건이 추가 삭제된다.
	 * @param search
	 * @return
	 */
	public static Predicate contentsFilterSearch(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Root<CodeTbl> code,Join<ContentsTbl, ContentsInstTbl> inst,Join<ContentsTbl, SegmentTbl> segment,Join<EpisodeTbl, SegmentTbl> episode,Join<EpisodeTbl, CategoryTbl> category,Search search) {

		Predicate totalWhere = criteriaBuilder.and(criteriaBuilder.like(inst.get(ContentsInstTbl_.ctiFmt), search.getCtiFmt()+"%"),criteriaBuilder.equal(from.get(ContentsTbl_.useYn), "Y"),criteriaBuilder.equal(code.get(CodeTbl_.id).get("clfCD"), "DSCD"),criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), code.get(CodeTbl_.id).get("sclCd")));

		if(StringUtils.isNotBlank(search.getDataStatCd())){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), search.getDataStatCd()));
		}

		if(StringUtils.isNotBlank(search.getCtTyp())){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(from.get(ContentsTbl_.ctTyp), search.getCtTyp()));
		}
		if(StringUtils.isNotBlank(search.getCtCla())){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(from.get(ContentsTbl_.ctCla), search.getCtTyp()));
		}
		if(StringUtils.isNotBlank(search.getRistClfCd())){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(from.get(ContentsTbl_.ristClfCd), search.getCtTyp()));
		}

		if(search.getStartDt() != null){
			if(search.getPeriod().equals("regDt")){
				totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.regDt), search.getStartDt()),criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.regDt), search.getEndDt()));
			}else{
				totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.brdDd), search.getStartDt()),criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.brdDd), search.getEndDt()));
			}
		}

		if(StringUtils.isNotBlank(search.getKeyword())){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.like(from.get(ContentsTbl_.ctNm), search.getKeyword()+"%"));
		}

		if(search.getCategoryId() != 0){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.or(criteriaBuilder.equal(from.get(ContentsTbl_.categoryId), search.getCategoryId()),criteriaBuilder.like(category.get(CategoryTbl_.nodes), search.getNodes()+".%")));
		}

		return totalWhere; 

	}

	/**
	 * 컨텐츠 단일건 조회
	 * @param search
	 * @return
	 */
	public static Predicate contentFilterSearch(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Root<CodeTbl> code, long ctId) {
		Predicate totalWhere = criteriaBuilder.and(criteriaBuilder.equal(from.get(ContentsTbl_.ctId), ctId),criteriaBuilder.equal(from.get(ContentsTbl_.useYn), "Y"),criteriaBuilder.equal(code.get(CodeTbl_.id).get("clfCD"), "RIST"),criteriaBuilder.equal(from.get(ContentsTbl_.ristClfCd), code.get(CodeTbl_.id).get("sclCd")));
		return totalWhere; 
	}



	/**
	 * 변환 검색집합. 변환 조회에 필요한 where 조건이 모두 존재하며 각 beans의 값유무에 따라 조회조건이 추가 삭제된다.
	 * @param search
	 * @return
	 */
	public static Expression traFilterSearch(CriteriaBuilder cb,
			CriteriaQuery cq, Search search, Root<TraTbl> root,
			Root<ContentsTbl> root1, Root<CategoryTbl> root2,Root<ProFlTbl> root3) {

		Predicate totalWhere = cb.and(cb.equal(root.get(TraTbl_.ctId), root1.get(ContentsTbl_.ctId)),cb.equal(root1.get(ContentsTbl_.categoryId), root2.get(CategoryTbl_.categoryId)),cb.equal(root3.get(ProFlTbl_.proFlId), root.get(TraTbl_.proFlId)));

		//변환관리의 컨텐츠명
		if(StringUtils.isNotBlank(search.getTraContentNm())){
			totalWhere = cb.and(totalWhere,cb.like(root1.<String>get(ContentsTbl_.ctNm), "%"+search.getTraContentNm()+"%"));
		}
		//변환관리의 작업상태
		if(StringUtils.isNotBlank(search.getWorkStat())){
			totalWhere = cb.and(totalWhere,cb.equal(root.get(TraTbl_.jobStatus), search.getWorkStat()));
		}
		//변환관리의 시작일
		if(search.getStartDt() != null){
			totalWhere = cb.and(totalWhere,cb.greaterThanOrEqualTo(root.get(TraTbl_.regDt), search.getStartDt()));
		}
		//변환관리의 종료일
		if(search.getEndDt() != null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(search.getEndDt());
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			search.setEndDt(cal.getTime());
			if(logger.isInfoEnabled())
				logger.info("search.setEndDt() : " + search.getEndDt());
			totalWhere = cb.and(totalWhere,cb.lessThanOrEqualTo(root.get(TraTbl_.modDt), search.getEndDt()));
		}
		if(logger.isInfoEnabled())
			logger.info("tra.search.getCategoryId() : " + search.getCategoryId());

		//변환관리의 카테고리 트리 값
		if(search.getCategoryId() != 0){
			//totalWhere = cb.and(totalWhere,cb.equal(root2.get(CategoryTbl_.categoryId), search.getCategoryId()));
			totalWhere = cb.and(totalWhere,cb.or(cb.equal(root1.get(ContentsTbl_.categoryId), search.getCategoryId()),cb.like(root2.get(CategoryTbl_.nodes), search.getNodes()+".%")));
		}
		return totalWhere; 
	}


	/**
	 * 전송 검색집합. 변환 조회에 필요한 where 조건이 모두 존재하며 각 beans의 값유무에 따라 조회조건이 추가 삭제된다.
	 * @param search
	 * @return
	 */
	public static Expression trsFilterSearch(CriteriaBuilder cb,
			CriteriaQuery cq, Search search, Root<ContentsTbl> root,
			Root<ContentsInstTbl> root1, Root<BusiPartnerTbl> root2,
			Root<ProFlTbl> root3,Root<TrsTbl> root4,
			Root<CategoryTbl> root5,Root<EpisodeTbl> root6) {


		Predicate totalWhere = (cb.and(cb.equal(root.get(ContentsTbl_.ctId), root1.get(ContentsInstTbl_.ctId))
				,cb.equal(root1.get(ContentsInstTbl_.ctiId), root4.get(TrsTbl_.ctiId))
				,cb.equal(root4.get(TrsTbl_.busiPartnerId), root2.get(BusiPartnerTbl_.busiPartnerId))
				,cb.equal(root4.get(TrsTbl_.proFlId), root3.get(ProFlTbl_.proFlId))
				,cb.equal(root.get(ContentsTbl_.categoryId), root5.get(CategoryTbl_.categoryId))
				,cb.equal(root.get(ContentsTbl_.episodeId), root6.get(EpisodeTbl_.id).get("episodeId"))
				,cb.equal(root5.get(CategoryTbl_.categoryId), root6.get(EpisodeTbl_.id).get("categoryId"))));

		//변환관리의 컨텐츠명
		if(StringUtils.isNotBlank(search.getTraContentNm())){
			totalWhere = cb.and(totalWhere,cb.like(root.<String>get(ContentsTbl_.ctNm), "%"+search.getTraContentNm()+"%"));
		}
		//변환관리의 작업상태
		if(StringUtils.isNotBlank(search.getWorkStat())){
			totalWhere = cb.and(totalWhere,cb.equal(root4.get(TrsTbl_.workStatcd), search.getWorkStat()));
		}

		//변환관리의 시작일
		if(search.getStartDt() != null){
			if(logger.isInfoEnabled())
				logger.info("search.getStartDt() : " + search.getStartDt());
			totalWhere = cb.and(totalWhere,cb.greaterThanOrEqualTo(root4.get(TrsTbl_.regDt), search.getStartDt()));
		}

		//변환관리의 종료일
		if(search.getEndDt() != null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(search.getEndDt());
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			search.setEndDt(cal.getTime());
			if(logger.isInfoEnabled())
				logger.info("search.setEndDt() : " + search.getEndDt());
			totalWhere = cb.and(totalWhere,cb.lessThanOrEqualTo(root4.get(TrsTbl_.modDt), search.getEndDt()));
		}

		if(logger.isInfoEnabled())
			logger.info("search.getCategoryId() : " + search.getCategoryId());

		//변환관리의 카테고리 트리 값
		if(search.getCategoryId() != 0){
			//totalWhere = cb.and(totalWhere,cb.equal(root2.get(CategoryTbl_.categoryId), search.getCategoryId()));
			totalWhere = cb.and(totalWhere,cb.or(cb.equal(root.<Integer>get("categoryId"), search.getCategoryId()),cb.like(root2.<String>get("nodes"), search.getNodes()+".%")));
		}
		return totalWhere; 
	}



	/**
	 * 사용자 관리 UI JOIN관련.
	 * @param cb
	 * @param cq
	 * @param userAuth
	 * @param userAuth1
	 * @param search
	 * @return
	 */
	public static Predicate userFilterSearch(CriteriaBuilder cb,CriteriaQuery<Object[]> cq, Join<UserTbl, UserAuthTbl> userAuth,Join<AuthTbl, UserAuthTbl> userAuth1,Search search) {
		return cb.and(cb.equal(userAuth, userAuth1));
	}

	/**
	 * 영상의 포맷코드를 like로 검색
	 * @param search
	 * @return
	 */
	public static Predicate formatLike(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst,String prefix) {
		if(StringUtils.isNotBlank(prefix)) {
			String likePattern = prefix+"%";
			return criteriaBuilder.like(inst.get(ContentsInstTbl_.ctiFmt), likePattern);
		} else return null;
	}


	/**
	 * 검색 키워드로 like 검색
	 * @param search
	 * @return
	 */
	public static Predicate keyWordLike(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst,String prefix) {
		if(StringUtils.isNotBlank(prefix)) {
			String likePattern = prefix+"%";
			return criteriaBuilder.like(from.get(ContentsTbl_.ctNm), likePattern);
		}else return null; 
	}


	/**
	 * 등록일을 기준으로 기간검색
	 * @param search
	 * @return
	 */
	public static Predicate dateBetween(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst,Date startDt, Date endDt) {
		if(startDt != null && endDt != null) {
			return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(from.get(ContentsTbl_.regDt), startDt),criteriaBuilder.lessThanOrEqualTo(from.get(ContentsTbl_.regDt), endDt));
		}else return null; 
	}


	/**
	 * 데이터 상태 equal 검색
	 * @param search
	 * @return
	 */
	public static Predicate dataStatCdEqual(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst,String dataStatCd) {
		if(StringUtils.isNotBlank(dataStatCd)) {
			return criteriaBuilder.equal(from.get(ContentsTbl_.dataStatCd), dataStatCd);
		}else return null; 
	}

	/**
	 * 영상유형  equal 검색
	 * @param search
	 * @return
	 */
	public static Predicate ctTypEqual(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst,String ctTyp) {
		if(StringUtils.isNotBlank(ctTyp)) {
			return criteriaBuilder.equal(from.get(ContentsTbl_.ctTyp), ctTyp);
		}else return null; 
	}

	/**
	 * 카테고리 id equal 검색
	 * @param search
	 * @return
	 */
	public static Predicate categoryIdEqual(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ContentsTbl> from,Join<ContentsTbl, ContentsInstTbl> inst
			,Join<ContentsTbl, SegmentTbl> segment,Join<EpisodeTbl, SegmentTbl> episode,Join<CategoryTbl, EpisodeTbl> category
			,Integer categoryId) {

		if(categoryId != 0) {
			return criteriaBuilder.equal(from.get(ContentsTbl_.categoryId), categoryId);
		}else return null; 
	}


	/**
	 * 에피소드명 LIKE  검색
	 * @param search
	 * @return
	 */
	public static Predicate episodeNmLike(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<EpisodeTbl> from, Search search) {
		return criteriaBuilder.and(criteriaBuilder.equal(from.get(EpisodeTbl_.id).get("categoryId"), search.getCategoryId()));
	}


	/**
	 * 메뉴별 권한 읽기
	 * @param search
	 * @return
	 */
	public static Predicate roleAuthInMenuForUser(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<MenuTbl> from,Root<RoleAuthTbl> roleAuth,Root<UserTbl> user,Join<UserTbl, UserAuthTbl> userAuth,String userId) {
		return criteriaBuilder.and(criteriaBuilder.equal(from.<Integer>get("menuId"), roleAuth.<Integer>get("id").get("menuId"))
				,criteriaBuilder.equal(roleAuth.<Integer>get("id").get("authId"), userAuth.<Integer>get("id").get("authId"))
				,criteriaBuilder.notLike(roleAuth.<String>get("controlGubun"), "L%")
				,criteriaBuilder.equal(from.<Integer>get("depth"), 0)
				,criteriaBuilder.equal(user.<String>get("userId"), userId));
	}


	/**
	 * 권한별 서브메뉴  읽기
	 * @param search
	 * @return
	 */
	public static Predicate subMenuForUser(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<MenuTbl> from,Root<RoleAuthTbl> roleAuth,Root<UserTbl> user,Join<UserTbl, UserAuthTbl> userAuth,Search search) {
		String nodes = search.getNodes()+"%";
		return criteriaBuilder.and(criteriaBuilder.equal(from.<Integer>get("menuId"), roleAuth.<Integer>get("id").get("menuId"))
				,criteriaBuilder.equal(roleAuth.<Integer>get("id").get("authId"), userAuth.<Integer>get("id").get("authId"))
				,criteriaBuilder.notLike(roleAuth.<String>get("controlGubun"), "L%")
				,criteriaBuilder.like(from.<String>get("nodes"), nodes)
				,criteriaBuilder.equal(from.<Integer>get("depth"), 1)
				,criteriaBuilder.equal(user.<String>get("userId"), search.getUserId()));
	}



	/**
	 * 아카이브 조회
	 * @param search
	 * @return
	 */
	public static Predicate archivesFilterSearch(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<ArchiveTbl> from,Root<UserTbl> user,Root<CodeTbl> code,Join<ContentsInstTbl, ArchiveTbl> archive,Join<ContentsInstTbl, ContentsTbl> inst,Join<ContentsTbl, SegmentTbl> segment,Join<EpisodeTbl, SegmentTbl> episode,Join<CategoryTbl, EpisodeTbl> category,Search search) {

		Predicate totalWhere = criteriaBuilder.and(criteriaBuilder.equal(from.get(ArchiveTbl_.approveId), user.get(UserTbl_.userId)));

		CodeId id = new CodeId();
		id.setClfCD("WOST");
		totalWhere  = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(code.get(CodeTbl_.id).get("clfCD"), "WOST"),criteriaBuilder.equal(code.get(CodeTbl_.id).get("sclCd"), from.get(ArchiveTbl_.workStatCd)));
		if(StringUtils.isNotBlank(search.getSearchGb())){
			if(search.getSearchGb().endsWith("ctNm")){
				totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.like(inst.get(ContentsTbl_.ctNm), "%"+search.getKeyword()+"%"));
			}
			if(search.getSearchGb().endsWith("userNm")){
				totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.like(user.get(UserTbl_.userNm), "%"+search.getUserNm()+"%"));
			}
		}
		if(search.getStartDt() != null){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.greaterThanOrEqualTo(from.get(ArchiveTbl_.regDt), search.getStartDt()),criteriaBuilder.lessThanOrEqualTo(from.get(ArchiveTbl_.regDt), search.getEndDt()));
		}
		if(search.getCategoryId() != null && search.getCategoryId() != 0){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(inst.get(ContentsTbl_.categoryId),  search.getCategoryId() ));
		}
		if(search.getCtiId() != null && search.getCtiId() != 0){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(from.get(ArchiveTbl_.ctiId),  search.getCtiId() ));
		}
		return totalWhere; 
	}


	/**
	 * 다운로드  조회
	 * @param search
	 * @return
	 */
	public static Predicate downloadsFilterSearch(CriteriaBuilder criteriaBuilder,CriteriaQuery<Object[]> criteriaQuery,Root<DownloadTbl> from,Root<UserTbl> user,Root<CodeTbl> code,Join<ContentsInstTbl, DownloadTbl> archive,Join<ContentsInstTbl, ContentsTbl> inst,Join<ContentsTbl, SegmentTbl> segment,Join<EpisodeTbl, SegmentTbl> episode,Join<CategoryTbl, EpisodeTbl> category,Search search) {

		Predicate totalWhere = criteriaBuilder.and(criteriaBuilder.equal(from.get(DownloadTbl_.approveId), user.get(UserTbl_.userId)));

		CodeId id = new CodeId();
		id.setClfCD("WOST");
		totalWhere  = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(code.get(CodeTbl_.id).get("clfCD"), "WOST"),criteriaBuilder.equal(code.get(CodeTbl_.id).get("sclCd"), from.get(DownloadTbl_.workStatCd)));
		if(StringUtils.isNotBlank(search.getSearchGb())){
			if(search.getSearchGb().endsWith("ctNm")){
				totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.like(inst.get(ContentsTbl_.ctNm), "%"+search.getKeyword()+"%"));
			}
			if(search.getSearchGb().endsWith("userNm")){
				totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.like(user.get(UserTbl_.userNm), "%"+search.getUserNm()+"%"));
			}
		}
		if(search.getStartDt() != null){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.greaterThanOrEqualTo(from.get(DownloadTbl_.regDt), search.getStartDt()),criteriaBuilder.lessThanOrEqualTo(from.get(DownloadTbl_.regDt), search.getEndDt()));
		}
		if(search.getCategoryId() != null && search.getCategoryId() != 0){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(inst.get(ContentsTbl_.categoryId),  search.getCategoryId() ));
		}
		if(search.getCtiId() != null && search.getCtiId() != 0){
			totalWhere = criteriaBuilder.and(totalWhere,criteriaBuilder.equal(from.get(DownloadTbl_.ctiId),  search.getCtiId() ));
		}
		return totalWhere; 

	}
}
