package org.romaframework.aspect.etl;

public class NonStopEtlException extends EtlException {
	private static final long	serialVersionUID	= -5059796365150998690L;

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
