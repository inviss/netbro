package kr.co.d2net.dto;

import java.io.Serializable;

import kr.co.d2net.utils.BeanUtils;

@SuppressWarnings("serial")
public class EquipmentPK implements Serializable {

	@BusinessKey
	private String deviceId;	//장비ID

	@BusinessKey
	private Integer deviceNum;	//

	public EquipmentPK() {}

	public EquipmentPK(String deviceId, Integer deviceNum) {
		this.deviceId = deviceId;
		this.deviceNum = deviceNum;
	}

	@Override
	public boolean equals(Object obj) {
		return BeanUtils.equals(this, obj);
	}
	@Override
	public int hashCode() {
		return BeanUtils.hashCode(this);
	}
	@Override
	public String toString() {
		return BeanUtils.toString(this);
	}
}
