package com.jbp.randommaster.gui.common.desktop;


public class DesktopOperationException extends RuntimeException {
  /**
	 * 
	 */
	private static final long serialVersionUID = -1909938081463251039L;

  public DesktopOperationException(String msg) {
    super(msg);
  }
  
  public DesktopOperationException(String msg, Throwable t) {
    super(msg, t);
  }
}