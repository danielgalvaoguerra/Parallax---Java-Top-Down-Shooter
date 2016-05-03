import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Bullet
{
	private final int BULLET_WIDTH = 10;
	
	private final int BULLET_HEIGHT = 10;
	
	double x;
	double y;
	double dir;
	private Image img;
	private String imageFile;
	private int level;
   
	double speed = 2;

   public Bullet(double xinit, double yinit, double  initialDirection, int level)
   {
      x = xinit;
      y = yinit;
      dir = initialDirection;
      this.level = level;
      
      img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
      getImage();
   }
   
   private void getImage()
   {
	   File bulletImgPath;
	   
	   if(level == 1)
	   {
		   bulletImgPath = new File("images/ball_green.png");
	   }
	   else if(level == 2)
	   {
		   bulletImgPath = new File("images/ball_blue.png");
	   }
	   else
	   {
		   bulletImgPath = new File("images/ball_red.png");
		   speed = 3;
	   }
	   
	   try
	   {
		   img = ImageIO.read(bulletImgPath);
	   }
	   catch(Exception e)
	   {
		   
	   }
   }
   
   public void move()
   {
	   x += Math.cos(dir-Math.PI/2)*speed;
	   y += Math.sin(dir-Math.PI/2)*speed; 
   }
   
   public void drawImage(Graphics2D g2)
   {
	   g2.rotate(this.dir, x, y);
	   
	   if(level == 1)
	   {
		   g2.setColor(Color.BLUE);
	   }
	   else if(level == 2)
	   {
		   g2.setColor(Color.ORANGE);
	   }
	   else
	   {
		   g2.setColor(Color.RED);
		   speed = 3;
	   }
	     
	   g2.drawImage(img, (int)x, (int)y, null);
	      
	   g2.rotate((-1)*(this.dir), x, y);
   }
   
   public double getX()
   { 
      return x; 
   }
   
   public double getY()
   { 
      return y; 
   }
   
   public double getDir()
   {
	   return dir;
   }
   
   public void changeSpeed(double newSpeed)
   {
      speed = newSpeed;
   }
   
   public Rectangle getBounds()
   {
      return new Rectangle((int)x, (int)y, 10, 10);
   }
   
   public boolean colliding(double x, double y)
   {
	   if(x >= this.x && x <= this.x + BULLET_WIDTH &&
		   y >= this.y && y <= this.y + BULLET_HEIGHT)
	   {
		   return true;
	   }
	   
	   return false;
   }
   
   public int getLevel()
   {
	   return level;
   }
   
   public double getSpeed()
   {
	   return speed;
   }
}