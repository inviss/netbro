package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import kr.co.d2net.dto.UserAuthTbl.UserAuthId;




@StaticMetamodel(UserAuthTbl.class)
public class UserAuthTbl_ {
	//public static volatile SingularAttribute<UserAuthTbl, String> userId;
	//public static volatile SingularAttribute<UserAuthTbl, Integer> authId;		//권한ID	
	public static volatile SingularAttribute<UserAuthTbl, String> modrId;	//수정자ID
	public static volatile SingularAttribute<UserAuthTbl, Date> modDt;//수정일시
	public static volatile SingularAttribute<UserAuthTbl, UserAuthId> id;
	public static volatile SingularAttribute<UserAuthTbl.UserAuthId, Integer> authId; //권한ID
	public static volatile SingularAttribute<UserAuthTbl.UserAuthId, String> userId;  //사용자ID
	
	public static volatile SingularAttribute<UserAuthTbl, UserTbl> userTbl;
	public static volatile SingularAttribute<UserAuthTbl, AuthTbl> authTbl;
}
