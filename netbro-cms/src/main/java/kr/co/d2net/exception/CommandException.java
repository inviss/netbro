package kr.co.d2net.exception;

public class CommandException extends ApplicationException {
	
	private static final long serialVersionUID = 1L;

	public CommandException(String message) {
		super(message);
	}

	public CommandException(Throwable cause) {
		super(cause);
	}

	public CommandException(String errorCode, String message) {
		super(errorCode, message);
	}

	public CommandException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CommandException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		setErrorCode(errorCode);
	}

}
