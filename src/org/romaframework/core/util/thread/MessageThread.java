package org.romaframework.core.util.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class MessageThread<T> extends SoftThread {

  protected static final int DEF_TIMEOUT = 1000;

  protected BlockingQueue<T> queue;
  protected int              timeout;

  public MessageThread() {
    this(null);
  }

  public MessageThread(ThreadGroup iThreadGroup) {
    this(iThreadGroup, DEF_TIMEOUT);
  }

  public MessageThread(ThreadGroup iThreadGroup, int iTimeout) {
    super(iThreadGroup);

    queue = new LinkedBlockingQueue<T>();
    timeout = iTimeout;
  }

  protected abstract void onMessage(T message);

  @Override
  protected void execute() {
    try {
      T message = null;
      while (message == null && running) {
        message = queue.poll(timeout, TimeUnit.MILLISECONDS);
      }

      if (message != null)
        onMessage(message);

    } catch (Exception e) {
      shutdown();
    }
  }

  public void sendMessage(T message) {
    queue.add(message);
  }
}
