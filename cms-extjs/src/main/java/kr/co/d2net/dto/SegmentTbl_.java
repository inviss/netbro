package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(SegmentTbl.class)
public class SegmentTbl_ {
	public static volatile SingularAttribute<SegmentTbl, SegmentTbl.SegmentId> id;		//세그먼트 테이블 PK
	public static volatile SingularAttribute<SegmentTbl.SegmentId, Integer> categoryId;	//카테고리ID
	public static volatile SingularAttribute<SegmentTbl.SegmentId, Integer> episodeId;	//에피소드ID
	public static volatile SingularAttribute<SegmentTbl.SegmentId, Integer> segmentId;	//세그먼트ID
	public static volatile SingularAttribute<SegmentTbl, String> useYn;					//사용여부
	public static volatile SingularAttribute<SegmentTbl, String> regrId;					//등록자Id
	public static volatile SingularAttribute<SegmentTbl, Date> regDt;						//등록일시
	public static volatile SingularAttribute<SegmentTbl, String> modrId;					//수정자아이디
	public static volatile SingularAttribute<SegmentTbl, Date> modDt;						//수정일시
	public static volatile SingularAttribute<SegmentTbl, String> segmentNm;				//세그먼트명
	
	public static volatile SingularAttribute<SegmentTbl, EpisodeTbl> episodeTbl;
	public static volatile SetAttribute<SegmentTbl, ContentsTbl> contentsTbls;
}
