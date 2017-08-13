package controller;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.Stack;

import model.Board;
import model.GameError;
import model.Location;
import model.Player;
import model.State;
import model.Token;
import view.BoardView;
import Command.Command;
import Command.CommandStack;
import Command.CreateCommand;

public class Controller implements Observer {

	// player who has the current turn
	private Player current;

	// both players
	private Player green;
	private Player yellow;

	// Board
	private Board board;

	// Board View - outputs the board
	private BoardView frame;

	// patterns to use as the delimiter for the scanner
	String re1 = "(<)"; // Any Single Character 1
	String re2 = ".*?"; // Non-greedy match on filler
	String re3 = "(>)"; // Any Single Character 2
	String re4 = "( )"; // Any Single Character 1

	Pattern p = Pattern.compile(re1 + re3 + re4);
	private ArrayList<String> cmds;

	// command stack
	private Stack<Stack<Command>> commands;

	/**
	 * Acts as the informers between the model and the view.
	 */
	public Controller() {
		green = new Player("green");
		yellow = new Player("yellow");
		// set yellow as the first player
		current = yellow;
		// create commandstack
		commands = new Stack<Stack<Command>>();
		// create model board
		board = new Board();
		// make the squares that are not to be used have x in them
		makeSqaures();
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

		//trick();

	}

	/**
	 * Receives info from parse class and create a token for current player
	 * depending on char
	 * 
	 * @param obj
	 */
	public boolean create(String input) {
		Stack<Command> create = new Stack<Command>();

		Scanner s = new Scanner(input);
		Character c = 'y';
		int dir;

		if (s.next().equals("create")) {
			String temp = s.next();
			if (temp.toCharArray().length == 3) {
				c = temp.toCharArray()[1];
				temp = s.next();
				temp = temp.substring(1, 3);
				dir = Integer.parseInt(temp);
			}
		}

		if (c.equals('y'))
			return false;
		// make it lowercase, because all map keys are lowercase
		c = Character.toLowerCase(c);
		Token token = current.getPlayerMap().getMap().get(c);
		// check if already on board
		if (token.getState().equals(State.ALIVE)) {
			return false;
		}
		// else put token on board
		// set the location of the token
		if (current.equals(green))
			token.setLocation(new Location(2, 2));
		else
			token.setLocation(new Location(7, 7));
		// add command to stack
		create.push(new CreateCommand(this, token));
		commands.push(create);
		// add token
		board.addToken(token);
		frame.drawBoard();
		return true;
	}

	public void undoCreate(Token t) {
		board.removeToken(t);
		t.setState(State.INACTIVE);
		frame.drawBoard();
	}

	/**
	 * Receives info from parse class and moves the given letter in the given
	 * direction
	 * 
	 * @param obj
	 */
	public boolean move(String input) {
		Stack<Command> move = new Stack<Command>();
		Scanner s = new Scanner(input);
		Character c = 'y';
		String dir = "null";

		if (s.next().equals("move")) {
			String temp = s.next();
			if (temp.toCharArray().length == 3) {
				c = temp.toCharArray()[1];
				dir = s.next();
				dir = dir.substring(1, dir.length() - 1);
			}
		}

		if (c.equals('y'))
			return false;

		// move the token
		c = Character.toLowerCase(c);
		Token token = current.getPlayerMap().getMap().get(c);
		// move token
		if(!token.setLocation(dir)) return false;
		if(!(board.moveToken(token, this))) return false;;
		commands.push(board.getStack());
		frame.drawBoard();
		return true;
	}

