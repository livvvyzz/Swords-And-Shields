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

	private boolean isOnBoard;

	/**
	 * Creates the location
	 * 
	 * @param x 
	 * @param y
	 */
	public Location(int x, int y) {
		isOnBoard = false;

		if (x <= 9 && x >= 0) {
			if (y <= 9 && y >= 0) {
				if (!((x == 0 && y == 0) || (x == 0 && y == 1) || (x == 1 && y == 0) || (x == 1 && y == 1)
						|| (x == 8 && y == 8) || (x == 9 && y == 9) || (x == 8 && y == 9) || (x == 9 && y == 8))) {
					isOnBoard = true;
				}
			}
		}
		this.y = y;
		this.x = x;
	}

	/**
	 * Only called in the makeSqaures method - lets the tokens go into the
	 * invalid sqaures
	 * 
	 * @param x
	 * @param y
	 * @param invalidSqaure
	 */
	public Location(int x, int y, boolean invalidSqaure) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the location
	 * 
	 * @return location that the token is on
	 */
	public Location getLocation() {
		return this;
	}

	/**
	 * Returns x coordinate
	 * 
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns y coordinate
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Returns whether or not the tokens location is on the board
	 * @return
	 */
	public boolean getIsOnBoard(){
		return isOnBoard;
	}
}
