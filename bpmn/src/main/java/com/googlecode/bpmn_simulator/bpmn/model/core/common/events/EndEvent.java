/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.googlecode.bpmn_simulator.bpmn.model.core.common.events;

import com.googlecode.bpmn_simulator.animation.token.Token;

public final class EndEvent
		extends AbstractThrowEvent {

	public EndEvent(final String id, final String name) {
		super(id, name);
	}

	@Override
	protected void onTokenAction(final Token token) {
		super.onTokenAction(token);
		final EventDefinition eventDefinition = getEventDefinition();
		if (eventDefinition instanceof TerminateEventDefinition) {
			token.getInstance().remove();
		}
	}

}
