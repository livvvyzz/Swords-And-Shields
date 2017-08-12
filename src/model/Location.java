package model;
/**
 * Represents the location of tokens on the board
 * 
 * @author olivia
 *
 */
public class Location {

	private int x;
	private int y;

	/**
	 * Creates the location
	 * 
	 * @param x
	 * @param y
	 */
	public Location(int x, int y) { 
		boolean isOnBoard = false;
		//ensures coordinates are within the board
		//invalid squares
		if((x == 0 && y == 0) || (x == 0 && y == 1) || (x == 1 && y == 0) || (x == 1 && y == 1) || (x == 8 && y == 8) ||
				(x == 9 && y == 9) || (x == 8 && y == 9) || (x == 9 && y == 8))
			throw new GameError("Attempting to move token to location that is an invalid square");

		if (x <= 9 && x >= 0) {
			this.x = x;
			if(y <= 9 && y >= 0){
				this.y = y;
				isOnBoard = true;
			}
		}
		
		//throws exception if they are outside the board
		if(!isOnBoard){
			throw new GameError("Attempting to move token to location that is not on the board");
		}
	}
	
	/**
	 * Only called in the makeSqaures method - lets the tokens go into the invalid sqaures 
	 * @param x
	 * @param y
	 * @param invalidSqaure
	 */
	public Location(int x, int y, boolean invalidSqaure){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the location
	 * @return		location that the token is on
	 */
	public Location getLocation(){
		return this;
	}
	
	/**
	 * Returns x coordinate
	 * @return x
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Returns y coordinate
	 * @return y
	 */
	public int getY(){
		return y;
	}
}
