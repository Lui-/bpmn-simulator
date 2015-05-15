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
package com.googlecode.bpmn_simulator.bpmn.model.core.common;

import com.googlecode.bpmn_simulator.animation.ref.Reference;
import com.googlecode.bpmn_simulator.animation.ref.ReferenceSet;
import com.googlecode.bpmn_simulator.animation.token.Token;

public abstract class AbstractFlowNode
		extends AbstractFlowElement
		implements FlowNode {

	private final ReferenceSet<SequenceFlow> incoming = new ReferenceSet<>();
	private final ReferenceSet<SequenceFlow> outgoing = new ReferenceSet<>();

	public AbstractFlowNode(final String id, final String name) {
		super(id, name);
	}

	@Override
	public void addIncoming(final Reference<SequenceFlow> incoming) {
		this.incoming.add(incoming);
	}

	@Override
	public void addOutgoing(final Reference<SequenceFlow> outgoing) {
		this.outgoing.add(outgoing);
	}

	@Override
	protected void tokenComplete(final Token token) {
		for (final SequenceFlow sequenceFlow : outgoing) {
			token.copyTo(sequenceFlow);
		}
		token.remove();
	}

}
