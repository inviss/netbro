package kr.co.d2net.dao.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;

import kr.co.d2net.dao.spec.AbstractSpecification;
import kr.co.d2net.dao.spec.Specification;
import kr.co.d2net.dto.CategoryTbl;
import kr.co.d2net.dto.CategoryTbl_;
import kr.co.d2net.dto.ContentsInstTbl;
import kr.co.d2net.dto.ContentsInstTbl_;
import kr.co.d2net.dto.ContentsTbl;
import kr.co.d2net.dto.ContentsTbl_;
import kr.co.d2net.dto.EpisodeTbl;
import kr.co.d2net.dto.EpisodeTbl_;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.SegmentTbl_;
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Search.Operator;
import kr.co.d2net.dto.vo.Search.Separator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentSpecifications {

	private final static Logger logger = LoggerFactory.getLogger(ContentSpecifications.class);

	public static Specification<ContentsTbl> findContentEqualsCategory(final Integer categoryId) {
		Specification<ContentsTbl> specifications = findContentByCategoryId(categoryId);
		return specifications.and(findContentsByExists());
	}

	@SuppressWarnings("serial")
	public static Specification<ContentsTbl> findContentsByExists() {
		return new AbstractSpecification<ContentsTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<ContentsTbl> root) {
				Subquery<ContentsInstTbl> contentInstSubquery = cq.subquery(ContentsInstTbl.class);
				Root<ContentsInstTbl> contentInstSubqueryRoot = contentInstSubquery.from(ContentsInstTbl.class);
				contentInstSubquery.select(contentInstSubqueryRoot);

				Predicate correlatePredicate = cb.equal(contentInstSubqueryRoot.get(ContentsInstTbl_.contentsTbl), root);
				Predicate formatLike = cb.like(contentInstSubqueryRoot.get(ContentsInstTbl_.ctiFmt), singlePattern("1"));
				contentInstSubquery.where(
						cb.and(correlatePredicate, formatLike)
						);

				return cb.exists(contentInstSubquery);
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<ContentsTbl> findContentsOnlyByParams(final Search search) {
		return new AbstractSpecification<ContentsTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<ContentsTbl> root) {

				// category_id
				Predicate p1 = cb.conjunction();
				if(search.getCategoryId() != null && search.getCategoryId() > 0) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[category_id]: "+search.getCategoryId());
					}
					p1 = cb.equal(root.get(ContentsTbl_.categoryId), search.getCategoryId());
				}

				// episode_id
				Predicate p2 = cb.conjunction();
				if(search.getEpisodeId() != null && search.getEpisodeId() > 0) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[episode_id]: "+search.getEpisodeId());
					}
					p2 = cb.equal(root.get(ContentsTbl_.episodeId), search.getEpisodeId());
				}

				// segment_id
				Predicate p3 = cb.conjunction();
				if(search.getSegmentId() != null && search.getSegmentId() > 0) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[segment_id]: "+search.getSegmentId());
					}
					p3 = cb.equal(root.get(ContentsTbl_.segmentId), search.getSegmentId());
				}

				// ct_nm like
				Predicate p4 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getKeyword())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[keyword]: "+search.getKeyword());
					}
					p4 = cb.or(searchContentKeywords(cb, root, search.getKeyword()));
				}

				// 방송일 or 등록일
				Predicate p5 = cb.conjunction();
				if(search.getStartDt() != null && search.getEndDt() != null) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[search separator]: "+search.getSeparator().toString());
						logger.debug("Content[startDt]: "+search.getStartDt()+", endDt: "+search.getEndDt());
					}
					if(search.getSeparator() == Separator.ALL) {
						p5 = cb.or(
								cb.and(fromOnAirDt(cb, root, search.getStartDt()), toOnAirDt(cb, root, search.getEndDt())),
								cb.and(fromRegDt(cb, root, search.getStartDt()), toRegDt(cb, root, search.getEndDt()))
								);
					}else if(search.getSeparator() == Separator.BRD_DD) {
						p5 = cb.and(fromOnAirDt(cb, root, search.getStartDt()), toOnAirDt(cb, root, search.getEndDt()));
					}else if(search.getSeparator() == Separator.REG_DD) {
						p5 = cb.and(fromRegDt(cb, root, search.getStartDt()), toRegDt(cb, root, search.getEndDt()));
					}
				}

				// 영상 출처
				Predicate p6 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getProdRoute())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[prod_route]: "+search.getProdRoute());
					}
					p6 = cb.and(searchProdRoute(cb, root, search.getProdRoute()));
				}

				// 콘텐츠 편집 상태
				Predicate p7 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getDataStatCd())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[data_stat_cd]: "+search.getDataStatCd());
					}
					p7 = cb.and(searchDataStatCd(cb, root, search.getCtCla()));
				}

				// 영상구분
				Predicate p8 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getCtCla())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[ct_cla]: "+search.getCtCla());
					}
					p8 = cb.and(searchCtCla(cb, root, search.getCtCla()));
				}

				// 영상유형
				Predicate p9 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getCtTyp())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[ct_typ]: "+search.getCtTyp());
					}
					p9 = cb.and(searchCtTyp(cb, root, search.getCtTyp()));
				}

				// 삭제유무
				Predicate p10 = cb.conjunction();
				String useYn = StringUtils.defaultIfBlank(search.getUseYn(), "Y");
				if(logger.isDebugEnabled()) {
					logger.debug("Content[use_yn]: "+useYn);
				}
				p10 = cb.and(searchUseYn(cb, root, useYn));

				if(logger.isDebugEnabled()) {
					logger.debug("Content[search operator]: "+search.getOperator().toString());
				}
				if(search.getOperator() == Operator.LIST) {
					Path<Long> ctId = root.get(ContentsTbl_.ctId);

					cq.orderBy(cb.desc(ctId));
				}

				return cb.and(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<ContentsTbl> findContentsWithSubByParams(final Search search) {
		return new AbstractSpecification<ContentsTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<ContentsTbl> root) {
				Join<ContentsTbl, SegmentTbl> segmentJoin = root.join(ContentsTbl_.segmentTbl, JoinType.INNER);
				Join<SegmentTbl, EpisodeTbl> episodeJoin = segmentJoin.join(SegmentTbl_.episodeTbl, JoinType.INNER);
				Join<EpisodeTbl, CategoryTbl> categoryJoin = episodeJoin.join(EpisodeTbl_.categoryTbl, JoinType.INNER);

				SetJoin<ContentsTbl, ContentsInstTbl> joinOrg = root.joinSet(ContentsTbl_.contentsInst.getName(), JoinType.LEFT);

				// category_id
				Predicate p1 = cb.conjunction();
				if(search.getCategoryId() != null && search.getCategoryId() > 0) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[category_id]: "+search.getCategoryId());
					}
					p1 = cb.equal(root.get(ContentsTbl_.categoryId), search.getCategoryId());
				}

				// episode_id
				Predicate p2 = cb.conjunction();
				if(search.getEpisodeId() != null && search.getEpisodeId() > 0) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[episode_id]: "+search.getEpisodeId());
					}
					p2 = cb.equal(root.get(ContentsTbl_.episodeId), search.getEpisodeId());
				}

				// segment_id
				Predicate p3 = cb.conjunction();
				if(search.getSegmentId() != null && search.getSegmentId() > 0) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[segment_id]: "+search.getSegmentId());
					}
					p3 = cb.equal(root.get(ContentsTbl_.segmentId), search.getSegmentId());
				}

				// ct_nm like
				Predicate p4 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getKeyword())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[keyword]: "+search.getKeyword());
					}
					p4 = cb.or(searchContentKeywords(cb, root, search.getKeyword()));
				}

				// 방송일 or 등록일
				Predicate p5 = cb.conjunction();
				if(search.getStartDt() != null && search.getEndDt() != null) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[search separator]: "+search.getSeparator().toString());
						logger.debug("Content[startDt]: "+search.getStartDt()+", endDt: "+search.getEndDt());
					}
					if(search.getSeparator() == Separator.ALL) {
						p5 = cb.or(
								cb.and(fromOnAirDt(cb, root, search.getStartDt()), toOnAirDt(cb, root, search.getEndDt())),
								cb.and(fromRegDt(cb, root, search.getStartDt()), toRegDt(cb, root, search.getEndDt()))
								);
					}else if(search.getSeparator() == Separator.BRD_DD) {
						p5 = cb.and(fromOnAirDt(cb, root, search.getStartDt()), toOnAirDt(cb, root, search.getEndDt()));
					}else if(search.getSeparator() == Separator.REG_DD) {
						p5 = cb.and(fromRegDt(cb, root, search.getStartDt()), toRegDt(cb, root, search.getEndDt()));
					}
				}

				// 영상 출처
				Predicate p6 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getProdRoute())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[prod_route]: "+search.getProdRoute());
					}
					p6 = cb.and(searchProdRoute(cb, root, search.getProdRoute()));
				}

				// 콘텐츠 편집 상태
				Predicate p7 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getDataStatCd())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[data_stat_cd]: "+search.getDataStatCd());
					}
					p7 = cb.and(searchDataStatCd(cb, root, search.getCtCla()));
				}

				// 영상구분
				Predicate p8 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getCtCla())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[ct_cla]: "+search.getCtCla());
					}
					p8 = cb.and(searchCtCla(cb, root, search.getCtCla()));
				}

				// 영상유형
				Predicate p9 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getCtTyp())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[ct_typ]: "+search.getCtTyp());
					}
					p9 = cb.and(searchCtTyp(cb, root, search.getCtTyp()));
				}

				// cti_fmt : 고화질(1%), 저화질(2%)
				Predicate p10 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getCtiFmt())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[cti_fmt]: "+search.getCtiFmt());
					}
					p10 = cb.and(searchContentFormat(cb, joinOrg, search.getCtiFmt()));
				}

				// nodes like : 카테고리별 콘텐츠
				Predicate p11 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getNodes())) {
					if(logger.isDebugEnabled()) {
						logger.debug("Content[nodes]: "+search.getNodes());
					}
					p11 = cb.and(searchLikeNodes(cb, categoryJoin, search.getNodes()));
				}

				// 삭제유무
				Predicate p12 = cb.conjunction();
				String useYn = StringUtils.defaultIfBlank(search.getUseYn(), "Y");
				if(logger.isDebugEnabled()) {
					logger.debug("Content[use_yn]: "+useYn);
				}
				p12 = cb.and(searchUseYn(cb, root, useYn));

				if(logger.isDebugEnabled()) {
					logger.debug("Content[search operator]: "+search.getOperator().toString());
				}
				if(search.getOperator() == Operator.LIST) {
					Path<Long> ctId = root.get(ContentsTbl_.ctId);
					Path<String> ctNm = root.get(ContentsTbl_.ctNm);
					Path<String> categoryNm = categoryJoin.get(CategoryTbl_.categoryNm);
					Path<String> episodeNm = episodeJoin.get(EpisodeTbl_.episodeNm);
					cq.multiselect(ctId, ctNm, categoryNm, episodeNm);

					cq.orderBy(cb.desc(ctId));
				}

				return cb.and(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12);
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<ContentsTbl> findContentByCategoryId(final Integer categoryId) {
		return new AbstractSpecification<ContentsTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<ContentsTbl> root) {
				return cb.equal(root.get(ContentsTbl_.categoryId), categoryId);
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<ContentsTbl> getContentWithPK(final Long ctId) {
		return new AbstractSpecification<ContentsTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<ContentsTbl> root) {
				return cb.and(contentObj(cb, root, ctId));
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<ContentsTbl> findKeywords(final String keywords) {
		return new AbstractSpecification<ContentsTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<ContentsTbl> root) {
				return cb.or(searchContentKeywords(cb, root, keywords));
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<ContentsTbl> findContentNames(final String keywords) {
		return new AbstractSpecification<ContentsTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<ContentsTbl> root) {
				return cb.or(searchContentTitles(cb, root, keywords));
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<ContentsTbl> betweenOnAirDate(final Date startDt, final Date endDt) {
		return new AbstractSpecification<ContentsTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<ContentsTbl> root) {
				return cb.and(fromOnAirDt(cb, root, startDt), toOnAirDt(cb, root, endDt));
			}
		};
	}

	@SuppressWarnings("serial")
	public static Specification<ContentsTbl> betweenRegDate(final Date startDt, final Date endDt) {
		return new AbstractSpecification<ContentsTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<ContentsTbl> root) {
				return cb.and(fromRegDt(cb, root, startDt), toRegDt(cb, root, endDt));
			}
		};
	}

	public static Predicate contentObj(final CriteriaBuilder cb, final Path<ContentsTbl> path, final Long ctId) {
		return cb.equal(path.get(ContentsTbl_.ctId), ctId);
	}

	public static Predicate fromOnAirDt(final CriteriaBuilder cb, final Path<ContentsTbl> path, final Date startDt) {
		return cb.greaterThanOrEqualTo(path.get(ContentsTbl_.brdDd), startDt);
	}

	public static Predicate toOnAirDt(final CriteriaBuilder cb, final Path<ContentsTbl> path, final Date endDt) {
		return cb.lessThanOrEqualTo(path.get(ContentsTbl_.brdDd), endDt);
	}

	public static Predicate fromRegDt(final CriteriaBuilder cb, final Path<ContentsTbl> path, final Date startDt) {
		return cb.greaterThanOrEqualTo(path.get(ContentsTbl_.regDt), startDt);
	}

	public static Predicate toRegDt(final CriteriaBuilder cb, final Path<ContentsTbl> path, final Date endDt) {
		return cb.lessThanOrEqualTo(path.get(ContentsTbl_.regDt), endDt);
	}

	public static Predicate searchCtCla(final CriteriaBuilder cb, final Path<ContentsTbl> path, final String ctCla) {
		return cb.equal(path.get(ContentsTbl_.ctCla), ctCla);
	}

	public static Predicate searchCtTyp(final CriteriaBuilder cb, final Path<ContentsTbl> path, final String ctTyp) {
		return cb.equal(path.get(ContentsTbl_.ctTyp), ctTyp);
	}

	public static Predicate searchUseYn(final CriteriaBuilder cb, final Path<ContentsTbl> path, final String useYn) {
		return cb.equal(path.get(ContentsTbl_.useYn), useYn);
	}

	public static Predicate searchDataStatCd(final CriteriaBuilder cb, final Path<ContentsTbl> path, final String dataStatCd) {
		return cb.equal(path.get(ContentsTbl_.dataStatCd), dataStatCd);
	}

	public static Predicate searchProdRoute(final CriteriaBuilder cb, final Path<ContentsTbl> path, final String prodRoute) {
		return cb.equal(path.get(ContentsTbl_.prodRoute), prodRoute);
	}

	public static Predicate[] searchContentKeywords(final CriteriaBuilder cb, final Path<ContentsTbl> path, final String keywords) {
		List<Predicate> predicateList = new ArrayList<Predicate>();
		String[] strs = keywords.split("\\s+");
		for(String str : strs) {
			Predicate predicate = cb.like(path.get(ContentsTbl_.keyWords), duplexPattern(str));
			predicateList.add(predicate);
		}
		Predicate[] predicates = new Predicate[predicateList.size()];
		return predicateList.toArray(predicates);
	}

	public static Predicate[] searchContentTitles(final CriteriaBuilder cb, final Path<ContentsTbl> path, final String keywords) {
		List<Predicate> predicateList = new ArrayList<Predicate>();
		String[] strs = keywords.split("\\s+");
		for(String str : strs) {
			Predicate predicate = cb.like(path.get(ContentsTbl_.ctNm), duplexPattern(str));
			predicateList.add(predicate);
		}
		Predicate[] predicates = new Predicate[predicateList.size()];
		return predicateList.toArray(predicates);
	}

	public static Predicate searchContentFormat(final CriteriaBuilder cb, final Path<ContentsInstTbl> path, final String ctiFmt) {
		return cb.like(path.get(ContentsInstTbl_.ctiFmt), singlePattern(ctiFmt));
	}

	public static Predicate searchLikeNodes(final CriteriaBuilder cb, final Path<CategoryTbl> path, final String ctiFmt) {
		return cb.like(path.get(CategoryTbl_.nodes), singlePattern(ctiFmt));
	}

	private static String duplexPattern(final String keyword) {
		StringBuilder pattern = new StringBuilder();
		pattern.append("%");
		pattern.append(keyword);
		pattern.append("%");

		return pattern.toString();
	}

	private static String singlePattern(final String keyword) {
		StringBuilder pattern = new StringBuilder();
		pattern.append(keyword);
		pattern.append("%");

		return pattern.toString();
	}

}
