package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(DisuseInfoTbl.class)
public class DisuseInfoTbl_ {
	public static volatile SingularAttribute<DisuseInfoTbl, Long> disuseNo;	//폐기번호
	public static volatile SingularAttribute<DisuseInfoTbl, Long> ctId;	//콘텐츠ID
	public static volatile SingularAttribute<DisuseInfoTbl, Date> disuseDd;	//폐기일
	public static volatile SingularAttribute<DisuseInfoTbl, String> disuseRsl;	//폐기사유
	public static volatile SingularAttribute<DisuseInfoTbl, String> disuseClf;	//폐기코드
	public static volatile SingularAttribute<DisuseInfoTbl, Date> regDt;	//등록일자
	public static volatile SingularAttribute<DisuseInfoTbl, String> regrId;	//등록자ID
	public static volatile SingularAttribute<DisuseInfoTbl, String> cancelCont;	//취소사유'
	public static volatile SingularAttribute<DisuseInfoTbl, Date> cancelDt;	//취소일자
	
}
