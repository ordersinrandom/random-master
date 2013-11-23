package com.jbp.randommaster.gui.common.threading;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class ScreenUpdateUtil {
  
  /**
   * Helper to wrap up <code>SwingUtilities.invokeAndWait()</code> method, 
   * such that it can be called regardless whether the current thread
   * is the AWT Event Dispatch Thread.
   * 
   * @return Throwable if the runnable has an exception.
   */
  public static Throwable waitFor(Runnable r) {
    if (!SwingUtilities.isEventDispatchThread()) {
      boolean done=false;
      while (!done) {
        try {
          SwingUtilities.invokeAndWait(r);
          done=true;
        } catch (InterruptedException e1) {
          // ignore and retry
        } catch (InvocationTargetException e2) {
          return e2.getTargetException();
        }
      }
    }
    else {
      try {
        r.run();
      } catch (Throwable e3) {
        return e3;
      }
    }
    // no exception caught
    return null;
    
  }

  
}