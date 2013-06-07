package com.jbp.randommaster.gui.common.threading;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SkippingScreenUpdateQueue implements Runnable {

	private Object newTaskLock;

	private boolean running;
	@SuppressWarnings("rawtypes")
	private List taskQueue;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SkippingScreenUpdateQueue() {
		newTaskLock = new Object();
		running = true;

		taskQueue = Collections.synchronizedList(new LinkedList());

		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	public void dispose() {
		running = false;
		synchronized (newTaskLock) {
			newTaskLock.notify();
		}
	}

	@SuppressWarnings("unchecked")
	public void nextTask(Runnable screenUpdateRunnable) {
		taskQueue.add(screenUpdateRunnable);
		synchronized (newTaskLock) {
			newTaskLock.notify();
		}
	}

	public void run() {
		// never end util stop
		while (running) {
			Runnable nextTask = null;
			synchronized (newTaskLock) {
				try {
					if (taskQueue.isEmpty())
						newTaskLock.wait();
				} catch (InterruptedException ie) {
					// ignore.
				}
				if (!taskQueue.isEmpty()) {
					nextTask = (Runnable) taskQueue.get(taskQueue.size() - 1);
					taskQueue.clear();
				}
			}

			if (!running)
				break;

			if (nextTask != null) {
				// SwingUtilities.invokeLater(nextTask);

				Throwable t = ScreenUpdateUtil.waitFor(nextTask);
				if (t != null)
					t.printStackTrace();
			}
		}
	}
}