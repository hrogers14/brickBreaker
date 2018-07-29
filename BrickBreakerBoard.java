package brickBreakerGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BrickBreakerBoard extends JPanel implements ActionListener, KeyListener {
	/**
	 * Brick Breaker board containing all elements of game
	 */
	private static final long serialVersionUID = 1L;
	private final int BRICK_WIDTH = 50;
	private final int BRICK_HEIGHT = 15;
	private final int USER_Y_LOC = 450;
	private final int USER_WIDTH = 70;
	private final int USER_HEIGHT = 10;
	private final int BALL_SIZE = 20;
	private final int BALL_START_YLOC = 75;
	private final int BOUNDARY_MAX_X = 500;
	private final int TIMER_VAL = 8;
	private final int ONE_SECOND_TICKS = 1000/TIMER_VAL;
	private final int GAME_STATS_RECT_HEIGHT = 50;
	private final int GAME_STATS_START_LOC = 490;
	private final int HIDDEN_OBJECT_SIZE = 15;
	
	Timer timer = new Timer(TIMER_VAL, this);
	UserBoard user = new UserBoard((BOUNDARY_MAX_X-USER_WIDTH)/2, USER_Y_LOC, USER_WIDTH, USER_HEIGHT, 0);
	Ball ball = new Ball(0, BALL_START_YLOC, BALL_SIZE, BALL_SIZE, Color.black);
	
	ArrayList<Bricks> bricksArr = new ArrayList<Bricks>();
	ArrayList<FallingObjects> fallingObjects = new ArrayList<FallingObjects>();
	
	private boolean stateChangedTimerRunning;
	private int stateChangedTimer;
	
	private int lives;
	private int level;
	private int score;
	
	private boolean countDownTimerRunning;
	private int countDownTimer;
	private int countDown;
	
	public BrickBreakerBoard() {
		addKeyListener(this);
		setBackground(Color.gray);
		setFocusable(true);
		
		// initialize level, number of lives, score and gameOver tracker
		level = 1;
		lives = 3;
		score = 0;
		
		// set up the board for teh correct level, and start the timer
		initLevel();
		timer.start();
	}
	
	public void startCountDown() {
		countDownTimerRunning = true;
		countDownTimer = 0;
		countDown = 3;
	}
	
	public void initLevel() {
		// reset the current state in case the state has been changed from a falling object, and start the count down timer
		resetMode();
		startCountDown();
		
		// reset the ball and user to the starting locations
		ball.init(0, BALL_START_YLOC);
		user.setLocation((BOUNDARY_MAX_X-USER_WIDTH)/2, USER_Y_LOC);
		fallingObjects.clear();
		
		// set the correct brick formations according to current level
		switch(level) {
		case 1:
			int val = 3;
			for (int x = 0; x < 10; x++) {
				for (int y = 1; y < 4; y++) {
					bricksArr.add(new Bricks(x*BRICK_WIDTH, y*BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT, val));
					val--;
				}
				val = 3;
			}
			break;
		case 2:
			int curVal = 2;
			boolean zero = true;
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 5; y++) {
					if (!zero) {
						bricksArr.add(new Bricks(x*BRICK_WIDTH, y*BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT, curVal));
					}
					zero = !zero;
					curVal = (curVal >= 4) ? ((curVal == 4) ? 1 : 2): curVal+2; 
				}
			}
			break;
		case 3:
			int curDur = 5;
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 5; y++) {
					bricksArr.add(new Bricks(x*BRICK_WIDTH, y*BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT, curDur));
				}
				curDur--;
				if (curDur < 1)
					curDur = 5;
			}
			break;
		case 4:
			curVal = 5;
			for (int y = 4; y >=0; y--) {
				for (int x = 0; x < 10; x++) {
					bricksArr.add(new Bricks(x*BRICK_WIDTH, y*BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT, curVal));
				}
				curVal--;
			}
			break;
		case 5:
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 5; y++) {
					bricksArr.add(new Bricks(x*BRICK_WIDTH, y*BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT, 5));
				}
			}
			break;
		}
		placeFallingObjects();
	}
	
	public void placeFallingObjects() {
		// choose 10 random bricks in which to place a random falling object 
		int randBrick = 0;
		for (int i = 0; i < 10; i++) {
			boolean objPlaced = false;
			while (!objPlaced) {
				randBrick = new Random().nextInt(bricksArr.size());
				// place a random object in a random brick, only if the brick does not have an object yet
				if (bricksArr.get(randBrick).getHiddenObj() == GameObjects.NONE) {
					bricksArr.get(randBrick).placeHiddenObj(GameObjects.getRandomObj());
					objPlaced = true;
				}
			}
		}
	}
	
	public void checkFallingObjects() {
		// check each of the falling objects to determine whether they have been caught, or have gone off board
		for (int idx = 0; idx < fallingObjects.size(); idx++) {
			// check if the user caught a falling object
			if (user.intersects(fallingObjects.get(idx))) {
				resetMode(); // reset the state, as only one state change is allowed at a time
				score += 50;
				// check the falling objects to determine the change of state required
				switch(fallingObjects.get(idx).getObjectType()) {
				case FIREBALL:
					ball.setFireMode(true);
					stateChangedTimerRunning = true;
					break;
				case SHORTEN:
					user.setWidth(USER_WIDTH - 10);
					stateChangedTimerRunning = true;
					break;
				case LENGTHEN:
					user.setWidth(USER_WIDTH + 10);
					stateChangedTimerRunning = true;
					break;
				case BOMB:
					lives--;
					break;
				case EXTRA_LIFE:
					lives++;
					break;
				default:
					break;
				}
				fallingObjects.remove(idx);
			} else if (fallingObjects.get(idx).getY() > user.getMaxY()) {
				// if the falling object goes off board, remove it 
				fallingObjects.remove(idx);
			}
		}
	}
	
	public void drawBoard(Graphics g2d) {
		// draw each of the bricks left
		for (int brickIdx = 0; brickIdx < bricksArr.size(); brickIdx++) {
			bricksArr.get(brickIdx).drawBrick(g2d);
		}
		
		// draw the bottom rectangle with the score, level, and lives left
		g2d.setColor(Color.darkGray);
		g2d.fillRect(0, GAME_STATS_START_LOC, BOUNDARY_MAX_X, GAME_STATS_RECT_HEIGHT);
		g2d.setColor(Color.black);
		g2d.drawString("SCORE:" + score, 10, GAME_STATS_START_LOC + GAME_STATS_RECT_HEIGHT/2);
		g2d.drawString("LEVEL:" + level, BOUNDARY_MAX_X/2 - 20, GAME_STATS_START_LOC + GAME_STATS_RECT_HEIGHT/2);
		g2d.drawString("LIVES:" + lives, BOUNDARY_MAX_X - 60, GAME_STATS_START_LOC + GAME_STATS_RECT_HEIGHT/2);
		
		// if counting down, print the count down value in the center of the screen
		if (countDownTimerRunning) {
			g2d.drawString("" + countDown, BOUNDARY_MAX_X/2, GAME_STATS_START_LOC/2);
		}
	}
	
	public Point getNewDirection(Rectangle intersector) {
		Point newDir = new Point();
		Rectangle intersection = ball.intersection(intersector);
		// if the ball intersects with the side of a brick, negate x direction
		if ((ball.getBallDy() < 0) && (intersection.getMaxY() < intersector.getMaxY())
				|| (ball.getBallDy() > 0) && (intersection.getMinY() > intersector.getMinY())) {
			newDir.x = ~ball.getBallDx()+1;
			newDir.y = ball.getBallDy();
		// if the ball intersects with the top or bottom of a brick, negate y direction and change
		// the x value based off of the part of the brick that the ball hit
		} else {
			newDir.x = (int) (ball.getBallDx()+0.1*(intersection.getCenterX() - intersector.getCenterX()));
			newDir.y = ~ball.getBallDy()+1;
		}
		return newDir;
	}
	
	public void checkLevelComplete() {
		// if no more bricks are left, increase the level
		if (bricksArr.size() == 0) {
			level++;
			if (level <= 5) { // only 5 levels, then user wins
				initLevel();
			} else {
				timer.stop();
				JOptionPane.showMessageDialog(this, "You win!", "Game Over", JOptionPane.YES_NO_OPTION);
				System.exit(ABORT);
			}
		}
	}
	
	public void checkGameOver() {
		// check if the ball passes the user board
		if (ball.getMinY() > user.getMaxY()) {
			lives--;
			fallingObjects.clear();
			if (lives == 0) { // if no more lives, game over
				timer.stop();
				JOptionPane.showMessageDialog(this, "You lose!", "Game Over", JOptionPane.YES_NO_OPTION);
				System.exit(ABORT);
			} else { // if more lives, reset the board pieces, and start count down again
				startCountDown();
				resetMode();
				ball.init(0, BALL_START_YLOC);
				user.setLocation((BOUNDARY_MAX_X-USER_WIDTH)/2, USER_Y_LOC);
			}
		}
	}
	
	public void checkCollision() {
		// if the ball hits the right or left wall, negate x direction
		if ((ball.getMaxX() >= BOUNDARY_MAX_X) || (ball.getX() <= 0)){
			ball.changeDirection(~ball.getBallDx() + 1, ball.getBallDy());
			return;
		}
		
		// if the ball hits the ceiling, negate y direction
		if (ball.getY() < 0) {
			ball.changeDirection(ball.getBallDx(), ~ball.getBallDy()+1);
			return;
		}
		
		// if the ball hits the user board, change ball direction based off of where the ball hits
		if (user.intersects(ball)) {
			Point newDir = getNewDirection(user);
			ball.changeDirection(newDir.x, newDir.y);
			return;
		}

		// check if the ball hits any of the bricks on the board
		for (int idx = 0; idx < bricksArr.size(); idx++) {
			if (ball.intersects(bricksArr.get(idx))) {
				if (!ball.getFireMode()) { // if in fire mode, the ball does not change direction
					Point newDir = getNewDirection(bricksArr.get(idx));
					ball.changeDirection(newDir.x, newDir.y);
				}
				bricksArr.get(idx).decrDurability(); // decrease brick durability
				if (bricksArr.get(idx).getDurability() == 0) { // check if brick should be removed
					if (bricksArr.get(idx).getHiddenObj() != GameObjects.NONE) { // check if the removed brick contains a hidden object
						fallingObjects.add(new FallingObjects((int) bricksArr.get(idx).getX(), (int) bricksArr.get(idx).getY(), 
								HIDDEN_OBJECT_SIZE, HIDDEN_OBJECT_SIZE, bricksArr.get(idx).getHiddenObj()));
					}
					bricksArr.remove(idx);
					score += 100;
				} else  {
					score += 10;
				}
			}
		}
	}
	
	public void resetMode() {
		// reset the state of ball and user to normal game play
		stateChangedTimer = 0;
		stateChangedTimerRunning = false;
		user.setWidth(USER_WIDTH);
		ball.setFireMode(false);
	}
	
	@Override 
	public void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintBoard(g);
    }

    private void paintBoard(Graphics g) {
    		// paint the board, user, ball, and falling objects
    		Graphics2D g2d = (Graphics2D) g;
    		drawBoard(g2d);
    		user.drawBoard(g2d);
    		ball.drawBall(g2d);
    		for (int idx = 0; idx < fallingObjects.size(); idx++) {
    			fallingObjects.get(idx).drawObject(g2d);
    		}
    }

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		// call User Board function to change user board direction
		user.keyEvent(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// keep moving user board until the right/left key is released
		user.stopMoving();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// check if the count down is running
		if (countDownTimerRunning) {
			countDownTimer++;
			if (countDownTimer > ONE_SECOND_TICKS) {
				countDown--;
				countDownTimer = 0;
				if (countDown == 0) {
					countDownTimerRunning = false;
				}
			}
		} else {
			// if count down timer is not running, move the ball, user board, and fallingObjects
			ball.moveBall();
			user.move();
			// check if the user board goes off board, and place the board on the opposite end
			if ((user.getMinX() > BOUNDARY_MAX_X) || (user.getMaxX() < 0))
				user.setLocation((user.getMinX() > BOUNDARY_MAX_X) ? 0 : BOUNDARY_MAX_X-USER_WIDTH, USER_Y_LOC);
			for (int idx = 0; idx < fallingObjects.size(); idx++) {
				fallingObjects.get(idx).moveObject();
			}
			
			// check if the ball collides with any objects, and check if falling objects were caught
			checkCollision();
			checkFallingObjects();
			
			// if the game is not in normal mode, run the timer before resetting back to normal mode
			if (stateChangedTimerRunning) {
				stateChangedTimer++;
				if (stateChangedTimer > 4000) {
					resetMode();
				}
				
			}
			
			// check if the level is over, and if the game has been lost
			checkLevelComplete();
			checkGameOver();
		}
		repaint();
	}
	
	
}