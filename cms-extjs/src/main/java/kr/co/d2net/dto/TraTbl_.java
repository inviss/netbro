package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(TraTbl.class)
public class TraTbl_ {
	public static volatile SingularAttribute<TraTbl, String> deviceId;
	public static volatile SingularAttribute<TraTbl, Integer> deviceNum;	//진행률
	public static volatile SingularAttribute<TraTbl, Long> seq;	//순번
	public static volatile SingularAttribute<TraTbl, Date> reqDt;	//요청일시
	public static volatile SingularAttribute<TraTbl, Date> regDt;	//등록일시
	public static volatile SingularAttribute<TraTbl, String> reqUsrid;	//등록자ID
	public static volatile SingularAttribute<TraTbl, Date> modDt;	//수정일시
	public static volatile SingularAttribute<TraTbl, String> modrId;	//수정자ID
	public static volatile SingularAttribute<TraTbl, Long> ctId;	//콘텐츠인스턴스ID
	public static volatile SingularAttribute<TraTbl, Long> ctiId;	//콘텐츠인스턴스ID
	public static volatile SingularAttribute<TraTbl, String> jobStatus;	//job상태
	public static volatile SingularAttribute<TraTbl, Integer> prgrs;	//진행률
	public static volatile SingularAttribute<TraTbl, Integer> errorCount;	//전송실패 횟수
	public static volatile SingularAttribute<TraTbl, String> busiPartnerid;	//사업자ID
	public static volatile SingularAttribute<TraTbl, Integer> proFlId;	//프로파일ID
	public static volatile SingularAttribute<TraTbl, String> workStatcd;	//작업상태
	
	public static volatile SingularAttribute<TraTbl, EquipmentTbl> deviceTbl;	//사용여부

	
}
