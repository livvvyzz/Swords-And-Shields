package model;

import java.util.Stack;

import Command.Command;
import Command.MoveCommand;
import controller.Controller;

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

	//current stack
	private Stack<Command> stack;
	private Controller cont;
	
	//whether of not to add moves to stack 
	private boolean add;
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
	public boolean moveToken(Token token) {
		//adds command to stack
		if(add) stack.push(new MoveCommand(cont,token));
		// checks if the piece is in the board
		if(token.getPrevLoc() == null) return false;

		// check if the new location is on the board
		if (!token.getLocation().getIsOnBoard()) {
			board[token.getPrevLoc().getX()][token.getPrevLoc().getY()] = null;
			token.setState(State.DEAD);
		}
		// add the token in its new location
		else if (token.getLocation().getIsOnBoard()) {

			board[token.getPrevLoc().getX()][token.getPrevLoc().getY()] = null;
			addToken(token);
		}
		return true;
		
	}
	
	/**
	 * Called at the beginning of a move, and initialses the stack that will hold ll the moves that are a consequent of this move
	 * @param stack
	 * @param t
	 */
	public boolean moveToken(Token t, Controller cont){
		this.cont = cont;
		add = true;
		this.stack = new Stack<Command>();
		if(moveToken(t)) return true;
		return false;
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
	
	/**
	 * Returns the stack of move commands
	 * @return		stack
	 */
	public Stack<Command> getStack(){
		add = false;
		return stack;
	}
}
