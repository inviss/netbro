package kr.co.d2net.exception;

import org.springframework.core.ErrorCoded;

/**
 * <pre>
 * 사용자 정의 에러클래스의 최상위 클래스
 * </pre>
 * @author Administrator
 *
 */
public class ApplicationException extends Exception implements ErrorCoded {

	private static final long serialVersionUID = 1L;

	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public ApplicationException(String message) {
		super(message);
	}
	
	public ApplicationException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public ApplicationException(Throwable cause) {
		super(cause);
	}
	
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ApplicationException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		setErrorCode(errorCode);
	}
	
}
