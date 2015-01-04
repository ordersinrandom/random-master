package com.jbp.randommaster.gui.common.table.renderer.common;

import javax.swing.SwingConstants;

import java.text.Format;

import com.jbp.randommaster.gui.common.table.renderer.RenderersFacade;
import com.jbp.randommaster.gui.common.table.renderer.PainterStyleAdapter;
import com.jbp.randommaster.gui.common.text.CashFormatter;

public class CashRenderer extends RenderersFacade {

  private static final Integer alignment=new Integer(SwingConstants.RIGHT);

  /**
   * Create a new instance of <code>CashHoldingsRenderer</code>
   * with four decimal places precision.
   *
   */
  public CashRenderer() {
    this(new CashFormatter("", 2));
  }
  

  /**
   * Create a new instance of <code>CashHoldingsRenderer</code>.
   * @param precision The number decimal places to render.
   */
  public CashRenderer(final CashFormatter formatter) {
    super();
    
    super.getPainter().addStyle(new PainterStyleAdapter() {
      public Format getTextFormat() {
        return formatter;
      }
      
      public Integer getHorizontalAlignment() {
        return alignment;
      }
    });
  }
}