package com.jbp.randommaster.gui.common.layout;

import java.util.*;


public class ComponentDecorator {

    private static ComponentDecorator cd = new ComponentDecorator();

    @SuppressWarnings("rawtypes")
	private List dlList = new ArrayList();

    private ComponentDecorator() {
    }

    public static ComponentDecorator getInstance() {
        return cd;
    }

    @SuppressWarnings("unchecked")
	public void addDecorationListener(DecorationListener dl) {
        this.dlList.add(dl);
    }

    public void updateDecoration(DecorationEvent de) {
        for (int i = 0; i < dlList.size(); i++) {
            Object obj = (Object) dlList.get(i);
            if (obj != null) {
                ((DecorationListener) obj).decorationUpdate(de);
            }
        }
    }
}
