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
package bpmn.element.artifact;

import java.awt.BasicStroke;
import java.awt.Stroke;

import bpmn.element.Graphics;

@SuppressWarnings("serial")
public class Group extends Artifact {

	protected static final int ARC_LENGTH = 20;

	public Group(final String id) {
		super(id);
	}

	@Override
	protected Stroke getStroke() {
		return new BasicStroke(getBorderWidth(),
				BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_MITER,
				1.f,
				new float[] { 8.f, 5.f, 1.f, 5.f },
				0); 
	}

	@Override
	protected void paintElement(final Graphics g) {

		g.drawRoundRect(getElementInnerBounds(), ARC_LENGTH, ARC_LENGTH);
	}

}
