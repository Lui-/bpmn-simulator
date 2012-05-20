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
package bpmn.element.event;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.Icon;

import bpmn.token.Instance;
import bpmn.token.Token;

@SuppressWarnings("serial")
public final class IntermediateCatchEvent extends IntermediateEvent
	implements CatchEvent, MouseListener {

	public IntermediateCatchEvent(final String id, final String name) {
		super(id, name);
		addMouseListener(this);
	}

	private boolean isInteractive() {
		return isTimer() || isMessage();
	}

	protected void updateCursor() {
		if (isInteractive()) {
			setCursor(
					getInnerTokens().isEmpty()
							? Cursor.getDefaultCursor()
							: Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}

	@Override
	public void happen(final Instance instance) {
		passFirstTokenToAllOutgoing();
	}

	@Override
	public void tokenEnter(final Token token) {
		super.tokenEnter(token);
		updateCursor();
	}

	@Override
	public void tokenExit(final Token token) {
		super.tokenExit(token);
		updateCursor();
	}

	protected void passFirstTokenToAllOutgoing() {
		final Iterator<Token> iterator = getInnerTokens().iterator();
		if (iterator.hasNext()) {
			final Token firstToken = iterator.next();
			passTokenToAllOutgoing(firstToken);
			firstToken.remove();
		}
	}

	@Override
	protected boolean canForwardToken(final Token token) {
		if (isInteractive()) {
			return false;
		} else {
			return super.canForwardToken(token);
		}
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		if (isInteractive()) {
			happen(null);
		}
	}

	@Override
	public void mousePressed(final MouseEvent e) {
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	}

	@Override
	protected Icon getTypeIcon() {
		return getDefinition().getIcon(getVisualization(), false);
	}

}
