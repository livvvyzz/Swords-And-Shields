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
		if (x <= 9 && x >= 0) {
			this.x = x;
			if(y <= 9 && y >= 0){
				this.y = y;
				isOnBoard = true;
			}
		}
		
		//throws exception if they are outside the board
		if(isOnBoard){
			throw new GameError("Attempting to move token to location that is not on the board");
		}
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
