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
package com.googlecode.bpmn_simulator.bpmn.model.core.common.events;

import java.awt.Point;

import javax.swing.Icon;

import com.googlecode.bpmn_simulator.bpmn.model.core.common.AbstractTokenFlowElement;
import com.googlecode.bpmn_simulator.framework.element.visual.GraphicsLayer;
import com.googlecode.bpmn_simulator.framework.element.visual.geometry.Bounds;
import com.googlecode.bpmn_simulator.framework.instance.InstanceManager;



@SuppressWarnings("serial")
public abstract class AbstractEvent
		extends AbstractTokenFlowElement
		implements Event {

	private InstanceManager instanceManager;

	private EventDefinition definition;

	public AbstractEvent(final String id, final String name,
			final InstanceManager instanceManager) {
		super(id, name);
		setInstanceManager(instanceManager);
	}

	protected void setInstanceManager(final InstanceManager manager) {
		this.instanceManager = manager;
	}

	protected InstanceManager getInstanceManager() {
		return instanceManager;
	}

	public void setDefinition(final EventDefinition definition) {
		this.definition = definition;
	}

	@Override
	public EventDefinition getDefinition() {
		return definition;
	}

	public boolean isTimer() {
		return getDefinition() instanceof TimerEventDefinition;
	}

	public boolean isConditional() {
		return getDefinition() instanceof ConditionalEventDefinition;
	}

	public boolean isPlain() {
		return getDefinition() == null;
	}

	@Override
	protected int getStepCount() {
		return 5;
	}

	@Override
	protected void paintBackground(final GraphicsLayer g) {
		super.paintBackground(g);

		g.fillOval(getElementInnerBounds());
	}

	@Override
	protected void paintElement(final GraphicsLayer g) {
		final Bounds innerBounds = getElementInnerBounds();
		g.drawOval(innerBounds);

		final int innerMargin = getInnerBorderMargin();
		if (innerMargin > 0) {
			innerBounds.grow(-innerMargin, -innerMargin);
			g.drawOval(innerBounds);
		}

		if (!isPlain()) {
			paintIcon(g);
		}
	}

	protected abstract Icon getTypeIcon();

	protected void paintIcon(final GraphicsLayer g) {
		final Icon icon = getTypeIcon();
		if (icon != null) {
			final Point position = getElementInnerBounds().getCenter();
			position.translate(-icon.getIconWidth() / 2, -icon.getIconHeight() / 2);
			g.drawIcon(icon, position);
		}
	}

	@Override
	public void updateElementLabelPosition() {
		getElementLabel().setCenterTopPosition(getInnerBounds().getCenterBottom());
	}

}
