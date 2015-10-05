package kr.co.d2net.exception;

/**
 * <pre>
 * 트랜잭션 처리를 하지 않는 조회(get*, find*) 관련 SQL 처리중 에러가 발생했을경우 반환
 * </pre>
 * @author Administrator
 *
 */
public class DataNotFoundException extends ServiceException {
	
	private static final long serialVersionUID = 1L;
	
	public DataNotFoundException(String message) {
		super(message);
	}

	public DataNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public DataNotFoundException(String errorCode, String message) {
		super(errorCode, message);
	}
	
	
	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
	public DataNotFoundException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
	
}
