package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(BusiPartnerCategoryTbl.class)
public class BusiPartnerCategory_ {
	public static volatile SingularAttribute<BusiPartnerCategoryTbl, Long> busiPartnerId;	//비지니스파트너ID
	//public static volatile SingularAttribute<BusiPartnerCategory, Integer> categoryId;	//프로그램코드
	//public static volatile SingularAttribute<BusiPartnerCategory, String> ctTyp;	//콘텐츠타입
	public static volatile SingularAttribute<BusiPartnerCategoryTbl, String> recYn;	//녹화여부
	public static volatile SingularAttribute<BusiPartnerCategoryTbl, Date> bgnTime;	//시작시간
	public static volatile SingularAttribute<BusiPartnerCategoryTbl, Date> endTime;	//종료시간
	public static volatile SingularAttribute<BusiPartnerCategoryTbl, String> regId;	//등록자ID
	public static volatile SingularAttribute<BusiPartnerCategoryTbl, Date> regDt;	//등록일자
	public static volatile SingularAttribute<BusiPartnerCategoryTbl, String> modId;	//수정자ID
	public static volatile SingularAttribute<BusiPartnerCategoryTbl, Date> modDt;	//수정일자
	public static volatile SingularAttribute<BusiPartnerCategoryTbl, BusiPartnerTbl> busiPartnerTbl;	//busiPartnerTbl
	public static volatile SingularAttribute<BusiPartnerCategoryTbl, BusiPartnerCategoryTbl.BusiPartnerCategoryId> id;	//id
	public static volatile SingularAttribute<BusiPartnerCategoryTbl.BusiPartnerCategoryId, Integer> categoryId;	//카테고리ID
	public static volatile SingularAttribute<BusiPartnerCategoryTbl.BusiPartnerCategoryId, String> ctTyp;	//콘텐츠타입
}
