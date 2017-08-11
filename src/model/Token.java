package model;
/**
 * Represents a token
 * @author olivia
 *
 */
public class Token {
	
	//Name (letter) of the token
	private Character name;
	//code that refers to token
	private String code;
	//location of the token on the board (wont be initialised until token is made alive)
	private Location location;
	//check what state the token is in (dead, alive or inactive if not yet been created by player)
	private State state;
	
	/** 
	 * Creates the token
	 * @param name	letter used to represent the token (A-X)
	 * @param code
	 * @param isUpper 
	 */
	public Token(Character name, String code, boolean isUpper){
		state = State.INACTIVE;
		//first check name is between A and X
		if(name > 'x' || !Character.isAlphabetic(name)) throw new GameError("Attempting to create a token thats"
				+ "name is not between A and X");
		
		//now check that name and the char in code match
		char[] temp = code.toCharArray();
		if(!name.equals(temp[0])) throw new GameError("Attempting to create a token whose name does not match"
				+ "letter inside its code");
		
		//check that code is a proper code (begins with a letter and ends with four chars (-,. or #);
		if(temp[0] > 'x' || !Character.isAlphabetic(temp[0])) throw new GameError("Attempting to use code that"
				+ "does not start with a letter between A-X");
		for(int i = 1; i < 5; i++){
			if(!((temp[i]=='.') || (temp[i] == '-') || (temp[i] == '#'))){
				throw new GameError("Attempting to use code that is not of correct format");
			}
		}
		//now can set fields
		this.name = name;
		this.code = code;
		//isUpper will be true if token is made by player1 - make the char uppercase
		if(isUpper) this.name = Character.toUpperCase(this.name);
	}
	
	/**
	 * Returns the code of the token in array form 3x3
	 * @return	code
	 */
	public Character[][] getCode(){
		Character[][]codeArray = new Character[3][3];
		char[] temp = code.toCharArray();
		
		//store characters in array
		codeArray[1][1] = temp[0];
		codeArray[1][0]  = temp[1]; //top
		codeArray[2][1]  = temp[2]; //right
		codeArray[1][2]  = temp[3]; //bottom
		codeArray[0][1]  = temp[4]; //left
		
		return codeArray;

	}
	
	/**
	 * Sets the location of the token on the board
	 * @param loc
	 */
	public void setLocation(Location loc){
		this.state = State.ALIVE;
		this.location = loc;
	}
	
	/**
	 * Returns the location
	 * @return	location
	 */
	public Location getLocation(){
		return this.location;
	}
	
	/**
	 * Returns state
	 * @return	state
	 */
	public State getState(){
		return this.state;
	}
	
	/**
	 * Changes the tokens state
	 * @param s		new state
	 */
	public void setState(State s){
		this.state = s;
	}
	
}
