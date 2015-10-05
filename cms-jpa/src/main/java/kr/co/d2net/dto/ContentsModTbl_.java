package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(ContentsModTbl.class)
public class ContentsModTbl_ {
	public static volatile SingularAttribute<ContentsModTbl, Long> ctId;
	public static volatile SingularAttribute<ContentsModTbl, String> modId;		//정리자ID
	public static volatile SingularAttribute<ContentsModTbl, Date> modDt;			//정리일
	public static volatile SingularAttribute<ContentsModTbl, Long> seq;			//seq
	public static volatile SingularAttribute<ContentsModTbl, String> dataStatcd;	//seq
	
	public static volatile SingularAttribute<ContentsModTbl, ContentsTbl> contentsTbl;
}