	/**
	 * Undos the most recent move, and its consequent moves
	 */
	public void undoMove(Token t) {
		t.setOldLocation();
		board.moveToken(t);
		frame.drawBoard();
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
	public boolean rotate(String input) {
		Scanner s = new Scanner(input);
		Character c = 'y';
		int dir;

		if (s.next().equals("create")) {
			String temp = s.next();
			if (temp.toCharArray().length == 1) {
				c = temp.toCharArray()[0];
				temp = s.next();
				dir = Integer.parseInt(temp);
			}
		}

		if (c.equals('y'))
			throw new GameError("Not correct input");
		Token token = current.getPlayerMap().getMap().get(c);

		// rotate the token
		// TODO: add the method
		return true;
	}

	/**
	 * Controlls the trick
	 */
	public void trick() {

		// round one - may only create or pass
		String output = (current.getName().toUpperCase() + "'S TURN: ADD LETTER TO BOARD OR PASS");
		String input = frame.getOutput(output);
		boolean success = roundOne(input);
		while (!success) {
			output = ("ENTERED WRONG COMMAND. TRY AGAIN: " + current.getName().toUpperCase()
					+ "'S TURN: ADD LETTER TO BOARD OR PASS");
			input = frame.getOutput(output);
			success = roundOne(input);
		}

		// round two - may only move or rotate
		output = (current.getName().toUpperCase() + "'S TURN: MOVE OR ROTATE");
		input = frame.getOutput(output);
		success = roundTwo(input);
		while (!success) {
			output = ("ENTERED WRONG COMMAND. TRY AGAIN: " + current.getName().toUpperCase()
					+ "'S TURN: MOVE OR ROTATE");
			input = frame.getOutput(output);
			success = roundTwo(input);
		}

		// round three & four - may move, rotate or pass
		for (int i = 0; i < 2; i++) {
			output = (current.getName().toUpperCase() + "'S TURN: PASS, MOVE OR ROTATE");
			input = frame.getOutput(output);
			success = roundThree(input);
			while (!success) {
				output = ("ENTERED WRONG COMMAND. TRY AGAIN: " + current.getName().toUpperCase()
						+ "'S TURN: PASS, MOVE OR ROTATE");
				input = frame.getOutput(output);
				success = roundTwo(input);
			}
		}
		
		if(current.equals(yellow)) current = green;
		else current = yellow;
		trick();

	}

	/**
	 * The first part of a players turn - must create or pass
	 */
	public boolean roundOne(String input) {

		String cmd = parseExpression(input);
		// must be create or pass
		if (cmd.equals("create")) {
			if (create(input))
				return true;
		} else if (cmd.equals("pass")) {
			return true;
		}
		return false;

	}

	/**
	 * The second part of a players turn - must move or rotate
	 */
	public boolean roundTwo(String input) {

		String cmd = parseExpression(input);
		// must be create or pass
		if (cmd.equals("rotate")) {
			if (rotate(input))
				return true;
		} else if (cmd.equals("move")) {
			if (move(input))
				return true;
		}
		return false;
	}

	/**
	 * The third and fourth part of a players round - they can move, rotate or
	 * pass.
	 */
	public boolean roundThree(String input) {

		String cmd = parseExpression(input);
		// must be create or pass
		if (cmd.equals("rotate")) {
			if (rotate(input))
				return true;
		} else if (cmd.equals("move")) {
			if (move(input))
				return true;
		} else if (cmd.equals("pass"))
			return true;
		return false;
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

	@Override
	public void update(Observable arg0, Object arg1) {
		frame.drawBoard();

	}

	/**
	 * Fills the invalid sqaures
	 */
	public void makeSqaures() {
		Token t = new Token('*', "*....", false);
		t.setLocation(new Location(0, 0, false));
		t.setState(State.INVALID);
		board.addToken(t);
		t.setLocation(new Location(0, 1, false));
		board.addToken(t);
		t.setLocation(new Location(1, 0, false));
		board.addToken(t);
		t.setLocation(new Location(9, 8, false));
		board.addToken(t);
		t.setLocation(new Location(9, 9, false));
		board.addToken(t);
		t.setLocation(new Location(8, 9, false));
		board.addToken(t);
		t = new Token('0', "0....", false);
		t.setLocation(new Location(8, 8, false));
		board.addToken(t);
		t = new Token('1', "1....", false);
		t.setLocation(new Location(1, 1, false));
		board.addToken(t);
	}

	/**
	 * Checks if a moved token reacts with another - or multiple tokens
	 */
	public void checkReaction(Token t) {

		Token[][] array = board.getBoard();

		ArrayList<Token> reactions = new ArrayList<Token>();

		Token toReact = null;

		Location loc = t.getLocation();
		// check if there is a token next to it
		// on top
		if (array[loc.getX()][loc.getY() - 1] != null) {
			reactions.add(array[loc.getX()][loc.getY() - 1]);
		}
		// below
		else if (array[loc.getX()][loc.getY() + 1] != null) {
			reactions.add(array[loc.getX()][loc.getY() + 1]);
		}
		// right
		else if (array[loc.getX() + 1][loc.getY()] != null) {
			reactions.add(array[loc.getX() + 1][loc.getY()]);
		}
		// left
		else if (array[loc.getX() - 1][loc.getY()] != null) {
			reactions.add(array[loc.getX() - 1][loc.getY()]);
		}

		if (reactions.size() > 1) {
			StringBuilder s = new StringBuilder("WHICH TOKEN DO YOU WANT TO REACT WITH: ");
			for (Token tok : reactions) {
				s.append(tok.getName() + ",");
			}
			s.substring(0, s.length() - 1);
			String input = frame.getOutput(s.toString());
			Scanner scan = new Scanner(input);
			String name = scan.next().toLowerCase();
			char letter;
			// make sure name is either <a> or a
			if (name.toCharArray().length == 1)
				letter = name.toCharArray()[0];
			else if (name.toCharArray().length == 3)
				letter = name.toCharArray()[1];
			else
				throw new GameError("Not correct letter");

			// check if the given letter matches one of the potential reactions
			for (Token tok : reactions) {
				if (tok.getName().equals(letter)) {
					toReact = tok;
				}
			}
			if (toReact.equals(null))
				throw new GameError("Not a correct letter");

		} else if (reactions.size() == 1) {
			toReact = reactions.get(0);
		}

		if (!toReact.equals(null))
			react(t, toReact);
	}

	/**
	 * Performs the reaction with the given token
	 * 
	 * @param t
	 */
	public void react(Token one, Token two) {

	}

	/**
	 * Returns the board
	 */
	public Board getBoard() {
		return this.board;
	}

	public void undo() {
		Stack<Command> stack = commands.pop();
		stack.pop().undo();
	}
	
	/**
	 * Returns the current player
	 * @return		current
	 */
	public Player getCurrentPlayer(){
		return current;
	}

}
