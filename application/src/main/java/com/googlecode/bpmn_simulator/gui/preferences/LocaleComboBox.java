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
package com.googlecode.bpmn_simulator.gui.preferences;

import java.awt.Component;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

import com.googlecode.bpmn_simulator.gui.Messages;

@SuppressWarnings("serial")
public class LocaleComboBox
		extends JComboBox<Locale> {

	private static final Locale[] LOCALES = {
			Locale.ENGLISH,
			Locale.GERMAN,
	};

	public LocaleComboBox() {
		super(new DefaultComboBoxModel<Locale>(LOCALES));
		setRenderer(new LocaleComboBoxRenderer());
	}

	private static class LocaleComboBoxRenderer
			extends DefaultListCellRenderer {

		private static String getDefaultText() {
			final StringBuilder string =
					new StringBuilder(Messages.getString("Locale.default")); //$NON-NLS-1$
			string.append(" ("); //$NON-NLS-1$
			string.append(Locale.getDefault().getDisplayName());
			string.append(')');
			return string.toString();
		}

		@Override
		public Component getListCellRendererComponent(final JList<?> list,
				final Object value, final int index, final boolean isSelected,
				final boolean cellHasFocus) {
			final Component component =
					super.getListCellRendererComponent(list, value, index,
							isSelected, cellHasFocus);
			if (value == null) {
				setText(getDefaultText());
			} else {
				final Locale locale = (Locale)value;
				setText(locale.getDisplayName());
			}
			return component;
		}

	}

}
