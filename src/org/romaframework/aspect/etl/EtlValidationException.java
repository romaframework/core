package org.romaframework.aspect.etl;

public class EtlValidationException extends Exception {

	private static final long	serialVersionUID	= 2272509777490247879L;

	public EtlValidationException() {
		super();
	}

	public EtlValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public EtlValidationException(String message) {
		super(message);
	}

	public EtlValidationException(Throwable cause) {
		super(cause);
	}

}
