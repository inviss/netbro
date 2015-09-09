package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(SegmentTbl.class)
public class SegmentTbl_ {

	public static volatile SingularAttribute<SegmentTbl, Integer> segmentId;	//세그먼트ID
	public static volatile SingularAttribute<SegmentTbl, Integer> episodeId;	//세그먼트ID
	public static volatile SingularAttribute<SegmentTbl, Integer> categoryId;//카테고리Id
	public static volatile SingularAttribute<SegmentTbl, String> useYn;	//사용여부
	public static volatile SingularAttribute<SegmentTbl, String> regrId;	//등록자Id
	public static volatile SingularAttribute<SegmentTbl, Date> regDt;	//등록일시
	public static volatile SingularAttribute<SegmentTbl, String> modrId;	//수정자아이디
	public static volatile SingularAttribute<SegmentTbl, Date> modDt;	//수정일시
	public static volatile SingularAttribute<SegmentTbl, String> segmentNm;	//세그먼트명
}
