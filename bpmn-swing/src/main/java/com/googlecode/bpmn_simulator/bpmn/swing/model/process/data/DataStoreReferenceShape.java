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
package com.googlecode.bpmn_simulator.bpmn.swing.model.process.data;

import java.awt.Graphics2D;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.Point;
import com.googlecode.bpmn_simulator.animation.element.visual.swing.Colors;
import com.googlecode.bpmn_simulator.animation.element.visual.swing.Presentation;
import com.googlecode.bpmn_simulator.bpmn.model.process.data.DataStoreReference;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNShape;
import com.googlecode.bpmn_simulator.bpmn.swing.di.Appearance;

@SuppressWarnings("serial")
public class DataStoreReferenceShape
		extends AbstractBPMNShape<DataStoreReference> {

	static {
		Appearance.setDefaultColor(DataStoreReferenceShape.class, Colors.GRAY);
	}

	public DataStoreReferenceShape(final DataStoreReference element) {
		super(element);
	}

	private int getN(final Bounds bounds) {
		return (int) Math.round(bounds.getHeight() * 0.1); 
	}

	@Override
	protected void paintElementBackground(final Graphics2D g) {
		super.paintElementBackground(g);
		final Bounds bounds = getInnerBoundsRelative();
		final int n = getN(bounds);
		final Presentation presentation = getPresentation();
		presentation.fillRect(g, bounds.shrink(0, n));
		presentation.fillOval(g, bounds.top(2 * n));
		presentation.fillOval(g, bounds.bottom(2 * n));
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		super.paintElementForeground(g);
		final Bounds bounds = getInnerBoundsRelative();
		final int n = getN(bounds);
		final Presentation presentation = getPresentation();
		presentation.drawLine(g,
				new Point(bounds.getMinX(), bounds.getMinY() + n),
				new Point(bounds.getMinX(), bounds.getMaxY() - n));
		presentation.drawLine(g,
				new Point(bounds.getMaxX(), bounds.getMinY() + n),
				new Point(bounds.getMaxX(), bounds.getMaxY() - n));
		presentation.drawOval(g, bounds.top(2 * n));
		presentation.drawArc(g, bounds.bottom(2 * n), 0, -180);
	}

}
