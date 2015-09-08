package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(ArchiveTbl.class)
public class ArchiveTbl_ {
	public static volatile SingularAttribute<ArchiveTbl, Long> seq;;	//순번
	public static volatile SingularAttribute<ArchiveTbl, Long> ctiId;	//컨텐츠인스트ID
	public static volatile SingularAttribute<ArchiveTbl, String> workStatCd;	//작업상태
	public static volatile SingularAttribute<ArchiveTbl, String> errorCd;	//에러코드
	public static volatile SingularAttribute<ArchiveTbl, String> cont;	//설명
	public static volatile SingularAttribute<ArchiveTbl, Date> regDt;	//등록일시
	public static volatile SingularAttribute<ArchiveTbl, String> approveId;	//승인자ID
	public static volatile SingularAttribute<ArchiveTbl, Integer> prgrs;	//진행률
	public static volatile SingularAttribute<ArchiveTbl, String> archivePath;	//아카이브 경로
	
	public static volatile SingularAttribute<ArchiveTbl, ContentsInstTbl> contentsInstTbls;
}
