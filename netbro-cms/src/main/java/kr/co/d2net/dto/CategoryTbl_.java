package kr.co.d2net.dto;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(CategoryTbl.class)
public class CategoryTbl_ {
	public static volatile SingularAttribute<CategoryTbl, Integer> categoryId;	//카테고리ID
	public static volatile SingularAttribute<CategoryTbl, String> categoryNm;	//카테고리명
	public static volatile SingularAttribute<CategoryTbl, Integer> preParent;	//직전노드
	public static volatile SingularAttribute<CategoryTbl, String> depth;	//깊이
	public static volatile SingularAttribute<CategoryTbl, String> nodes;	//상위카테고리부터 현제카테고리까지
	public static volatile SingularAttribute<CategoryTbl, Integer> order;	//카테고리 순번
}
