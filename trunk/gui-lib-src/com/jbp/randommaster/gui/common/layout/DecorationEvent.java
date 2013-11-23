package com.jbp.randommaster.gui.common.layout;

import javax.swing.*;

public abstract class DecorationEvent {

    //private String[] decorationIdentifiers = {"DecorationEvent.DEFAULT"};
    private String[] decorationIdentifiers = {""};

    public void setDecorationIdentifier(String decorationIdentifier) {
        if (decorationIdentifier != null) {
            this.decorationIdentifiers[0] = decorationIdentifier;
        }
    }

    public void setDecorationIdentifier(String[] decorationIdentifiers) {
        this.decorationIdentifiers = decorationIdentifiers;
    }

    public String[] getDecorationIdentifiers() {
        return decorationIdentifiers;
    }

    public void decorateMe(JComponent jc) {
        if (jc instanceof DedicatedDecorationComponent) {
            for (int i = 0; i < decorationIdentifiers.length; i++) {
                if (decorationIdentifiers[i].equals(((DedicatedDecorationComponent) jc).getDecorationIdentifier())) {
                    decoration(jc);
                }
            }
        }
        else if (jc instanceof DecorationWrapper) {
            DecorationWrapper dw = (DecorationWrapper) jc;
            for (int i = 0; i < decorationIdentifiers.length; i++) {
                if (decorationIdentifiers[i].equals(dw.getDecorationIdentifier())) {
                    decoration(dw.getJComponent());
                }
            }
        }
        else {
            if (jc != null) {
                for (int i = 0; i < decorationIdentifiers.length; i++) {
                    if (decorationIdentifiers[i].equals(jc.getClass().getName())) {
                        decoration(jc);
                    }
                }
            }
        }
    }

    protected abstract void decoration(JComponent jc);
}
