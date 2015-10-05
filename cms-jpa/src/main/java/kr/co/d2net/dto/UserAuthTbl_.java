package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(UserAuthTbl.class)
public class UserAuthTbl_ {
		
	public static volatile SingularAttribute<UserAuthTbl, String> modrId;	//수정자ID
	public static volatile SingularAttribute<UserAuthTbl, Date> modDt;//수정일시
	public static volatile SingularAttribute<UserAuthTbl, Integer> authId; //권한ID
	public static volatile SingularAttribute<UserAuthTbl, String> userId;  //사용자ID
	
	public static volatile SingularAttribute<UserAuthTbl, UserTbl> userTbl;
	public static volatile SingularAttribute<UserAuthTbl, AuthTbl> authTbl;
}
