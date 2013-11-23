package com.jbp.randommaster.gui.common.splash;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 * 
 * <code>SplashScreen</code> is an utility to show a splash screen
 * on the terminal.
 * 
 * @author plchung
 *
 */
public class SplashScreen {
  
  /**
   * Cannot be instantiate directly.
   *
   */
  private SplashScreen() {
  }

  /**
   * Show a splash screen.
   * 
   * @param contentPane The GUI component to be shown in the window.
   * @param jobToDo The lengthy task to do during the splash screen showing.
   * @param mouseClosable Indicate whether the user can interrupted the splash
   * by clicking the screen.
   */
  public static void show(final Container contentPane, 
                          final SplashScreenRunnable jobToDo, 
                          boolean mouseClosable) {
    final JWindow screen=new JWindow(new JFrame());
    
    screen.setContentPane(contentPane);
    screen.pack();
    Dimension screenSize =
      Toolkit.getDefaultToolkit().getScreenSize();
    Dimension labelSize = contentPane.getPreferredSize();
    screen.setLocation(screenSize.width/2 - (labelSize.width/2),
                screenSize.height/2 - (labelSize.height/2));

    if (mouseClosable) {
      screen.addMouseListener(new MouseAdapter(){
          public void mousePressed(MouseEvent e) {
            jobToDo.userInterrupted();
            screen.setVisible(false);
            screen.dispose();
          }
        });
    }
    
    Runnable waitRunner = new Runnable() {
      public void run() {
        try {
          jobToDo.setSplashContentPane(contentPane);
          jobToDo.run();
          // close the window.
          SwingUtilities.invokeAndWait(
            new Runnable() {
              public void run() {
                screen.setVisible(false);
                screen.dispose();
              }
            }
          );
        }
        catch(Exception e) {
          e.printStackTrace();
        }
      }
    };
    
    screen.setVisible(true);
    Thread splashThread = new Thread(waitRunner, "SplashThread");
    splashThread.start();
  }

}

