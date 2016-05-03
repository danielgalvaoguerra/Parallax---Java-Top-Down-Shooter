import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameplayFrame extends JPanel implements KeyListener, MouseMotionListener, MouseListener
{
	private static final int MOVEMENT_TIME = 5;
	private Timer movementTimer;
	
	private static final int ANIMATION_TIME = 20;
	private Timer animationTimer;
	
	private static final int waveWaitTime = 400;
	private Timer waveWaitTimer;
	
	private static final int collisionsTime = 20;
	private Timer collisionsTimer;
	
	private int enemySpawnCounter = 5;
	
	private Hero player;
	private EnemyWaveHandler waveHandler = new EnemyWaveHandler();
	private LootHandler lootHandler = new LootHandler();
	
	private ArrayList<Enemy> enemyArray;
	
	private ArrayList<Bullet> bulletArray;
	private int bulletLevel;
	
	private ArrayList<Bullet> enemyBullets;
	private Timer enemyShootTimer;
	private int enemyShootingDelay = 3000;
	
	private ArrayList<Loot> lootArray = new ArrayList<Loot>();
	private ArrayList<Animation> animationsArray = new ArrayList<Animation>();
	
	private ParallaxMain mainClass;
	private int mouseX;
	private int mouseY;
	private double angle;
	
	private boolean wPressed;
	private boolean aPressed;
	private boolean sPressed;
	private boolean dPressed;
	private boolean ableToMoveUp;
	private boolean ableToMoveLeft;
	private boolean ableToMoveDown;
	private boolean ableToMoveRight;
	
	private int livesLeft;
	private int score;
	
	private Image bgImg;
	
	private static MediaPlayer mediaPlayer;
	JFXPanel panel = new JFXPanel();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameplayFrame(ParallaxMain c)
	{
		mouseX = 0;
		mouseY = 0;
		angle = 0;
		
		wPressed = false;
		aPressed = false;
		sPressed = false;
		dPressed = false;
		ableToMoveRight = true;
		ableToMoveLeft = true;
		ableToMoveUp = true;
		ableToMoveDown = true;
		
		bgImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		
		livesLeft = 1;
		score = 0;
		
		mainClass = c;
		
		enemyArray = new ArrayList<Enemy>();
		
		spawnPlayer();
		spawnEnemies();
		spawnBullets();
		
		bulletLevel = 1;
		
		createWaveHandler();
		
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		
		runScene();
		getBackgroundImage();
	}
	
	private void getBackgroundImage()
	{
		File bgImgPath = new File("images/background.png");
		
		try
		{
			bgImg = ImageIO.read(bgImgPath);
		}
		catch(Exception e)
		{}
	}
	
	public int getLivesLeft() 
	{
		return livesLeft;
	}
	
	public void addLivesLeft(int hp)
	{
		livesLeft += hp;
	}

	public int getScore() 
	{
		return score;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		//turn on anti-aliasing
	      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	         RenderingHints.VALUE_ANTIALIAS_ON); 
	      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	         RenderingHints.VALUE_TEXT_ANTIALIAS_ON); 
		
	    g2.drawImage(bgImg, 0, 0, null);
	      
		player.drawImage(g2);

		// -- LOOT ARRAY LOOP -- 
		for(int lootIdx = 0; lootIdx < lootArray.size(); lootIdx++)
		{
			lootArray.get(lootIdx).draw(g2);
			
			if(lootArray.size() != lootIdx)
			{
				if(lootArray.get(lootIdx).getBounds().intersects(player.getBounds()))
				{
					if(lootArray.get(lootIdx).getType() == Loot.GUN)
					{
						bulletLevel++;
					}
					if(lootArray.get(lootIdx).getType() == Loot.HP)
					{
						livesLeft++;
					}
					
					lootArray.remove(lootIdx);
				}
				// THE ELSE PREVENTS AN ARRAYOUTOFBOUNDS EXCEPTION WHERE GETTING THE ITEM CAUSES IT
				else if(lootArray.get(lootIdx).lifeOver())
				{
					lootArray.remove(lootIdx);
				}
			}
		}
		
		// -- ENEMY LOOP -- MAIN LOOP -- XX -- XX -- XX
		for(int e = 0; e < enemyArray.size(); e++)
		{	
			enemyArray.get(e).drawImage(g2);
			
			// PLAYER HITS AN ENEMY
			if(enemyArray.get(e).getBounds().intersects(player.getBounds()))
			{	
				playerGotHit();
			}
			
			// BULLET LOOP
			for(int b = 0; b < bulletArray.size(); b++)
			{
				
				//clear bullets outside of stage
				if(bulletArray.get(b).getX() > ParallaxMain.SCREEN_WIDTH || bulletArray.get(b).getX() < 0
					|| bulletArray.get(b).getY() > ParallaxMain.SCREEN_HEIGHT || bulletArray.get(b).getY() < 0)
				{
					bulletArray.remove(b);
				}
				
				if(bulletArray.size() != 0 && bulletArray.size() != b)
				{
					bulletArray.get(b).drawImage(g2);
				}
			}
		}
		
		// in case all enemies die, so that bullets can still be drawn
		if(enemyArray.size() == 0)
		{
			for(int b = 0; b < bulletArray.size(); b++)
			{
				bulletArray.get(b).drawImage(g2);
			}
		}
		
		// LOOP FOR HEAVYENEMY SHOOTING BULLETS
		for(int eb = 0; eb < enemyBullets.size(); eb++)
		{
			if(enemyBullets.size() != eb)
			{
				if(player.getBounds().intersects(enemyBullets.get(eb).getBounds()))
				{
					playerGotHit();
				}
			}
			enemyBullets.get(eb).drawImage(g2);
		}
		
		// -- ANIMATIONS ARRAY LOOP --
		if(animationsArray.size() != 0)
		{
			for(int animationsIdx = 0; animationsIdx < animationsArray.size(); animationsIdx++)
			{
				Animation currElt = animationsArray.get(animationsIdx);
				currElt.drawImage(g2);
				
				if(currElt.animationDone())
				{
					animationsArray.remove(animationsIdx);
				}
			}
		}
		
		g2.setFont(new Font("Time New Roman", Font.PLAIN, 18));
		
		// BLACK DROP SHADOW
		g2.setColor(Color.BLACK);
		g2.drawString("Enemies: ", 12, 32);
		g2.drawString(((Integer)enemyArray.size()).toString(), 90, 32);
		
		g2.drawString("Lives: ", 125, 32);
		g2.drawString(((Integer)livesLeft).toString(), 177, 32);
		
		g2.drawString("Score: ", 212, 32);
		g2.drawString(((Integer)score).toString(), 270, 32);
		g2.setColor(Color.BLACK);
		
		// ACTUAL WHITE TEXT
		g2.setColor(Color.WHITE);
		g2.drawString("Enemies: ", 10, 30);
		g2.drawString(((Integer)enemyArray.size()).toString(), 88, 30);
		
		g2.drawString("Lives: ", 123, 30);
		g2.drawString(((Integer)livesLeft).toString(), 175, 30);
		
		g2.drawString("Score: ", 210, 30);
		g2.drawString(((Integer)score).toString(), 268, 30);
		g2.setColor(Color.BLACK);
	}
	
	public void playerGotHit()
	{
		if(livesLeft > 1){
			livesLeft--;
			player.setX(ParallaxMain.SCREEN_WIDTH/2);
			player.setY((ParallaxMain.SCREEN_HEIGHT/4)*3);
		}
		else
		{
			// GAME IS OVER
			System.out.println("GAME OVER");
			livesLeft = 5;
			
			removeMouseListener(this);
			movementTimer.stop();
			animationTimer.stop();
			waveWaitTimer.stop();
			
			mainClass.displayGameOverFrame(score);
		}
	}
	
	private void checkCollisions()
	{
		for(int e = 0; e < enemyArray.size(); e++)
		{
			for(int b = 0; b < bulletArray.size(); b++)
			{	
				// SOLVES ARRAY OUT OF INDEX EXCEPTION PROBLEM WHEN IDX = ARRAY SIZE
				if(enemyArray.size() != e)
				{
					// -- IF A BULLET HIT AN ENEMY --
					if(bulletArray.get(b).getBounds().intersects(enemyArray.get(e).getBounds()))
					{
						if(bulletLevel <= 2)
						{
							enemyArray.get(e).takeDamage(bulletLevel);
						}
						else
						{
							enemyArray.get(e).takeDamage(2);
						}
						
						if(enemyArray.get(e).getHP() <= 0)
						{
							// ---xx--- KILLED ENEMY ---xx--- //
							enemyKilled(e, b);
						}
						else
						{	
							enemyArray.get(e).rockBack(Math.sin(bulletArray.get(b).getDir())*(-1), 
														Math.cos(bulletArray.get(b).getDir())*(1));
						}
						
						animationsArray.add(new BulletCollisionAnimation(bulletArray.get(b).getX(), bulletArray.get(b).getY()));
						bulletArray.remove(b);
					}
				}
			}
		}
	}
	
	private void enemyKilled(int e, int b)
	{
		//add smaller enemy
		if(enemyArray.get(e).getClassName().equals("Enemy"))
		{	
			//generates random direction for SmallEnemy to start going on
			int randXDir = (int)(Math.random()*2);
			if(randXDir == 0){ randXDir = -1; }
			
			int randYDir = (int)(Math.random()*2);
			if(randYDir == 0){ randYDir = -1; }
			
			// create new SmallEnemy and add to array
			enemyArray.add(new SmallEnemy(enemyArray.get(e).getX(), enemyArray.get(e).getY(), 
							randXDir, randYDir));
			
			animationsArray.add(new EnemyDeathAnimation(enemyArray.get(e).getX(), enemyArray.get(e).getY()));
		}
		
		// drop loot
		if(lootHandler.dropLoot() == Loot.GUN && bulletLevel < 4)
		{
			lootArray.add(new Loot(Loot.GUN, enemyArray.get(e).getX(), enemyArray.get(e).getY()));
		}
		if(lootHandler.dropLoot() == Loot.HP)
		{
			lootArray.add(new Loot(Loot.HP, enemyArray.get(e).getX(), enemyArray.get(e).getY()));
		}
		
		if(enemyArray.get(e).getClassName().equals("HeavyEnemy") && enemyArray.get(e).getBulletsArraySize() > 0)
		{
			for(int temp = 0; temp < enemyArray.get(e).getBulletsArraySize(); temp++)
			{
				enemyBullets.add(enemyArray.get(e).getBulletsArray().get(temp));
			}
		}
		enemyArray.remove(e);
		score++;
	}
	
	private void updateObjectsPos()
	{
		angle = Math.atan2((player.getY()+25) - mouseY, (player.getX()+25) - mouseX) - (Math.PI / 2);
		player.move(angle);
		
		// MOVE PLAYER
		setPlayerMovementSpeed();
		
		// MOVE ENEMIES (CALL THEIR MOVE METHODS)
		for(int i=0; i < enemyArray.size(); i++)
		{
			enemyArray.get(i).move(player.getX(), player.getY());
		}
		
		// MOVE BULLETS
		for(int i=0; i < bulletArray.size(); i++)
		{
			bulletArray.get(i).move();
		}
		
		// MOVE ENEMY BULLETS
		for(int eb = 0; eb < enemyBullets.size(); eb++)
		{
			enemyBullets.get(eb).move();
		}
	}
	
	public void setPlayerMovementSpeed()
	{
		if((player.getX()+player.PLAYER_WIDTH) > ParallaxMain.SCREEN_WIDTH)
		{
			ableToMoveRight = false;
		} 
		else 
		{ 
			ableToMoveRight = true; 
		}
		
		if(player.getX() < 0)
		{
			ableToMoveLeft = false;
		}
		else
		{
			ableToMoveLeft = true;
		}
		
		if((player.getY()+player.PLAYER_HEIGHT) > ParallaxMain.SCREEN_HEIGHT)
		{
			ableToMoveDown = false;
		}
		else
		{
			ableToMoveDown = true;
		}
		
		if(player.getY() < 0)
		{
			ableToMoveUp = false;
		}
		else
		{
			ableToMoveUp = true;
		}
		
		if(ableToMoveRight == false || ableToMoveLeft == false)
		{
			player.setSpeed("x", 0);
		}
		if(ableToMoveUp == false || ableToMoveDown == false)
		{
			player.setSpeed("y", 0);
		}
		
		if(wPressed && ableToMoveUp == true)
		{
			player.setSpeed("y", -1);
		}
		if(aPressed && ableToMoveLeft == true)
		{
			player.setSpeed("x", -1);
		}
		if(sPressed && ableToMoveDown == true)
		{
			player.setSpeed("y", 1);
		}
		if(dPressed && ableToMoveRight == true)
		{
			player.setSpeed("x", 1);
		}
		
		if(wPressed == false && sPressed == false)
		{
			player.setSpeed("y", 0);
		}
		if(aPressed == false && dPressed == false)
		{
			player.setSpeed("x", 0);
		}
	}
	
	private void runScene()
	{
		Thread movementThread = new Thread(
				new Runnable()
				{
					public void run()
					{
						startMoving();
					}
				});
		
		Thread animationThread = new Thread(
				new Runnable()
				{
					public void run()
					{
						startAnimating();
					}
				});
		
		Thread enemyWaveThread = new Thread(
				new Runnable()
				{
					public void run()
					{
						startSpawningEnemies();
					}
				});
		
		Thread collisionsThread = new Thread(
				new Runnable()
				{
					public void run()
					{
						startCheckingCollisions();
					}
				});
		
		movementThread.start();
		animationThread.start();
		enemyWaveThread.start();
		collisionsThread.start();
	}
	
	private void startMoving()
	{
		movementTimer = new Timer(MOVEMENT_TIME,
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						updateObjectsPos();
					}
				});
		
		movementTimer.start();
	}
	
	private void startAnimating()
	{
		animationTimer = new Timer(ANIMATION_TIME,
				new ActionListener()
				{ 
					public void actionPerformed(ActionEvent e)
					{
						repaint();
					}
				});
		
		animationTimer.start();
	}
	
	private void startSpawningEnemies()
	{
		waveWaitTimer = new Timer(waveWaitTime,
				new ActionListener()
				{ 
					public void actionPerformed(ActionEvent e)
					{
						spawnEnemies();
					}
				});
		
		waveWaitTimer.start();
	}
	
	private void startCheckingCollisions()
	{
		collisionsTimer = new Timer(collisionsTime,
				new ActionListener()
				{ 
					public void actionPerformed(ActionEvent e)
					{
						checkCollisions();
					}
				});
		
		collisionsTimer.start();
	}
	
	private void startTimer(final double enemyX, final double enemyY, final double enemyDir)
	{
		Timer tempTimer = new Timer(enemyShootingDelay,
				new ActionListener()
				{ 
					public void actionPerformed(ActionEvent e)
					{
						shootFromEnemy(enemyX, enemyY, enemyDir);
					}
				});
		
		tempTimer.start();
	}
	
	private void shootFromEnemy(double enemyX, double enemyY, double enemyDir)
	{
		enemyBullets.add(new Bullet(enemyX, enemyY, enemyDir, 1));
	}
	
	private void spawnPlayer()
	{
		player = new Hero(200, 200);
	}
   
   private void playSound(String file)
   {
	   Media sound = new Media(Paths.get(file).toUri().toString());
	   mediaPlayer = new MediaPlayer(sound);
	   mediaPlayer.play();
	   
   }
	
	private void spawnEnemies()
	{
		waveHandler.goToNextWave();
		
		//int chanceOfSpawn = (int)(Math.random()*(1/score));
		
		int randX, randY, chanceOfHeavy;
		
		if(enemySpawnCounter == 0)
		{
			randX = (int)(Math.random()*500);
			randY = (int)(Math.random()*250);
			chanceOfHeavy = (int)(Math.random()*9);
			
			if(chanceOfHeavy > 1)
			{
				enemyArray.add(new Enemy(randX, randY, 3));
			}
			else
			{
				enemyArray.add(new HeavyEnemy(randX, randY, this));
			}
			
			// increase rate at which enemies spawn
			if(score < 25)
			{
				enemySpawnCounter = 3;
			}
			else if(score < 45)
			{
				enemySpawnCounter = 2;
			}
			else
			{
				enemySpawnCounter = 1;
			}
		}
		else
		{
			enemySpawnCounter--;
		}
			
		//waveWaitTime -= 50;

	}
	
	private void spawnBullets()
	{
		bulletArray = new ArrayList<Bullet>();
		enemyBullets = new ArrayList<Bullet>();
	}
	
	private void createWaveHandler()
	{
		waveHandler = new EnemyWaveHandler();
	}
	
	public void levelUpBullet(int dl)
	{
		bulletLevel += dl;
	}

	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode() == 87){
			wPressed = true;
		}
		if(e.getKeyCode() == 65){
			aPressed = true;
		}
		if(e.getKeyCode() == 83){
			sPressed = true;
		}
		if(e.getKeyCode() == 68){
			dPressed = true;
		}
	}

	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == 87){
			wPressed = false;
		}
		if(e.getKeyCode() == 65){
			aPressed = false;
		}
		if(e.getKeyCode() == 83){
			sPressed = false;
		}
		if(e.getKeyCode() == 68){
			dPressed = false;
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		mouseX  = e.getX();
		mouseY  = e.getY();
	}
	
	public void mousePressed(MouseEvent e) 
	{
		bulletArray.add(new Bullet(player.getX()+30, player.getY()+30, angle, bulletLevel));
		
		if(bulletLevel >= 4)
		{
			bulletArray.add(new Bullet(player.getX()+30, player.getY()+30, angle+0.2, bulletLevel));
			bulletArray.add(new Bullet(player.getX()+30, player.getY()+30, angle-0.2, bulletLevel));
		}
		
	}

	public void mouseReleased(MouseEvent e) 
	{
		
	}

	public void keyTyped(KeyEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	

}
