/*
 * Copyright (C) 2014 Stefan Schweitzer
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
package com.googlecode.bpmn_simulator.bpmn.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.element.visual.Waypoint;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNDiagram;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNEdge;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNLabel;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNPlane;
import com.googlecode.bpmn_simulator.bpmn.di.BPMNShape;
import com.googlecode.bpmn_simulator.bpmn.model.core.foundation.BaseElement;

public abstract class AbstractDIDefinition<E extends BPMNDiagram>
		extends AbstractBPMNDefinition<E> {

	private static final String BPMNDI = "http://www.omg.org/spec/BPMN/20100524/DI"; //$NON-NLS-1$

	private static final String DC = "http://www.omg.org/spec/DD/20100524/DC"; //$NON-NLS-1$

	private static final String DI = "http://www.omg.org/spec/DD/20100524/DI"; //$NON-NLS-1$

	private final Collection<E> diagrams = new ArrayList<E>();

	@Override
	public final Collection<E> getDiagrams() {
		return diagrams;
	}

	protected abstract BPMNShape createShapeFor(BaseElement element);

	protected abstract BPMNEdge createEdgeFor(BaseElement element);

	protected abstract BPMNPlane createPlaneFor(BaseElement element);

	protected abstract BPMNLabel createLabelFor(BaseElement element);

	protected abstract E createDiagram(String name);

	@Override
	protected boolean readElementsForDefinitionsElement(final Node node) {
		return super.readElementsForDefinitionsElement(node)
				|| readElementBPMNDiagram(node);
	}

	protected Bounds getBoundsAttribute(final Node node) {
		final int x = (int) getAttributeFloat(node, "x"); //$NON-NLS-1$
		final int y = (int) getAttributeFloat(node, "y"); //$NON-NLS-1$
		final int width = (int) getAttributeFloat(node, "width"); //$NON-NLS-1$
		final int height = (int) getAttributeFloat(node, "height"); //$NON-NLS-1$
		return new Bounds(x, y, width, height);
	}

	protected Waypoint getPointAttribute(final Node node) {
		final int x = (int) getAttributeFloat(node, "x"); //$NON-NLS-1$
		final int y = (int) getAttributeFloat(node, "y"); //$NON-NLS-1$
		return new Waypoint(x, y);
	}

	protected boolean getIsExpandedAttribute(final Node node) {
		return getAttributeBoolean(node, "isExpanded", true); //$NON-NLS-1$
	}

	protected boolean getIsHorizontalAttribute(final Node node) {
		return getAttributeBoolean(node, "isHorizontal", false); //$NON-NLS-1$
	}

	protected BaseElement getAttributeElement(final Node node, final String name) {
		return getElement(getAttributeString(node, name));
	}

	protected void showInvalidBPMNElement(final Node node) {
		notifyError(MessageFormat.format("Unknown BPMN element ''{0}''",
				getAttributeElement(node, "bpmnElement")), null);
	}

	protected BaseElement getBPMNElementAttribute(final Node node) {
		return getAttributeElement(node, "bpmnElement"); //$NON-NLS-1$
	}

	protected boolean readElementBPMNShape(final Node node, final BPMNDiagram diagram) {
		if (isElementNode(node, BPMNDI, "BPMNShape")) { //$NON-NLS-1$
			final BaseElement element = getBPMNElementAttribute(node);
			if (element != null) {
				final BPMNShape shape = createShapeFor(element);
				if (shape != null) {
					final NodeList childNodes = node.getChildNodes();
					for (int i = 0; i < childNodes.getLength(); ++i) {
						final Node childNode = childNodes.item(i);
						if (!readElementBounds(childNode, shape)
								&& !readElementLabel(childNode, diagram, element)) {
							showUnknowNode(childNode);
						}
					}
					diagram.add(shape);
				} else {
					notifyError(MessageFormat.format("can''t create shape for {0}", element), null);
				}
			} else {
				showInvalidBPMNElement(node);
			}
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementBPMNEdge(final Node node, final BPMNDiagram diagram) {
		if (isElementNode(node, BPMNDI, "BPMNEdge")) { //$NON-NLS-1$
			final BaseElement element = getBPMNElementAttribute(node);
			if (element != null) {
				final BPMNEdge edge = createEdgeFor(element);
				if (edge != null) {
					final NodeList childNodes = node.getChildNodes();
					for (int i = 0; i < childNodes.getLength(); ++i) {
						final Node childNode = childNodes.item(i);
						if (!readElementWaypoint(childNode, edge)
								&& !readElementLabel(childNode, diagram, element)) {
							showUnknowNode(childNode);
						}
					}
					
					diagram.add(edge);
				} else {
					notifyError(MessageFormat.format("can''t create shape for {0}", element), null);
				}
			} else {
				showInvalidBPMNElement(node);
			}
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementBPMNPlane(final Node node, final BPMNDiagram diagram) {
		if (isElementNode(node, BPMNDI, "BPMNPlane")) { //$NON-NLS-1$
			final BaseElement element = getBPMNElementAttribute(node);
			if (element != null) {
				final BPMNPlane plane = createPlaneFor(element);
				if (plane != null) {
					final NodeList childNodes = node.getChildNodes();
					for (int i = 0; i < childNodes.getLength(); ++i) {
						final Node childNode = childNodes.item(i);
						if (!readElementBPMNShape(childNode, diagram)
								&& !readElementBPMNEdge(childNode, diagram)) {
							showUnknowNode(childNode);
						}
					}
					diagram.setPlane(plane);
				} else {
					notifyError(MessageFormat.format("can''t create plane for {0}", element), null);
				}
			} else {
				showInvalidBPMNElement(node);
			}
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementBPMNDiagram(final Node node) {
		if (isElementNode(node, BPMNDI, "BPMNDiagram")) { //$NON-NLS-1$
			final String name = getNameAttribute(node);
			final E diagram = createDiagram(name);
			final NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); ++i) {
				final Node childNode = childNodes.item(i);
				if (!readElementBPMNPlane(childNode, diagram)) {
					showUnknowNode(childNode);
				}
			}
			diagrams.add(diagram);
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementBounds(final Node node,
			final BPMNShape shape) {
		if (isElementNode(node, DC, "Bounds")) { //$NON-NLS-1$
			shape.setElementBounds(getBoundsAttribute(node));
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementWaypoint(final Node node,
			final BPMNEdge edge) {
		if (isElementNode(node, DI, "waypoint")) { //$NON-NLS-1$
			edge.addElementWaypoint(getPointAttribute(node));
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementBounds(final Node node,
			final BPMNLabel label) {
		if (isElementNode(node, DC, "Bounds")) { //$NON-NLS-1$
			label.setBounds(getBoundsAttribute(node));
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementLabel(final Node node,
			final BPMNDiagram diagram, final BaseElement element) {
		if (isElementNode(node, BPMNDI, "BPMNLabel")) { //$NON-NLS-1$
			final BPMNLabel label = createLabelFor(element);
			if (label != null) {
				final NodeList childNodes = node.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); ++i) {
					final Node childNode = childNodes.item(i);
					if (!readElementBounds(childNode, label)) {
						showUnknowNode(childNode);
					}
				}
				diagram.add(label);
			} else {
				
			}
			return true;
		} else {
			return false;
		}
	}

}
