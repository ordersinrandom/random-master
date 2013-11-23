package com.jbp.randommaster.gui.common.table.block;

import java.util.Comparator;

/**
 * ValueSorter.
 */
public class ValueSorter extends ComparatorSorter {
    public ValueSorter() {
        super(new ValueComparator());
    }
    @SuppressWarnings("rawtypes")
	private static class ValueComparator implements Comparator {
        public int compare(Object o1, Object o2) {

            String s1 = String.valueOf(o1);
            String s2 = String.valueOf(o2);


            if ((s1 == null && "".equals(s1)) && (s2 == null && "".equals(s2))) {
                return 0;
            }
            else if (s1 == null && !"".equals(s1)) {
                return -1;
            }
            else if (s2 != null && "".equals(s2)) {
                return 1;
            }
            else {
                //s1 = Character.isDigit(s1.charAt(0)) ? s1 : s1.substring(1, s1.length());
                //s2 = Character.isDigit(s2.charAt(0)) ? s2 : s2.substring(1, s2.length());

                float value1 = 0.0F;
                float value2 = 0.0F;
                if (Character.isLetter(s1.charAt(s1.length() - 1))) {
                    if ('M' == s1.charAt(s1.length() - 1)) {
                        value1 = Float.parseFloat(s1.substring(0, s1.length() - 1)) * 1000000;
                    }
                    else if ('K'  == s1.charAt(s1.length() - 1)) {
                        value1 = Float.parseFloat(s1.substring(0, s1.length() - 1)) * 1000;
                    }
                }
                else {
                    value1 = Float.parseFloat(s1);
                }
                if (Character.isLetter(s2.charAt(s2.length() - 1))) {
                    if ('M' == s2.charAt(s2.length() - 1)) {
                        value2 = Float.parseFloat(s2.substring(0, s2.length() - 1)) * 1000000;
                    }
                    else if ('K' ==  s2.charAt(s2.length() - 1)) {
                        value2 = Float.parseFloat(s2.substring(0, s2.length() - 1)) * 1000;
                    }
                }
                else {
                    value2 = Float.parseFloat(s2);
                }
                return (new Float(value1)).compareTo(new Float(value2));
            }

        }
        public boolean equals(Object obj) {
            return false;
        }
    }
    public void temp() {
        String s1 = "20.33";
        String s2 = "20.34";
        ValueComparator vc = new ValueComparator();
        System.out.println(vc.compare(s1, s2));
    }
    public static void main(String args[]) {
        (new ValueSorter()).temp();
    }
}
/**
 * $Log: ValueSorter.java,v $
 * Revision 1.3  2005/05/27 09:36:54  poloi
 * no message
 *
 * Revision 1.2  2005/05/27 08:54:29  poloi
 * no message
 *
 * Revision 1.2  2004/08/18 06:54:43  sklau
 * no message
 *
 * Revision 1.1  2004/06/24 07:17:40  sklau
 * no message
 *
 */
