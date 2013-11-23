package com.jbp.randommaster.gui.common.desktop;

import javax.swing.plaf.*;
import javax.swing.*;
import java.awt.*;

/**
 * This class provides an empty DesktopIconUI which draws nothing
 * when an internal frame is iconified. 
 */

public class EmptyDesktopIconUI extends DesktopIconUI {

  /**
    * stores the instance of this class. 
    */
  protected static EmptyDesktopIconUI desktopIconUI;


  /**
    * creates the EmptyDesktopIconUI object
    *
    * @param c the reference to the JComponent object required by createUI
    */
  public static ComponentUI createUI (JComponent c) {
    if (desktopIconUI == null) {
      desktopIconUI = new EmptyDesktopIconUI();
    }
    return desktopIconUI;
  }


  /**
    * overrides the paint method with a blank routine so that no 
    * component is displayed when an internal frame is iconified
    *
    * @param g the reference to the Graphics object used to paint the desktop
    */
  protected void paint(Graphics g) {}

}
