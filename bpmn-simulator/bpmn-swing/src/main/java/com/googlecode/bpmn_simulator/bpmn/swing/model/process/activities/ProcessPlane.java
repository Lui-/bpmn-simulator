/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities;

import java.awt.Graphics2D;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.bpmn.model.process.activities.Process;
import com.googlecode.bpmn_simulator.bpmn.swing.di.AbstractBPMNPlane;
import com.googlecode.bpmn_simulator.bpmn.swing.di.Appearance;

@SuppressWarnings("serial")
public class ProcessPlane
		extends AbstractBPMNPlane<Process> {

	public ProcessPlane(final Process element) {
		super(element);
	}

	@Override
	protected void paintElementBackground(final Graphics2D g) {
		super.paintElementBackground(g);
		final Bounds bounds = new Bounds(0, 0, getWidth(), getHeight());
		getPresentation().fillRoundRect(g, bounds.shrink(MARGIN), Appearance.getDefault().getArcSize());
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		super.paintElementForeground(g);
		final Bounds bounds = new Bounds(0, 0, getWidth(), getHeight());
		getPresentation().drawRoundRect(g, bounds.shrink(MARGIN), Appearance.getDefault().getArcSize());
	}

}
