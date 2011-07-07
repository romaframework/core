package org.romaframework.aspect.etl;

public class EtlValidationException extends Exception {

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
