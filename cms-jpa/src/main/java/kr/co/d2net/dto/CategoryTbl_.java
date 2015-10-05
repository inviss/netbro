package kr.co.d2net.dto;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(CategoryTbl.class)
public class CategoryTbl_ {
	public static volatile SingularAttribute<CategoryTbl, Integer> categoryId;	// 카테고리ID
	public static volatile SingularAttribute<CategoryTbl, String> categoryNm;		// 카테고리명
	public static volatile SingularAttribute<CategoryTbl, Integer> preParent;		// 직전노드
	public static volatile SingularAttribute<CategoryTbl, String> depth;			// 깊이
	public static volatile SingularAttribute<CategoryTbl, String> nodes;			// 상위카테고리부터 현제카테고리까지
	public static volatile SingularAttribute<CategoryTbl, Integer> groupId;		// 그룹ID
	public static volatile SingularAttribute<CategoryTbl, Integer> orderNum;		// 카테고리 순번
	public static volatile SingularAttribute<CategoryTbl, UseEnum> useYn;			// 사용여부
	
	public static volatile SetAttribute<CategoryTbl, EpisodeTbl> episodeTbl;
}
