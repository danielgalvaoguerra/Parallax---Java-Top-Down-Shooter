import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Timer;


public class HeavyEnemy extends Enemy{
	
	public static final int HEAVYENEMY_WIDTH = 85;
	public static final int HEAVYENEMY_HEIGHT = 85;
	public static final double HEAVYENEMY_HP = 6;
	
	private double dir;
	private double eyeDir;
	
	private int rockBackTime = 50;
	private Timer rockBackTimer;
	
	private int shootingDelay = 2000;
	private Timer shootingTimer;
	
	private double counter;
	private double counter2;
	private boolean drawFriction;
	
	private Image enemyImg;
	private Image eyeImg;
	private Image frictionImg;

	private ArrayList<Bullet> enemyBullets;
	
	private GameplayFrame gameplayClass;
	
	public HeavyEnemy(double xinit, double yinit, GameplayFrame c)
	{
		super(xinit, yinit, HEAVYENEMY_HP);
		dir = 0;
		eyeDir = 0;
		
		drawFriction = false;
		
		enemyImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		eyeImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		getImages();
		
		gameplayClass = c;
		
		//frictionImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		//getImages("images/pixelFriction.png");
		
		enemyBullets = new ArrayList<Bullet>();
		
		startShootingThread();
	}
	
	private void startShootingThread()
	{
		Thread shootingThread = new Thread(
				new Runnable()
				{
					public void run()
					{
						startShootingTimer();
					}
				});
		
		shootingThread.start();
	}
	
	private void startShootingTimer()
	{
		shootingTimer = new Timer(shootingDelay,
				new ActionListener()
				{ 
					public void actionPerformed(ActionEvent e)
					{
						shoot();
					}
				});
		
		shootingTimer.start();
	}
	
	private void shoot()
	{
		enemyBullets.add(new Bullet(super.getX() + HEAVYENEMY_WIDTH/2,
									super.getY() + HEAVYENEMY_HEIGHT/2, 
									eyeDir, 1));
	}
	
	private void getImages()
	{
		File enemyImgPath = new File("images/heavyEnemy.png");
		File eyeImgPath = new File("images/heavyEnemy_eyeball.png");
		
		try
		{
			enemyImg = ImageIO.read(enemyImgPath);
			eyeImg = ImageIO.read(eyeImgPath);
		}
		catch(Exception e)
		{}
	}
	
	public void move(double px, double py)
	{
		eyeDir = Math.atan2(
						(super.getY() + HeavyEnemy.HEAVYENEMY_WIDTH/2) - (py+25),
						(super.getX() + HeavyEnemy.HEAVYENEMY_WIDTH/2) - (px+25)
							) - (Math.PI / 2);
		
		for(int b = 0; b < enemyBullets.size(); b++)
		{
			enemyBullets.get(b).move();
			
			if(enemyBullets.size() != b)
			{
				if(enemyBullets.get(b).getBounds().intersects(new Rectangle((int)px, (int)py, 50, 50)))
				{
					enemyBullets.remove(b);
					gameplayClass.playerGotHit();
				}
			
				// CLEAR BULLETS OUT OF STAGE
				/*if(enemyBullets.get(b).getX() > 1000 || enemyBullets.get(b).getX() < 0
					|| enemyBullets.get(b).getY() > 700 || enemyBullets.get(b).getY() < 0)
				{
					enemyBullets.remove(b);
				}*/
			}
		}
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
		drawFriction = false;
		rockBackTimer.start();
		
		//dir = newDir;
	}
	
	private void animateRockBack(double dx, double dy)
	{		
		super.setX(super.getX() - ((dx*counter2)/2));
		super.setY(super.getY() - ((dy*counter2)/2));
		
		counter++;
		counter2 -= 0.5;
		drawFriction = true;
		
		if(counter == 10)
		{
			rockBackTimer.stop();
			counter = 0;
			counter2 = 5;
			drawFriction = false;
		}
	}
	
	public void drawImage(Graphics2D g2)
	{
		/*if(drawFriction)
	   	{
	   		g2.drawImage(frictionImg, (int)super.getX() - 20, (int)super.getY(), null);
	   		g2.drawImage(frictionImg, (int)super.getX() - 10, (int)super.getY() + 20, null);
	   		g2.drawImage(frictionImg, (int)super.getX() - 5, (int)super.getY() + 40, null);
	   		g2.drawImage(frictionImg, (int)super.getX() - 25, (int)super.getY() + 45, null);
	   		g2.drawImage(frictionImg, (int)super.getX() - 15, (int)super.getY() + 80, null);
	   	}*/
		
		for(int b = 0; b < enemyBullets.size(); b++)
		{
			enemyBullets.get(b).drawImage(g2);
		}
		
		g2.setColor(Color.DARK_GRAY);
		g2.rotate(dir, super.getX() + (HeavyEnemy.HEAVYENEMY_WIDTH/2), super.getY() + (HeavyEnemy.HEAVYENEMY_HEIGHT/2));
	      
		g2.drawImage(enemyImg, (int)super.getX(), (int)super.getY(), null);
		
		// DRAW EYEBALL
		g2.rotate(eyeDir, super.getX() + (HeavyEnemy.HEAVYENEMY_WIDTH/2), super.getY() + (HeavyEnemy.HEAVYENEMY_HEIGHT/2));
		g2.drawImage(eyeImg, (int)super.getX(), (int)super.getY(), null);
		g2.rotate((-1)*eyeDir, super.getX() + (HeavyEnemy.HEAVYENEMY_WIDTH/2), super.getY() + (HeavyEnemy.HEAVYENEMY_HEIGHT/2));
		
	   	//Rectangle2D.Double enemy = new Rectangle2D.Double(super.getX(), super.getY(), HEAVYENEMY_WIDTH, HEAVYENEMY_HEIGHT);
	   	//g2.fill(enemy);
	      
	   	g2.rotate((-1)*dir, super.getX() + (HeavyEnemy.HEAVYENEMY_WIDTH/2), super.getY() + (HeavyEnemy.HEAVYENEMY_HEIGHT/2));
	   	g2.setColor(Color.BLACK);
	}
	
	public int getBulletsArraySize()
	{
		return enemyBullets.size();
	}
	
	public ArrayList<Bullet> getBulletsArray()
	{
		return enemyBullets;
	}

	public Rectangle getBounds()
	{
		return new Rectangle((int)super.getX(), (int)super.getY(), HEAVYENEMY_WIDTH, HEAVYENEMY_HEIGHT);
	}
	
	public String getClassName()
	{
		return "HeavyEnemy";
	}
}
