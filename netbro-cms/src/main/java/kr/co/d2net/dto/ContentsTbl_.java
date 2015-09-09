package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(ContentsTbl.class)
public class ContentsTbl_ {
	public static volatile SingularAttribute<ContentsTbl, Long> ctId;	//컨텐츠ID
	public static volatile SingularAttribute<ContentsTbl, String> ctTyp;	//콘텐츠유형코드
	public static volatile SingularAttribute<ContentsTbl, String> ctCla;	//콘텐츠구분코드
	public static volatile SingularAttribute<ContentsTbl, String> ctNm;	//컨텐츠명
	public static volatile SingularAttribute<ContentsTbl, String> cont;	//내용
	public static volatile SingularAttribute<ContentsTbl, String> vdQlty;	//화질코드
	public static volatile SingularAttribute<ContentsTbl, String> aspRtoCd;	//종횡비코드
	public static volatile SingularAttribute<ContentsTbl, String> keyWords;	//키워드
	public static volatile SingularAttribute<ContentsTbl, String> kfrmPath;	//키프레임경로
	public static volatile SingularAttribute<ContentsTbl, String> kfrmPxCd;	//키프레임해상도코드
	public static volatile SingularAttribute<ContentsTbl, Integer> rpimgKfrmSeq;	//대표화면키프레임순번
	public static volatile SingularAttribute<ContentsTbl, Integer> totKfrmNums;	//총키프레임수
	public static volatile SingularAttribute<ContentsTbl, String> useYn;	//사용여부
	public static volatile SingularAttribute<ContentsTbl, Date> regDt;	//등록일시
	public static volatile SingularAttribute<ContentsTbl, String> regrId;	//등록자ID
	public static volatile SingularAttribute<ContentsTbl, String> modrId;	//수정자ID
	public static volatile SingularAttribute<ContentsTbl, Date> modDt;	//수정일시
	public static volatile SingularAttribute<ContentsTbl, Date> delDd;	//삭제일자
	public static volatile SingularAttribute<ContentsTbl, String> dataStatCd;	//자료상태코드
	public static volatile SingularAttribute<ContentsTbl, String> ctLeng;	//콘텐츠길이
	public static volatile SingularAttribute<ContentsTbl, Long> ctSeq;	//콘텐츠일련번호
	public static volatile SingularAttribute<ContentsTbl, Integer> duration;	//DURATION
	public static volatile SingularAttribute<ContentsTbl, Date> brdDd;	//방송일자
	public static volatile SingularAttribute<ContentsTbl, String> spcInfo;	//특이사항
	public static volatile SingularAttribute<ContentsTbl, Integer> categoryId;	//카테고리ID
	public static volatile SingularAttribute<ContentsTbl, Integer> episodeId;	//회차ID
	public static volatile SingularAttribute<ContentsTbl, Integer> segmentId;	//세그먼트ID
	public static volatile SingularAttribute<ContentsTbl, Integer> lockStatcd;	//Lock상태
	public static volatile SingularAttribute<ContentsTbl, String> prodRoute;	//제작경로
	public static volatile SingularAttribute<ContentsTbl, Date> arrangeDt;	//정리일,오류등록일

}
