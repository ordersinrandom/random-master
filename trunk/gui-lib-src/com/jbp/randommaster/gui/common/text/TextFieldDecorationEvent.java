package com.jbp.randommaster.gui.common.text;

import java.awt.*;
import javax.swing.*;

import com.jbp.randommaster.gui.common.layout.DecorationEvent;

public class TextFieldDecorationEvent extends DecorationEvent {

    private boolean toggle;

    protected void decoration(JComponent jc) {
        System.out.println("checked jtextfield");
        jc.setBorder(BorderFactory.createLineBorder(Color.black));
        if (toggle) {
            jc.setBackground(Color.green);
        }
        else {
            jc.setBackground(Color.red);
        }
        toggle = toggle ? false : true;
    }
}
