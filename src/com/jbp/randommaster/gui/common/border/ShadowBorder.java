package com.jbp.randommaster.gui.common.border;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class ShadowBorder extends AbstractBorder
{
	private static final long serialVersionUID = -5954796672881190560L;
	
    protected int thickness;
    protected Color lineColor;
    protected boolean roundedCorners;

    public ShadowBorder() {
      this(new Color(120,120,120), 6, false);
    }

    /** 
     * Creates a line border with the specified color and a 
     * thickness = 1.
     * @param color the color for the border
     */
    public ShadowBorder(Color color) {
        this(color, 6, false);
    }

    /**
     * Creates a line border with the specified color and thickness.
     * @param color the color of the border
     * @param thickness the thickness of the border
     */
    public ShadowBorder(Color color, int thickness)  {
        this(color, thickness, false);
    }

    /**
     * Creates a line border with the specified color, thickness,
     * and corner shape.
     * @param color the color of the border
     * @param thickness the thickness of the border
     * @param roundedCorners whether or not border corners should be round
     * @since 1.3
     */
    public ShadowBorder(Color color, int thickness, boolean roundedCorners)  {
        lineColor = color;
        this.thickness = thickness;
  this.roundedCorners = roundedCorners;
    }

    /**
     * Paints the border for the specified component with the 
     * specified position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        
        int i;

        JComponent jc=(JComponent) c;
        jc.setOpaque(false);
        Graphics2D g2=(Graphics2D) g.create();

//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
  
        g2.setColor(lineColor);
        Color currentColor=lineColor;
        for(i = 0; i < thickness; i++)  {
          g2.setColor(currentColor);
          if(!roundedCorners)
            g2.drawRect(x+thickness+i, y+thickness+i, width-thickness-i-i-1, height-thickness-i-i-1);
          else
            g2.drawRoundRect(x+thickness+i, y+thickness+i, width-thickness-i-i-1, height-thickness-i-i-1, thickness, thickness);
          currentColor=currentColor.darker();
        }
        
        g2.dispose();

    }

    /**
     * Returns the insets of the border.
     * @param c the component for which this border insets value applies
     */
    public Insets getBorderInsets(Component c)       {
        return new Insets(0, 0, thickness, thickness);
    }

    /** 
     * Reinitialize the insets parameter with this Border's current Insets. 
     * @param c the component for which this border insets value applies
     * @param insets the object to be reinitialized
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = 0;
        insets.right = insets.bottom = thickness;
        return insets;
    }

    /**
     * Returns the color of the border.
     */
    public Color getLineColor()     {
        return lineColor;
    }

    /**
     * Returns the thickness of the border.
     */
    public int getThickness()       {
        return thickness;
    }

    /**
     * Returns whether this border will be drawn with rounded corners.
     */
    public boolean getRoundedCorners() {
        return roundedCorners;
    }

    /**
     * Returns whether or not the border is opaque.
     */
    public boolean isBorderOpaque() { 
//        return !roundedCorners;
return false; 
    }

}