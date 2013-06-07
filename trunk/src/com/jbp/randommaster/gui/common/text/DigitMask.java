package com.jbp.randommaster.gui.common.text;

public class DigitMask implements TextFieldMask {

    public boolean check(char c) {
        return Character.isDigit(c);
    }
}
