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
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;

import bpmn.element.Visualization;
import bpmn.element.activity.ExpandedProcess;
import bpmn.token.Instance;
import bpmn.token.InstanceManager;

@SuppressWarnings("serial")
public final class StartEvent extends AbstractEvent
		implements CatchEvent, MouseListener {

	public StartEvent(final String id, final String name,
			final InstanceManager instanceManager) {
		super(id, name, instanceManager);
		addMouseListener(this);
	}

	@Override
	public void happen(final Instance instance) {
		if (instance != null) {
			instance.newChildInstance().newToken(this);
		} else {
			getInstanceManager().newInstance().newToken(this);
		}
	}

	@Override
	protected Color getElementDefaultBackground() {
		return getVisualization().getBackground(Visualization.Element.EVENT_START);
	}

	@Override
	protected void setInstanceManager(final InstanceManager manager) {
		super.setInstanceManager(manager);
		updateCursor();
	}

	@Override
	public void setProcess(final ExpandedProcess parentProcess) {
		super.setProcess(parentProcess);
		updateCursor();
	}

	public boolean canStartManuell() {
		final ExpandedProcess process = getProcess();
		return (getInstanceManager() != null) && (process != null) && !process.hasIncoming(); 
	}

	protected void updateCursor() {
		setCursor(canStartManuell()
				? new Cursor(Cursor.HAND_CURSOR)
				: Cursor.getDefaultCursor());
	}

	@Override
	public void mouseClicked(final MouseEvent event) {
		if (canStartManuell()) {
			happen(null);
		}
	}

	@Override
	public void mouseEntered(final MouseEvent event) {
	}

	@Override
	public void mouseExited(final MouseEvent event) {
	}

	@Override
	public void mousePressed(final MouseEvent event) {
	}

	@Override
	public void mouseReleased(final MouseEvent event) {
	}

	@Override
	protected Icon getTypeIcon() {
		return getDefinition().getIcon(getVisualization(), false);
	}

}
