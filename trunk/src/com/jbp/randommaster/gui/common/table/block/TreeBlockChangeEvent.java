package com.jbp.randommaster.gui.common.table.block;

import java.util.EventObject;

public class TreeBlockChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -739726629410794411L;
	public static final int NODE_ADDED = 1;
	public static final int NODE_REMOVED = 2;
	public static final int NODE_VALUE_CHANGED = 3;

	private int type;
	private TreeBlock targetNode;
	private TreeBlock parentNode;

	public TreeBlockChangeEvent(Object src, int type, TreeBlock targetNode) {
		this(src, type, targetNode, null);
	}

	public TreeBlockChangeEvent(Object src, int type, TreeBlock targetNode,
			TreeBlock parentNode) {
		super(src);

		if (type != NODE_ADDED && type != NODE_REMOVED
				&& type != NODE_VALUE_CHANGED)
			throw new IllegalArgumentException("event type: " + type
					+ " not recognized");

		setType(type);
		setTargetNode(targetNode);
		setParentNode(parentNode);
	}

	public void setParentNode(TreeBlock parentNode) {
		this.parentNode = parentNode;
	}

	public TreeBlock getParentNode() {
		return parentNode;
	}

	public void setTargetNode(TreeBlock targetNode) {
		this.targetNode = targetNode;
	}

	public TreeBlock getTargetNode() {
		return targetNode;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("TreeBlockChangeEvent { parentNode=");
		buf.append(parentNode);
		buf.append(", targetNode=");
		buf.append(targetNode);
		buf.append(", type=");
		buf.append(type);
		buf.append(" }");
		return buf.toString();
	}
}