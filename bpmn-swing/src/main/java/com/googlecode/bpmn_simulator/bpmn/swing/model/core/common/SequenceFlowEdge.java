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
package com.googlecode.bpmn_simulator.bpmn.swing.model.core.common;

import java.awt.Graphics2D;
import java.awt.Paint;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.GeometryUtils;
import com.googlecode.bpmn_simulator.animation.element.visual.Point;
import com.googlecode.bpmn_simulator.animation.element.visual.Waypoints;
import com.googlecode.bpmn_simulator.animation.element.visual.swing.Colors;
import com.googlecode.bpmn_simulator.bpmn.model.core.common.SequenceFlow;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNTokenEdge;
import com.googlecode.bpmn_simulator.bpmn.swing.di.Appearance;

@SuppressWarnings("serial")
public final class SequenceFlowEdge
		extends AbstractBPMNTokenEdge<SequenceFlow> {

	private static final double SYMBOL_DEFAULT_RAD = Math.toRadians(45.);

	static {
		Appearance.setDefaultColor(SequenceFlowEdge.class, Colors.WHITE, Colors.BLACK);
	}

	private static final int START_SYMBOL_SIZE = 8;

	public SequenceFlowEdge(final SequenceFlow element) {
		super(element);
	}

	@Override
	protected void paintElementStart(final Graphics2D g) {
		super.paintElementStart(g);
		SequenceFlow sequenceFlow = getLogicalElement();
		final boolean isDefault = sequenceFlow.isDefault();
		final boolean hasConditionExpression = sequenceFlow.getConditionExpression() != null;
		if (isDefault) {
			if (!hasConditionExpression) {
				drawDefaultSymbol(g);
			}
		} else {
			if (hasConditionExpression) {
				drawConditionalSymbol(g);
			}
		}
	}

	private void drawDefaultSymbol(final Graphics2D g) {
		final Waypoints waypoints = getWaypointsRelative();
		final Point position = waypoints.getWaypoint(START_SYMBOL_SIZE);
		final double angle = waypoints.getAngleAt(START_SYMBOL_SIZE) - SYMBOL_DEFAULT_RAD;
		final double length = START_SYMBOL_SIZE * 0.8;
		final Point from = GeometryUtils.polarToCartesian(position, length, angle);
		final Point to = GeometryUtils.polarToCartesian(position, length, GeometryUtils.invert(angle));
		getPresentation().drawLine(g, from, to);
	}

	private void drawConditionalSymbol(final Graphics2D g) {
		final Waypoints waypoints = getWaypointsRelative();
		final Point position = waypoints.getWaypoint(START_SYMBOL_SIZE);
		final double angle = waypoints.getAngleAt(START_SYMBOL_SIZE);
		final Bounds diamondBounds = Bounds.fromCenter(position, START_SYMBOL_SIZE, (int) (START_SYMBOL_SIZE * 0.6));
		getPresentation().rotate(g, position, angle);
		final Paint paint = g.getPaint();
		g.setPaint(Appearance.getDefault().getForElement(getClass()).getBackground());
		getPresentation().fillDiamond(g, diamondBounds);
		g.setPaint(paint);
		getPresentation().drawDiamond(g, diamondBounds);
		getPresentation().restore(g);
	}

	@Override
	protected void paintElementEnd(final Graphics2D g) {
		super.paintElementEnd(g);
		final Waypoints waypoints = getWaypointsRelative();
		if (waypoints.isValid()) {
			getPresentation().fillArrowhead(g, waypoints.nextToLast(), waypoints.last());
		}
	}

}
