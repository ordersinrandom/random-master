package com.jbp.randommaster.gui.common.splash;

import java.awt.Container;

/**
 * 
 * <code>SplashScreenRunnable</code> is the lengthy task
 * to be done when a splash screen showing on screen.
 * 
 * @author plchung
 *
 */
public interface SplashScreenRunnable extends Runnable {

  /**
   * Set the content pane showing on screen.<br/>
   * This method is invoked before the <code>void run()</code> method
   * being called.<br/>
   * Any modification performed on the content pane has to be 
   * invoke through the 
   * <code>SwingUtilities.invokeLater(Runnable)</code> method.
   * 
   * @param contentPane The GUI content showing on screen.
   */
  public void setSplashContentPane(Container contentPane);
  
  /**
   * Indicate the user has clicked on the splash screen and it is going to be
   * disposed.
   */
  public void userInterrupted();
}