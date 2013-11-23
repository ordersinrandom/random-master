package com.jbp.randommaster.gui.common.label;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 *
 */
@SuppressWarnings("serial")
public class ClickableHoverLabel extends JLabel {

    public static final String HOVER_BORDER_COLOR = "C.HB";
    public static final String NORMAL_BORDER_COLOR = "C.NB";

    private Border hoverBorder;
    private Border normalBorder;
    private boolean hoverable;

    private Action action;
    //private List lsrList;
    
    public ClickableHoverLabel() {
        this(null, false, null);
    }
    public ClickableHoverLabel(String text, boolean hoverable) {
        this(text, hoverable, null);
    }
    @SuppressWarnings("rawtypes")
	public ClickableHoverLabel(String text, boolean hoverable, Map map) {
        super(text);
        super.addMouseListener(new InMouseListener(this));

        //lsrList = new ArrayList();

        this.hoverable = hoverable;
        this.hoverBorder = BorderFactory.createLineBorder(map != null ? (Color) map.get(HOVER_BORDER_COLOR) : Color.blue);
        this.normalBorder = BorderFactory.createLineBorder(map != null ? (Color) map.get(NORMAL_BORDER_COLOR) : super.getBackground());
    }
    /**
    public void addActionListener(ActionListener lsr) {
        if (lsr != null) {
            lsrList.add(lsr);
        }
    }
    */
    public void setAction(Action action) {
        this.action = action;
    }
    public Action getAction() {
        return action;
    }
    public void setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
    }
    public boolean isHoverable() {
        return hoverable;
    }

    /**
     *
     */
    private class InMouseListener extends MouseAdapter {
        private ClickableHoverLabel source;
        public InMouseListener(ClickableHoverLabel source) {
            this.source = source;
        }
        public void mouseClicked(MouseEvent e) {
            if (action != null) {
                action.actionPerformed(new ActionEvent(source, 0, null));
            }
            /**
            for (int i = 0; i < lsrList.size(); i++) {
                ((ActionListener) lsrList.get(i)).actionPerformed(new ActionEvent(source.getAction(), 0, null));
            }
            */
        }
        public void mouseEntered(MouseEvent e) {
            if (hoverable) {
                setBorder(hoverBorder);
            }
        }
        public void mouseExited(MouseEvent e) {
            if (hoverable) {
                setBorder(normalBorder);
            }
        }
    }
}
/**
 * $Log: ClickableHoverLabel.java,v $
 * Revision 1.1.1.1  2005/01/04 14:32:13  poloi
 * no message
 *
 * Revision 1.1.1.1  2005/01/01 22:51:37  poloi
 * no message
 *
 * Revision 1.2  2004/07/07 07:10:03  sklau
 * no message
 *
 * Revision 1.1  2004/06/17 02:30:53  sklau
 * no message
 *
 */
