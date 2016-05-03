import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class SmallEnemy extends Enemy{
	
	public static final int SMALLENEMY_WIDTH = 25;
	public static final int SMALLENEMY_HEIGHT = 25;
	public static final double SMALLENEMY_SPEED = 0.3;
	public static final double SMALLENEMY_HP = 1;
	
	private double moveX;
	private double moveY;
	private double speedX;
	private double speedY;
	
	private Image img;
	
	public SmallEnemy(double xinit, double yinit, int xdir, int ydir)
	{
		super(xinit, yinit, SMALLENEMY_HP);
		
		moveX = xinit;
		moveY = yinit;
		speedX = SMALLENEMY_SPEED*xdir;
		speedY = SMALLENEMY_SPEED*ydir;
		
		img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		getImages();
	}
	
	private void getImages()
	{
		File imgPath = new File("images/smallEnemy.png");
		
		try
		{
			img = ImageIO.read(imgPath);
		}
		catch(Exception e)
		{}
	}
	
	public void move(double px, double py)
	{
		if(moveX+SMALLENEMY_WIDTH > ParallaxMain.SCREEN_WIDTH || super.getX() < 0)
		{
			speedX *= -1;
		}
		if(super.getY()+SMALLENEMY_HEIGHT > ParallaxMain.SCREEN_HEIGHT || super.getY() < 0)
		{
			speedY *= -1;
		}
		
		moveX += speedX;
		moveY += speedY;
		
		super.setX(moveX);
		super.setY(moveY);
	}
	
	public void drawImage(Graphics2D g2)
	{
		g2.setColor(Color.BLUE);
	      
		g2.drawImage(img, (int)super.getX(), (int)super.getY(), null);
		
	   	//Ellipse2D.Double enemy = new Ellipse2D.Double(super.getX(), super.getY(), SMALLENEMY_WIDTH, SMALLENEMY_HEIGHT);
	   	//g2.fill(enemy);
	      
	   	g2.setColor(Color.BLACK);
   	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int)super.getX(), (int)super.getY(), SMALLENEMY_WIDTH, SMALLENEMY_HEIGHT);
	}

	public String getClassName()
	{
	   return "SmallEnemy";
   	}
	
	public double getSpeedX()
	{
		return speedX;
	}
	
	public double getSpeedY()
	{
		return speedY;
	}
}
