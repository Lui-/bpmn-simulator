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
package com.google.code.bpmn_simulator.gui.instances;

import java.awt.Component;
import java.text.MessageFormat;
import java.util.Enumeration;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import com.google.code.bpmn_simulator.framework.instance.Instance;
import com.google.code.bpmn_simulator.framework.instance.InstanceListener;
import com.google.code.bpmn_simulator.framework.instance.InstanceManager;
import com.google.code.bpmn_simulator.framework.token.Token;
import com.google.code.bpmn_simulator.framework.token.TokenFlow;
import com.google.code.bpmn_simulator.framework.token.TokenListener;


@SuppressWarnings("serial")
public class InstancesTree
		extends JTree
		implements InstanceListener, TokenListener {

	private static final String ROOT_NODE_TITLE = "Instances";

	private InstanceManager instanceManager;

	public InstancesTree() {
		super(new DefaultMutableTreeNode(ROOT_NODE_TITLE));
		setRootVisible(false);
		setCellRenderer(new InstancesTreeCellRenderer());
	}

	public void setInstanceManager(final InstanceManager instanceManager) {
		if (this.instanceManager != null) {
			this.instanceManager.removeInstanceListener(this);
		}
		this.instanceManager = instanceManager;
		clear();
		if (this.instanceManager != null) {
			this.instanceManager.addInstanceListener(this);
		}
	}

	private DefaultTreeModel getDefaultModel() {
		return (DefaultTreeModel)getModel();
	}

	private DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode)getModel().getRoot();
	}

	public void clear() {
		getRoot().removeAllChildren();
		getDefaultModel().reload();
	}

	private DefaultMutableTreeNode getNodeByUserObject(final Object userObject) {
		final DefaultMutableTreeNode rootNode = getRoot();
		if (userObject == null) {
			return rootNode;
		} else {
			final Enumeration<?> enumeration = rootNode.breadthFirstEnumeration();
			while (enumeration.hasMoreElements()) {
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode)enumeration.nextElement();
				if (userObject.equals(node.getUserObject())) {
					return node;
				}
			}
			return null;
		}
	}

	private void addAndExpandNode(final DefaultMutableTreeNode parentNode,
			final Object userData) {
		assert parentNode != null;
		if (parentNode != null) {
			assert userData != null;
			final MutableTreeNode node = new DefaultMutableTreeNode(userData);
			getDefaultModel().insertNodeInto(node, parentNode, parentNode.getChildCount());
			expandPath(new TreePath(parentNode.getPath()));
		}
	}

	private void removeNode(final Object userData) {
		final DefaultMutableTreeNode node = getNodeByUserObject(userData);
		assert node != null;
		if (node != null) {
			assert node.getChildCount() == 0;
			getDefaultModel().removeNodeFromParent(node);
		}
	}

	@Override
	public void instanceAdded(final Instance instance) {
		instance.addInstanceListener(this);
		instance.addTokenListener(this);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				addAndExpandNode(getNodeByUserObject(instance.getParent()), instance);
			}
		});
	}

	@Override
	public void instanceRemoved(final Instance instance) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				removeNode(instance);
			}
		});
	}

	@Override
	public void tokenAdded(final Token token) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				addAndExpandNode(getNodeByUserObject(token.getInstance()), token);
			}
		});
	}

	@Override
	public void tokenRemoved(final Token token) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				removeNode(token);
			}
		});
	}

	@Override
	public void tokenMoved(final Token token) {
	}

	private static class InstancesTreeCellRenderer
			extends DefaultTreeCellRenderer {

		private static void createInstanceComponent(final JLabel component, final Instance instance) {
			component.setText(MessageFormat.format("Instance of {0} ({1} token)",
					instance.getActivity().getFullName(),
					instance.getTokenCount(false)));
			component.setOpaque(true);
			component.setBackground(instance.getColor());
		}

		private static void createTokenComponent(final JLabel component, final Token token) {
			final TokenFlow tokenFlow = token.getCurrentFlow();
			component.setText(MessageFormat.format("Token in {0}",
					(tokenFlow == null) ? tokenFlow : tokenFlow.getFullName()));
			final Instance instance = token.getInstance();
			if (instance != null) {
				component.setOpaque(true);
				component.setBackground(instance.getColor());
			}
		}

		@Override
		public Component getTreeCellRendererComponent(final JTree tree,
				final Object value, final boolean sel, final boolean expanded,
				final boolean leaf, final int row, final boolean hasFocus) {
			final Component component = super.getTreeCellRendererComponent(tree,
					value, sel, expanded, leaf, row, hasFocus);
			if (value instanceof DefaultMutableTreeNode) {
				final JLabel label = (JLabel)component;
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
				final Object userObject = node.getUserObject();
				if (userObject instanceof Instance) {
					createInstanceComponent(label, (Instance)userObject);
				} else if (userObject instanceof Token) {
					createTokenComponent(label, (Token)userObject);
				} else {
					if (!userObject.equals(ROOT_NODE_TITLE)) {
						assert false;
					}
				}
			} else {
				assert false;
			}
			return component;
		}

	}

}
