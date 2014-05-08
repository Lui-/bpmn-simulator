/*
 * Copyright (C) 2014 Stefan Schweitzer
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
package com.googlecode.bpmn_simulator.bpmn.swing.di;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.visual.swing.AbstractVisualNodeElement;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNShape;

@SuppressWarnings("serial")
public abstract class AbstractBPMNShape<E extends LogicalElement>
		extends AbstractVisualNodeElement<E>
		implements BPMNShape {

	private boolean horizontal;
	private boolean expanded;

	public AbstractBPMNShape(final E element) {
		super(element);
	}

	@Override
	public void setHorizontal(final boolean isHorizontal) {
		horizontal = isHorizontal;
	}

	@Override
	public boolean isHorizontal() {
		return horizontal;
	}

	@Override
	public void setExpanded(final boolean isExpanded) {
		horizontal = isExpanded;
	}

	@Override
	public boolean isExpanded() {
		return expanded;
	}

}
