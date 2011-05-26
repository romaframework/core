package org.romaframework.core.util.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class SoftThread extends Thread {
	protected boolean		running	= true;

	private static Log	log			= LogFactory.getLog(SoftThread.class);

	public SoftThread(ThreadGroup iThreadGroup) {
		super(iThreadGroup, SoftThread.class.getSimpleName());
	}

	public SoftThread(String name) {
		super(name);
	}

	public SoftThread(ThreadGroup group, String name) {
		super(group, name);
	}

	protected abstract void execute();

	public void startup() {
		running = true;
	}

	public void shutdown() {
		running = false;
	}

	public void sendShutdown() {
		log.info("[SoftThread.sendShutdown] Sending shutdown to thread: " + toString());
		running = false;
		interrupt();
	}

	@Override
	public void run() {
		startup();

		while (running) {
			try {
				beforeExecution();
				execute();
				afterExecution();
			} catch (Throwable t) {
				log.error("[SoftThread.run] Caught exception but continue the execution", t);
			}
		}

		shutdown();
	}

	public boolean isRunning() {
		return running;
	}

	public boolean pause(long iTime) {
		try {
			sleep(iTime);
			return true;
		} catch (InterruptedException e) {
			return false;
		}
	}

	public static boolean pauseCurrentThread(long iTime) {
		try {
			Thread.sleep(iTime);
			return true;
		} catch (InterruptedException e) {
			return false;
		}
	}

	protected void beforeExecution() throws InterruptedException {
		return;
	}

	protected void afterExecution() throws InterruptedException {
		return;
	}
}
