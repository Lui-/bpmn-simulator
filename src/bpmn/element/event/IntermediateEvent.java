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
package bpmn.element.event;

import java.awt.Color;

import bpmn.element.Graphics;
import bpmn.element.Rectangle;
import bpmn.element.VisualConfig;

@SuppressWarnings("serial")
public abstract class IntermediateEvent extends AbstractEvent {

	private static final int CIRCLE_MARGIN = 4;

	public IntermediateEvent(final String id, final String name) {
		super(id, name, null);
	}

	@Override
	protected Color getElementDefaultBackground() {
		return getVisualConfig().getBackground(VisualConfig.Element.EVENT_INTERMEDIATE);
	}

	@Override
	protected void paintElement(final Graphics g) {
		super.paintElement(g);

		final Rectangle bounds = getElementInnerBounds();
		bounds.grow(-CIRCLE_MARGIN, -CIRCLE_MARGIN);
		g.drawOval(bounds);
	}

}
