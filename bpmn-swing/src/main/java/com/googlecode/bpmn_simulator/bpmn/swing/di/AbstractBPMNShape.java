/*
 * Copyright (C) 2015 Stefan Schweitzer
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
package com.googlecode.bpmn_simulator.bpmn.swing.di;

import java.awt.Graphics2D;
import java.awt.Stroke;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.swing.AbstractVisualNodeElement;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNShape;
import com.googlecode.bpmn_simulator.bpmn.di.ParticipantBandKind;
import com.googlecode.bpmn_simulator.bpmn.swing.di.Appearance.ElementAppearance;

@SuppressWarnings("serial")
public abstract class AbstractBPMNShape<E extends LogicalElement>
		extends AbstractVisualNodeElement<E>
		implements BPMNShape {

	protected static final int ICON_WIDTH = 16;
	protected static final int ICON_HEIGHT = 16;
	protected static final int ICON_MARGIN = 8;

	protected static final Stroke DEFAULT_STROKE = Appearance.getDefault().createStrokeSolid(1);

	protected static final Stroke CALL_STROKE = Appearance.getDefault().createStrokeSolid(3);

	private boolean horizontal;
	private boolean expanded;
	private boolean markerVisible;
	private boolean messageVisible;
	private ParticipantBandKind participantBandKind;

	public AbstractBPMNShape(final E element) {
		super(element);
	}

	@Override
	public void setHorizontal(final boolean isHorizontal) {
		horizontal = isHorizontal;
	}

	@Override
	public boolean isHorizontal() {
		return horizontal;
	}

	@Override
	public void setExpanded(final boolean isExpanded) {
		expanded = isExpanded;
	}

	@Override
	public boolean isExpanded() {
		return expanded;
	}

	@Override
	public void setMarkerVisible(final boolean isVisible) {
		markerVisible = isVisible;
	}

	@Override
	public boolean isMarkerVisible() {
		return markerVisible;
	}

	@Override
	public void setMessageVisible(final boolean isVisible) {
		messageVisible = isVisible;
	}

	@Override
	public boolean isMessageVisible() {
		return messageVisible;
	}

	@Override
	public void setParticipantBandKind(ParticipantBandKind bandKind) {
		participantBandKind = bandKind;
	}

	@Override
	public ParticipantBandKind getParticipantBandKind() {
		return participantBandKind;
	}

	protected Stroke getStroke() {
		return DEFAULT_STROKE;
	}

	protected ElementAppearance getAppearance() {
		return Appearance.getDefault().getForElement(getClass());
	}

	@Override
	protected void paintElementBackground(final Graphics2D g) {
		g.setPaint(getAppearance().getBackground());
	}

	@Override
	protected void paintElementForeground(final Graphics2D g) {
		g.setStroke(getStroke());
		g.setPaint(getAppearance().getForeground());
	}

	protected Bounds getIconBounds() {
		final Bounds bounds = getInnerBoundsRelative();
		return new Bounds(
				bounds.getMinX() + ICON_MARGIN,
				bounds.getMinY() + ICON_MARGIN,
				ICON_WIDTH, ICON_HEIGHT);
	}

}
