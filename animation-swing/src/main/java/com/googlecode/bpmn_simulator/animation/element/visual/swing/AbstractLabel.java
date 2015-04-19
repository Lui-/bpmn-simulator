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
package com.googlecode.bpmn_simulator.animation.element.visual.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.HorizontalPosition;
import com.googlecode.bpmn_simulator.animation.element.visual.Label;
import com.googlecode.bpmn_simulator.animation.element.visual.Point;
import com.googlecode.bpmn_simulator.animation.element.visual.VerticalPosition;

@SuppressWarnings("serial")
public abstract class AbstractLabel
		extends JLabel
		implements Label {

	private static final Border BORDER = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	private boolean needsRotate;
	private boolean vertical;

	public AbstractLabel() {
		super();
		setBorder(BORDER);
		setFont(getFont().deriveFont(11.f));
	}

	@Override
	public void setBounds(final Bounds bounds) {
		setBounds(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
	}

	@Override
	public void setText(final String text) {
		final StringBuilder builder = new StringBuilder("<html><body>");
		builder.append(text);
		builder.append("</body></html>");
		super.setText(builder.toString());
	}

	@Override
	public void setTextVertical(boolean vertical) {
		this.vertical = vertical;
		invalidate();
	}

	public boolean isTextVertical() {
		return vertical;
	}

	@Override
	public Dimension getPreferredSize() {
		final Dimension preferredSize = super.getPreferredSize();
		if (isTextVertical()) {
			return new Dimension(preferredSize.height, preferredSize.width);
		}
		return preferredSize;
	}

	@Override
	public int getHeight() {
		if (isTextVertical() && needsRotate) {
			return super.getWidth();
		}
		return super.getHeight();
	}

	@Override
	public int getWidth() {
		if (isTextVertical() && needsRotate) {
			return super.getHeight();
		}
		return super.getWidth();
	}

	@Override
	protected void paintComponent(final Graphics g) {
		final Graphics2D g2d = (Graphics2D)g.create();
		if (isTextVertical()) {
			g2d.translate(0, getHeight());
			g2d.transform(AffineTransform.getQuadrantRotateInstance(-1));
		}
		needsRotate = true;
		super.paintComponent(g2d);
		needsRotate = false;
	}

	private int getPosX(final int x, final HorizontalPosition hpos) {
		switch (hpos) {
			case LEFT:
				return x - getWidth();
			case CENTER:
				return x - getWidth() / 2;
			case RIGHT:
				return x;
			default:
				throw new IllegalArgumentException();
		}
	}

	private int getPosY(final int y, final VerticalPosition vpos) {
		switch (vpos) {
			case TOP:
				return y - getHeight();
			case CENTER:
				return y - getHeight() / 2;
			case BOTTOM:
				return y;
			default:
				throw new IllegalArgumentException();
		}
	}

	@Override
	public void setPosition(final Point point,
			final HorizontalPosition hpos,
			final VerticalPosition vpos) {
		setSize(getPreferredSize());
		final int x = getPosX(point.getX(), hpos);
		final int y = getPosY(point.getY(), vpos);
		setLocation(x, y);
	}

}
