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

public class TitledFlowElement extends FlowElement {

	private static final long serialVersionUID = 1L;

	private static final int HEADER_SIZE = 32;

	private boolean horizontal = false;

	public TitledFlowElement(final String id, final String name) {
		super(id, name);
		setHorizontal(true);
	}

	public void setHorizontal(final boolean horizontal) {
		this.horizontal = horizontal;
	}

	protected final boolean isHorizontal() {
		return horizontal;
	}

	@Override
	protected void paintBackground(Graphics g) {
		g.fillRect(getElementInnerBounds());
	}

	@Override
	protected void paintElement(Graphics g) {
		g.drawRect(getElementInnerBounds());
	}

	protected Rectangle getTitleBounds() {
		final Rectangle innerBounds = getElementInnerBounds();
		if (isHorizontal()) {
			return new Rectangle((int)innerBounds.getMinX(), (int)innerBounds.getMinY(),
					HEADER_SIZE, (int)innerBounds.getHeight());
		} else {
			return new Rectangle((int)innerBounds.getMinX(), (int)innerBounds.getMinY(),
					(int)innerBounds.getWidth(), HEADER_SIZE);
		}
	}

	@Override
	protected void paintText(Graphics g) {
		final Rectangle bounds = getTitleBounds();
		bounds.grow(-4, -4);
		if (isHorizontal()) {
			g.drawMultilineTextVertical(bounds, getName(), false, false);
		} else {
			g.drawMultilineText(bounds, getName(), false, false);
		}
	}

	@Override
	public Label createElementLabel() {
		return null;
	}

}
