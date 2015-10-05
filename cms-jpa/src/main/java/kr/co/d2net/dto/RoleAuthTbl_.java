package kr.co.d2net.dto;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(RoleAuthTbl.class)
public class RoleAuthTbl_ {
	public static volatile SingularAttribute<RoleAuthTbl, Integer> menuId; //메뉴Id
	public static volatile SingularAttribute<RoleAuthTbl, Integer> authId; //권한Id
	public static volatile SingularAttribute<RoleAuthTbl, String> controlGubun;	//접근구분
	public static volatile SingularAttribute<RoleAuthTbl, MenuTbl> menuTbl;	
	public static volatile SingularAttribute<RoleAuthTbl, AuthTbl> authTbl;	

}
