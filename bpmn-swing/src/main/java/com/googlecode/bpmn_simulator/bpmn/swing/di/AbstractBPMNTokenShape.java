/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.googlecode.bpmn_simulator.bpmn.swing.di;

import java.awt.Graphics2D;

import com.googlecode.bpmn_simulator.animation.element.logical.LogicalFlowElement;
import com.googlecode.bpmn_simulator.animation.element.visual.Bounds;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.animation.token.TokenFlowListener;
import com.googlecode.bpmn_simulator.animation.token.Tokens;

@SuppressWarnings("serial")
public abstract class AbstractBPMNTokenShape<E extends LogicalFlowElement>
		extends AbstractBPMNShape<E>
		implements TokenFlowListener {

	public AbstractBPMNTokenShape(final E element) {
		super(element);
		element.addTokenFlowListener(this);
	}

	@Override
	public void tokenChanged(final Token token) {
		repaint();
	}

	@Override
	protected void paintElement(final Graphics2D g) {
		super.paintElement(g);
		paintTokens(g);
	}

	protected void paintTokens(final Graphics2D g) {
		final Tokens tokens = getLogicalElement().getTokens();
		final Bounds innerBounds = getInnerBoundsRelative();
		getPresentation().drawTokens(g, tokens, innerBounds.getMinX(), innerBounds.getMinY());
	}

}
