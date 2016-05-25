import java.awt.Rectangle;
import java.nio.file.Paths;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.JFrame;

/*
 * Daniel Galvao Guerra
 * June 2015
 */
public class ParallaxMain {
	
	private JFrame frame;
	//private JPanel mainPanel;
	
	public static final int SCREEN_WIDTH = 992;
	public static final int SCREEN_HEIGHT = 558;
	
	private GameplayFrame gpFrame;
	private TitleFrame tFrame;
	private GameOverFrame gameOFrame;
	private InstructionsFrame iFrame;
	
	private static MediaPlayer mediaPlayer;
	JFXPanel panel = new JFXPanel();

	public ParallaxMain()
	{
		frame = new JFrame();
		
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Epic Adventures");
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		displayTitleFrame();
		
		// for testing purposes
		//displayGameplayFrame();
		//displayGameOverFrame(0);
		//displayInstructionsFrame();
		
		playSound("sounds/bgSound.mp3");
	}
	
	private void playSound(String file)
	{
		Media sound = new Media(Paths.get(file).toUri().toString());
		mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}
	
	public void displayGameplayFrame() 
	{
		frame.getContentPane().removeAll();
		
		gpFrame = new GameplayFrame(this);
		
			//handle the insets of the JFrame
		      int contentWidth = SCREEN_WIDTH 
		         - frame.getInsets().left - frame.getInsets().right;
		      int contentHeight = SCREEN_HEIGHT 
		         - frame.getInsets().top - frame.getInsets().bottom;
		
		frame.add(gpFrame);
		gpFrame.setFocusable(true);
		gpFrame.requestFocusInWindow();
		
		frame.setVisible(true);
	}
	
	public void displayTitleFrame()
	{
	    frame.getContentPane().removeAll();
		
		tFrame = new TitleFrame(this);
		
			//handle the insets of the JFrame
		      int contentWidth = SCREEN_WIDTH 
		         - frame.getInsets().left - frame.getInsets().right;
		      int contentHeight = SCREEN_HEIGHT 
		         - frame.getInsets().top - frame.getInsets().bottom;
		
		frame.add(tFrame);
		tFrame.setFocusable(true);
		tFrame.requestFocusInWindow();
		
		frame.setVisible(true);
	}
	
	public void displayGameOverFrame(int score)
	{
		frame.getContentPane().removeAll();
		
		gameOFrame = new GameOverFrame(this, score);
		
			//handle the insets of the JFrame
		      int contentWidth = SCREEN_WIDTH 
		         - frame.getInsets().left - frame.getInsets().right;
		      int contentHeight = SCREEN_HEIGHT 
		         - frame.getInsets().top - frame.getInsets().bottom;
		
		frame.add(gameOFrame);
		gameOFrame.setFocusable(true);
		gameOFrame.requestFocusInWindow();
		
		frame.setVisible(true);
	}
	
	public void displayInstructionsFrame()
	{
		frame.getContentPane().removeAll();
		
		iFrame = new InstructionsFrame(this);
		
			//handle the insets of the JFrame
		      int contentWidth = SCREEN_WIDTH 
		         - frame.getInsets().left - frame.getInsets().right;
		      int contentHeight = SCREEN_HEIGHT 
		         - frame.getInsets().top - frame.getInsets().bottom;
		
		frame.add(iFrame);
		iFrame.setFocusable(true);
		iFrame.requestFocusInWindow();
		
		frame.setVisible(true);
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
	}

	public static void main(String[] args)
	{
		new ParallaxMain();
	}
}
