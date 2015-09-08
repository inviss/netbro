package kr.co.d2net.exception;

/**
 * <pre>
 * 트랜잭션 처리를 하지 않는 조회(get*, find*) 관련 SQL 처리중 에러가 발생했을경우 반환
 * </pre>
 * @author Administrator
 *
 */
public class DaoNonRollbackException extends ServiceException {
	
	private static final long serialVersionUID = 1L;
	
	public DaoNonRollbackException(String message) {
		super(message);
	}

	public DaoNonRollbackException(Throwable cause) {
		super(cause);
	}
	
	public DaoNonRollbackException(String errorCode, String message) {
		super(errorCode, message);
	}
	
	
	public DaoNonRollbackException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
	public DaoNonRollbackException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
	
}
