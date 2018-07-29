package brickBreakerGame;
import javax.swing.JFrame;

public class BrickBreakerGame extends JFrame {
	/**
	 * Initializes the Brick Breaker frame 
	 */
	private static final long serialVersionUID = 1L;

	public BrickBreakerGame() {
		initGame();
	}
	
	private void initGame() {
		add(new BrickBreakerBoard());
		setSize(500, 540);
		
		setTitle("Brick Breaker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
	}
	
	public static void main(String[] args) {
		BrickBreakerGame ex = new BrickBreakerGame();
		ex.setVisible(true);
	}
}
