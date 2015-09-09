package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(ProFlTbl.class)
public class ProFlTbl_ {
	public static volatile SingularAttribute<ProFlTbl, String> proFlid;	//프로파일ID
	public static volatile SingularAttribute<ProFlTbl, String> regrId;	//등록자ID
	public static volatile SingularAttribute<ProFlTbl, String> servBit;	//서비스BIT
	public static volatile SingularAttribute<ProFlTbl, String> modrId;	//수정자ID
	public static volatile SingularAttribute<ProFlTbl, String> ext;	//확장자
	public static volatile SingularAttribute<ProFlTbl, String> vdoCodec;	//비디오코덱
	public static volatile SingularAttribute<ProFlTbl, String> vdoBitRate;	//비디오비트레이트
	public static volatile SingularAttribute<ProFlTbl, String> vdohori;	//비디오가로
	public static volatile SingularAttribute<ProFlTbl, String> vdoVert;	//비디오세로
	public static volatile SingularAttribute<ProFlTbl, String> vdoFS;	//비디오FS
	public static volatile SingularAttribute<ProFlTbl, String> vdoSync;	//비디오종회맞춤
	public static volatile SingularAttribute<ProFlTbl, String> audCodec;	//오디오코덱
	public static volatile SingularAttribute<ProFlTbl, String> audBitRate;	//오디오비트레이트
	public static volatile SingularAttribute<ProFlTbl, String> audChan;	//오디오채널
	public static volatile SingularAttribute<ProFlTbl, String> audSRate;	//오디오샘플레이트
	public static volatile SingularAttribute<ProFlTbl, String> keyFrame;	//키프레임
	public static volatile SingularAttribute<ProFlTbl, Integer> chanPriority;	//변경순위
	public static volatile SingularAttribute<ProFlTbl, Integer> priority;	//순위
	public static volatile SingularAttribute<ProFlTbl, String> proFlnm;	//프로파일명
	public static volatile SingularAttribute<ProFlTbl, Date> modDt;	//수정일시
	public static volatile SingularAttribute<ProFlTbl, Date> regDt;	//등록일시
	public static volatile SingularAttribute<ProFlTbl, String> picKind;	//그림종류
	public static volatile SingularAttribute<ProFlTbl, String> useYn;	//사용여부
	public static volatile SingularAttribute<ProFlTbl, String> flNameRule;	//파일이름규칙
	
}
