package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(CornerTbl.class)
public class CornerTbl_ {
	public static volatile SingularAttribute<CornerTbl, Long> ctId;
	public static volatile SingularAttribute<CornerTbl, Long> cnId;			//코너ID
	public static volatile SingularAttribute<CornerTbl, String> cnNm;			//코너명
	public static volatile SingularAttribute<CornerTbl, String> bgmTime;		//시작시간
	public static volatile SingularAttribute<CornerTbl, String> endTime;		//종료시간
	public static volatile SingularAttribute<CornerTbl, String> cnCont;		//코너 내용
	public static volatile SingularAttribute<CornerTbl, Date> regDt;			//등록일
	public static volatile SingularAttribute<CornerTbl, String> regId;		//등록자
	public static volatile SingularAttribute<CornerTbl, Date> modDt;			//수정일
	public static volatile SingularAttribute<CornerTbl, String> modId;		//수정자
	public static volatile SingularAttribute<CornerTbl, Long> duration;		// DURATION
	public static volatile SingularAttribute<CornerTbl, Long> sDuration;		// Start DURATION
	public static volatile SingularAttribute<CornerTbl, Long> rpimgKfrmSeq;	//대표화면키프레임순번
	
}
