package kr.co.d2net.dto;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(Opt.class)
public class Opt_ {
	public static volatile SingularAttribute<Opt, String> proFlid;
	public static volatile SingularAttribute<Opt, Integer> optId;	//옵션아이디
	public static volatile SingularAttribute<Opt, String> optDesc;	//옵션설명
	public static volatile SingularAttribute<Opt, String> optInfo;	//옵션정보
	public static volatile SingularAttribute<Opt, String> defaultYn;	//디폴트여부
	public static volatile SingularAttribute<Opt, String> useYn;	//사용여부

	
}
