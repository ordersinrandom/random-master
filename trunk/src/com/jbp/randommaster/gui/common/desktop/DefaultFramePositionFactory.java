package com.jbp.randommaster.gui.common.desktop;

import java.awt.Point;
import java.util.Random;

public class DefaultFramePositionFactory implements FramePositionFactory {
  
  private int nextX, nextY;
  private int max;
  private int gap;
  private Random rand;
  
  public DefaultFramePositionFactory() {
    nextX=0;
    nextY=0;
    max=300;
    gap=20;
    rand=new Random();
  }
  
  public Point nextPosition() {
    Point result=new Point(nextX, nextY);
    nextX+=gap;
    nextY+=gap;
    if (nextX>max || nextY>max) {
      nextX=0+rand.nextInt(20);
      nextY=0+rand.nextInt(10);
    }
    return result;
  }
}