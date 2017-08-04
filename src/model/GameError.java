package model;

public class GameError extends RuntimeException{

	/**
	 * Represents a runtime exception in the game
	 * @param msg
	 */
	public GameError(String msg){
		super(msg);
	}
}
