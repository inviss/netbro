package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(MenuTbl.class)
public class MenuTbl_ {
	
	public static volatile SingularAttribute<MenuTbl, Integer> menuId;	//메뉴ID
	public static volatile SingularAttribute<MenuTbl, String> useYn;	//사용여부
	public static volatile SingularAttribute<MenuTbl, String> regrId;	//등록자ID
	public static volatile SingularAttribute<MenuTbl, Date> regDt;	//등록일시
	public static volatile SingularAttribute<MenuTbl, String> modrId;	//수정자ID
	public static volatile SingularAttribute<MenuTbl, Date> modDt;	//수정일시
	public static volatile SingularAttribute<MenuTbl, String> menuNm;	//메뉴명
	public static volatile SingularAttribute<MenuTbl, String> menuEnNm;	//영문메뉴명
	public static volatile SingularAttribute<MenuTbl, String> url;	//url
	public static volatile SingularAttribute<MenuTbl, Integer> depth;	//뎁스
	public static volatile SingularAttribute<MenuTbl, String> nodes;	//노드
	public static volatile SingularAttribute<MenuTbl, Integer> orderNum;	//정렬순번
	public static volatile SingularAttribute<MenuTbl, Integer> groupId;	//그룹ID
	
	public static volatile SetAttribute<MenuTbl, RoleAuthTbl> roleAuth;
}
