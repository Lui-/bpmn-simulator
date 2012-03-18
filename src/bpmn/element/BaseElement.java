package bpmn.element;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;


public abstract class BaseElement extends JComponent {

	private static final long serialVersionUID = 1L;

	protected static final int MARGIN = 10;

	private ExpandedProcess parentProcess = null;

	private String id = null;

	private Label label = null;

	private boolean exception = false;

	private static final ImageIcon exceptionIcon = loadElementPNG("exception.png"); 

	protected static ImageIcon loadElementPNG(final String filename) {
		final URL url = BaseElement.class.getResource(filename);
		if (url != null) {
			return new ImageIcon(url);
		}
		return null;
	}

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

	public void setInnerBounds(Rectangle bounds) {
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
	public final void paint(java.awt.Graphics g) {
		Graphics graphics = new Graphics((Graphics2D)g);

		synchronized (this) {

			graphics.push();

			graphics.setPaint(getBackgroundPaint());
			paintBackground(graphics);

			graphics.setPaint(getForeground());
			graphics.setStroke(getStroke());
			paintElement(graphics);

			paintText(graphics);

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
			return new RadialGradientPaint(0.f, 0.f, Math.min(size.width, size.height), new float[] { 0.f, 1.f }, new Color[] { Color.WHITE, backgroundColor });
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

	protected void paintBackground(Graphics g) {
	}

	protected abstract void paintElement(Graphics g);

	protected void paintException(Graphics g) {
		g.drawIcon(exceptionIcon, new Point(0, 0));
	}

	protected void paintText(Graphics g) {
	}

	protected void paintTokens(Graphics g) {		
	}

	protected Point getElementLeftTop() {
		final Rectangle bounds = getInnerBounds();
		return new Point(bounds.x, bounds.y);
	}

	protected Point getElementCenter() {
		final Rectangle bounds = getInnerBounds();
		return new Point(bounds.x + (bounds.width / 2), bounds.y + (bounds.height / 2));
	}

	protected Point getElementBottomCenter() {
		final Rectangle bounds = getInnerBounds();
		return new Point(bounds.x + (bounds.width / 2), bounds.y + bounds.height);
	}

	protected Point getElementRightBottom() {
		final Rectangle bounds = getInnerBounds();
		return new Point(bounds.x + bounds.width, bounds.y + bounds.height);
	}

	public final Label getElementLabel() {
		return label;
	}

	public Label createElementLabel() {
		final String name = getName();
		if ((name != null) && !name.isEmpty()) {
			assert(label == null);
			label = new Label(name);
			initLabel(label);
			getParent().add(label, 0);
			return label;
		}
		return null;
	}

	protected void initLabel(Label label) {
		label.setCenterPosition(getElementCenter());
	}

	protected Dimension calcSizeByComponents() {
		int width = 10;
		int height = 10;
		java.awt.Rectangle rectangle;
		for (Component component : getComponents()) {
			rectangle = component.getBounds();
			if ((int)rectangle.getMaxX() > width) {
				width = (int)rectangle.getMaxX();
			}
			if ((int)rectangle.getMaxY() > height) {
				height = (int)rectangle.getMaxY();
			}
		}
		return new Dimension(width + 10, height + 10);
	}

	public void enableClickThrough() {
		addMouseListener(new MouseListener() {

			private final void dispatchEventToUnderlyingComponent(final MouseEvent event) {
				final Component sourceComponent = event.getComponent();
				Component targetComponent = null;
				final Container parent = getParent();
				final Point point = SwingUtilities.convertPoint(sourceComponent, event.getPoint(), parent);
				final int sourceComponentZOrder = parent.getComponentZOrder(sourceComponent); 
				for (Component component : parent.getComponents()) {
					if (!component.equals(sourceComponent)
							&& component.getBounds().contains(point)) {
						final int componentZOrder = parent.getComponentZOrder(component); 
						final int targetComponentZOrder = parent.getComponentZOrder(targetComponent); 
						if ((targetComponent == null)
								|| ((componentZOrder > sourceComponentZOrder)
								&& (componentZOrder < targetComponentZOrder))) {
							targetComponent = component;
						}
					}
				}
				if (targetComponent != null) {
					targetComponent.dispatchEvent(SwingUtilities.convertMouseEvent(event.getComponent(), event, targetComponent));
				}
			}

			@Override
			public void mouseReleased(MouseEvent event) {
				dispatchEventToUnderlyingComponent(event);
			}
			
			@Override
			public void mousePressed(MouseEvent event) {
				dispatchEventToUnderlyingComponent(event);
			}
			
			@Override
			public void mouseExited(MouseEvent event) {
				//dispatchEventToUnderlyingComponent(event);
			}
			
			@Override
			public void mouseEntered(MouseEvent event) {
				//dispatchEventToUnderlyingComponent(event);
			}
			
			@Override
			public void mouseClicked(MouseEvent event) {
				dispatchEventToUnderlyingComponent(event);
			}
		});
	}

}
