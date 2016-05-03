import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Timer;


public class Loot {
	
	public static final int GUN = 0;
	public static final int HP = 1;
	
	private int type;
	private double x;
	private double y;
	
	private Image img;
	
	private Timer lifeTimer;
	private int lifeTime = 3000;
	private boolean startFading = false;
	private boolean lifeOver;
	
	private float alpha = 1;
	private float alphaCounter = 1;
	
	public Loot(int type, double x, double y)
	{
		this.type = type;
		this.x = x;
		this.y = y;
		
		lifeOver = false;
		
		getImage();
		startTimer();
	}
	
	private void startTimer()
	{
		lifeTimer = new Timer(lifeTime,
				new ActionListener()
				{ 
					public void actionPerformed(ActionEvent e)
					{
						startFading = true;
					}
				});
		
		lifeTimer.start();
	}
	
	public boolean lifeOver()
	{
		return lifeOver;
	}
	
	private void getImage()
	{
		File imgPath;
		
		if(type == GUN)
		{
			imgPath = new File("images/gunLoot.png");
		}
		else if(type == HP)
		{
			imgPath = new File("images/hpLoot.png");
		}
		else
		{
			imgPath = null;
		}
		
		try
		{
			img = ImageIO.read(imgPath);
		}
		catch(Exception e)
		{}
	}
	
	public void draw(Graphics2D g2)
	{
		if(startFading)
		{
			alpha = alphaCounter * 1f;
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			
			if(alphaCounter > 0.1)
			{
				alphaCounter -= 0.01;
			}
			else
			{
				lifeOver = true;
			}
		}
		g2.drawImage(img, (int)x, (int)y, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int)x, (int)y, 25, 25);
	}
	
	public int getType()
	{
		return type;
	}
}
