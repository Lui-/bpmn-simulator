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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

import bpmn.element.BaseElement;
import bpmn.element.VisualConfig;

public class PreferencesDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final int GAP = 10;

	private final LocaleComboBox selectLanguage = new LocaleComboBox();

	private final JCheckBox checkShowExclusiveSymbol
			= new JCheckBox(Messages.getString("Preferences.showSymbolInExclusiveGateway"));  //$NON-NLS-1$
	private final JCheckBox checkAntialiasing
			= new JCheckBox(Messages.getString("Preferences.enableAntialiasing"));  //$NON-NLS-1$

	private final JCheckBox checkIgnoreModelerColors
			= new JCheckBox(Messages.getString("Preferences.ignoreModelerColors")); //$NON-NLS-1$

	private final ColorSelector colorStartEventBackground
			= new ColorSelector(Messages.getString("Preferences.backgroundColor")); //$NON-NLS-1$
	private final ColorSelector colorIntermediateEventBackground
			= new ColorSelector(Messages.getString("Preferences.backgroundColor")); //$NON-NLS-1$
	private final ColorSelector colorEndEventBackground
			= new ColorSelector(Messages.getString("Preferences.backgroundColor")); //$NON-NLS-1$
	private final ColorSelector colorGatewayBackground
			= new ColorSelector(Messages.getString("Preferences.backgroundColor")); //$NON-NLS-1$
	private final ColorSelector colorTaskBackground
			= new ColorSelector(Messages.getString("Preferences.backgroundColor")); //$NON-NLS-1$
	private final ColorSelector colorProcessBackground
			= new ColorSelector(Messages.getString("Preferences.backgroundColor")); //$NON-NLS-1$

	public PreferencesDialog() {
		super((Frame)null, Messages.getString("Preferences.preferences"), true); //$NON-NLS-1$

		create();

		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		load();

		pack();
	}

	private static void setButtonWidth(final JButton button) {
		setButtonWidth(button, 100);
	}

	private static void setButtonWidth(final JButton button, final int width) {
		final Dimension dimension = button.getPreferredSize();
		dimension.width = width;
		button.setPreferredSize(dimension);
	}

	protected void create() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(createPreferencesPane(), BorderLayout.CENTER);
		getContentPane().add(createButtonPanel(), BorderLayout.PAGE_END);
	}

	protected JTabbedPane createPreferencesPane() {
		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab(Messages.getString("Preferences.general"), createGeneralPanel()); //$NON-NLS-1$
		tabbedPane.addTab(Messages.getString("Preferences.display"), createDisplayPanel()); //$NON-NLS-1$
		tabbedPane.addTab(Messages.getString("Preferences.elementDefaults"), createElementsPanel()); //$NON-NLS-1$
		return tabbedPane;
	}

	private static Border createGapBorder() {
		return BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP);
	}

	protected JPanel createGeneralPanel() {
		final JPanel generalPanel = new JPanel(new BorderLayout());
		generalPanel.setBorder(createGapBorder());

		final JPanel panel = new JPanel(new GridLayout(0, 2, GAP, GAP));
		final StringBuilder textLanguage = new StringBuilder(Messages.getString("Preferences.language")); //$NON-NLS-1$
		textLanguage.append(": *"); //$NON-NLS-1$
		final JLabel labelLanguage = new JLabel(textLanguage.toString());
		labelLanguage.setLabelFor(selectLanguage);
		panel.add(labelLanguage);
		panel.add(selectLanguage);

		generalPanel.add(panel, BorderLayout.PAGE_START);
		final StringBuilder textRestart = new StringBuilder("* "); //$NON-NLS-1$
		textRestart.append(Messages.getString("Preferences.requiresRestart")); //$NON-NLS-1$
		generalPanel.add(new JLabel(textRestart.toString()), BorderLayout.PAGE_END);
		return generalPanel;
	}

	protected JPanel createDisplayPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(createGapBorder());

		panel.add(checkShowExclusiveSymbol);
		panel.add(checkAntialiasing);

		return panel;
	}

	protected JPanel createElementsPanel() {
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(createGapBorder());

		checkIgnoreModelerColors.setBorder(BorderFactory.createEmptyBorder(0, 0, GAP, 0));
		panel.add(checkIgnoreModelerColors, BorderLayout.PAGE_START);
		panel.add(createElementsDefaultsPanel(), BorderLayout.CENTER);

		return panel;
	}

	protected JPanel createElementsDefaultsPanel() {
		final JPanel panel = new JPanel(new GridLayout(0, 2, GAP, GAP));

		panel.add(new JLabel(Messages.getString("Preferences.startEvent"))); //$NON-NLS-1$
		panel.add(colorStartEventBackground);

		panel.add(new JLabel(Messages.getString("Preferences.intermediateEvent"))); //$NON-NLS-1$
		panel.add(colorIntermediateEventBackground);

		panel.add(new JLabel(Messages.getString("Preferences.endEvent"))); //$NON-NLS-1$
		panel.add(colorEndEventBackground);

		panel.add(new JLabel(Messages.getString("Preferences.gateway"))); //$NON-NLS-1$
		panel.add(colorGatewayBackground);

		panel.add(new JLabel(Messages.getString("Preferences.task"))); //$NON-NLS-1$
		panel.add(colorTaskBackground);

		panel.add(new JLabel(Messages.getString("Preferences.process"))); //$NON-NLS-1$
		panel.add(colorProcessBackground);

		return panel;
	}

	protected JPanel createButtonPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setBorder(createGapBorder());

		panel.add(Box.createHorizontalGlue());

		final JButton buttonCancel = new JButton(Messages.getString("Preferences.cancel")); //$NON-NLS-1$
		setButtonWidth(buttonCancel);
		buttonCancel.setMnemonic(KeyEvent.VK_C);
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				PreferencesDialog.this.dispose();
			}
		});
		panel.add(buttonCancel);

		panel.add(Box.createHorizontalStrut(10));

		final JButton buttonOk = new JButton(Messages.getString("Preferences.ok"));  //$NON-NLS-1$
		setButtonWidth(buttonOk);
		buttonOk.setMnemonic(KeyEvent.VK_O);
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				store();
				PreferencesDialog.this.dispose();
			}
		});
		panel.add(buttonOk);

		getRootPane().setDefaultButton(buttonOk);

		return panel;
	}

	protected void store() {
		final Config config = Config.getInstance(); 

		config.setLocale((Locale)selectLanguage.getSelectedItem());

		final VisualConfig visualConfig = BaseElement.getDefaultVisualConfig();
		visualConfig.setAntialiasing(checkAntialiasing.isSelected());
		visualConfig.setShowExclusiveGatewaySymbol(checkShowExclusiveSymbol.isSelected());
		visualConfig.setIgnoreColors(checkIgnoreModelerColors.isSelected());
		visualConfig.setBackground(VisualConfig.Element.EVENT_START, colorStartEventBackground.getSelectedColor());
		visualConfig.setBackground(VisualConfig.Element.EVENT_INTERMEDIATE, colorIntermediateEventBackground.getSelectedColor());
		visualConfig.setBackground(VisualConfig.Element.EVENT_END, colorEndEventBackground.getSelectedColor());
		visualConfig.setBackground(VisualConfig.Element.GATEWAY, colorGatewayBackground.getSelectedColor());
		visualConfig.setBackground(VisualConfig.Element.TASK, colorTaskBackground.getSelectedColor());
		visualConfig.setBackground(VisualConfig.Element.PROCESS, colorProcessBackground.getSelectedColor());

		config.setVisualConfig(visualConfig);
		config.store();
	}

	protected void load() {
		final Config config = Config.getInstance();

		selectLanguage.setSelectedItem(config.getLocale());

		final VisualConfig visualConfig = BaseElement.getDefaultVisualConfig();
		checkAntialiasing.setSelected(visualConfig.isAntialiasing());
		checkShowExclusiveSymbol.setSelected(visualConfig.getShowExclusiveGatewaySymbol());
		checkIgnoreModelerColors.setSelected(visualConfig.getIgnoreColors());
		colorStartEventBackground.setSelectedColor(visualConfig.getBackground(VisualConfig.Element.EVENT_START));
		colorIntermediateEventBackground.setSelectedColor(visualConfig.getBackground(VisualConfig.Element.EVENT_INTERMEDIATE));
		colorEndEventBackground.setSelectedColor(visualConfig.getBackground(VisualConfig.Element.EVENT_END));
		colorGatewayBackground.setSelectedColor(visualConfig.getBackground(VisualConfig.Element.GATEWAY));
		colorTaskBackground.setSelectedColor(visualConfig.getBackground(VisualConfig.Element.TASK));
		colorProcessBackground.setSelectedColor(visualConfig.getBackground(VisualConfig.Element.PROCESS));
	}

}
