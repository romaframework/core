package org.romaframework.core.util.thread;

public class TimerThread extends SoftThread {

	protected long					time;
	protected TimerListener	listener;

	public TimerThread(TimerListener iListener, long iTime) {
		this(null, iListener, iTime);
	}

	public TimerThread(ThreadGroup iThreadGroup, TimerListener iListener, long iTime) {
		super(iThreadGroup, TimerThread.class.getSimpleName() + "-" + iTime / 1000 + "sec");

		listener = iListener;
		time = iTime;

		if (listener != null)
			start();
	}

	@Override
	protected void execute() {
		listener.onExpiration();
		pause(time);
	}

	public TimerListener getListener() {
		return listener;
	}

	public void setListener(TimerListener listener) {
		this.listener = listener;
	}

	@Override
	public String toString() {
		return TimerThread.class.getSimpleName() + "-" + time / 1000 + "sec" + "-" + hashCode();
	}
}
