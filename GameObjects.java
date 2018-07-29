package brickBreakerGame;

import java.util.Random;

public enum GameObjects {
	FIREBALL, SHORTEN, LENGTHEN, BOMB, EXTRA_LIFE, NONE;
	
	// function to return a random GameObject
	public static GameObjects getRandomObj() {
		int randObj = new Random().nextInt(values().length);
		return values()[randObj];
	}
}
