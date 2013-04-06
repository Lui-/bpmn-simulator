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
package com.google.code.bpmn_simulator.bpmn.model.process.activities;

import com.google.code.bpmn_simulator.bpmn.model.BPMNModel;
import com.google.code.bpmn_simulator.framework.element.GraphicsLayer;
import com.google.code.bpmn_simulator.framework.element.geometry.Bounds;

@SuppressWarnings("serial")
public final class Transaction
		extends Subprocess {

	public Transaction(final BPMNModel model, final String id, final String name,
			final boolean triggeredByEvent) {
		super(model, id, name, triggeredByEvent);
	}

	@Override
	public int getInnerBorderMargin() {
		return DEFAULT_INNER_MARGIN;
	}

	@Override
	protected void paintElement(final GraphicsLayer g) {
		super.paintElement(g);

		final Bounds rect = getElementInnerBounds();
		final int innerMargin = getInnerBorderMargin();
		rect.grow(-innerMargin, -innerMargin);
		g.drawRoundRect(rect, ARC_LENGTH, ARC_LENGTH);
	}

}
