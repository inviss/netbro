package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import kr.co.d2net.dto.EpisodeTbl.EpisodeId;




@StaticMetamodel(EpisodeTbl.class)
public class EpisodeTbl_ {
	//public static volatile SingularAttribute<EpisodeTbl, Integer> categoryId;
	//public static volatile SingularAttribute<EpisodeTbl, Integer> episodeId;	//회차ID
	public static volatile SingularAttribute<EpisodeTbl, String> useYn;	//사용여부
	public static volatile SingularAttribute<EpisodeTbl, String> regrId;	//등록자Id
	public static volatile SingularAttribute<EpisodeTbl, Date> regDt;	//등록일시
	public static volatile SingularAttribute<EpisodeTbl, String> modrId;	//수정자아이디
	public static volatile SingularAttribute<EpisodeTbl, Date> modDt;	//수정일시
	public static volatile SingularAttribute<EpisodeTbl, String> episodeNm;	//회차명
	public static volatile SingularAttribute<EpisodeTbl, EpisodeId> id;
	public static volatile SingularAttribute<EpisodeTbl, Integer> episodeId;
	public static volatile SingularAttribute<EpisodeTbl, Integer> categoryId;
	
	
}
