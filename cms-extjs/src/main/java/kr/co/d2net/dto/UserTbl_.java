package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(UserTbl.class)
public class UserTbl_ {
	public static volatile SingularAttribute<UserTbl, String> userId;	//사용자ID
	public static volatile SingularAttribute<UserTbl, String> userNm;	//사용자명
	public static volatile SingularAttribute<UserTbl, String> regrId;	//등록자ID
	public static volatile SingularAttribute<UserTbl, Date> regDt; //등록일시
	public static volatile SingularAttribute<UserTbl, String> modrId;	//수정자ID
	public static volatile SingularAttribute<UserTbl, Date> modDt;	//수정일시
	public static volatile SingularAttribute<UserTbl, String> useYn;		//사용여부
	public static volatile SingularAttribute<UserTbl, String> userPass;	//비밀번호
	public static volatile SingularAttribute<UserTbl, String> userPhone;	//비밀번호

	public static volatile SetAttribute<UserTbl, UserAuthTbl> userAuth;	//userAuth
}
