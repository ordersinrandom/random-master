package com.jbp.randommaster.gui.common.table.block;

import javax.swing.JTree;

public class TreeBlockTableModel extends BlockTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5284385268768369208L;
	@SuppressWarnings("unused")
	private JTree renderingTree;

	public TreeBlockTableModel(TreeBlock root) {
		this.renderingTree = new JTree();
	}

}