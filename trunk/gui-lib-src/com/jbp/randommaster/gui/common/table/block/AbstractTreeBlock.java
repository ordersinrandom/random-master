package com.jbp.randommaster.gui.common.table.block;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public abstract class AbstractTreeBlock extends AbstractBlock implements
		TreeBlock {

	@SuppressWarnings("rawtypes")
	protected LinkedHashSet listeners;
	@SuppressWarnings("rawtypes")
	protected LinkedList children;
	protected TreeBlock parent;
	protected Object value;

	@SuppressWarnings("rawtypes")
	public AbstractTreeBlock() {
		super();
		children = new LinkedList();
		listeners = new LinkedHashSet();
	}

	public TreeBlock[] getChildren() {
		return (TreeBlock[]) children.toArray();
	}

	public TreeBlock getChildAt(int index) {
		return (TreeBlock) children.get(index);
	}

	@SuppressWarnings("unchecked")
	public TreeBlock setChildAt(int index, TreeBlock newChild) {
		if (!children.contains(newChild)) {

			TreeBlock old = (TreeBlock) children.get(index);
			fireTreeNodeRemoved(old);

			old.setParent(null);
			children.set(index, newChild);
			newChild.setParent(this);
			fireTreeNodeAdded(newChild);

			return old;
		} else
			return null;
	}

	public TreeBlock getParent() {
		return parent;
	}

	public void setParent(TreeBlock p) {
		parent = p;
	}

	@SuppressWarnings("unchecked")
	public void appendChild(TreeBlock c) {
		if (!children.contains(c)) {
			c.setParent(this);
			children.add(c);

			fireTreeNodeAdded(c);
		}
	}

	public TreeBlock removeChild(TreeBlock c) {
		if (children.contains(c)) {

			fireTreeNodeRemoved(c);

			c.setParent(null);
			children.remove(c);
			return c;
		} else
			return null;
	}

	public int getChildIndex(TreeBlock child) {
		return children.indexOf(child);
	}

	public Object getTreeNodeValue() {
		return value;
	}

	public void setTreeNodeValue(Object obj) {
		value = obj;
		fireTreeNodeValueChanged(this);
	}

	@SuppressWarnings("unchecked")
	public void addTreeBlockChangeListener(TreeBlockChangeListener l) {
		listeners.add(l);
	}

	public void removeTreeBlockChangeListener(TreeBlockChangeListener l) {
		listeners.remove(l);
	}

	@SuppressWarnings("rawtypes")
	private void fireTreeNodeValueChanged(TreeBlock b) {
		LinkedHashSet targets = (LinkedHashSet) listeners.clone();
		TreeBlockChangeEvent e = new TreeBlockChangeEvent(this,
				TreeBlockChangeEvent.NODE_VALUE_CHANGED, b, b.getParent());

		for (Iterator it = targets.iterator(); it.hasNext();) {
			TreeBlockChangeListener l = (TreeBlockChangeListener) it.next();
			l.treeBlockNodeValueChanged(e);
		}
	}

	@SuppressWarnings("rawtypes")
	private void fireTreeNodeAdded(TreeBlock child) {
		LinkedHashSet targets = (LinkedHashSet) listeners.clone();
		TreeBlockChangeEvent e = new TreeBlockChangeEvent(this,
				TreeBlockChangeEvent.NODE_ADDED, child, child.getParent());

		for (Iterator it = targets.iterator(); it.hasNext();) {
			TreeBlockChangeListener l = (TreeBlockChangeListener) it.next();
			l.treeBlockNodeAdded(e);
		}
	}

	@SuppressWarnings("rawtypes")
	private void fireTreeNodeRemoved(TreeBlock child) {
		LinkedHashSet targets = (LinkedHashSet) listeners.clone();
		TreeBlockChangeEvent e = new TreeBlockChangeEvent(this,
				TreeBlockChangeEvent.NODE_REMOVED, child, child.getParent());

		for (Iterator it = targets.iterator(); it.hasNext();) {
			TreeBlockChangeListener l = (TreeBlockChangeListener) it.next();
			l.treeBlockNodeRemoved(e);
		}
	}
}