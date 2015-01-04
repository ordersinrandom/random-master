package com.jbp.randommaster.gui.common.grouping.buttons;

import java.awt.Color;
import java.util.*;

import javax.swing.Action;
import javax.swing.AbstractButton;
import javax.swing.JPanel;

import com.jbp.randommaster.gui.common.grouping.Grouping;
import com.jbp.randommaster.gui.common.grouping.GroupingControl;

public class GroupButtonsBar extends ContextPanel implements GroupingControl {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5534847003553149620L;
	@SuppressWarnings("rawtypes")
	protected Vector names = new Vector();
    @SuppressWarnings("rawtypes")
	protected Vector views = new Vector();

    @SuppressWarnings("unused")
	private Grouping root;

    public GroupButtonsBar(Grouping root) {
      this.setRootGrouping(root);
    }
    
    @SuppressWarnings("rawtypes")
	public void setRootGrouping(Grouping root) {
      this.root=root;
      // for each group
      for (Iterator it=root.getGroupings().iterator();it.hasNext();) {
        Grouping group=(Grouping) it.next();
//        String gn=group.getName();
        if (group.getAction()==null)
          throw new IllegalStateException("group :"+group.getName()+" has no action");
          
        // for each item
        for (Iterator it2=group.getGroupings().iterator();it2.hasNext();) {
          Grouping child=(Grouping) it2.next();
          Action a=child.getAction();
          if (a!=null)
            addIcon(group.getAction(), a);
          else throw new IllegalStateException("group : "+child.getName()+" has no action, ignored");
        }
      }
    }

    @SuppressWarnings("unchecked")
	private AbstractButton addIcon(Action context, Action action)
    {
        int index;
        JPanel view;
        if ((index = names.indexOf(context)) > -1)
        {
            view = (JPanel)views.elementAt(index);
        }
        else
        {
            view = new JPanel();
            view.setBackground(Color.gray);
            view.setLayout(new ListLayout());
            names.addElement(context);
            views.addElement(view);
            addTab(context, new ScrollingPanel(view));
        }
        RolloverButton button = new RolloverButton(action);
//        button.addActionListener(action);
        view.add(button);
        doLayout();
        return button;
    }

}

