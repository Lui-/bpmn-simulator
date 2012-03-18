package bpmn.element;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;

import javax.swing.JCheckBox;

import bpmn.element.gateway.ExclusiveGateway;
import bpmn.element.gateway.Gateway;
import bpmn.element.gateway.InclusiveGateway;


public class SequenceFlow extends TokenConnectingElement {

	private static final long serialVersionUID = 1L;

	private static final Color COLOR_TRUE = new Color(0, 196, 0);
	private static final Color COLOR_FALSE = new Color(0, 0, 0);

	private class ConditionExpression extends JCheckBox {

		private static final long serialVersionUID = 1L;

		private boolean value = false;

		public ConditionExpression(final String text) {
			super((String)null);

			setToolTipText(text);
			setVerticalAlignment(TOP);
			setFocusable(false);
			setOpaque(false);
			setValue(false);

			addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent event) {
					setValue(event.getStateChange() == ItemEvent.SELECTED);
				}
			});
		}

		protected void setValue(final boolean value) {
			Label label = getElementLabel();
			if (label != null) {
				label.setForeground(value ? COLOR_TRUE : COLOR_FALSE);
			}
			synchronized (this) {
				this.value = value;
			}
		}

		public synchronized boolean getValue() {
			return value;
		}

	}

	private ConditionExpression conditionExpression = null;
	private String expression = null;

	public SequenceFlow(String id, String name, ElementRef<FlowElement> source,
			ElementRef<FlowElement> target) {
		super(id, name, source, target);
	}

	public void setConditionExpression(final String expression) {
		assert(expression != null);
		this.expression = expression;
	}

	protected String getConditionExpression() {
		return expression;
	}

	@Override
	public String getName() {
		final String name = super.getName(); 
		if ((name == null) || name.isEmpty()) {
			return getConditionExpression();
		}
		return name;
	}

	public boolean isConditional() {
		final String expression = getConditionExpression();
		final boolean hasExpression = ((expression != null) && !expression.isEmpty());
		return ((hasExpression || isSourceElementInclusiveOrExclusiveGatewayAndHasMoreThanOnceOutgoing()) && !isDefault());
	}

	public boolean acceptsToken() {
		return (!isConditional() || conditionExpression.getValue());
	}

	@Override
	public Label createElementLabel() {
		createExpressionControl();
		return super.createElementLabel();
	}

	protected void createExpressionControl() {
		if (isConditional()) {
			conditionExpression = new ConditionExpression(expression);
			add(conditionExpression, BorderLayout.CENTER);
			repositionConditionExpression();
		}
	}

	private void repositionConditionExpression() {
		assert(conditionExpression != null);
		if (getParent() != null) {
			final Point center = getElementCenter();
			if (center != null) {
				final Point position = waypointToRelative(center);
				final Dimension preferredSize = conditionExpression.getPreferredSize();
				conditionExpression.setBounds(
						position.x - (preferredSize.width / 2),
						position.y - (int)((preferredSize.height / 3.) * 2.),
						preferredSize.width, preferredSize.height);
			}
		}
	}

	@Override
	protected void updateBounds() {
		super.updateBounds();
		if (conditionExpression != null) {
			repositionConditionExpression();
		}
	}

	protected void drawDefaultSymbol(Graphics g, final Point orgin, final double a) {
		final double angle = (Math.PI / 1.5);
		final double length = 12.;

		Point x = waypointToRelative(getPosition(5));
		Point from = Graphics.polarToCartesian(x, length / 2., a + angle);
		Point to = Graphics.polarToCartesian(from, length, a + angle + Math.PI);

		g.drawLine(from, to);
	}

	public boolean isDefault() {
		final ElementRef<FlowElement> sourceRef = getSourceRef();
		if ((sourceRef != null) && sourceRef.hasElement()) {
			final FlowElement flowElement = sourceRef.getElement();
			if (flowElement instanceof ElementWithDefaultSequenceFlow) {
				ElementWithDefaultSequenceFlow element = (ElementWithDefaultSequenceFlow)flowElement;
				ElementRef<SequenceFlow> defaultElementFlowRef = element.getDefaultElementFlowRef();
				if (defaultElementFlowRef != null) {
					return defaultElementFlowRef.equals(this);
				}
			}
		}
		return false;  
	}

	protected boolean isSourceElementInclusiveOrExclusiveGatewayAndHasMoreThanOnceOutgoing() {
		final ElementRef<FlowElement> sourceRef = getSourceRef();
		if ((sourceRef != null) && sourceRef.hasElement()) {
			final FlowElement sourceElement = sourceRef.getElement();
			if ((sourceElement instanceof InclusiveGateway)
					|| (sourceElement instanceof ExclusiveGateway)) {
				final Gateway gateway = (Gateway)sourceElement;
				return (gateway.getOutgoing().size() > 1);
			}
		}
		return false;
	}

	@Override
	protected void paintElement(Graphics g) {
		super.paintElement(g);

		Iterator<Point> waypoints = getWaypoints().iterator();
		if (waypoints.hasNext()) {
			Point prevPoint = waypointToRelative(waypoints.next());
			if (waypoints.hasNext()) {
				// Startsymbol
				Point curPoint = waypointToRelative(waypoints.next());
				if (isDefault()) {
					g.drawDefaultSymbol(prevPoint, curPoint);
				} else if (!isSourceElementInclusiveOrExclusiveGatewayAndHasMoreThanOnceOutgoing() && isConditional()) {
					g.drawConditionalSymbol(prevPoint, curPoint);
				}
				// Endepfeil
				while (waypoints.hasNext()) {
					prevPoint = curPoint;
					curPoint = waypointToRelative(waypoints.next());
				}
				g.fillArrow(prevPoint, curPoint);
			}
		}
	}

}
