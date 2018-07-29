package brickBreakerGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class UserBoard extends Rectangle {
	/**
	 * The board that the user controls to keep the ball up
	 */
	
	private static final long serialVersionUID = 1L;
	private int dx; // holds the value to move the user board by
	
	public UserBoard(int x, int y, int width, int height, int dx) {
		setBounds(x,y,width,height);
		this.dx = dx;
	}
	
	public void drawBoard(Graphics g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(this.x, this.y, this.width, this.height);
	}
	
	// move user board dx amount
	public void move() {
		this.x += this.dx;
	}
	
	// change the dx amount to 0, so the user stops moving
	public void stopMoving() {
		this.dx = 0;
	}
	// set width function to use when a decr/incr falling object is caught
	public void setWidth(int newWidth) {
		setBounds(this.x, this.y, newWidth, this.height);
	}
	
	// change direction of user board based off of key that is pressed
	public void keyEvent(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			this.dx = 3;
			break;
		case KeyEvent.VK_LEFT:
			this.dx = -3;
			break;
		}
	}

}
