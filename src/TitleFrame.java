import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;


public class TitleFrame extends JPanel implements MouseListener, MouseMotionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ParallaxMain mainClass;
	
	private Image titleFrameImg;
	private static final int TITLEIMG_WIDTH = 1000;
	private static final int TITLEIMG_HEIGHT = 700;
	private static final int TITLEIMG_X = 0 - 2;
	private static final int TITLEIMG_Y = 0 - 2;
	
	private Image startButtonImg;
	private static final int STARTBTN_WIDTH = 235;
	private static final int STARTBTN_HEIGHT = 70;
	private static final int STARTBTN_X = (ParallaxMain.SCREEN_WIDTH/2) - (STARTBTN_WIDTH/2);
	private static final int STARTBTN_Y = ((ParallaxMain.SCREEN_HEIGHT/2) - (STARTBTN_HEIGHT/2)) + 75;
	private static final Rectangle STARTBTN_BOUNDS = new Rectangle(STARTBTN_X, STARTBTN_Y, STARTBTN_WIDTH, STARTBTN_HEIGHT);
	
	private Image instructionsButtonImg;
	private static final int INSTRUCTIONSBTN_WIDTH = 235;
	private static final int INSTRUCTIONSBTN_HEIGHT = 70;
	private static final int INSTRUCTIONSBTN_X = (ParallaxMain.SCREEN_WIDTH/2) - (INSTRUCTIONSBTN_WIDTH/2);
	private static final int INSTRUCTIONSBTN_Y = ((ParallaxMain.SCREEN_HEIGHT/2) - (INSTRUCTIONSBTN_HEIGHT/2)) + 170; // const. should be ~100 + startbtn const.
	private static final Rectangle INSTRUCTIONSBTN_BOUNDS = new Rectangle(INSTRUCTIONSBTN_X, INSTRUCTIONSBTN_Y, 
																		INSTRUCTIONSBTN_WIDTH, INSTRUCTIONSBTN_HEIGHT);
	
	private Image starsImg;
	private double starsImgX = -10;
	private double starsImgY = 0;
	private double starsImgWidth = 1000;
	private double starsImgHeight = 700;
	
	private Image titleImg;
	private double titleImgX = 0;
	private double titleImgY = 0;
	private double titleImgWidth = 1000;
	private double titleImgHeight = 700;
	
	private Image eyeImg;
	private double eyeImgX = -148;
	private double eyeImgY = ParallaxMain.SCREEN_HEIGHT - 148;
	private double eyeImgWidth = 296;
	private double eyeImgHeight = 296;
	
	private int animationTime = 60;
	private Timer animationTimer;
	private boolean expand = true;
	
	double mouseX;
	double mouseY;
	
	double counter = 1;
	
	public TitleFrame(ParallaxMain c)
	{
		mainClass = c;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		starsImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		titleImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		eyeImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		
		titleFrameImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);		
		startButtonImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		instructionsButtonImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		
		getBackgroundImages();
		getImages("images/playGameButton.png", "images/instructionsButton.png");
		
		startAnimating();
	}
	
	private void getBackgroundImages()
	{
		File starsImgPath = new File("images/stars.png");
		File titleImgPath = new File("images/title.png");
		File eyeImgPath = new File("images/eyetitlescreen.png");
		
		try
		{
			starsImg = ImageIO.read(starsImgPath);
			titleImg = ImageIO.read(titleImgPath);
			eyeImg = ImageIO.read(eyeImgPath);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void getImages(String fileName1, String fileName2) 
	{
		if(fileName1 != null)
		{
			File startGameButtonPath = new File(fileName1);
			
			try
			{
				startButtonImg = ImageIO.read(startGameButtonPath);
			}
			catch(Exception e)
			{}
		}
		
		if(fileName2 != null)
		{
			File instructionsButtonPath = new File(fileName2);
			
			try
			{
				instructionsButtonImg = ImageIO.read(instructionsButtonPath);
			}
			catch(Exception e)
			{}
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g; 
		
		g2.setColor(new Color(15,45,83));
		g2.fillRect(0, 0, ParallaxMain.SCREEN_WIDTH, ParallaxMain.SCREEN_HEIGHT);
		
		// title back and forth animation
		if(expand == true)
		{
			starsImgWidth += 2;	titleImgWidth -= 2;
			starsImgHeight += 2;	titleImgHeight -= 2;
			starsImgX -= 1;	titleImgX += 1;
			starsImgY -= 1;	titleImgY += 1;
		}
		else
		{
			starsImgWidth -= 2;	titleImgWidth += 2;
			starsImgHeight -= 2;	titleImgHeight += 2;
			starsImgX += 1;	titleImgX -= 1;
			starsImgY += 1;	titleImgY -= 1;
		}
		if(starsImgWidth >= 1060)
		{
			counter = 1;
			expand = false;
		}
		if(starsImgWidth <= 1000)
		{
			counter = 1;
			expand = true;
		}
		
		g2.drawImage(starsImg, (int)starsImgX, (int)starsImgY, (int)starsImgWidth, (int)starsImgHeight, null);
		g2.drawImage(titleImg, (int)titleImgX-20, (int)titleImgY-20, (int)titleImgWidth+40, (int)titleImgHeight+40, null);
		
		double angle = Math.atan2(700 - mouseY, 0 - mouseX) - (Math.PI / 2);
		g2.rotate(angle, 0, ParallaxMain.SCREEN_HEIGHT);
		g2.drawImage(eyeImg, (int)eyeImgX, (int)eyeImgY, null);
		g2.rotate(-angle, 0, ParallaxMain.SCREEN_HEIGHT);
	
		//g2.drawImage(titleFrameImg, TITLEIMG_X, TITLEIMG_Y, null);
		g2.drawImage(startButtonImg, STARTBTN_X, STARTBTN_Y, null);
		g2.drawImage(instructionsButtonImg, INSTRUCTIONSBTN_X, INSTRUCTIONSBTN_Y, null);
	}
	
	private void startAnimating()
	{
		animationTimer = new Timer(animationTime,
				new ActionListener()
				{ 
					public void actionPerformed(ActionEvent e)
					{
						repaint();
					}
				});
		
		animationTimer.start();
	}

	public void mousePressed(MouseEvent e) 
	{
		mouseX = e.getX();
		mouseY = e.getY();
		
		if(mouseX >= STARTBTN_X && mouseX <= (STARTBTN_X + STARTBTN_WIDTH)
				&& mouseY >= STARTBTN_Y && mouseY <= (STARTBTN_Y + STARTBTN_HEIGHT))
		{
			// CLICKED START GAME
			removeMouseListener(this);
			mainClass.displayGameplayFrame();
		}
		
		if(mouseX >= INSTRUCTIONSBTN_X && mouseX <= (INSTRUCTIONSBTN_X + INSTRUCTIONSBTN_WIDTH)
				&& mouseY >= INSTRUCTIONSBTN_Y && mouseY <= (INSTRUCTIONSBTN_Y + INSTRUCTIONSBTN_HEIGHT))
		{
			// CLICKED INSTRUCTIONS BTN
			removeMouseListener(this);
			mainClass.displayInstructionsFrame();
		}
		
	}
	
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
		
		Rectangle mouseBounds = new Rectangle(e.getX(), e.getY(), 2, 2);
		
		if(mouseBounds.intersects(STARTBTN_BOUNDS))
		{
			getImages("images/playGameButton_Hovered2.png", null);
		}
		else
		{
			getImages("images/playGameButton.png", null);
		}
		
		if(mouseBounds.intersects(INSTRUCTIONSBTN_BOUNDS))
		{
			getImages(null, "images/instructionsButton_Hovered.png");
		}
		else
		{
			getImages(null, "images/instructionsButton.png");
		}
	}
	
	public void mouseDragged(MouseEvent e){}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
