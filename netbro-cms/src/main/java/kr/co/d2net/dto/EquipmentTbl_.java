package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(EquipmentTbl.class)
public class EquipmentTbl_ {
	public static volatile SingularAttribute<EquipmentTbl, String> deviceId;	//장비ID
	public static volatile SingularAttribute<EquipmentTbl, String> deviceNm;	//장비이름
	public static volatile SingularAttribute<EquipmentTbl, String> deviceIp;	//장비IP
	public static volatile SingularAttribute<EquipmentTbl, String> devicePort;	//장비Port
	public static volatile SingularAttribute<EquipmentTbl, String> useYn;	//사용여부
	public static volatile SingularAttribute<EquipmentTbl, Date> regDt;	//등록일시
	public static volatile SingularAttribute<EquipmentTbl, String> regrId;	//등록자ID
	public static volatile SingularAttribute<EquipmentTbl, Date> modDt;	//수정일시
	public static volatile SingularAttribute<EquipmentTbl, String> modrId;	//수정자ID
	public static volatile SingularAttribute<EquipmentTbl, Long> ctiId;	//콘텐츠인스턴스ID
	public static volatile SingularAttribute<EquipmentTbl, String> workStatcd;	//장비상태
	public static volatile SingularAttribute<EquipmentTbl, String> deviceClfCd;	//장비구분코드

	
}
