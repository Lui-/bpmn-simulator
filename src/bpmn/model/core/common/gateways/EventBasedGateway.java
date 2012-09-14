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
package bpmn.model.core.common.gateways;

import bpmn.Graphics;
import bpmn.Rectangle;
import bpmn.instance.Instance;
import bpmn.model.core.common.AbstractFlowElement;
import bpmn.model.core.common.SequenceFlow;
import bpmn.token.Token;
import bpmn.token.TokenCollection;
import bpmn.trigger.TriggerCatchingElement;
import bpmn.trigger.StoringTriggerCatchingElement;
import bpmn.trigger.Trigger;
import bpmn.trigger.InstantiableNotifiyTarget;

@SuppressWarnings("serial")
public final class EventBasedGateway
	extends AbstractGateway
	implements InstantiableNotifiyTarget {

	private final boolean instantiate;

	public EventBasedGateway(final String id, final String name,
			final boolean instantiate) {
		super(id, name);
		this.instantiate = instantiate;
	}

	@Override
	public boolean isInstantiable() {
		return instantiate;
	}

	@Override
	protected void paintElement(final Graphics g) {
		super.paintElement(g);

		final Rectangle bounds = getElementInnerBounds();
		bounds.shrinkHalf();
		g.drawOval(bounds);
		if (!isInstantiable()) {
			bounds.shrink(2, 2, 2, 2);
			g.drawOval(bounds);
		}
		bounds.shrink(2, 2, 2, 2);
		g.drawPentagon(bounds);
	}

	protected SequenceFlow getSequenceFlowToCatchElement(final TriggerCatchingElement catchElement) {
		for (final SequenceFlow outgoing : getOutgoing()) {
			if (catchElement.equals(outgoing.getTarget())) {
				return outgoing;
			}
		}
		return null;
	}

	@Override
	public void eventTriggered(final TriggerCatchingElement catchElement, final Trigger trigger) {
		final SequenceFlow sequenceFlow = getSequenceFlowToCatchElement(catchElement);
		assert sequenceFlow != null;
		if (sequenceFlow != null) {
			if (isInstantiable()) {
				final Instance instance = getContainerActivity().createInstance(null);
				final Instance sourceInstance = trigger.getSourceInstance();
				if (sourceInstance != null) {
					instance.setColor(sourceInstance.getColor());
				}
				instance.addNewToken(sequenceFlow);
			} else {
				final TokenCollection tokens = getInnerTokens().byInstance(trigger.getDestinationInstance());
				if (!tokens.isEmpty()) {
					final Token token = tokens.firstElement();
					token.passTo(sequenceFlow);
					token.remove();
				}
			}
		}
	}

	private StoringTriggerCatchingElement getTargetCatchElement(final Token token) {
		final Instance instance = token.getInstance();
		Trigger targetTrigger = null;
		StoringTriggerCatchingElement targetCatchElement = null;
		for (AbstractFlowElement flowElement : getOutgoingFlowElements()) {
			if (flowElement instanceof StoringTriggerCatchingElement) {
				final StoringTriggerCatchingElement catchEvent = (StoringTriggerCatchingElement)flowElement;
				final Trigger catchTrigger = catchEvent.getFirstTrigger(instance);
				if ((catchTrigger != null)
						&& ((targetTrigger == null)
								|| (catchTrigger.getTime() < targetTrigger.getTime()))) {
					targetTrigger = catchTrigger;
					targetCatchElement = catchEvent;
				}
			}
		}
		return targetCatchElement;
	}

	@Override
	protected boolean canForwardTokenToNextElement(final Token token) {
		return getTargetCatchElement(token) != null;
	}

	@Override
	protected void tokenForwardToNextElement(final Token token, final Instance instance) {
		final StoringTriggerCatchingElement targetCatchElement = getTargetCatchElement(token);
		token.passTo(getSequenceFlowToCatchElement(targetCatchElement));
		token.remove();
		if (targetCatchElement != null) {
			targetCatchElement.removeFirstTrigger(instance);
		}
	}

}
