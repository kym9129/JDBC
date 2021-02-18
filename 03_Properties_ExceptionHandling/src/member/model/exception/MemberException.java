package member.model.exception;

public class MemberException extends RuntimeException {

	public MemberException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public MemberException(String message, Throwable cause) {
		super(message, cause);//cause : 애초에 발생한 예외
		// TODO Auto-generated constructor stub
	}

	public MemberException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MemberException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * checkedException : extends Exception
	 * uncheckedException : extends RuntimeException
	 */
	
	
	

}
