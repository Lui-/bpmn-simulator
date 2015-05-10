/*
 * Copyright (C) 2015 Stefan Schweitzer
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
package com.googlecode.bpmn_simulator.gui.instances;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.googlecode.bpmn_simulator.animation.token.Instance;
import com.googlecode.bpmn_simulator.animation.token.InstancesListener;
import com.googlecode.bpmn_simulator.animation.token.RootInstances;
import com.googlecode.bpmn_simulator.animation.token.Token;
import com.googlecode.bpmn_simulator.animation.token.TokenFlow;
import com.googlecode.bpmn_simulator.animation.token.TokensListener;

@SuppressWarnings("serial")
public class InstancesTree
		extends JTree
		implements InstancesListener {

	private RootInstances instances;

	public InstancesTree() {
		super(new DefaultMutableTreeNode(null));
		setRootVisible(false);
		setEditable(false);
		setCellRenderer(new CellRenderer());
	}

	public void setInstances(final RootInstances instances) {
		if (this.instances != null) {
			this.instances.removeInstancesListener(this);
		}
		this.instances = instances;
		clear();
		if (this.instances != null) {
			this.instances.addInstancesListener(this);
		}
	}

	private DefaultTreeModel getDefaultModel() {
		return (DefaultTreeModel) getModel();
	}

	private DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode) getModel().getRoot();
	}

	public void clear() {
		getRoot().removeAllChildren();
		getDefaultModel().reload();
	}

	private void insertNode(final DefaultMutableTreeNode parentNode,
			final DefaultMutableTreeNode node) {
		if (parentNode != null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					getDefaultModel().insertNodeInto(node, parentNode, parentNode.getChildCount());
					expandPath(new TreePath(node.getPath()));
				}
			});
		}
	}

	private static DefaultMutableTreeNode findChildNodeByUserData(final DefaultMutableTreeNode parentNode,
			final Object userData) {
		if (parentNode != null) {
			final int childCount = parentNode.getChildCount();
			for (int childIndex = 0; childIndex < childCount; ++childIndex) {
				final DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(childIndex);
				if (userData.equals(childNode.getUserObject())) {
					return childNode;
				}
			}
		}
		return null;
	}

	private void removeNode(final DefaultMutableTreeNode parentNode, final Object userData) {
		final DefaultMutableTreeNode node = findChildNodeByUserData(parentNode, userData);
		if (node != null) {
			assert node.getChildCount() == 0;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					getDefaultModel().removeNodeFromParent(node);
				}
			});
		}
	}

	@Override
	public void instanceAdded(final Instance instance) {
		insertNode(getRoot(), new InstanceNode(instance));
	}

	@Override
	public void instanceRemoved(final Instance instance) {
		removeNode(getRoot(), instance);
	}

	private static class CellRenderer
			extends DefaultTreeCellRenderer {

		public CellRenderer() {
			super();
		}

		@Override
		public Component getTreeCellRendererComponent(final JTree tree, final Object value,
				final boolean sel, final boolean expanded, final boolean leaf, final int row,
				final boolean hasFocus) {
			final String text;
			if (value instanceof InstanceNode) {
				final Instance instance = ((InstanceNode) value).getInstance();
				text = "Instance";
				setOpenIcon(UIManager.getIcon("Tree.expandedIcon"));
				setClosedIcon(UIManager.getIcon("Tree.collapsedIcon"));
				setLeafIcon(null);
				setFont(getFont().deriveFont(Font.PLAIN));
				setTextNonSelectionColor(new Color(instance.getColor()));
			} else if (value instanceof TokenNode) {
				final Token token = ((TokenNode) value).getToken();
				final TokenFlow tokenFlow = token.getCurrentTokenFlow();
				if (tokenFlow == null) {
					text = "Token";
				} else {
					text = tokenFlow.toString();
				}
				setOpenIcon(null);
				setClosedIcon(null);
				setLeafIcon(UIManager.getIcon("Tree.leafIcon"));
				setFont(getFont().deriveFont(Font.BOLD));
			} else {
				text = "";
			}
			super.getTreeCellRendererComponent(tree, text, sel, expanded, leaf, row, hasFocus);
			return this;
		}

	}

	private class InstanceNode
			extends DefaultMutableTreeNode
			implements InstancesListener, TokensListener {

		public InstanceNode(final Instance instance) {
			super(instance);
			instance.addInstancesListener(this);
			instance.addTokensListener(this);
		}

		public Instance getInstance() {
			return (Instance) getUserObject();
		}

		@Override
		public void instanceAdded(final Instance instance) {
			getDefaultModel().insertNodeInto(new InstanceNode(instance), this, getChildCount());
		}

		@Override
		public void instanceRemoved(final Instance instance) {
			removeNode(this, instance);
		}

		@Override
		public void tokenAdded(final Token token) {
			getDefaultModel().insertNodeInto(new TokenNode(token), this, getChildCount());
		}

		@Override
		public void tokenRemoved(final Token token) {
			removeNode(this, token);
		}

	}

	private class TokenNode
			extends DefaultMutableTreeNode {

		private TokenNode(final Token token) {
			super(token, false);
		}

		public Token getToken() {
			return (Token) getUserObject();
		}

	}

}
