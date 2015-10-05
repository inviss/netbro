package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;



@StaticMetamodel(DownloadTbl.class)
public class DownloadTbl_ {
	public static volatile SingularAttribute<DownloadTbl, Long> seq;;	//권한ID
	public static volatile SingularAttribute<DownloadTbl, Long> ctiId;	//권한명
	public static volatile SingularAttribute<DownloadTbl, String> workStatCd;	//권한명
	public static volatile SingularAttribute<DownloadTbl, String> errorCd;	//등록자ID
	public static volatile SingularAttribute<DownloadTbl, String> reason;	//등록일시
	public static volatile SingularAttribute<DownloadTbl, Date> regDt;	//등록일시
	public static volatile SingularAttribute<DownloadTbl, String> approveId;	//수정자ID
	public static volatile SingularAttribute<DownloadTbl, Integer> prgrs;	//진행률
	public static volatile SingularAttribute<DownloadTbl, Date> dnStrDt;	//다운로드시작시간
	public static volatile SingularAttribute<DownloadTbl, Date> dnEndDt;	//다운로드종료시간
	public static volatile SingularAttribute<DownloadTbl, String> downloadPath;	//다운로드경로
	public static volatile SingularAttribute<DownloadTbl, String> reqId;	//수정자ID
	public static volatile SingularAttribute<DownloadTbl, String> downloadNm;	//다운로드명
	
	public static volatile SingularAttribute<DownloadTbl, ContentsInstTbl> contentsInstTbls;	//사용여부
}
