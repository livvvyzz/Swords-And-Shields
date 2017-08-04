package model;

/**
 * Part of the Model
 * 
 * Holds a 10x10 array and keeps track of where tokens are on the board
 * @author olivia
 *
 */
public class Board extends java.util.Observable{
	
	//10x10 array that holds the tokens
	private Token board[][];
	
	/**
	 * Constructs board 
	 */
	public Board(){
		board = new Token[10][10];
	}
	
	/**
	 * Adds token to the board at the location that the token is
	 * @param token		token to be added
	 */
	public void addToken(Token token){
		Location loc = token.getLocation();
		board[loc.getX()][loc.getY()] = token;
		
		//notifies view that it needs to redraw the board
		notifyObservers();
	}
	
	/**
	 * move token that is already on the board
	 */
	public void moveToken(Token token){
		//checks if the piece is in the board
		boolean moved = false;
		
		//finds piece
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j<board.length; j++){
				if(board[i][j].equals(token)){
					board[i][j] = null;
					moved = true;
				}
			}
		}
		//if piece is not on board, throw error
		if(!moved) throw new GameError("Attempted to move token that is not on the Board");
		
		//add the token in its new location
		addToken(token);
	}
	
	/**
	 * Returns the board 
	 * @return
	 */
	public Token[][] getBoard(){
		return board;
	}
}
