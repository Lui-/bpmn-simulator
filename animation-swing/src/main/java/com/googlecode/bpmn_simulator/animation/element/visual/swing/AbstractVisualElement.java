package com.googlecode.bpmn_simulator.animation.element.visual.swing;

import javax.swing.JComponent;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalElement;
import com.googlecode.bpmn_simulator.animation.element.visual.VisualElement;

@SuppressWarnings("serial")
abstract class AbstractVisualElement<E extends LogicalElement>
		extends JComponent
		implements VisualElement {

	private final E logicalElement;

	private static final PresentationDrawer presentationDrawer = new PresentationDrawer();

	public AbstractVisualElement(final E element) {
		super();
		logicalElement = element;
		logicalElement.addTokenFlowListener(this);
	}

	public E getLogicalElement() {
		return logicalElement;
	}

	public PresentationDrawer getPresentationDrawer() {
		return presentationDrawer;
	}

}
