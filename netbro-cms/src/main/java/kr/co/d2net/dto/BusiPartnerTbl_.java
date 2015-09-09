package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(BusiPartnerTbl.class)
public class BusiPartnerTbl_ {
	public static volatile SingularAttribute<BusiPartnerTbl, String> busiPartnerId;	//사업자ID
	public static volatile SingularAttribute<BusiPartnerTbl, String> regrId;	//수정자ID
	public static volatile SingularAttribute<BusiPartnerTbl, Date> regDt;	//수정일시
	public static volatile SingularAttribute<BusiPartnerTbl, String> modrId;	//등록자ID
	public static volatile SingularAttribute<BusiPartnerTbl, Date> modDt;	//등록일시
	public static volatile SingularAttribute<BusiPartnerTbl, String> password;	//패스워드
	public static volatile SingularAttribute<BusiPartnerTbl, String> company;	//업체명
	public static volatile SingularAttribute<BusiPartnerTbl, String> servYn;	//사용여부
	public static volatile SingularAttribute<BusiPartnerTbl, String> ftpServYn;	//	FTP 서비스 여부
	public static volatile SingularAttribute<BusiPartnerTbl, String> ip;	//아이피
	public static volatile SingularAttribute<BusiPartnerTbl, String> port;	//포트
	public static volatile SingularAttribute<BusiPartnerTbl, String> transMethod;	//전송방식
	public static volatile SingularAttribute<BusiPartnerTbl, String> remoteDir;	//전송 타겟 디렉토리
	public static volatile SingularAttribute<BusiPartnerTbl, String> ftpId;	//FTP ID
	public static volatile SingularAttribute<BusiPartnerTbl, String> srvUrl;	//프로그램코드
	public static volatile SingularAttribute<BusiPartnerTbl, String> trsGubun;	//전송구분
	
}
