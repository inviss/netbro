package kr.co.d2net.dto;

import java.io.Serializable;

import kr.co.d2net.utils.BeanUtils;

public abstract class BaseObject implements Serializable {

	private static final long serialVersionUID = 1L;

    public boolean equals(Object obj) {
        return BeanUtils.equals(this, obj);
    }

    public int hashCode() {
        return BeanUtils.hashCode(this);
    }

    public String toString() {
        return BeanUtils.toString(this);
    }
    
}
