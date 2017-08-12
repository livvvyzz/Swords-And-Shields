package controller;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import model.Board;
import model.GameError;
import model.Player;
import model.State;
import model.Token;
import view.BoardView;

public class Controller {

	// player who has the current turn
	private Player current;

	// both players
	private Player green;
	private Player yellow;

	// Board
	private Board board;

	// Board View - outputs the board
	private BoardView frame;

	// parser to evaluate expressions
	private Parser parser;

	// patterns to use as the delimiter for the scanner
	static Pattern OPEN = Pattern.compile("\\<");
	static Pattern CLOSE = Pattern.compile("\\>");
	private ArrayList<String> cmds;

	/**
	 * Acts as the informers between the model and the view.
	 */
	public Controller() {
		green = new Player("green");
		yellow = new Player("yellow");
		// set yellow as the first player
		current = yellow;

		// create parser
		parser = new Parser(this);
		// create model board
		board = new Board();
		// create board view
		frame = new BoardView(board, yellow.getPlayerMap(), green.getPlayerMap());

		// draw the board
		frame.drawBoard();
		// create cmds array
		cmds = new ArrayList<String>();
		cmds.add("create");
		cmds.add("pass");
		cmds.add("rotate");
		cmds.add("move");

	}

	/**
	 * Receives info from parse class and create a token for current player
	 * depending on char
	 * 
	 * @param obj
	 */
	public boolean create(String input) {
		Scanner s = new Scanner(input);
		s.useDelimiter("<|>");
		Character c = 'y';
		int dir;
		
		if(s.next().equals("create")){
			String temp = s.next();
			if(temp.toCharArray().length == 1){
				c = temp.toCharArray()[0];
				temp = s.next();
				dir = Integer.parseInt(temp);
			}
		}
		
		if(c.equals('y')) throw new GameError("Not correct input");
		Token token = current.getPlayerMap().getMap().get(c);
		// check if already on board
		if (token.getState().equals(State.ALIVE)) {
			frame.getOutput(">>>Token already on Board! Cannot be created. Please try again");
		}
		// else put token on board
		board.addToken(token);
		return true;
	}

	/**
	 * Receives info from parse class and moves the given letter in the given
	 * direction
	 * 
	 * @param obj
	 */
	public void move(String input) {
		Scanner s = new Scanner(input);
		s.useDelimiter("<|>");
		Character c = 'y';
		String dir;
		
		if(s.next().equals("move")){
			String temp = s.next();
			if(temp.toCharArray().length == 1){
				c = temp.toCharArray()[0];
				dir = s.next();
			}
		}
		
		if(c.equals('y')) throw new GameError("Not correct input");
	}

	/**
	 * Receives info from parse class and rotates the given letter by the given
	 * degrees.
	 * 
	 * @param c
	 *            token to rotate
	 * @param deg
	 *            how many degrees to rotate
	 */
	public void rotate(char c, int deg) {

	}

	/**
	 * Starts a players round
	 */
	public void startRound() {

	}

	/**
	 * The first part of a players turn - must create or pass
	 */
	public void roundOne() {

		String output = (current.getName().toUpperCase() + "'S TURN: ADD LETTER TO BOARD OR PASS");
		// receive input/cmd
		String input = frame.getOutput(output);

		String cmd = parseExpression(input);
		// must be create or pass
		if (cmd.equals("create")) {
			create(input);
			roundTwo();
		} else if (cmd.equals("pass")) {
			roundTwo();
		} else {
			throw new GameError("Entered wrong command format");
		}

	}

	/**
	 * The second part of a players turn - must move or rotate
	 */
	public void roundTwo() {
		String output = (current.getName().toUpperCase() + "'S TURN: MOVE OR ROTATE");
		// receive input/cmd
		String input = frame.getOutput(output);

		String cmd = parseExpression(input);
		// must be create or pass
		if (cmd.equals("rotate")) {
			rotate(input);
			roundThree(false);
		} else if (cmd.equals("move")) {
			move(input);
			roundThree(false);
		} else {
			throw new GameError("Entered wrong command format");
		}
	}

	/**
	 * The third and fourth part of a players round - they can move, rotate or
	 * pass.
	 */
	public void roundThree(boolean fourth) {
		String output = (current.getName().toUpperCase() + "'S TURN: PASS, MOVE OR ROTATE");
		// receive input/cmd
		String input = frame.getOutput(output);

		String cmd = parseExpression(input);
		// must be create or pass
		if (cmd.equals("rotate")) {
			rotate(input);
		} else if (cmd.equals("move")) {
			move(input);
		}else if (!cmd.equals("pass")){
			throw new GameError("Entered wrong command format");
		}
		if(!fourth) roundThree(true);
		else {
			if(current.equals(green)) current = yellow;
			else current = green;
			roundOne();
		}
	}

	public String parseExpression(String input) {
		Scanner s = new Scanner(input);
		String cmd = s.next();
		String toReturn;
		if (cmds.contains(cmd)) {
			if (cmd.equals("create"))
				return toReturn = "create";
			else if (cmd.equals("rotate"))
				return toReturn = "rotate";
			else if (cmd.equals("move"))
				return toReturn = "move";
			else if (cmd.equals("pass"))
				return toReturn = "pass";
		}

		// if none of the above, wrong input
		return toReturn = "null";

	}

}
