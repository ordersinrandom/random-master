package com.jbp.randommaster.gui.common.grouping.buttons;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Stephen Lee
 * @version 1.0
 */

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

public class ContextPanel extends JPanel
    implements ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5678697876074297311L;
	@SuppressWarnings("rawtypes")
	protected Vector buttons = new Vector();

    public ContextPanel()
    {
        setLayout(new ContextLayout());
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setPreferredSize(new Dimension(80, 80));
    }

    public void setIndex(int index)
    {
        ((ContextLayout)getLayout()).setIndex(this, index);
    }

    @SuppressWarnings("unchecked")
	public void addTab(javax.swing.Action name, Component comp)
    {
        JButton button = new TabButton(name);
        add(button, comp);
        buttons.addElement(button);
        button.addActionListener(this);
    }

    public void removeTab(JButton button)
    {
        button.removeActionListener(this);
        buttons.removeElement(button);
        remove(button);
    }

    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();
        for (int i = 0; i < buttons.size(); i++)
        {
            if (source == buttons.elementAt(i))
            {
                setIndex(i + 1);
                return;
            }
        }
    }
}


