package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(TrsTbl.class)
public class TrsTbl_ {
	public static volatile SingularAttribute<TrsTbl, EquipmentTbl> deviceId; //장비ID
	public static volatile SingularAttribute<TrsTbl, Long> seq;	//순번
	public static volatile SingularAttribute<TrsTbl, Long> ctiId;	//영상 인스턴스ID
	public static volatile SingularAttribute<TrsTbl, Long> ctId;	//콘텐츠ID
	public static volatile SingularAttribute<TrsTbl, String> trsStatus;	//진행상태
	public static volatile SingularAttribute<TrsTbl, String> tcType;	//작업형태
	public static volatile SingularAttribute<TrsTbl, Integer> prgrs;	//진행률
	public static volatile SingularAttribute<TrsTbl, Date> reqDt;	//요청일
	public static volatile SingularAttribute<TrsTbl, Date> regDt;	//등록일
	public static volatile SingularAttribute<TrsTbl, String> reqrId;	//요청자
	public static volatile SingularAttribute<TrsTbl, Date> modDt;	//수정일
	public static volatile SingularAttribute<TrsTbl, String> modrId;	//수정자
	public static volatile SingularAttribute<TrsTbl, String> errorCd;	//에러코드
	public static volatile SingularAttribute<TrsTbl, String> errorCont;	//에러코드 내용
	public static volatile SingularAttribute<TrsTbl, String> priority;	//우선순위
	public static volatile SingularAttribute<TrsTbl, String> useYn;	//사용여부
	
	
}
