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
package bpmn.element.gateway;

import java.awt.BasicStroke;
import java.awt.Point;

import bpmn.element.ElementRef;
import bpmn.element.ElementWithDefaultSequenceFlow;
import bpmn.element.Graphics;
import bpmn.element.Label;
import bpmn.element.Rectangle;
import bpmn.element.SequenceFlow;
import bpmn.element.TokenFlowElement;
import bpmn.token.Instance;
import bpmn.token.Token;
import bpmn.token.TokenCollection;

public abstract class Gateway extends TokenFlowElement implements ElementWithDefaultSequenceFlow {

	private static final long serialVersionUID = 1L;

	private static final int SYMBOL_MARGIN = 14;

	private ElementRef<SequenceFlow> defaultSequenceFlowRef;

	public Gateway(final String id, final String name) {
		super(id, name);
	}

	public void setDefaultSequenceFlowRef(final ElementRef<SequenceFlow> sequenceFlowRef) {
		defaultSequenceFlowRef = sequenceFlowRef;
	}

	public ElementRef<SequenceFlow> getDefaultElementFlowRef() {
		return defaultSequenceFlowRef;
	}

	@Override
	protected int getStepCount() {
		return 10;
	}

	@Override
	protected void paintBackground(final Graphics g) {
		super.paintBackground(g);

		g.fillDiamond(getElementInnerBounds());
	}

	@Override
	protected void paintElement(final Graphics g) {
		g.drawDiamond(getElementInnerBounds());

		g.setStroke(new BasicStroke(3));
	}

	protected Rectangle getSymbolBounds() {
		final Rectangle bounds = getElementInnerBounds();
		bounds.grow(-SYMBOL_MARGIN, -SYMBOL_MARGIN);
		return bounds;
	}

	protected final void forwardMergedTokensToAllOutgoing(final TokenCollection tokens) {
		final Token mergedToken = tokens.merge();
		if (mergedToken != null) {
			passTokenToAllOutgoing(mergedToken);
			mergedToken.remove();
		}
	}

	protected Token getFirstTokenForIncoming(final SequenceFlow sequenceFlow,
			final Instance instance) {
		for (Token token : getInnerTokens().byInstance(instance)) {
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
		final Rectangle innerBounds = getInnerBounds(); 
		final Point position = innerBounds.getRightBottom();
		position.translate(
				-(int)(innerBounds.getWidth() / 4),
				-(int)(innerBounds.getHeight() / 4));
		getElementLabel().setLeftTopPosition(position);
	}

}
