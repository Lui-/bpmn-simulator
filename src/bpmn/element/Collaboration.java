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
package bpmn.element;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Scrollable;

import bpmn.Graphics;
import bpmn.element.activity.Process;
import bpmn.instance.Instance;
import bpmn.trigger.Instantiable;
import bpmn.trigger.InstantiableNotifiySource;
import bpmn.trigger.TriggerCatchingElement;
import bpmn.trigger.Trigger;

@SuppressWarnings("serial")
public class Collaboration extends FlowElement implements Scrollable {

	private final Collection<MessageFlow> messageFlows = new ArrayList<MessageFlow>();

	public Collaboration(final String id) {
		super(id, null);
	}

	public void addMessageFlow(final MessageFlow messageFlow) {
		messageFlows.add(messageFlow);
	}

	public boolean hasMessageTarget(final FlowElement element) {
		for (final MessageFlow messageFlow : messageFlows) {
			if (element.equals(messageFlow.getTarget())) {
				return true;
			}
		}
		return false;
	}

	private boolean requiresCorrelation(final TriggerCatchingElement catchingElement) {
		final boolean instantiable = (catchingElement instanceof Instantiable)
				&& ((Instantiable)catchingElement).isInstantiable();
		final boolean instantiableNotifing = (catchingElement instanceof InstantiableNotifiySource)
				&& ((InstantiableNotifiySource)catchingElement).isInstantiableNotifying();
		return !(instantiable || instantiableNotifing);
	}

	private static Instance findMessageTargetInstance(final Collection<Instance> instances,
			final Process process) {
		for (Instance instance : instances) {
			if (!instance.hasCorrelationTo(process)) {
				return instance;
			}
		}
		return null;
	}

	private static Instance findCorrelationInstance(
			final Process sourceProcess, final Instance sourceInstance,
			final Process targetProcess) {
		final Collection<Instance> targetInstances = targetProcess.getInstances();
		Instance targetInstance
			= sourceInstance.getCorrelationInstance(targetInstances);
		if (targetInstance == null) {
			targetInstance = findMessageTargetInstance(targetInstances, sourceProcess);
			if (targetInstance != null) {
				targetInstance.createCorrelationTo(sourceInstance);
				sourceInstance.createCorrelationTo(targetInstance);
			}
		}
		return targetInstance;
	}

	public void sendMessages(final FlowElement sourceElement,
			final Instance sourceInstance) {
		for (final MessageFlow messageFlow : messageFlows) {
			if (sourceElement.equals(messageFlow.getSource())) {
				final FlowElement targetElement = messageFlow.getTarget();
				if (targetElement instanceof TriggerCatchingElement) {
					final TriggerCatchingElement catchingElement = (TriggerCatchingElement)targetElement;  
					if (requiresCorrelation(catchingElement)) {
						final Instance targetInstance = findCorrelationInstance(
								sourceElement.getProcess(), sourceInstance,
								targetElement.getProcess());
						if (targetInstance != null) {
							catchingElement.catchTrigger(new Trigger(sourceInstance, targetInstance));
						}
					} else {
						catchingElement.catchTrigger(new Trigger(sourceInstance, null));
					}
				}
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return calcSizeByInnerComponents();
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public int getScrollableBlockIncrement(final Rectangle arg0,
			final int arg1, final int arg2) {
		return 0;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public int getScrollableUnitIncrement(final Rectangle arg0,
			final int arg1, final int arg2) {
		return 0;
	}

	@Override
	protected void paintElement(final Graphics g) {
	}

}
