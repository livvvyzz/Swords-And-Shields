package controller;

import java.util.Scanner;

import model.Board;
import model.Player;
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
		parser = new Parser();
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
	
	 
}
