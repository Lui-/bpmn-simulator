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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Stroke;

import javax.swing.Icon;
import javax.swing.JComponent;

import bpmn.element.activity.ExpandedProcess;

public abstract class BaseElement extends JComponent {

	private static final long serialVersionUID = 1L;

	protected static final int MARGIN = 10;

	protected static final int TOKEN_MARGIN = 5;

	private static final Icon EXCEPTION_ICON = Graphics.loadIcon("exception.png"); 

	private ExpandedProcess parentProcess;

	private String id;

	private Label label;

	private boolean exception;

	public BaseElement(final String id, final String name) {
		super();
		setId(id);
		setName(name);
		//setToolTipText(name +  " - " + id);
		setBackground(Color.WHITE);
		setForeground(Color.BLACK);
		setFocusable(false);
		setDoubleBuffered(true);
	}

	public final void setId(final String id) {
		this.id = id;
	}

	public final String getId() {
		return id;
	}

	protected void setException(final boolean exception) {
		this.exception = exception;
	}

	protected boolean hasException() {
		return exception;
	}

	public void setParentProcess(final ExpandedProcess parentProcess) {
		this.parentProcess = parentProcess;
	}

	public final ExpandedProcess getParentProcess() {
		return parentProcess;
	}

	public String getElementName() {
		final String name = getName();
		if ((name == null) || name.isEmpty()) {
			return getId();
		}
		return getName();
	}

/*
	protected final void debug(final String message) {
		String name = getName();
		if ((name == null) || name.isEmpty()) {
			name = getId();
		}
		System.out.println(Thread.currentThread().getName() + " " + getClass().getName() + "[" + name + "]: " + message);
	}
*/

	public void setInnerBounds(final Rectangle bounds) {
		bounds.grow(MARGIN, MARGIN);
		setBounds(bounds);
	}

	public Rectangle getInnerBounds() {
		final Rectangle bounds = new Rectangle(getBounds());
		bounds.grow(-MARGIN, -MARGIN);
		return bounds;
	}

	public Rectangle getElementInnerBounds() {
		final Rectangle bounds = new Rectangle(getBounds());
		return new Rectangle(MARGIN, MARGIN, bounds.width - (2 * MARGIN), bounds.height - (2 * MARGIN));
	}

	public Rectangle getElementOuterBounds() {
		return new Rectangle(0, 0, getWidth(), getHeight());
	}

	@Override
	public final void paint(final java.awt.Graphics g) {
		final Graphics graphics = new Graphics((Graphics2D)g);

		synchronized (this) {

			graphics.push();

			graphics.setPaint(getBackgroundPaint());
			paintBackground(graphics);

			graphics.setPaint(getForeground());
			graphics.setStroke(getStroke());
			paintElement(graphics);

			paintTokens(graphics);

			if (hasException()) {
				paintException(graphics);
			}

			graphics.pop();
		}

		super.paint(g);
	}

	protected Paint getBackgroundPaint() {
		final Color backgroundColor = getBackground();
		if (backgroundColor != null) {
			final Rectangle size = new Rectangle(getBounds());
			return new RadialGradientPaint(0.f, 0.f, size.min(), new float[] { 0.f, 1.f }, new Color[] { Color.WHITE, backgroundColor });
		}
		return null;
	}

	protected int getBorderWidth() {
		return 1;
	}

	protected Stroke getStroke() {
		return new BasicStroke(getBorderWidth(),
				BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.f); 
	}

	protected void paintBackground(final Graphics g) {
	}

	protected abstract void paintElement(final Graphics g);

	protected void paintException(final Graphics g) {
		g.drawIcon(EXCEPTION_ICON, new Point(0, 0));
	}

	protected void paintTokens(final Graphics g) {
	}

	protected Point getElementCenter() {
		return getInnerBounds().getCenter();
	}

	public Label getElementLabel() {
		return label;
	}

	protected void setElementLabel(final Label label) {
		assert this.label == null;
		this.label = label;
		getParent().add(label, 0);
		updateElementLabelPosition();
	}

	protected Label createElementLabel() {
		final String name = getName();
		Label label = null;
		if ((name != null) && !name.isEmpty()) {
			label = new Label(this, name);
		}
		return label;
	}

	private void initElementLabel() {
		final Label label = createElementLabel();
		if (label != null) {
			setElementLabel(label);
		}
	}

	public void initSubElements() {
		initElementLabel();
	}

	protected void updateElementLabelPosition() {
		getElementLabel().setCenterPosition(getElementCenter());
	}

	protected Dimension calcSizeByInnerComponents() {
		int width = MARGIN;
		int height = MARGIN;
		for (Component component : getComponents()) {
			final java.awt.Rectangle rectangle = component.getBounds();
			if ((int)rectangle.getMaxX() > width) {
				width = (int)rectangle.getMaxX();
			}
			if ((int)rectangle.getMaxY() > height) {
				height = (int)rectangle.getMaxY();
			}
		}
		return new Dimension(MARGIN + width + MARGIN, MARGIN + height + MARGIN);
	}

}
