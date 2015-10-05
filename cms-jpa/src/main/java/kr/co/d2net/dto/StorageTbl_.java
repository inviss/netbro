package kr.co.d2net.dto;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(StorageTbl.class)
public class StorageTbl_ {
	public static volatile SingularAttribute<StorageTbl, Integer> storageId;	//스토리지ID
	public static volatile SingularAttribute<StorageTbl, Long> totalVolume;	//총용량
	public static volatile SingularAttribute<StorageTbl, Long> useVolume;	//사용용량
	public static volatile SingularAttribute<StorageTbl, Long> idleVolume;	//남은용량
	public static volatile SingularAttribute<StorageTbl, Integer> limit;	//임계치	
	public static volatile SingularAttribute<StorageTbl, String> storagePath;	//스토리지경로	
	public static volatile SingularAttribute<StorageTbl, String> storageGubun;	//스토리지 구분(고용량/저용량)
	
}
