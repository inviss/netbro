package kr.co.d2net.dto;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(RoleAuthTbl.class)
public class RoleAuthTbl_ {
	public static volatile SingularAttribute<RoleAuthTbl, Integer> menuId;
	public static volatile SingularAttribute<RoleAuthTbl, Integer> authId;
	public static volatile SingularAttribute<RoleAuthTbl, String> controlGubun;	//접근구분

}
