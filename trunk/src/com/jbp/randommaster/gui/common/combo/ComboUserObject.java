package com.jbp.randommaster.gui.common.combo;
import java.io.Serializable;

public class ComboUserObject implements Serializable{

	private static final long serialVersionUID = 7117965740061987549L;
	
	private String index;
    private String displayText;

    public ComboUserObject(String index, String displayText) {
        this.index = index;
        this.displayText = displayText;
    }
    public void setIndex(String index) {
        this.index = index;
    }
    public String getIndex() {
        return index;
    }
    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }
    public String getDisplayText() {
        return displayText;
    }
    public String toString() {
        return displayText;
    }
}
