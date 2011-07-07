package org.romaframework.core.util.thread;

public class OnceTimerThread extends TimerThread {

	public OnceTimerThread(TimerListener iListener, long iTime) {
		super(iListener, iTime);
	}

	@Override
	protected void execute() {
		if (pause(time))
			listener.onExpiration();
		shutdown();
	}
}
