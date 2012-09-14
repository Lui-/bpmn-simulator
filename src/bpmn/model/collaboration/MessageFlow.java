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
package bpmn.model.collaboration;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;

import bpmn.Graphics;
import bpmn.Rectangle;
import bpmn.model.ElementRef;
import bpmn.model.core.common.AbstractTokenConnectingElement;
import bpmn.model.core.common.AbstractTokenFlowElement;

@SuppressWarnings("serial")
public final class MessageFlow
		extends AbstractTokenConnectingElement {

	public MessageFlow(final String id, final String name,
			final ElementRef<AbstractTokenFlowElement> sourceRef,
			final ElementRef<AbstractTokenFlowElement> targetRef) {
		super(id, name, sourceRef, targetRef);
		setElementBackground(Color.WHITE);
	}

	@Override
	protected Stroke getStroke() {
		return getVisualization().createStrokeDashed(getBorderWidth()); 
	}

	@Override
	protected void paintConnectingStart(final Graphics g, final Point from, final Point start) {
		g.setStroke(new BasicStroke(1));
		g.setPaint(getElementBackground());
		final Rectangle point = new Rectangle(start);
		point.grow(3, 3);
		g.fillOval(point);
		g.setPaint(getForeground());
		g.drawOval(point);
	}

	@Override
	protected void paintConnectingEnd(final Graphics g, final Point from, final Point end) {
		g.setStroke(new BasicStroke(1));
		g.setPaint(getElementBackground());
		g.fillArrow(from, end);
		g.setPaint(getForeground());
		g.drawArrow(from, end);
	}

}
