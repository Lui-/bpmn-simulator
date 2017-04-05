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
package com.googlecode.bpmn_simulator.animation.element.visual.swing;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.HorizontalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.Label;
import com.googlecode.bpmn_simulator.animation.element.visual.VerticalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.VisualNodeElement;

@SuppressWarnings("serial")
public abstract class AbstractVisualNodeElement<E extends LogicalElement>
		extends AbstractVisualElement<E>
		implements VisualNodeElement {

	private Bounds bounds;

	public AbstractVisualNodeElement(final E element) {
		super(element);
	}

	@Override
	public void setElementBounds(final Bounds bounds) {
		this.bounds = bounds;
		setInnerBounds(bounds);
	}

	@Override
	public Bounds getElementBounds() {
		return bounds;
	}

	@Override
	public void alignLabel(final Label label) {
		label.setPosition(getInnerBounds().getCenter(), HorizontalPosition.CENTER, VerticalPosition.CENTER);
	}

}
