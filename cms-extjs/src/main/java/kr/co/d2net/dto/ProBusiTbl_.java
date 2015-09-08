package kr.co.d2net.dto;


import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import kr.co.d2net.dto.ProBusiTbl.ProBusiId;




@StaticMetamodel(ProBusiTbl.class)
public class ProBusiTbl_ {
	public static volatile SingularAttribute<ProBusiTbl, ProBusiTbl.ProBusiId> id;//사업자ID
	public static volatile SingularAttribute<ProBusiId, Long> busiPartnerId;//사업자ID
	public static volatile SingularAttribute<ProBusiId, Long> proFlId;	//프로파일ID
	public static volatile SingularAttribute<ProBusiTbl, String> remark;	//비고
	
	public static volatile SingularAttribute<ProBusiTbl, ProFlTbl> proFlTbl;	//비고

}
