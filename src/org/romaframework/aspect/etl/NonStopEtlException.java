package org.romaframework.aspect.etl;

public class NonStopEtlException extends EtlException {
	public NonStopEtlException(String message, Throwable cause) {
		super(message, cause);
	}

	public NonStopEtlException(String message) {
		super(message);
	}

	public NonStopEtlException(Throwable cause) {
		super(cause);
	}
}
