package com.jbp.randommaster.draft.rserve;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

public class TestRserve1 {

	public static void main(String[] args) throws RserveException, REXPMismatchException {

		// first start these in R
		//> library(Rserve)
		//> Rserve()
		
		
		RConnection c = new RConnection("127.0.0.1");
		REXP xp = c.eval("R.version.string");
		System.out.println(xp.asString());

		

		String tempJpgFile = "test"+(new Random().nextLong())+".jpg";
		
		int width = 500;
		int height = 500;
		
		c.eval("try(jpeg(\""+tempJpgFile+"\", quality=100))");

        // ok, so the device should be fine - let's plot
		c.voidEval("x = rnorm(2000)+seq(0, 1, length=2000)");
		c.voidEval("y = 4*x+2*rnorm(2000)");
        c.voidEval("plot(x,y,main='Test Rserve Plotting', xlab='Some random value', ylab='Another random value')");
        c.voidEval("axis(side=1, tck=1, lty=3, col='red')");
        c.voidEval("axis(side=2, tck=1, lty=3, col='red')");
        c.voidEval("dev.off()");		
		
		xp = c.eval("r=readBin('" + tempJpgFile + "','raw',500*500); unlink('" + tempJpgFile + "'); r");
        
        
        Image img = Toolkit.getDefaultToolkit().createImage(xp.asBytes());
        
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel(new ImageIcon(img));
        f.getContentPane().add(label);
        f.setSize(width, height);
        f.setVisible(true);
	}

}
