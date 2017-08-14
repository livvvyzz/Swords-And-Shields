package model;

import java.util.Stack;

/**
 * Represents a token
 * 
 * @author olivia
 *
 */
public class Token {

	// Name (letter) of the token
	private Character name;
	// code that refers to token
	private String code;
	// location of the token on the board (wont be initialised until token is
	// made alive)
	private Location location;
	// check what state the token is in (dead, alive or inactive if not yet been
	// created by player)
	private State state;
	// holds the most recent move
	private Stack<String> recentMove;
	// prev location
	private Stack<Location> prevLoc;
	//rotate
	private Stack<Integer> degree;

	private boolean upper;
 
	/**
	 * Creates the token
	 * 
	 * @param name
	 *            letter used to represent the token (A-X)
	 * @param code
	 * @param isUpper
	 */
	public Token(Character name, String code, boolean isUpper) {
		state = State.INACTIVE;
		prevLoc = new Stack<Location>();
		recentMove = new Stack<String>();
		degree = new Stack<Integer>();
		// first check name is between A and X4
		if (!(name.equals('*') || name.equals('1') || name.equals('0'))) {
			if (name > 'x' || name < 'a')
				throw new GameError("Attempting to create a token thats" + "name is not between A and X");
		}
		// now check that name and the char in code match
		char[] temp = code.toCharArray();
		char lowerTemp = Character.toLowerCase(temp[0]);
		if (!name.equals(lowerTemp))
			throw new GameError("Attempting to create a token whose name does not match" + "letter inside its code");

		// check that code is a proper code (begins with a letter and ends with
		// four chars (-,. or #);

		for (int i = 1; i < 5; i++) {
			if (!((temp[i] == '.') || (temp[i] == '-') || (temp[i] == '#'))) {
				throw new GameError("Attempting to use code that is not of correct format");
			}
		}
		// now can set fields
		this.name = name;
		this.code = code;
		this.upper = isUpper;
		// isUpper will be true if token is made by player1 - make the char
		// uppercase
		if (upper)
			this.name = Character.toUpperCase(this.name);
	}

	/**
	 * Returns the code of the token in array form 3x3
	 * 
	 * @return code
	 */
	public Character[][] getCode() {
		Character[][] codeArray = new Character[3][3];
		char[] temp = code.toCharArray();

		// store characters in array
		codeArray[1][1] = temp[0];
		codeArray[0][1] = temp[1]; // left
		codeArray[1][0] = temp[2]; // top
		codeArray[2][1] = temp[3]; // right
		codeArray[1][2] = temp[4]; // bottom
		return codeArray;

	}
	
	/**
	 * Returns the sword or shield in the top part of the token
	 * @return
	 */
	public Character getTop(){
		return code.toCharArray()[2];
	}
	
	/**
	 * Returns the sword or shield in the bottom part of the token
	 * @return
	 */
	public Character getBottom(){
		return code.toCharArray()[4];
	}
	
	/**
	 * Returns the sword or shield in the left part of the token
	 * @return
	 */
	public Character getLeft(){
		return code.toCharArray()[1];
	}
	
	/** 
	 * Returns the sword or shield in the right part of the token
	 * @return
	 */
	public Character getRight(){
		return code.toCharArray()[3];
	}

	/**
	 * Sets the location of the token on the board
	 * 
	 * @param loc
	 */
	public boolean setLocation(String dir) {
		recentMove.push(dir);
		prevLoc.push(location);

		if (this.location == null)
			return false;
		if (dir.equals("up")) {
			this.location = new Location(this.location.getX(), this.location.getY() - 1);
		} else if (dir.equals("down")) {
			this.location = new Location(this.location.getX(), this.location.getY() + 1);
		} else if (dir.equals("right")) {
			this.location = new Location(this.location.getX() + 1, this.location.getY());
		} else if (dir.equals("left")) {
			this.location = new Location(this.location.getX() - 1, this.location.getY());
		} else
			return false;
		setState();
		return true;
	}

	/**
	 * Sets the location of the token on the board
	 * @param loc
	 */
	public void setLocation(Location loc) {
		if (location != null)
			prevLoc.push(location);
		this.location = loc;
		setState();
	}

	/**
	 * Returns the location
	 * 
	 * @return location
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * Returns state
	 * 
	 * @return state
	 */
	public State getState() {
		return this.state;
	}

	/**
	 * Changes the tokens state
	 * 
	 * @param s
	 *            new state
	 */
	public void setState(State s) {
		this.state = s;
	}

	public void setState() {
		if (location.getIsOnBoard())
			state = State.ALIVE;
		else
			state = State.DEAD;
	}

	/**
	 * Returns the name of the token
	 * 
	 * @return
	 */
	public Character getName() {
		Character c = this.name;
		if (upper)
			c = Character.toUpperCase(c);
		return c;
	}

	/**
	 * Returns the msot recent move
	 * 
	 * @return recentmove
	 */
	public String getRecentMove() {
		if(recentMove.isEmpty()) return "null";
		return recentMove.peek();
	}

	/**
	 * Returns the old location
	 * 
	 * @return
	 */
	public Location getPrevLoc() {
		if (prevLoc.empty())
			return null;
		return prevLoc.peek();
	}

	public void setOldLocation() {
		location = prevLoc.pop();
		setState();
		recentMove.pop();

	}

	/**
	 * Returns the stack of previous locations
	 * 
	 * @return prevLoc
	 */
	public Stack<Location> getPrevStack() {
		return prevLoc;
	}

	/**
	 * Rotates the token
	 * 
	 * @param deg
	 *            degrees to rotate
	 */
	public void rotate(int deg) {
		String line = code.substring(1);
		line = line + line + line;

		int start = 0;
		int end = 4;
		if (deg == 0) {
			line = line.substring(start, end);
		} else if (deg == 90) {
			line = line.substring(start + 3, end + 3);
		} else if (deg == 180) {
			line = line.substring(start + 2, end + 2);
		} else if (deg == 270) {
			line = line.substring(start + 1, end + 1);
		}
		degree.push(deg);
		this.code = code.toCharArray()[0] + line;
	}

	/**
	 * Returns the code
	 * 
	 * @return code
	 */
	public String getCodeWord() {
		return code;
	}
	
	/**
	 * Returns the last rotate direction
	 * @return degree
	 */
	public int getDirection(){
		if(degree.isEmpty()) return 0;
		return degree.pop();
	}

}
