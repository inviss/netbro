package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(CodeTbl.class)
public class CodeTbl_ {
	public static volatile SingularAttribute<CodeTbl, CodeTbl.CodeId> id;			//id
	public static volatile SingularAttribute<CodeTbl.CodeId, String> clfCD;			//구분코드
	public static volatile SingularAttribute<CodeTbl.CodeId, String> sclCd;			//구분상세코드
	public static volatile SingularAttribute<CodeTbl, String> clfNM;				//구분명
	public static volatile SingularAttribute<CodeTbl, String> sclNm;				//구분상세코드명
	public static volatile SingularAttribute<CodeTbl, String> codeCont;			//코드설명
	public static volatile SingularAttribute<CodeTbl, String> rmk1;				//비고1
	public static volatile SingularAttribute<CodeTbl, String> rmk2;				//비고2
	public static volatile SingularAttribute<CodeTbl, Date> regDt;				//등록일
	public static volatile SingularAttribute<CodeTbl, String> regrId;				//등록자
	public static volatile SingularAttribute<CodeTbl, Date> modDt;				//수정일
	public static volatile SingularAttribute<CodeTbl, String> modrId;				//수정자
	public static volatile SingularAttribute<CodeTbl, String> useYn;				//사용여부
	public static volatile SingularAttribute<CodeTbl, String> clfGubun;			//S : 시스템 사용 코드 U : 유저사용코드
	
}
