package kr.co.d2net.exception;

/**
 * <pre>
 * 트랜잭션을 포함하는 저장, 삭제관련 DB 처리중 에러가 발생했을경우 반환
 * </pre>
 * @author Administrator
 *
 */
public class ConnectionTimeOutException extends ServiceException {
	
	private static final long serialVersionUID = 1L;
	
	public ConnectionTimeOutException(String message) {
		super(message);
	}

	public ConnectionTimeOutException(Throwable cause) {
		super(cause);
	}
	
	public ConnectionTimeOutException(String errorCode, String message) {
		super(errorCode, message);
	}
	
	public ConnectionTimeOutException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ConnectionTimeOutException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
	
}
