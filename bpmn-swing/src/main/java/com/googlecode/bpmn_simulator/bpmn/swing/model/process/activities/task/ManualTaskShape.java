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
package com.googlecode.bpmn_simulator.bpmn.swing.model.process.activities.task;

import java.awt.Color;
import java.awt.Image;

import com.googlecode.bpmn_simulator.bpmn.model.process.activities.tasks.ManualTask;
import com.googlecode.bpmn_simulator.bpmn.swing.di.Appearance;

@SuppressWarnings("serial")
public class ManualTaskShape
		extends AbstractTaskShape<ManualTask> {

	static {
		Appearance.getDefault().getForElement(ManualTaskShape.class).setBackground(new Color(0xFFFFB5));
	}

	public ManualTaskShape(final ManualTask element) {
		super(element);
	}

	@Override
	protected Image getTaskImage() {
		return Appearance.getDefault().getImage(Appearance.IMAGE_MANUAL);
	}

}