import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JComponent;


public class InstructionsFrame extends JComponent implements MouseListener {

	private ParallaxMain main;
	
	private Image instructionsImg;
	private int origImgWidth = 995;
	private int origImgHeight = 558;
	
	private int backBtnX= 700;
	private int backBtnY = 447;
	private int backBtnWidth = 234;
	private int backBtnHeight = 69;
	
	public InstructionsFrame(ParallaxMain main)
	{
		this.main = main;
		
		addMouseListener(this);
		
		instructionsImg = new BufferedImage(origImgWidth, origImgHeight, BufferedImage.TYPE_INT_RGB);
		getImages("images/InstructionsFramePic.png");
	}
	
	private void getImages(String fileName)
	{
		File instructionsPicPath = new File(fileName);
		
		try
		{
			instructionsImg = ImageIO.read(instructionsPicPath);
		}
		catch(Exception e)
		{}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		g2.drawImage(instructionsImg, -5, -8, origImgWidth, origImgHeight, null);
		
		//g2.setColor(Color.RED);
		//g2.drawRect(backBtnX, backBtnY,backBtnWidth, backBtnHeight);
		
		repaint();
	}
	
	public void mouseClicked(MouseEvent e) 
	{
		System.out.println(e.getY());
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
