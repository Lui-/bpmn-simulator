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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

public class ScrollDesktop extends JScrollPane {

	private static final long serialVersionUID = 1L;

	public class ScrollDesktopPane extends JDesktopPane {

		private static final long serialVersionUID = 1L;

		private class ScrollDesktopManager extends DefaultDesktopManager {

			private static final long serialVersionUID = 1L;

			@Override
			public void dragFrame(final JComponent f, final int newX, final int newY) {
				super.dragFrame(f, newX, newY);
				resizeDesktop();
			}

			@Override
			public void resizeFrame(final JComponent f, final int newX, final int newY,
					final int newWidth, final int newHeight) {
				super.resizeFrame(f, newX, newY, newWidth, newHeight);
				resizeDesktop();
			}

		}

		public ScrollDesktopPane() {
			super();
			setDesktopManager(new ScrollDesktopManager());
			setBackground(Color.LIGHT_GRAY);
		}

		@Override
		public Component add(final Component comp) {
			final Component component = super.add(comp);
			resizeDesktop();
			return component;
		}

		protected ScrollDesktop getScrollDesktop() {
			return ScrollDesktop.this;
		}

		protected Dimension getPreferredDesktopSize() {
			int maxX = 0;
			int maxY = 0;
			for (JInternalFrame frame : getAllFrames()) {
				maxX = Math.max(maxX, (int)frame.getBounds().getMaxX());
				maxY = Math.max(maxY, (int)frame.getBounds().getMaxY());
			}
			return new Dimension(maxX, maxY);
		}

		protected void resizeDesktop() {
			setFixedSize(getPreferredDesktopSize());
		}

		public void arrangeFrames() {
			int x = 0;
			int y = 0;
			int maxHeight = 0;
			final Dimension desktopSize = getSize();
			for (JInternalFrame frame : getAllFrames()) {
				final Dimension frameSize = frame.getSize();
				final int width = (int)frameSize.getWidth();
				final int height = (int)frameSize.getHeight(); 
				frame.setLocation(x, y);
				if ((x + width) > desktopSize.getWidth()) {
					x = 0;
					y += maxHeight;
					maxHeight = height;
				} else {
					x += width;
					maxHeight = Math.max(maxHeight, height);
				}
			}
			resizeDesktop();
		}

		public void setFixedSize(final Dimension dimension) {
			setMinimumSize(dimension);
			setMaximumSize(dimension);
			setPreferredSize(dimension);
			getScrollDesktop().invalidate();
			getScrollDesktop().validate();
		}

	}

	private final ScrollDesktopPane desktopPane = new ScrollDesktopPane(); 

	public ScrollDesktop() {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getViewport().add(desktopPane);
	}

	public ScrollDesktopPane getDesktopPane() {
		return desktopPane;
	}

	public void arrangeFrames() {
		getDesktopPane().arrangeFrames();
	}

}
