package kr.co.d2net.exception;

public class BaseApplicationException extends ApplicationException {

	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	private String xml;
	private String[] args;

	/**
	 * Constructs a new application exception with the given error code.
	 * @param errorCode The error code of the exception.
	 */
	public BaseApplicationException(String message) {
		super(message);
	}

	/**
	 * Constructs a new application exception.
	 * @param errorCode The error code of the exception.
	 * @param message The message of the exception.
	 */
	public BaseApplicationException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public BaseApplicationException(String errorCode, String message, String xml) {
		super(message);
		this.errorCode = errorCode;
		this.xml = xml;
	}
	
	public BaseApplicationException(String errorCode, String message, String[] args) {
		super(message);
		this.errorCode = errorCode;
		this.args = args;
	}
	
	public BaseApplicationException(String errorCode, String message, String xml, String[] args) {
		super(message);
		this.errorCode = errorCode;
		this.xml = xml;
		this.args = args;
	}

	/**
	 * Constructs a new application exception.
	 * @param errorCode The error code of the exception.
	 * @param cause The cause for the exception.
	 */
	public BaseApplicationException(String errorCode, Throwable cause) {
		this(errorCode, errorCode, cause);
	}

	/**
	 * Constructs a new application exception.
	 * @param errorCode The error code of the exception.
	 * @param message The message of the exception.
	 * @param cause The cause for the exception.
	 */
	public BaseApplicationException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}
	
	public BaseApplicationException(String errorCode, String message, Throwable cause, String[] args) {
		super(message, cause);
		this.errorCode = errorCode;
		this.args = args;
	}
	
	public BaseApplicationException(String errorCode, String message, Throwable cause, String xml) {
		super(message, cause);
		this.errorCode = errorCode;
		this.xml = xml;
	}

	/**
	 * Returns the error code of this exception.
	 * @return The error code of this exception.
	 * @see ApplicationException#getErrorCode()
	 */
	public String getErrorCode() {
		return errorCode;
	}
	
	public String getXml() {
		return xml;
	}
	
	public String[] getArgs() {
		return args;
	}
}
