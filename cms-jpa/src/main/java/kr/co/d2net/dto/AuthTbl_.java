package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(AuthTbl.class)
public class AuthTbl_ {
	public static volatile SingularAttribute<AuthTbl, Integer> authId;;	//권한ID
	public static volatile SingularAttribute<AuthTbl, String> authNm;	//권한명
	public static volatile SingularAttribute<AuthTbl, String> authSubNm;	//권한명
	public static volatile SingularAttribute<AuthTbl, String> regrId;	//등록자ID
	public static volatile SingularAttribute<AuthTbl, Date> regDt;	//등록일시
	public static volatile SingularAttribute<AuthTbl, String> modrId;	//수정자ID
	public static volatile SingularAttribute<AuthTbl, Date> modDt;	//수정일시
	public static volatile SingularAttribute<AuthTbl, String> useYn;	//사용여부
	
	public static volatile SetAttribute<AuthTbl, UserAuthTbl> userAuth;	
	public static volatile SetAttribute<AuthTbl, RoleAuthTbl> roleAuth;	
}
