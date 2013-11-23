package com.jbp.randommaster.gui.common.table.block;

import java.util.EventObject;
import java.awt.Dimension;

/**
 * 
 * <code>BlockChangeEvent</code> is the event object sent to notify the changes
 * in either the block data, or the block dimension.
 * 
 * @author plchung
 * 
 */
public class BlockChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6317632829940383628L;

	public static final int DATA_CHANGED = 0;
	public static final int SIZE_CHANGED = 1;

	private int type;

	// for new size.
	private Dimension newSize;

	// for data change range.
	private int startX;
	private int endX;
	private int startY;
	private int endY;

	public BlockChangeEvent(Block src) {
		super(src);
	}

	public BlockChangeEvent(Block src, int type) {
		this(src);
		this.setType(type);
	}

	public BlockChangeEvent(Block src, Dimension newSize) {
		this(src, SIZE_CHANGED);
		this.newSize = newSize;
	}

	public BlockChangeEvent(Block src, int startX, int endX, int startY,
			int endY) {
		this(src, DATA_CHANGED);
		this.startX = startX;
		this.endX = endX;
		this.startY = startY;
		this.endY = endY;
	}

	public Block getBlock() {
		return (Block) super.getSource();
	}

	public Dimension getNewSize() {
		return newSize;
	}

	public void setNewSize(Dimension d) {
		newSize = d;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int s) {
		startX = s;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int s) {
		startY = s;
	}

	public int getEndX() {
		return endX;
	}

	public void setEndX(int s) {
		endX = s;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndY(int s) {
		endY = s;
	}

	public int getType() {
		return type;
	}

	public void setType(int t) {
		type = t;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("BlockChangeEvent { startX=");
		buf.append(startX);
		buf.append(", endX=");
		buf.append(endX);
		buf.append(", startY=");
		buf.append(startY);
		buf.append(", endY=");
		buf.append(endY);
		buf.append(", newSize=");
		buf.append(newSize);
		buf.append(" }");
		return buf.toString();
	}
}