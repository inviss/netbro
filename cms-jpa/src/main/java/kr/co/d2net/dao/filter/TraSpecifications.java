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
import kr.co.d2net.dto.vo.Search;
import kr.co.d2net.dto.vo.Search.Operator;
import kr.co.d2net.dto.vo.Search.Separator;
import kr.co.d2net.dto.SegmentTbl;
import kr.co.d2net.dto.SegmentTbl_;
import kr.co.d2net.dto.TraTbl;

import org.apache.commons.lang.StringUtils;

public class TraSpecifications {

	@SuppressWarnings("serial")
	public static Specification<TraTbl> findContentsByExists() {
		return new AbstractSpecification<TraTbl> () {
			@Override
			public Predicate toPredicate(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<TraTbl> root) {
				Subquery<TraTbl> relationshipSubquery = cq.subquery(TraTbl.class);
				Root<TraTbl> residencyRelationshipSubqueryRoot = relationshipSubquery.from(TraTbl.class);
				relationshipSubquery.select(residencyRelationshipSubqueryRoot);

				Predicate correlatePredicate = cb.equal(residencyRelationshipSubqueryRoot.get("contentsTbl"), root);
				relationshipSubquery.where(
						cb.and(correlatePredicate)
						);
				return cb.exists(relationshipSubquery);
				//return null;
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
					cb.equal(root.get(ContentsTbl_.categoryId), search.getCategoryId());
				}

				// episode_id
				Predicate p2 = cb.conjunction();
				if(search.getEpisodeId() != null && search.getEpisodeId() > 0) {
					cb.equal(root.get(ContentsTbl_.episodeId), search.getEpisodeId());
				}

				// segment_id
				Predicate p3 = cb.conjunction();
				if(search.getSegmentId() != null && search.getSegmentId() > 0) {
					cb.equal(root.get(ContentsTbl_.segmentId), search.getSegmentId());
				}

				// ct_nm like
				Predicate p4 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getKeyword())) {
					p4 = cb.or(searchContentKeywords(cb, root, search.getKeyword()));
				}

				// 방송일 or 등록일
				Predicate p5 = cb.conjunction();
				if(search.getStartDt() != null && search.getEndDt() != null) {
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

				// 폐기여부
				Predicate p6 = cb.conjunction();

				// 콘텐츠ID
				Predicate p7 = cb.conjunction();

				// 영상구분
				Predicate p8 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getCtCla())) {
					p8 = cb.and(searchCtCla(cb, root, search.getCtCla()));
				}

				// 영상유형
				Predicate p9 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getCtTyp())) {
					p9 = cb.and(searchCtTyp(cb, root, search.getCtTyp()));
				}

				if(search.getOperator() == Operator.LIST) {
					Path<Long> ctId = root.get(ContentsTbl_.ctId);

					cq.orderBy(cb.desc(ctId));
				}

				return cb.and(p1, p2, p3, p4, p5, p6, p7, p8, p9);
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
					p1 = cb.equal(root.get(ContentsTbl_.categoryId), search.getCategoryId());
				}

				// episode_id
				Predicate p2 = cb.conjunction();
				if(search.getEpisodeId() != null && search.getEpisodeId() > 0) {
					p2 = cb.equal(root.get(ContentsTbl_.episodeId), search.getEpisodeId());
				}

				// segment_id
				Predicate p3 = cb.conjunction();
				if(search.getSegmentId() != null && search.getSegmentId() > 0) {
					p3 = cb.equal(root.get(ContentsTbl_.segmentId), search.getSegmentId());
				}

				// ct_nm like
				Predicate p4 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getKeyword())) {
					p4 = cb.or(searchContentKeywords(cb, root, search.getKeyword()));
				}

				// 방송일 or 등록일
				Predicate p5 = cb.conjunction();
				if(search.getStartDt() != null && search.getEndDt() != null) {
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

				// 폐기여부
				Predicate p6 = cb.conjunction();

				// 콘텐츠ID
				Predicate p7 = cb.conjunction();

				// 영상유형 or 영상구분
				Predicate p8 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getCtCla())) {
					p8 = cb.and(searchCtCla(cb, root, search.getCtCla()));
				}

				Predicate p9 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getCtTyp())) {
					p9 = cb.and(searchCtTyp(cb, root, search.getCtTyp()));
				}

				// cti_fmt : 고화질(1%), 저화질(2%)
				Predicate p10 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getCtiFmt())) {
					p10 = cb.and(searchContentFormat(cb, joinOrg, search.getCtiFmt()));
				}

				// nodes like : 카테고리별 콘텐츠
				Predicate p11 = cb.conjunction();
				if(StringUtils.isNotBlank(search.getNodes())) {
					p11 = cb.and(searchLikeNodes(cb, categoryJoin, search.getNodes()));
				}

				if(search.getOperator() == Operator.LIST) {
					Path<Long> ctId = root.get(ContentsTbl_.ctId);
					Path<String> ctNm = root.get(ContentsTbl_.ctNm);
					Path<String> categoryNm = categoryJoin.get(CategoryTbl_.categoryNm);
					Path<String> episodeNm = episodeJoin.get(EpisodeTbl_.episodeNm);
					cq.multiselect(ctId, ctNm, categoryNm, episodeNm);

					cq.orderBy(cb.desc(ctId));
				}

				return cb.and(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
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

	public static Predicate[] searchContentKeywords(final CriteriaBuilder cb, final Path<ContentsTbl> path, final String keywords) {
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
