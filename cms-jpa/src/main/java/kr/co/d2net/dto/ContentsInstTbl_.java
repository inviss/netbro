package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(ContentsInstTbl.class)
public class ContentsInstTbl_ {
	public static volatile SingularAttribute<ContentsInstTbl, Long> ctId;
	public static volatile SingularAttribute<ContentsInstTbl, Long> ctiId;	//콘텐츠인스턴스ID
	public static volatile SingularAttribute<ContentsInstTbl, String> ctiFmt;	//콘텐츠인스턴스포맷코드
	public static volatile SingularAttribute<ContentsInstTbl, String> meCd;	//ME분리코드
	public static volatile SingularAttribute<ContentsInstTbl, String> bitRt;	//비트전송율
	public static volatile SingularAttribute<ContentsInstTbl, String> drpFrmYn;	//드롭프레임여부
	public static volatile SingularAttribute<ContentsInstTbl, Integer> vdHresol;	//비디오수평해상도
	public static volatile SingularAttribute<ContentsInstTbl, Integer> vdVresol;	//비디오수직해상도
	public static volatile SingularAttribute<ContentsInstTbl, String> recordTypeCd;	//녹음방식코드
	public static volatile SingularAttribute<ContentsInstTbl, String> flPath;	//파일경로
	public static volatile SingularAttribute<ContentsInstTbl, String> flSz;	//파일크기
	public static volatile SingularAttribute<ContentsInstTbl, Date> regDt;	//등록일시
	public static volatile SingularAttribute<ContentsInstTbl, String> regrId;	//등록자ID
	public static volatile SingularAttribute<ContentsInstTbl, Date> modDt;	//수정일시
	public static volatile SingularAttribute<ContentsInstTbl, String> modrId;	//수정자ID
	public static volatile SingularAttribute<ContentsInstTbl, String> colorCd;	//색상코드
	public static volatile SingularAttribute<ContentsInstTbl, String> orgFileNm;	//원파일명
	public static volatile SingularAttribute<ContentsInstTbl, String> deviceId;	//장비ID
	public static volatile SingularAttribute<ContentsInstTbl, String> audioYn;	//오디오여부
	public static volatile SingularAttribute<ContentsInstTbl, String> wrkFileNm;	//작업파일명
	public static volatile SingularAttribute<ContentsInstTbl, String> proFlid;	//프로파일ID
	public static volatile SingularAttribute<ContentsInstTbl, String> usrFileNm;	//유저 파일 이름
	public static volatile SingularAttribute<ContentsInstTbl, String> flExt;	//파일확장명
	public static volatile SingularAttribute<ContentsInstTbl, String> useYn;	//사용여부
	public static volatile SingularAttribute<ContentsInstTbl, String> frmPerSec;	//프레임
	public static volatile SingularAttribute<ContentsInstTbl, String> avGubun;	//'A:오디오' 'V:비디오'
	public static volatile SingularAttribute<ContentsInstTbl, Date> startTime;	//시작시간
	public static volatile SingularAttribute<ContentsInstTbl, Date> endTime;	//종료시간
	public static volatile SingularAttribute<ContentsInstTbl, String> rpYn;	//대표여부
	
	public static volatile SingularAttribute<ContentsInstTbl, ContentsTbl> contentsTbl;
	
}
