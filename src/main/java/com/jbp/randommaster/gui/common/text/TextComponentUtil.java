package com.jbp.randommaster.gui.common.text;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.text.JTextComponent;

public class TextComponentUtil {
  
  public static void setHighlightOnFocus(final JTextComponent c) {
    if (c!=null) {
      c.addFocusListener(new FocusAdapter() {
          public void focusGained(FocusEvent e) {
            c.selectAll();
          }
          public void focusLost(FocusEvent e) {
            c.setSelectionStart(0);
            c.setSelectionEnd(0);
          }
        });
    }
  }
  
}