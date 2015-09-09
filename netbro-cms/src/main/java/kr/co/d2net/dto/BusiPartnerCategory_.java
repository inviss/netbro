package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(BusiPartnerCategory.class)
public class BusiPartnerCategory_ {
	public static volatile SingularAttribute<BusiPartnerCategory, String> busiPartnerId;	//비지니스파트너ID
	public static volatile SingularAttribute<BusiPartnerCategory, Long> categoryId;	//프로그램코드
	public static volatile SingularAttribute<BusiPartnerCategory, String> ctTyp;	//콘텐츠타입
	public static volatile SingularAttribute<BusiPartnerCategory, String> recYn;	//녹화여부
	public static volatile SingularAttribute<BusiPartnerCategory, Date> bgnTime;	//시작시간
	public static volatile SingularAttribute<BusiPartnerCategory, Date> endTime;	//종료시간
	public static volatile SingularAttribute<BusiPartnerCategory, String> regrId;	//등록자ID
	public static volatile SingularAttribute<BusiPartnerCategory, Date> regDt;	//등록일자
	public static volatile SingularAttribute<BusiPartnerCategory, String> modrId;	//수정자ID
	public static volatile SingularAttribute<BusiPartnerCategory, Date> modDt;	//수정일자
	public static volatile SingularAttribute<BusiPartnerCategory, String> audioModeCode;	//오디오모드코드
}
