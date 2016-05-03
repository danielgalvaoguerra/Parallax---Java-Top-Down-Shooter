import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Hero
{
	public final double PLAYER_SPEED = 1;
	public static final double PLAYER_WIDTH = 50;
	public static final double PLAYER_HEIGHT = 50;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	private double dir;
	private Image img;
   
   public Hero(double xinit, double yinit)
   {
      x = xinit;
      y = yinit;
      dx = 0;
      dy = 0;
      
      img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
      
      getImage();
      
   }
   
   private void getImage() 
   {
	   File playerImagePath = new File("images/player.png");
	   
	   try
	   {
		   img = ImageIO.read(playerImagePath);
	   }
	   catch(Exception e)
	   {
		   
	   }
   }
   
   public void drawImage(Graphics2D g2)
   {
	   g2.rotate(dir, x+(PLAYER_WIDTH/2), y+(PLAYER_HEIGHT/2));
	   
	   //g2.drawRect((int)x, (int)y, (int)PLAYER_WIDTH, (int)PLAYER_HEIGHT);
	   g2.drawImage(img, (int)x, (int)y, null);
	   
	   g2.rotate(-dir, x+(PLAYER_WIDTH/2), y+(PLAYER_HEIGHT/2));
   }
   
   public void move(double angle)
   {
	   dir = angle;
	   /*dx = Math.cos(angle-Math.PI/2)*PLAYER_SPEED;
	   dy = Math.sin(angle-Math.PI/2)*PLAYER_SPEED;*/
	   
	   this.x += dx;
	   this.y += dy;
   }
   
   public void setSpeed(String dir, double direction)
   {
      if(dir == "x")
      {
         dx = direction*PLAYER_SPEED;
      }
      if(dir == "y")
      {
         dy = direction*PLAYER_SPEED;
      }
   }
   
   public void setX(double val)
   {
	   x = val;
   }
   
   public double getX()
   {
      return this.x;
   }
   
   public void setY(double val)
   {
	   y = val;
   }
   
   public double getY()
   {
      return this.y;
   }
   
   public Rectangle getBounds()
   {
      return new Rectangle((int)x, (int)y, 50, 50);
   }
}