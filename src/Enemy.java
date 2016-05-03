import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Enemy
{
	private static final int ENEMY_WIDTH = 50;
	private static final int ENEMY_HEIGHT = 50;
	
	private double ENEMY_SPEED = 0.2;
	
	private int rockBackTime = 50;
	private Timer rockBackTimer;
	private double counter;
	private double counter2;
	
	private double x;
	private double y;
	public double hp;
	private Image img;
	
	private double dir;
	private double followDir;
	private double rotationRate = 0.04;
   
   public Enemy(double xinit, double yinit, double health)
   {
      x = xinit;
      y = yinit;
      hp = health;
      dir = 0;
      followDir = 0;
      
      img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
      getImage();
   }
   
   private void getImage()
   {
	   File imgPath = new File("images/normalEnemy.png");
	   
	   try
	   {
		   img = ImageIO.read(imgPath);
	   }
	   catch(Exception e)
	   {
		   
	   }
   }
   
   public void move(double px, double py)
   {
	   followDir = Math.atan2(
				(this.y + ENEMY_HEIGHT/2) - (py + Hero.PLAYER_HEIGHT),
				(this.x + ENEMY_WIDTH/2) - (px + Hero.PLAYER_WIDTH)
					) - (Math.PI / 2);
	   this.x += Math.cos(followDir-Math.PI/2)*ENEMY_SPEED;
	   this.y += Math.sin(followDir-Math.PI/2)*ENEMY_SPEED;
   }
   
   public void rockBack(final double dx, final double dy)
   {
	   if(rockBackTimer != null)
		{
			rockBackTimer.stop();
		}

		rockBackTimer = new Timer(rockBackTime, 
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						animateRockBack(dx, dy);
					}
				});

		counter = 0;
		counter2 = 5;
		rockBackTimer.start();
   }
   
   private void animateRockBack(double dx, double dy)
	{		
		setX(x - ((dx*counter2)/2));
		setY(y - ((dy*counter2)/2));
		
		counter++;
		counter2 -= 0.5;
		
		if(counter == 10)
		{
			rockBackTimer.stop();
			counter = 0;
			counter2 = 5;
		}
	}
   
   public void drawImage(Graphics2D g2)
   {
	   g2.setColor(new Color(196, 16, 16));
	      
	   dir +=  rotationRate;
	   g2.rotate(dir, (int)x + ENEMY_WIDTH/2, (int)y + ENEMY_HEIGHT/2);
	   g2.drawImage(img, (int)x, (int)y, null);
	   g2.rotate(-dir, (int)x + ENEMY_WIDTH/2, (int)y + ENEMY_HEIGHT/2);
	   
	   //Ellipse2D.Double enemy = new Ellipse2D.Double(x, y, 50, 50);
	   //g2.fill(enemy);
	      
	   g2.setColor(Color.BLACK);
   }
   
   public double getX()
   {
      return this.x;
   }
   
   public double getY()
   {
      return this.y;
   }
   
   public void setX(double newX)
   {
      this.x = newX;
   }
   
   public void setY(double newY)
   {
      this.y = newY;
   }
   
   public void takeDamage(double dmg)
   {
      hp -= dmg;
   }
   
   public double getHP()
   {
      return hp;
   }
   
   public Rectangle getBounds()
   {
      return new Rectangle((int)x, (int)y, 50, 50);
   }
   
   public boolean colliding(double x, double y)
   {
	   if(x >= this.x && x <= this.x + ENEMY_WIDTH &&
		   y >= this.y && y <= this.y + ENEMY_HEIGHT)
	   {
		   return true;
	   }
	   
	   return false;
   }
   
   public String getClassName()
   {
	   return "Enemy";
   }
   
   public int getBulletsArraySize()
   {
	   return 0;
   }
   
   public ArrayList<Bullet> getBulletsArray()
   {
	   return null;
   }
   
   public void update(Graphics g, double playerX, double playerY)
   {
      Graphics2D g2 = (Graphics2D) g;
      
      x += (playerX - x)/9000;
      y += (playerY - y)/9000;
      
      g2.setColor(new Color(196, 16, 16));
      
      Ellipse2D.Double enemy = new Ellipse2D.Double(x, y, 50, 50);
      g2.fill(enemy);
      
      g2.setColor(Color.BLACK);
   }
}