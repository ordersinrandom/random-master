package com.jbp.randommaster.gui.common.splash;

import java.awt.Container;

/**
 * 
 * <code>AbstractSplashScreenRunnable</code> provides convenient 
 * implementation methods for implementing <code>SplashScreenRunnable</code>
 * interface.
 * 
 * @author plchung
 *
 */
public abstract class AbstractSplashScreenRunnable implements SplashScreenRunnable {
  
  private Container splashContent;
  
  /**
   * Get the content pane previously set.
   */
  protected Container getSplashContentPane() {
    return splashContent;
  }

  /**
   * Default implementation, read the content pane 
   * reference by <code>getSplashContentPane()</code>
   */
  public void setSplashContentPane(Container contentPane) {
    splashContent=contentPane;
  }

  /**
   * Default implementation, does nothing.
   */
  public void userInterrupted() { 
  }
  
}  