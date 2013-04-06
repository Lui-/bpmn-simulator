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
package com.google.code.bpmn_simulator.bpmn.di;

import com.google.code.bpmn_simulator.framework.element.AbstractVisualNodeElement;
import com.google.code.bpmn_simulator.framework.element.LogicalElement;

@SuppressWarnings("serial")
public abstract class AbstractBPMNShape
		extends AbstractVisualNodeElement
		implements BPMNShape {

	private final boolean horizontal;
	private final boolean expanded;

	public AbstractBPMNShape(final LogicalElement element,
			final boolean horizontal, final boolean expanded) {
		super(element);
		this.horizontal = horizontal;
		this.expanded = expanded;
	}

	@Override
	public boolean isHorizontal() {
		return horizontal;
	}

	@Override
	public boolean isExpanded() {
		return expanded;
	}

}
