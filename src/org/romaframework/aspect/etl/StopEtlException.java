package org.romaframework.aspect.etl;

public class StopEtlException extends EtlException {
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
