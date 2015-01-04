package com.jbp.randommaster.gui.common.table.renderer.common;

import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.SwingConstants;

import com.jbp.randommaster.gui.common.table.renderer.PainterStyleAdapter;
import com.jbp.randommaster.gui.common.table.renderer.RenderersFacade;


public class QuantityRenderer extends RenderersFacade {

  private static final Integer alignment=new Integer(SwingConstants.RIGHT);

  public QuantityRenderer() {
    super();
    
    final DecimalFormat formatter=new DecimalFormat("#,###,###,###");
    
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