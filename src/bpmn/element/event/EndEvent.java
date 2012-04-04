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

import bpmn.element.Graphics;
import bpmn.element.Rectangle;
import bpmn.token.InstanceController;
import bpmn.token.Token;

public class EndEvent extends Event {

	private static final long serialVersionUID = 1L;

	private boolean termination = false;

	public EndEvent(final String id, final String name,
			final InstanceController tockenController) {
		super(id, name, tockenController);
	}

	public void setTermination(final boolean termination) {
		this.termination = termination;
	}

	public boolean isTermination() {
		return termination;
	}

	@Override
	protected int getBorderWidth() {
		return 3;
	}

	@Override
	protected void paintElement(final Graphics g) {
		super.paintElement(g);

		if (isTermination()) {
			final Rectangle bounds = getElementInnerBounds();
			bounds.grow(-4, -4);
			g.fillOval(bounds);
		}
	}

	@Override
	protected void tokenForward(final Token token) {
		if (isTermination()) {
			token.getInstance().removeAllOtherTokens(token);
		}
		super.tokenForward(token);
	}

}
