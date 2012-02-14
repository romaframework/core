package org.romaframework.aspect.flow;

public interface ConfirmListener {

	/**
	 * Called on user response
	 * 
	 * @param response
	 *          true if user accept false if refuse.
	 */
	public void onResponse(boolean response);

}
