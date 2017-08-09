package controller;

import java.util.Scanner;

import model.Board;
import model.Player;
import model.State;
import model.Token;
import view.BoardView;

public class Controller {

	//player who has the current turn
	private Player current;
	
	//both players
	private Player green;
	private Player yellow;
	
	//Board
	private Board board;
	
	//Board View - outputs the board
	private BoardView frame;
	
	//parser to evaluate expressions
	private Parser parser;
	/**
	 * Acts as the informers between the model and the view. 
	 */
	public Controller(){
		green = new Player("green");
		yellow = new Player("yellow");
		//set yellow as the first player
		current = yellow;
		
		//create parser
		parser = new Parser(this);
		//create model board
		board = new Board();
		//create board view
		frame = new BoardView(board, yellow.getPlayerMap(), green.getPlayerMap());
		
		//draw the board
		frame.drawBoard();
		//start the game
		startGame();
	} 
	
	/**
	 * Starts the game by asking yellow to create token
	 */
	public void startGame(){
		String output = ("YELLOW STARTS");
		//receive input
		String input = frame.getOutput(output);
		parser.ParseExpressions(input);
	}
	
	/**
	 * Receives info from parse class and create a token for current player depending on char
	 * @param c		name of token to be created
	 */
	public void create(char c){
		Token token = current.getPlayerMap().getMap().get(c);
		//check if already on board
		if(token.getState().equals(State.ALIVE)){
			frame.getOutput(">>>Token already on Board! Cannot be created. Please try again");
		}
		//else put token on board
		
	}
	 
}
