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
package com.googlecode.bpmn_simulator.bpmn.swing.model.core.common.artifacts;

import java.awt.Graphics2D;
import java.awt.Stroke;

import com.googlecode.bpmn_simulator.bpmn.model.core.common.artifacts.Group;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNShape;
import com.googlecode.bpmn_simulator.bpmn.swing.di.Appearance;

@SuppressWarnings("serial")
public class GroupShape
		extends AbstractBPMNShape<Group> {

	private static final Stroke STROKE = Appearance.getDefault().createStrokeDashedDotted(1);

	public GroupShape(final Group element) {
		super(element);
	}

	@Override
	protected void paintElementBackground(final Graphics2D g) {
		/*XXX: some modeler adding groups in the wrong order, which causes overlapping of the grouped elements
		super.paintElementBackground(g);
		getPresentation().fillRoundRect(g, getInnerBoundsRelative(), Appearance.getDefault().getArcSize());
		*/
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		super.paintElementForeground(g);
		g.setStroke(STROKE);
		getPresentation().drawRoundRect(g, getInnerBoundsRelative(), Appearance.getDefault().getArcSize());
	}

}
