package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;





@StaticMetamodel(AttachTbl.class)
public class AttachTbl_ {
	public static volatile SingularAttribute<AttachTbl, Long> seq;	//순번
	public static volatile SingularAttribute<AttachTbl, Long> ctId;	//컨텐츠ID
	public static volatile SingularAttribute<AttachTbl, String> orgFilenm;	//원본파일명
	public static volatile SingularAttribute<AttachTbl, String> transFilenm;	//변환파일명
	public static volatile SingularAttribute<AttachTbl, String> attachType;	//첨부타입
	public static volatile SingularAttribute<AttachTbl, String> flPath;	//파일경로
	public static volatile SingularAttribute<AttachTbl, Date> regDt;	//등록일
	public static volatile SingularAttribute<AttachTbl, String> regrId;	//등록자
	public static volatile SingularAttribute<AttachTbl, Date> modDt;	//수정일
	public static volatile SingularAttribute<AttachTbl, String> modrId;	//수정자
	public static volatile SingularAttribute<AttachTbl, String> useYN;	//사용여부
	
}
