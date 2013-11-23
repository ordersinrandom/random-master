package com.jbp.randommaster.gui.common.layout;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class DecorationWrapper extends JComponent implements DecorationListener {

    //private String id = "DecorationEvent.DEFAULT";
    private JComponent jc;
    private String identifier;

    public DecorationWrapper(JComponent jc) {
        this(jc, jc.getClass().getName());
    }

    public DecorationWrapper(JComponent jc, String identifier) {
        this.jc = jc;
        this.identifier = identifier;
    }

    public String getDecorationIdentifier() {
        return identifier;
    }

    public JComponent getJComponent() {
        return jc;
    }

    public void decorationUpdate(DecorationEvent de) {
        de.decorateMe(jc);
    }

}
