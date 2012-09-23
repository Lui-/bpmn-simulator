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

import java.awt.Point;

import bpmn.Graphics;
import bpmn.Messages;
import bpmn.Rectangle;
import bpmn.model.ElementRef;
import bpmn.model.process.activities.Subprocess;

@SuppressWarnings("serial")
public class Participant
		extends TitledFlowElement {

	public static final String ELEMENT_NAME = Messages.getString("participant"); //$NON-NLS-1$

	public Participant(final String id, final String name,
			final ElementRef<Subprocess> processRef) {
		super(id, name);
	}

	@Override
	public String getElementName() {
		return ELEMENT_NAME;
	}

	@Override
	protected int getBorderWidth() {
		return 2;
	}

	@Override
	protected void paintElement(final Graphics g) {
		super.paintElement(g);

		final Rectangle bounds = getInnerTitleBounds();
		if (isHorizontal()) {
			g.drawLine(new Point((int)bounds.getMaxX(), (int)bounds.getMinY()),
					new Point((int)bounds.getMaxX(), (int)bounds.getMaxY()));
		} else {
			g.drawLine(new Point((int)bounds.getMinX(), (int)bounds.getMaxY()),
					new Point((int)bounds.getMaxX(), (int)bounds.getMaxY()));
		}
	}

}
