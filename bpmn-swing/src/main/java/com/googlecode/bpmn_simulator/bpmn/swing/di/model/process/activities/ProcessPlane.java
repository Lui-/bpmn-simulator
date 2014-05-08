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
package com.googlecode.bpmn_simulator.bpmn.swing.di.model.process.activities;

import java.awt.Color;
import java.awt.Graphics2D;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.swing.Presentation;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.Process;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNPlane;

@SuppressWarnings("serial")
public class ProcessPlane
		extends AbstractBPMNPlane<Process> {

	private static final int ARC_SIZE = 10;

	public ProcessPlane(final Process element) {
		super(element);
	}

	@Override
	protected void paintElement(final Graphics2D g, final Presentation presentation) {
		g.setPaint(Color.RED);
		final Bounds bounds = new Bounds(0, 0, getWidth(), getHeight());
		presentation.drawRoundRect(g, bounds.shrink(MARGIN), ARC_SIZE);
	}

}
