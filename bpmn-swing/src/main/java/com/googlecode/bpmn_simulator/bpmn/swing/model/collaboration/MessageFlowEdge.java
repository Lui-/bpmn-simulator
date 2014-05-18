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
package com.googlecode.bpmn_simulator.bpmn.swing.model.collaboration;

import java.awt.Graphics2D;
import java.awt.Stroke;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.Waypoints;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.MessageFlow;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNEdge;
import com.googlecode.bpmn_simulator.bpmn.swing.di.Appearance;

@SuppressWarnings("serial")
public class MessageFlowEdge
			extends AbstractBPMNEdge<MessageFlow> {

	private static final Stroke START_STROKE = Appearance.getDefault().createStrokeSolid(1);
	private static final Stroke LINE_STROKE = Appearance.getDefault().createStrokeDashed(1);
	private static final Stroke END_STROKE = Appearance.getDefault().createStrokeSolid(1);

	public MessageFlowEdge(final MessageFlow element) {
		super(element);
	}

	@Override
	protected void paintElementLine(final Graphics2D g) {
		g.setStroke(LINE_STROKE);
		super.paintElementLine(g);
	}

	@Override
	protected void paintElementStart(final Graphics2D g) {
		super.paintElementStart(g);
		final Waypoints waypoints = getWaypointsRelative();
		if (waypoints.isValid()) {
			final Bounds bounds = new Bounds(waypoints.first(), 2);
			g.setStroke(START_STROKE);
			getPresentation().drawOval(g, bounds);
		}
	}

	@Override
	protected void paintElementEnd(final Graphics2D g) {
		super.paintElementEnd(g);
		g.setStroke(END_STROKE);
		final Waypoints waypoints = getWaypointsRelative();
		if (waypoints.isValid()) {
			getPresentation().drawArrowhead(g, waypoints.nextToLast(), waypoints.last());
		}
	}

}
