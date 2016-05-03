import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Timer;


public class Animation {
	private double x;
	private double y;
	
	private int animationTime;
	private Timer animationTimer;
	
	private int counter;
	
	private boolean drawFrame1;
	private boolean drawFrame2;
	private boolean drawFrame3;
	
	private boolean animationDone;
	
	Image img1;
	Image img2;
	Image img3;
	
	public Animation(double x, double y, int time, String imgString1, String imgString2, String imgString3)
	{
		this.x = x;
		this.y = y;
		
		animationTime = time;
		
		counter = 2;
		drawFrame1 = true;
		drawFrame2 = false;
		drawFrame3 = false;
		
		animationDone = false;
		
		img1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	    img2 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	    img3 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	    getImages(imgString1, imgString2, imgString3);
		
		startAnimationTimer();
	}
	
	private void getImages(String file1, String file2, String file3)
	{
		File path1 = new File(file1);
		File path2 = new File(file2);
		File path3 = new File(file3);
		
		try
		{
			img1 = ImageIO.read(path1);
			img2 = ImageIO.read(path2);
			img3 = ImageIO.read(path3);
         
		}
		catch(Exception e)
		{}
	}
	
	private void startAnimationTimer()
	{
		animationTimer = new Timer(animationTime, 
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						playAnimation();
					}
				}
				);
	   
		animationTimer.start();
	}
	
	private void playAnimation()
	{
		   if(counter == 2)
		   {
			   drawFrame1 = false;
			   drawFrame2 = true;
			   drawFrame3 = false;
		   }
		   else if(counter == 3)
		   {
			   drawFrame1 = false;
			   drawFrame2 = false;
			   drawFrame3 = true;
		   }
		   else
		   {
			   drawFrame1 = false;
			   drawFrame2 = false;
			   drawFrame3 = false;
			   
			   animationDone = true;
			   animationTimer.stop();
		   }
		   
		   counter++;
	}
	
	public void drawImage(Graphics2D g2)
	{
		if(drawFrame1)
		{
		   g2.drawImage(img1, (int)x, (int)y, null);
		}
		if(drawFrame2)
		{
		   g2.drawImage(img2, (int)x, (int)y, null);
		}
		if(drawFrame3)
		{
		   g2.drawImage(img3, (int)x, (int)y, null);
		}
	}
	
	public boolean animationDone()
	{
		return animationDone;
	}
}
