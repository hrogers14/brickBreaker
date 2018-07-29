package brickBreakerGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bricks extends Rectangle {
	/**
	 * The bricks the user must break before winning the game
	 */
	
	private static final long serialVersionUID = 1L;
	private GameObjects hiddenObj; // type of hidden object inside brick 
	private int durability; // determines how many time the ball must hit the brick before breaking it
	private Color[] colorArr = {Color.blue, Color.green, Color.yellow, Color.orange, Color.red};
	
	public Bricks(int x, int y, int width, int height, int durability) {
		setBounds(x, y, width, height);
		this.hiddenObj = GameObjects.NONE; // initialize brick with no hidden objects
		this.durability = durability;
	}
	
	public void placeHiddenObj(GameObjects obj) {
		this.hiddenObj = obj;
	}
	
	public GameObjects getHiddenObj() {
		return hiddenObj;
	}
	
	public int getDurability() {
		return this.durability;
	}
	
	public void decrDurability() {
		this.durability--;
	}
	
	public void drawBrick(Graphics g) {
		g.setColor(colorArr[this.durability-1]);
		g.fillRect(this.x+1, this.y, this.width-1, this.height-1);
	}
}
