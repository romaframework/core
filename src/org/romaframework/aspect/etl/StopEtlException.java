package org.romaframework.aspect.etl;

public class StopEtlException extends EtlException {
	private static final long	serialVersionUID	= 2582505210453250011L;

	public StopEtlException(String message, Throwable cause) {
		super(message, cause);
	}

	public StopEtlException(String message) {
		super(message);
	}

	public StopEtlException(Throwable cause) {
		super(cause);
	}
}
