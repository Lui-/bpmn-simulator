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
package com.googlecode.bpmn_simulator.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class AboutDialog
		extends AbstractDialog {

	public AboutDialog(final JFrame owner) {
		super(owner, Messages.getString("About.about")); //$NON-NLS-1$

		create();
	}

	@Override
	protected void create() {
		setLayout(new BorderLayout());

		getContentPane().add(createTabbedPane(), BorderLayout.CENTER);
		getContentPane().add(createActionPanel(), BorderLayout.PAGE_END);
	}

	protected JPanel createTabInfo() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(Box.createVerticalStrut(20));

		final StringBuilder applicationInfo =
				new StringBuilder(ApplicationInfo.getName());
		final String version = ApplicationInfo.getVersion();
		if (version != null) {
			applicationInfo.append(' ');
			applicationInfo.append(version);
		}
		final JLabel labelInfo = new JLabel(applicationInfo.toString());
		labelInfo.setFont(labelInfo.getFont().deriveFont(Font.BOLD, 20));
		labelInfo.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(labelInfo);

		panel.add(Box.createVerticalStrut(20));

		try {
			final Hyperlink hyperlink =
					new Hyperlink(new URI(ApplicationInfo.getUrl()));
			hyperlink.setAlignmentX(CENTER_ALIGNMENT);
			panel.add(hyperlink);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return panel;
	}

	protected JComponent createTabLicence() {
		final JTextArea textArea =
				new JTextArea(ApplicationInfo.getLicense());
		textArea.setEditable(false);
		textArea.setFont(new Font("Courier New", Font.PLAIN, 11)); //$NON-NLS-1$
		textArea.setMargin(new Insets(8, 8, 8, 8));
		final JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(null);
		return scrollPane;
	}

	protected JComponent createTabProperties() {
		final PropertiesTable propertiesTable = new PropertiesTable();
		propertiesTable.setProperties(System.getProperties());
		return new JScrollPane(propertiesTable);
	}

	protected JTabbedPane createTabbedPane() {
		final JTabbedPane pane = new JTabbedPane();
		pane.addTab(Messages.getString("About.info"), createTabInfo()); //$NON-NLS-1$
		pane.addTab(Messages.getString("About.licence"), createTabLicence()); //$NON-NLS-1$
		pane.addTab(Messages.getString("About.properties"), createTabProperties());
		return pane;
	}

	protected JPanel createActionPanel() {
		final JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

		final JButton buttonClose = new JButton(Messages.getString("close")); //$NON-NLS-1$
		buttonClose.setMnemonic(KeyEvent.VK_C);
		buttonClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				dispose();
			}
		});
		getRootPane().setDefaultButton(buttonClose);

		panel.add(buttonClose);

		return panel;
	}

}
