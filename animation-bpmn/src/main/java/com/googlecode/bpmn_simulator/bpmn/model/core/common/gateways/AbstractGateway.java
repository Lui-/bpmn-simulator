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
package com.googlecode.bpmn_simulator.bpmn.model.core.common.gateways;

import java.awt.Color;
import java.awt.Point;

import com.googlecode.bpmn_simulator.bpmn.model.core.common.AbstractTokenFlowElementWithDefault;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.Label;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.SequenceFlow;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.Visualization;
import com.googlecode.bpmn_simulator.framework.element.visual.GraphicsLayer;
import com.googlecode.bpmn_simulator.framework.element.visual.geometry.Bounds;
import com.googlecode.bpmn_simulator.framework.instance.Instance;
import com.googlecode.bpmn_simulator.framework.token.Token;



@SuppressWarnings("serial")
public abstract class AbstractGateway
		extends AbstractTokenFlowElementWithDefault {

	private static final int SYMBOL_MARGIN = 14;

	public AbstractGateway(final String id, final String name) {
		super(id, name);
	}

	@Override
	protected int getStepCount() {
		return 10;
	}

	@Override
	protected Color getElementDefaultBackground() {
		return getVisualization().getBackground(Visualization.Element.GATEWAY);
	}

	@Override
	protected void paintBackground(final GraphicsLayer g) {
		super.paintBackground(g);

		g.fillDiamond(getElementInnerBounds());
	}

	@Override
	protected void paintElement(final GraphicsLayer g) {
		g.drawDiamond(getElementInnerBounds());
	}

	protected Bounds getSymbolBounds() {
		final Bounds bounds = getElementInnerBounds();
		bounds.grow(-SYMBOL_MARGIN, -SYMBOL_MARGIN);
		return bounds;
	}

	protected Token getFirstTokenForIncoming(final SequenceFlow sequenceFlow,
			final Instance instance) {
		for (final Token token : getInnerTokens().byInstance(instance)) {
			assert token.getPreviousFlow() != null;
			if (sequenceFlow.equals(token.getPreviousFlow())) {
				return token;
			}
		}
		return null;
	}

	@Override
	public Label createElementLabel() {
		final Label label = super.createElementLabel();
		if (label != null) {
			label.setAlignCenter(false);
		}
		return label;
	}

	@Override
	public void updateElementLabelPosition() {
		final Bounds innerBounds = getInnerBounds();
		final Point position = innerBounds.getRightBottom();
		position.translate(
				-(int)(innerBounds.getWidth() / 4),
				-(int)(innerBounds.getHeight() / 4));
		getElementLabel().setLeftTopPosition(position);
	}

	@Override
	protected void tokenForwardToNextElement(final Token token,
			final Instance instance) {
		if (passTokenToAllNextElements(token, instance)) {
			setException(false);
			token.remove();
		} else {
			setException(true);
		}
	}

}
