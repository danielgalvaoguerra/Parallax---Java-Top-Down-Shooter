import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JComponent;


public class GameOverFrame extends JComponent implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Image gameOverImg;
	
	private int score;
	private int scoreDisplayX = ParallaxMain.SCREEN_WIDTH/2 - 10;
	private int scoreDisplayY = ParallaxMain.SCREEN_HEIGHT/2 + 15;
	
	private int backBtnX= 384;
	private int backBtnY = 420;
	private int backBtnWidth = 234;
	private int backBtnHeight = 69;
	
	private ParallaxMain main;
	
	GameOverFrame(ParallaxMain main, int score)
	{
		this.score = score;
		this.main = main;
		
		addMouseListener(this);
		
		gameOverImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		getImages("images/GameOverFramePic.png");
	}
	
	private void getImages(String fileName)
	{
		File gameOverPicPath = new File(fileName);
		
		try
		{
			gameOverImg = ImageIO.read(gameOverPicPath);
		}
		catch(Exception e)
		{}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		//NOTE: back button is not a separate image - it's contained in 'gameOverImg', backBtnX/Y are still correct values of where the button is placed
		g2.drawImage(gameOverImg, 0, -80, null);
		
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Times", Font.PLAIN, 35));
		g2.drawString(((Integer)(score)).toString(), scoreDisplayX, scoreDisplayY);
		
		// to make sure btnx/y vals are right
		//g2.drawRect(backBtnX,backBtnY,backBtnWidth,backBtnHeight);
		
		repaint();
	}
	
	public void mouseClicked(MouseEvent e) {
		// if back button is clicked, go to title frame
		if(e.getX() < (backBtnX + backBtnWidth) && e.getX() > backBtnX &&
			e.getY() < (backBtnY + backBtnHeight) && e.getY() > backBtnY)
		{
			removeMouseListener(this);
			main.displayTitleFrame();
		}
		
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

}
