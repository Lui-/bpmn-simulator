/*
 * Copyright (C) 2012 Stefan Schweitzer
 *
 * This software was created by Stefan Schweitzer as a student's project at
 * Fachhochschule Kaiserslautern (University of Applied Sciences).
 * Supervisor: Professor Dr. Thomas Allweyer. For more information please see
 * http://www.fh-kl.de/~allweyer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this Software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.bpmn_simulator.framework.execution;

import java.util.Collection;
import java.util.Vector;

public abstract class AbstractAnimator
		implements Runnable {

	private static final long FPS_25 = 1000 / 25;

	private final Collection<AnimationListener> animationListeners = new Vector<AnimationListener>();

	private Thread thread;

	private boolean paused;

	private float speedFactor = 1.0f;

	public void addAnimationListener(final AnimationListener listener) {
		synchronized (animationListeners) {
			animationListeners.add(listener);
		}
	}

	public void removeAnimationListener(final AnimationListener listener) {
		synchronized (animationListeners) {
			animationListeners.remove(listener);
		}
	}

	protected void notifyAnimationPlay() {
		synchronized (animationListeners) {
			for (final AnimationListener listener : animationListeners) {
				listener.animationPlay();
			}
		}
	}

	protected void notifyAnimationPause() {
		synchronized (animationListeners) {
			for (final AnimationListener listener : animationListeners) {
				listener.animationPause();
			}
		}
	}

	protected void notifyAnimationReset() {
		synchronized (animationListeners) {
			for (final AnimationListener listener : animationListeners) {
				listener.animationReset();
			}
		}
	}

	public synchronized void setSpeed(final float factor) {
		assert factor > 0.0f;
		speedFactor = factor;
	}

	protected final synchronized long getStepSleep() {
		return (long)(FPS_25 / speedFactor);
	}

	@Override
	public void run() {
		try {
			while (thread.isAlive() && !thread.isInterrupted()) {
				Thread.sleep(getStepSleep());
				if (!isPaused()) {
					step(1);
				}
			}
		} catch (InterruptedException e) {
			// ok
		}
	}

	public abstract void step(int count);

	public void play() {
		paused = false;
		notifyAnimationPlay();
	}

	public void pause() {
		paused = true;
		notifyAnimationPause();
	}

	public boolean isPaused() {
		return paused;
	}

	public void reset() {
		notifyAnimationReset();
	}

	protected synchronized void start() {
		assert thread == null;
		if (thread == null) {
			thread = new Thread(this, "Animation"); //$NON-NLS-1$
			thread.start();
		}
	}

	public void end() {
		assert thread != null;
		if (thread != null) {
			synchronized (thread) {
				thread.interrupt();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				thread = null;
			}
		}
		paused = false;
	}

}
