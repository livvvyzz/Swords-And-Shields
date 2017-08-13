package model;

/**
 * Part of the Model
 * 
 * Holds a 10x10 array and keeps track of where tokens are on the board
 * 
 * @author olivia
 *
 */
public class Board extends java.util.Observable {

	// 10x10 array that holds the tokens
	private Token board[][];

	/**
	 * Constructs board
	 */
	public Board() {
		board = new Token[10][10];
	}

	/**
	 * Adds token to the board at the location that the token is
	 * 
	 * @param token
	 *            token to be added
	 */
	public void addToken(Token token) {
		Location loc = token.getLocation();
		// current token in this location, if any
		Token current;

		// check that the piece is on the board
		if (!token.getLocation().getIsOnBoard())
			throw new GameError("Attempting to place a token on a location that is not on the board");
		// check that no piece is on this spot already
		if (board[loc.getX()][loc.getY()] != null) {
			current = board[loc.getX()][loc.getY()];
			String dir = token.getRecentMove();
			if (dir.equals("up") || dir.equals("down") || dir.equals("right") || dir.equals("left")) {
				current.setLocation(dir);
				moveToken(current);
			}
		}

		board[loc.getX()][loc.getY()] = token;

		// check for reaction

		// change token state
		// token.setState(State.ALIVE);
		// notifies view that it needs to redraw the board
		// notifyObservers();
	}

	/**
	 * move token that is already on the board
	 */
	public void moveToken(Token token) {
		// checks if the piece is in the board
		boolean foundToken = false;

		if (board[token.getPrevLoc().getX()][token.getPrevLoc().getY()] != null) {
			if (board[token.getPrevLoc().getX()][token.getPrevLoc().getY()].equals(token)) {
				foundToken = true;
			}
		}

		// check if the new location is on the board
		if (foundToken && !token.getLocation().getIsOnBoard()) {
			board[token.getPrevLoc().getX()][token.getPrevLoc().getY()] = null;
			token.setState(State.DEAD);
		}
		// add the token in its new location
		else if (foundToken && token.getLocation().getIsOnBoard()) {

			board[token.getPrevLoc().getX()][token.getPrevLoc().getY()] = null;
			addToken(token);
		}
	}
	
	/**
	 * Removes the token from the board
	 * @param t
	 */
	public void removeToken(Token t){
		board[t.getLocation().getX()][t.getLocation().getY()] = null;
	}

	/**
	 * Returns the board
	 * 
	 * @return
	 */
	public Token[][] getBoard() {
		return board;
	}
}
