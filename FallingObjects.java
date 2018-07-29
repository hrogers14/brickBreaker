package brickBreakerGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class FallingObjects extends Rectangle {
	/**
	 * The objects that fall from bricks, and can change the state of the game
	 */
	
	private static final long serialVersionUID = 1L;
	// variable to determine which falling object is to be used
	private GameObjects object;
	
	public FallingObjects(int x, int y, int width, int height, GameObjects object) {
		setBounds(x, y, width, height);
		this.object = object;
	}
	
	// falling objects only move downwards, so just change the y direction
	public void moveObject() {
		this.y += 1;
	}
	
	public GameObjects getObjectType() {
		return object;
	}
	
	public void drawObject(Graphics g) {
		// center the object
		int newX = this.x + 17;
		// check which object is to be used, and draw the object accordingly
		switch(this.object) {
		case FIREBALL:
			g.setColor(Color.orange);
			g.fillOval(newX, this.y, this.width, this.height);
			break;
		case SHORTEN:
			g.setColor(Color.black);
			g.drawLine(newX-2, this.y+this.height/2, newX+2 + this.width, this.y+this.height/2);
			g.drawLine(newX + this.width/4, this.y + this.height/4, newX + this.width*3/4, this.y + this.height*3/4);
			g.drawLine(newX + this.width*3/4, this.y + this.height/4, newX + this.width/4, this.y + this.height*3/4);
			break;
		case LENGTHEN:
			g.setColor(Color.black);
			g.drawLine(newX, this.y+this.height/2, newX + this.width, this.y+this.height/2);
			g.drawLine(newX + this.width/4, this.y + this.height/4, newX, this.y+height/2);
			g.drawLine(newX + this.width*3/4, this.y + this.height/4, newX + this.width, this.y+height/2);
			g.drawLine(newX + this.width/4, this.y + this.height*3/4, newX, this.y+height/2);
			g.drawLine(newX + this.width*3/4, this.y + this.height*3/4, newX + this.width, this.y+height/2);
			break;
		case BOMB:
			g.setColor(Color.black);
			g.fillOval(newX, this.y, this.width, this.height);
			g.setColor(Color.red);
			g.drawLine(newX + this.width/4, this.y + this.height/4, newX + this.width*3/4, this.y + this.height*3/4);
			g.drawLine(newX + this.width*3/4, this.y + this.height/4, newX + this.width/4, this.y + this.height*3/4);
			break;
		case EXTRA_LIFE:
			g.setColor(Color.red);
			int[] triangleXPoints = {newX, newX + this.width, newX + this.width/2};
			int[] triangleYPoints = {this.y + this.height/2, this.y + this.height/2, this.y + this.height};
			g.fillPolygon(triangleXPoints, triangleYPoints, triangleXPoints.length);
			g.fillOval(newX-1, this.y, this.width/2+1, this.height*3/5);
			g.fillOval(newX+this.width/2, this.y, this.width/2+1, this.height*3/5);
			break;
		default:
			break;
		}
	}
}
