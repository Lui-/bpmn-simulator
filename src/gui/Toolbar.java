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
package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bpmn.Model;
import bpmn.token.AnimationListener;
import bpmn.token.Animator;

public class Toolbar extends JToolBar implements AnimationListener {

	private static final long serialVersionUID = 1L;

	private static Icon iconOpen = loadIcon("open.png"); //$NON-NLS-1$

	private static Icon iconStart = loadIcon("start.png"); //$NON-NLS-1$;
	private static Icon iconReset = loadIcon("stop.png"); //$NON-NLS-1$;

	private static Icon iconPause = loadIcon("pause.png"); //$NON-NLS-1$
	private static Icon iconPlay = loadIcon("play.png"); //$NON-NLS-1$
	private static Icon iconStep = loadIcon("step.png"); //$NON-NLS-1$
	private static Icon iconSpeed = loadIcon("speed.png"); //$NON-NLS-1$
	private static Icon iconMessages = loadIcon("messages.png"); //$NON-NLS-1$
	private static Icon iconMessagesError = loadIcon("messagesError.png"); //$NON-NLS-1$

	private JButton buttonOpen = null;

	private StartButton buttonStart = null;
	private JButton buttonReset = null;

	private JButton buttonPauseContinue = null;
	private JButton buttonStep = null;

	private JLabel labelSpeed = null; 
	private SpeedSpinner spinnerSpeed = null; 

	private JButton buttonMessages = null;

	private Model model = null;

	protected static ImageIcon loadIcon(final String filename) {
		final URL url = Toolbar.class.getResource(filename);
		if (url != null) {
			return new ImageIcon(url);
		}
		return null;
	}

	public Toolbar() {
		super();

		create();
	}

	public void setModel(final Model model) {
		Animator animator = getAnimator();
		if (animator != null) {
			animator.removeAnimationListener(this);
		}
		this.model = model;
		buttonStart.setModel(model);
		animator = getAnimator();
		if (animator != null) {
			animator.addAnimationListener(this);
			animator.setSpeed(spinnerSpeed.getSpeedFactor());
		}
		updateControls();
		updateMessages();
	}

	protected Animator getAnimator() {
		return (model == null) ? null : model.getAnimator();
	}

	public JButton getOpenButton() {
		return buttonOpen;
	}

	protected void create() {

		buttonOpen = new JButton(iconOpen);
		buttonOpen.setToolTipText(Messages.getString("Toolbar.open")); //$NON-NLS-1$
		add(buttonOpen);

		addSeparator();

		buttonStart = new StartButton(iconStart);
		buttonStart.setToolTipText(Messages.getString("Toolbar.start")); //$NON-NLS-1$
		add(buttonStart);

		buttonReset = new JButton(iconReset);
		buttonReset.setToolTipText(Messages.getString("Toolbar.reset")); //$NON-NLS-1$
		buttonReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				getAnimator().reset();
			}
		});
		add(buttonReset);

		addSeparator(new Dimension(32, 32));

		buttonPauseContinue = new JButton();
		buttonPauseContinue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				final Animator animator = getAnimator();
				if (animator.isPaused()) {
					animator.play();
				} else {
					animator.pause();
				}
			}
		});
		add(buttonPauseContinue);

		buttonStep = new JButton(iconStep);
		buttonStep.setToolTipText(Messages.getString("Toolbar.step")); //$NON-NLS-1$
		buttonStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				getAnimator().step(3);
			}
		});
		add(buttonStep);

		addSeparator();

		labelSpeed = new JLabel(iconSpeed);
		labelSpeed.setToolTipText(Messages.getString("Toolbar.frameRate")); //$NON-NLS-1$
		labelSpeed.setLabelFor(spinnerSpeed);
		add(labelSpeed);

		add(Box.createHorizontalStrut(6));

		spinnerSpeed = new SpeedSpinner();
		spinnerSpeed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent event) {
				getAnimator().setSpeed(((SpeedSpinner)event.getSource()).getSpeedFactor());
			}
		});
		add(spinnerSpeed);

		add(Box.createHorizontalGlue());

		buttonMessages = new JButton();
		buttonMessages.setBorderPainted(false);
		buttonMessages.setOpaque(false);
		buttonMessages.setFocusable(false);
		buttonMessages.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				model.showMessages();
			}
		});
		add(buttonMessages);

		updateControls();
		updateMessages();
	}

	protected void updateMessages() {
		if (model == null) {
			buttonMessages.setVisible(false);
			buttonMessages.setToolTipText(null);
		} else {
			buttonMessages.setVisible(model.hasMessages());
			if (model.hasErrorMessages()) {
				buttonMessages.setIcon(iconMessagesError);
				buttonMessages.setToolTipText(Messages.getString("Toolbar.hintErrors")); //$NON-NLS-1$
			} else {
				buttonMessages.setIcon(iconMessages);
				buttonMessages.setToolTipText(Messages.getString("Toolbar.hintMessages")); //$NON-NLS-1$
			}
		}
	}

	protected void updateControls() {
		final Animator animator = getAnimator();

		final boolean isPaused = ((animator != null) && animator.isPaused());

		buttonStart.setEnabled(animator != null);
		buttonReset.setEnabled(animator != null);

		buttonPauseContinue.setEnabled(animator != null);
		buttonStep.setEnabled(isPaused);
		spinnerSpeed.setEnabled(animator != null);
		labelSpeed.setEnabled(animator != null);

		buttonPauseContinue.setIcon(isPaused ? iconPlay : iconPause);
		buttonPauseContinue.setToolTipText(isPaused ? Messages.getString("Toolbar.play") : Messages.getString("Toolbar.pause")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void animationPlay() {
		updateControls();
	}

	@Override
	public void animationPause() {
		updateControls();
	}

	@Override
	public void animationReset() {
		updateControls();
	}

}
