package com.jbp.randommaster.gui.common.table.block.impl;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.jbp.randommaster.gui.common.table.block.TreeBlock;
import com.jbp.randommaster.gui.common.table.block.TreeBlockChangeEvent;
import com.jbp.randommaster.gui.common.table.block.TreeBlockChangeListener;

public class DefaultTreeBlockTreeModel implements TreeModel,
		TreeBlockChangeListener {

	private TreeBlock root;

	@SuppressWarnings("rawtypes")
	private LinkedHashSet listeners;

	@SuppressWarnings("rawtypes")
	public DefaultTreeBlockTreeModel(TreeBlock root) {
		if (root == null)
			throw new IllegalArgumentException("root tree block cannot be null");

		this.root = root;
		listeners = new LinkedHashSet();
		// register as listener.
		root.addTreeBlockChangeListener(this);
	}

	/**
	 * Implementation of <code>TreeModel</code>.
	 */
	@SuppressWarnings("unchecked")
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(l);
	}

	/**
	 * Implementation of <code>TreeModel</code>.
	 */
	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}

	/**
	 * Implementation of <code>TreeModel</code>.
	 */
	public Object getChild(Object parent, int index) {
		if (parent instanceof TreeBlock) {
			TreeBlock p = (TreeBlock) parent;
			return p.getChildAt(index);
		} else
			throw new IllegalArgumentException(
					"parent is not TreeBlock instance");
	}

	/**
	 * Implementation of <code>TreeModel</code>.
	 */
	public int getChildCount(Object parent) {
		if (parent instanceof TreeBlock) {
			TreeBlock[] b = ((TreeBlock) parent).getChildren();
			return b.length;
		} else
			throw new IllegalArgumentException(
					"parent is not TreeBlock instance");
	}

	/**
	 * Implementation of <code>TreeModel</code>.
	 */
	public int getIndexOfChild(Object parent, Object child) {
		if (parent instanceof TreeBlock) {
			TreeBlock p = (TreeBlock) parent;
			return p.getChildIndex((TreeBlock) child);
		} else
			throw new IllegalArgumentException(
					"parent is not TreeBlock instance");
	}

	/**
	 * Implementation of <code>TreeModel</code>.
	 */
	public Object getRoot() {
		return root;

	}

	/**
	 * Implementation of <code>TreeModel</code>.
	 */
	public boolean isLeaf(Object node) {
		if (node instanceof TreeBlock) {
			TreeBlock n = (TreeBlock) node;
			return n.getChildren().length == 0;
		} else
			throw new IllegalArgumentException(
					"parent is not TreeBlock instance");
	}

	/**
	 * Implementation of <code>TreeModel</code>. This method is invoked from
	 * tree editor.
	 */
	public void valueForPathChanged(TreePath path, Object newValue) {
		TreeBlock b = (TreeBlock) path.getLastPathComponent();
		b.setTreeNodeValue(newValue);
		TreeModelEvent evt = null;
		if (b == root) {
			evt = new TreeModelEvent(this, new TreePath(new Object[0]),
					new int[] { 0 }, new Object[] { b });
		} else {
			TreeBlock parent = b.getParent();
			evt = new TreeModelEvent(this, path.getParentPath(),
					new int[] { parent.getChildIndex(b) }, new Object[] { b });
		}

		fireNodesChanged(evt);
	}

	/**
	 * Implementation of <code>TreeBlockChangeListener</code>. Has to be called
	 * right after the node insertion.
	 */
	public void treeBlockNodeAdded(TreeBlockChangeEvent e) {
		TreeBlock b = e.getTargetNode();
		TreeBlock p = e.getParentNode();
		TreeModelEvent evt = new TreeModelEvent(this, getPathToRoot(p),
				new int[] { p.getChildIndex(b) }, new Object[] { b });
		fireNodesInserted(evt);
	}

	/**
	 * Implementation of <code>TreeBlockChangeListener</code>. Has to be called
	 * before the node removal.
	 */
	public void treeBlockNodeRemoved(TreeBlockChangeEvent e) {
		TreeBlock b = e.getTargetNode();
		TreeBlock p = e.getParentNode();
		TreeModelEvent evt = new TreeModelEvent(this, getPathToRoot(p),
				new int[] { p.getChildIndex(b) }, new Object[] { b });
		fireNodesRemoved(evt);
	}

	/**
	 * Implementation of <code>TreeBlockChangeListener</code>.
	 */
	public void treeBlockNodeValueChanged(TreeBlockChangeEvent e) {
		TreeBlock b = e.getTargetNode();
		TreePath pathToRoot = getPathToRoot(b);
		valueForPathChanged(pathToRoot, b.getTreeNodeValue());
	}

	/**
	 * Helper method to fire tree model event.
	 */
	@SuppressWarnings("rawtypes")
	private void fireNodesChanged(TreeModelEvent evt) {
		LinkedHashSet targets = (LinkedHashSet) listeners.clone();
		for (Iterator it = targets.iterator(); it.hasNext();) {
			TreeModelListener l = (TreeModelListener) it.next();
			l.treeNodesChanged(evt);
		}
	}

	/**
	 * Helper method to fire tree model event.
	 */
	@SuppressWarnings("rawtypes")
	private void fireNodesRemoved(TreeModelEvent evt) {
		LinkedHashSet targets = (LinkedHashSet) listeners.clone();
		for (Iterator it = targets.iterator(); it.hasNext();) {
			TreeModelListener l = (TreeModelListener) it.next();
			l.treeNodesRemoved(evt);
		}
	}

	/**
	 * Helper method to fire tree model event.
	 */
	@SuppressWarnings("rawtypes")
	private void fireNodesInserted(TreeModelEvent evt) {
		LinkedHashSet targets = (LinkedHashSet) listeners.clone();
		for (Iterator it = targets.iterator(); it.hasNext();) {
			TreeModelListener l = (TreeModelListener) it.next();
			l.treeNodesInserted(evt);
		}
	}

	/**
	 * Utility to find the tree path to root.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private TreePath getPathToRoot(TreeBlock b) {
		LinkedList list = new LinkedList();
		TreeBlock current = b;
		list.add(current);
		while (current.getParent() != null) {
			current = current.getParent();
			list.add(0, current);
		}
		return new TreePath(list.toArray());
	}
}