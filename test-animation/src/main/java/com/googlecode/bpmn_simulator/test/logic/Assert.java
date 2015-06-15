/*
 * Copyright (C) 2015 Stefan Schweitzer
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
package com.googlecode.bpmn_simulator.test.logic;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Objects;

public final class Assert {

	private Assert() {
	}

	public static <STATE> void assertExpectedFlow(
			final StateSequence<? extends STATE> expectedStates, final StateSequence<? extends STATE> states) {
		assertExpectedFlow(expectedStates, states.iterator());
	}

	public static <STATE> void assertExpectedFlow(
			final StateSequence<? extends STATE> expectedStates, final Iterator<? extends STATE> states) {
		assertExpectedFlow(expectedStates.iterator(), states);
	}

	public static <STATE> void assertNoMoreStates(final Iterator<STATE> states) {
		if (states.hasNext()) {
			fail(MessageFormat.format("unexpected state {0}. no more states expected", states.next()));
		}
	}

	private static <STATE> void assertMoreStates(final Iterator<STATE> states) {
		if (!states.hasNext()) {
			fail("no next state. more states expected");
		}
	}

	private static <STATE> boolean equals(final STATE s1, final STATE s2) {
		return Objects.equals(s1, s2);
	}

	public static <STATE> void assertExpectedFlow(
			final Iterator<? extends STATE> expectedStates, final Iterator<? extends STATE> states) {
		STATE expectedState;
		if (expectedStates.hasNext()) {
			expectedState = expectedStates.next();
			assertMoreStates(states);
		} else {
			assertNoMoreStates(states);
			return;
		}
		int stateIndex = 0;
		while (states.hasNext()) {
			final STATE state = states.next();
			if (!equals(state, expectedState)) {
				boolean fail = false;
				final boolean isFirstState = stateIndex == 0;
				final boolean isLastState = !expectedStates.hasNext();
				if (isFirstState || isLastState) {
					fail = true;
				} else {
					expectedState = expectedStates.next();
					if (!equals(state, expectedState)) {
						fail = true;
					}
				}
				if (fail) {
					fail(MessageFormat.format("unexpected state {0}. expected state {1}", state, expectedState));
				}
			}
			++stateIndex;
		}
		assertNoMoreStates(states);
		if (expectedStates.hasNext()) {
			fail(MessageFormat.format("no next state available. next state expected is {0}", expectedStates.next()));
		}
	}

}
