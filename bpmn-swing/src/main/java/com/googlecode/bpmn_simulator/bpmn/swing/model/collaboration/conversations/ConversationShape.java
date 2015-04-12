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
package com.googlecode.bpmn_simulator.bpmn.swing.model.collaboration.conversations;

import java.awt.Graphics2D;

import com.googlecode.bpmn_simulator.animation.element.visual.swing.Colors;
import com.googlecode.bpmn_simulator.animation.element.visual.swing.Presentation;
import com.googlecode.bpmn_simulator.bpmn.model.collaboration.conversations.Conversation;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNShape;
import com.googlecode.bpmn_simulator.bpmn.swing.di.Appearance;
import com.googlecode.bpmn_simulator.bpmn.swing.model.collaboration.ParticipantShape;

@SuppressWarnings("serial")
public class ConversationShape
		extends AbstractBPMNShape<Conversation> {

	static {
		Appearance.setDefaultColor(ParticipantShape.class, Colors.YELLOW);
	}

	public ConversationShape(final Conversation element) {
		super(element);
	}

	@Override
	protected void paintElementBackground(final Graphics2D g) {
		super.paintElementBackground(g);
		getPresentation().fillConvexPolygon(g, getInnerBoundsRelative(), Presentation.HEXAGON);
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		super.paintElementForeground(g);
		getPresentation().drawConvexPolygon(g, getInnerBoundsRelative(), Presentation.HEXAGON);
	}

}
