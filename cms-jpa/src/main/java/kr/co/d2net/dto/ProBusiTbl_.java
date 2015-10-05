package kr.co.d2net.dto;


import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;



@StaticMetamodel(ProBusiTbl.class)
public class ProBusiTbl_ {
	public static volatile SingularAttribute<ProBusiTbl, Long> busiPartnerId;//사업자ID
	public static volatile SingularAttribute<ProBusiTbl, Long> proFlId;	//프로파일ID
	public static volatile SingularAttribute<ProBusiTbl, String> remark;	//비고
	public static volatile SingularAttribute<ProBusiTbl, ProFlTbl> proFlTbl;	//비고

}
