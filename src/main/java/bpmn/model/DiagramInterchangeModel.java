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
package bpmn.model;

import java.awt.Dimension;
import java.awt.Point;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JComponent;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bpmn.Messages;
import bpmn.Rectangle;
import bpmn.di.BPMNDiagram;
import bpmn.exception.StructureException;
import bpmn.model.collaboration.AbstractTitledFlowElement;
import bpmn.model.collaboration.Collaboration;
import bpmn.model.core.common.AbstractConnectingElement;
import bpmn.model.core.common.AbstractFlowElement;
import bpmn.model.core.common.Label;
import bpmn.model.process.activities.Process;
import bpmn.model.process.activities.Subprocess;

public class DiagramInterchangeModel
		extends AbstractBPMNModel {

	protected static final String BPMNDI = "http://www.omg.org/spec/BPMN/20100524/DI"; //$NON-NLS-1$
	protected static final String DC = "http://www.omg.org/spec/DD/20100524/DC"; //$NON-NLS-1$
	protected static final String DI = "http://www.omg.org/spec/DD/20100524/DI"; //$NON-NLS-1$

	private final Collection<BPMNDiagram> diagrams = new LinkedList<BPMNDiagram>();

	public DiagramInterchangeModel() {
		super();
	}

	public final Collection<BPMNDiagram> getDiagrams() {
		return diagrams;
	}

	@Override
	protected boolean readElementsForDefinitionsElement(final Node node) {
		return super.readElementsForDefinitionsElement(node)
				|| readElementBPMNDiagram(node);
	}

	protected Dimension getDimensionAttribute(final Node node) {
		final int width = (int)getAttributeFloat(node, "width"); //$NON-NLS-1$
		final int height = (int)getAttributeFloat(node, "height"); //$NON-NLS-1$
		return new Dimension(width, height);
	}

	protected Point getPointAttribute(final Node node) {
		return new Point(
				(int)getAttributeFloat(node, "x"), //$NON-NLS-1$
				(int)getAttributeFloat(node, "y")); //$NON-NLS-1$
	}

	protected Rectangle getRectangleAttribute(final Node node) {
		return new Rectangle(getPointAttribute(node), getDimensionAttribute(node));
	}

	protected boolean getIsExpandedAttribute(final Node node) {
		return getAttributeBoolean(node, "isExpanded", true); //$NON-NLS-1$
	}

	protected boolean getIsHorizontalAttribute(final Node node) {
		return getAttributeBoolean(node, "isHorizontal", false); //$NON-NLS-1$
	}

	protected <E extends AbstractFlowElement> E getBPMNElementAttribute(
			final Node node, final Class<E> type)
			throws StructureException {
		return getAttributeElement(node, "bpmnElement", type); //$NON-NLS-1$
	}

	private static boolean isValidPlaneElement(final AbstractFlowElement planeElement) {
		return (planeElement instanceof Process)
				|| (planeElement instanceof Collaboration);
	}

	protected boolean readElementBPMNShape(final Node node, final AbstractFlowElement plane)
			throws StructureException {
		if (isElementNode(node, BPMNDI, "BPMNShape")) { //$NON-NLS-1$
			AbstractFlowElement element = getBPMNElementAttribute(node, AbstractFlowElement.class);
			if (element != null) {
				if (element instanceof Subprocess) {
					final Subprocess expandedProcess = (Subprocess)element;
					if (!getIsExpandedAttribute(node)) {
						element = expandedProcess.createCollapsed();
					}
				}
				if (element instanceof AbstractTitledFlowElement) {
					final AbstractTitledFlowElement titledElementContainer = (AbstractTitledFlowElement)element;
					titledElementContainer.setHorizontal(getIsHorizontalAttribute(node));
				}

				plane.add(element, 0);

				final NodeList childNodes = node.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); ++i) {
					final Node childNode = childNodes.item(i);
					if (!readElementBounds(childNode, element)
							&& !readElementLabel(childNode, plane, element)) {
						showUnknowNode(childNode);
					}
				}

				element.initSubElements();
			}
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementBPMNEdge(final Node node, final AbstractFlowElement plane)
			throws StructureException {
		if (isElementNode(node, BPMNDI, "BPMNEdge")) { //$NON-NLS-1$
			final AbstractConnectingElement element = getBPMNElementAttribute(node, AbstractConnectingElement.class);
			if (element != null) {
				plane.add(element, 0);

				final NodeList childNodes = node.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); ++i) {
					final Node childNode = childNodes.item(i);
					if (!readElementWaypoint(childNode, element)
							&& !readElementLabel(childNode, plane, element)) {
						showUnknowNode(childNode);
					}
				}

				element.initSubElements();
			}
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementBPMNPlane(final Node node, final String name)
			throws StructureException {
		if (isElementNode(node, BPMNDI, "BPMNPlane")) { //$NON-NLS-1$
			final AbstractFlowElement planeElement = getBPMNElementAttribute(node, AbstractFlowElement.class);
			if (planeElement != null) {
				if (isValidPlaneElement(planeElement)) {
					final NodeList childNodes = node.getChildNodes();
					for (int i = 0; i < childNodes.getLength(); ++i) {
						final Node childNode = childNodes.item(i);
						try {
							if (!readElementBPMNShape(childNode, planeElement)
									&& !readElementBPMNEdge(childNode, planeElement)) {
								showUnknowNode(childNode);
							}
						} catch (StructureException exception) {
							notifyStructureExceptionListeners(exception);
						}
					}
					diagrams.add(new BPMNDiagram(planeElement, name));
				} else {
					final StructureException exception = new StructureException(planeElement,
						MessageFormat.format(
							Messages.getString("Protocol.invalidPlaneElement"), //$NON-NLS-1$
							planeElement));
					notifyStructureExceptionListeners(exception);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementBPMNDiagram(final Node node) {
		if (isElementNode(node, BPMNDI, "BPMNDiagram")) { //$NON-NLS-1$
			final String name = getNameAttribute(node);
			final NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); ++i) {
				final Node childNode = childNodes.item(i);
				try {
					if (!readElementBPMNPlane(childNode, name)) {
						showUnknowNode(childNode);
					}
				} catch (StructureException exception) {
					notifyStructureExceptionListeners(exception);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementBounds(final Node node,
			final AbstractFlowElement element) {
		if (isElementNode(node, DC, "Bounds")) { //$NON-NLS-1$
			element.setInnerBounds(getRectangleAttribute(node));
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementWaypoint(final Node node,
			final AbstractConnectingElement element) {
		if (isElementNode(node, DI, "waypoint")) { //$NON-NLS-1$
			element.addWaypoint(getPointAttribute(node));
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementBounds(final Node node,
			final Label label) {
		if (isElementNode(node, DC, "Bounds")) { //$NON-NLS-1$
			label.setBounds(getRectangleAttribute(node));
			return true;
		} else {
			return false;
		}
	}

	protected boolean readElementLabel(final Node node,
			final JComponent planeElement, final AbstractFlowElement element) {
		if (isElementNode(node, BPMNDI, "BPMNLabel")) { //$NON-NLS-1$
			final Label label = element.getElementLabel();
			if (label != null) {
				final NodeList childNodes = node.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); ++i) {
					final Node childNode = childNodes.item(i);
					if (!readElementBounds(childNode, label)) {
						showUnknowNode(childNode);
					}
				}
				planeElement.add(label, 0);
			}
			return true;
		} else {
			return false;
		}
	}

}
