package kr.co.d2net.dto;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;




@StaticMetamodel(StatisticsTbl.class)
public class StatisticsTbl_ {
	public static volatile SingularAttribute<StatisticsTbl, Integer> categoryId;	//카테고리ID
	public static volatile SingularAttribute<StatisticsTbl, Long> regist;	//등록건
	public static volatile SingularAttribute<StatisticsTbl, Long> beforeArrange;	//정리전건
	public static volatile SingularAttribute<StatisticsTbl, Long> completeArrange;	//정리완료건
	public static volatile SingularAttribute<StatisticsTbl, Long> discard;	//폐기건	
	public static volatile SingularAttribute<StatisticsTbl, Long> error;	//에러건	
	public static volatile SingularAttribute<StatisticsTbl, String> regDd;	//등록일
	public static volatile SingularAttribute<StatisticsTbl, Integer> depth;	//깊이
	public static volatile SingularAttribute<StatisticsTbl, Integer> groupId;	//그룹
	
}
