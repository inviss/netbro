package kr.co.d2net.exception;

/**
 * <pre>
 * 트랜잭션을 포함하는 저장, 삭제관련 DB 처리중 에러가 발생했을경우 반환
 * </pre>
 * @author Administrator
 *
 */
public class DaoRollbackException extends ServiceException {
	
	private static final long serialVersionUID = 1L;
	
	public DaoRollbackException(String message) {
		super(message);
	}

	public DaoRollbackException(Throwable cause) {
		super(cause);
	}
	
	public DaoRollbackException(String errorCode, String message) {
		super(errorCode, message);
	}
	
	public DaoRollbackException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DaoRollbackException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
	
}
