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
package com.google.code.bpmn_simulator.bpmn.model.collaboration;

import com.google.code.bpmn_simulator.bpmn.model.core.common.AbstractFlowElement;
import com.google.code.bpmn_simulator.bpmn.model.core.common.Label;
import com.google.code.bpmn_simulator.framework.element.visual.GraphicsLayer;
import com.google.code.bpmn_simulator.framework.element.visual.geometry.Bounds;

@SuppressWarnings("serial")
public abstract class AbstractTitledFlowElement
		extends AbstractFlowElement {

	private static final int HEADER_SIZE = 32;

	private static final int LABEL_MARGIN = 4;

	private boolean horizontal;

	public AbstractTitledFlowElement(final String id, final String name) {
		super(id, name);
		setHorizontal(true);
	}

	public void setHorizontal(final boolean horizontal) {
		this.horizontal = horizontal;
	}

	protected final boolean isHorizontal() {
		return horizontal;
	}

	@Override
	protected void paintBackground(final GraphicsLayer g) {
		g.fillRect(getElementInnerBounds());
	}

	@Override
	protected void paintElement(final GraphicsLayer g) {
		g.drawRect(getElementInnerBounds());
	}

	protected Bounds getInnerTitleBounds() {
		final Bounds innerBounds = getElementInnerBounds();
		if (isHorizontal()) {
			return new Bounds((int)innerBounds.getMinX(), (int)innerBounds.getMinY(),
					HEADER_SIZE, (int)innerBounds.getHeight());
		} else {
			return new Bounds((int)innerBounds.getMinX(), (int)innerBounds.getMinY(),
					(int)innerBounds.getWidth(), HEADER_SIZE);
		}
	}

	@Override
	public Label createElementLabel() {
		final String name = getName();
		Label label = null;
		if ((name != null) && !name.isEmpty()) {
			label = new Label(this, name, isHorizontal());
		}
		return label;
	}

	@Override
	public void updateElementLabelPosition() {
		final Bounds titleBounds = getInnerBounds();
		if (isHorizontal()) {
			titleBounds.shrink(LABEL_MARGIN, 0, 0, LABEL_MARGIN);
			getElementLabel().setLeftBottomPosition(titleBounds.getLeftBottom());
		} else {
			titleBounds.shrink(LABEL_MARGIN, 0, LABEL_MARGIN, 0);
			getElementLabel().setLeftTopPosition(titleBounds.getLeftTop());
		}
	}

}
