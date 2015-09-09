package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;





@StaticMetamodel(NoticeTbl.class)
public class NoticeTbl_ {
	public static volatile SingularAttribute<NoticeTbl, Long> noticeId;	//공지사항 id
	public static volatile SingularAttribute<NoticeTbl, String> TITLE;	//제목
	public static volatile SingularAttribute<NoticeTbl, String> regrId;	//등록자ID
	public static volatile SingularAttribute<NoticeTbl, Date> regDt;	//등록일시
	public static volatile SingularAttribute<NoticeTbl, String> modrId;	//수정자ID
	public static volatile SingularAttribute<NoticeTbl, Date> modDt;	//수정일시
	public static volatile SingularAttribute<NoticeTbl, String> startDd;	//팝업시작일
	public static volatile SingularAttribute<NoticeTbl, String> endDd;	//팝업종료일
	public static volatile SingularAttribute<NoticeTbl, String> cont;	//공지내용
	public static volatile SingularAttribute<NoticeTbl, String> popUpYn;	//팝업여부
}
