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
package com.google.code.bpmn_simulator.bpmn.model.process.activities.tasks;

import com.google.code.bpmn_simulator.bpmn.model.core.common.Message;
import com.google.code.bpmn_simulator.framework.ElementRef;

@SuppressWarnings("serial")
public abstract class AbstractMessageTask
		extends Task {

	private final ElementRef<Message> messageRef;

	public AbstractMessageTask(final String id, final String name,
			final ElementRef<Message> messageRef) {
		super(id, name);
		this.messageRef = messageRef;
	}

	public Message getMessage() {
		return (messageRef != null) && messageRef.hasElement()
				? messageRef.getElement()
				: null;
	}

}
