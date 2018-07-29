package brickBreakerGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Ball extends Rectangle {
	/**
	 * The ball that moves around the brick breaker game
	 */
	
	private static final long serialVersionUID = 1L;
	// amount by which ball will move in the x and y direction
	private int ballDx;
	private int ballDy;
	
	// variable to hold color for when ball is changed to fireMode
	private Color ballColor;
	private boolean fireMode;
	
	public Ball(int x, int y, int width, int height, Color color) {
		setBounds(x, y, width, height);
		init(x, y);
		this.ballColor = color;
		this.fireMode = false;
	}
	
	// move ball by current dx values
	public void moveBall() {
		this.x += ballDx;
		this.y += ballDy;
	}
	
	// change the amount by which the ball will move in the x and y directions
	public void changeDirection(int newDx, int newDy) {
		ballDx = newDx;
		ballDy = newDy;
	}
	
	public int getBallDx() {
		return ballDx;
	}
	
	public int getBallDy() {
		return ballDy;
	}
	
	// enable/disable firemode
	public void setFireMode(boolean enable) {
		this.ballColor = enable ? Color.orange : Color.black;
		fireMode = enable;
	}
	
	// function to determine whether the ball is in firemode
	public boolean getFireMode() {
		return fireMode;
	}
	
	// initialize location of ball, and amount ball moves in x and y direction
	public void init(int x, int y) {
		this.x = x;
		this.y = y;
		this.ballDx = 3;
		this.ballDy = 3;
	}
	
	public void drawBall(Graphics2D g) {
		g.setColor(ballColor);
		g.fillOval(this.x, this.y, this.width, this.height);
	}

}
