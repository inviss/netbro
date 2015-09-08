package kr.co.d2net.exception;


/**
 * <pre>
 * String 문자열인 XML을 Dom 문서로 변환하는 과정에서 에러가 발생했을경우 반환
 * </pre>
 * @author Administrator
 *
 */
public class XmlParsingException extends ApplicationException {
	
	private static final long serialVersionUID = 1L;

	public XmlParsingException(String message) {
		super(message);
	}

	public XmlParsingException(Throwable cause) {
		super(cause);
	}

	public XmlParsingException(String errorCode, String message) {
		super(errorCode, message);
	}

	public XmlParsingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public XmlParsingException(String errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}

}
