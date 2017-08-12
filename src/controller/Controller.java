package controller;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.regex.Pattern;

import model.Board;
import model.GameError;
import model.Location;
import model.Player;
import model.State;
import model.Token;
import view.BoardView;

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

	/**
	 * Acts as the informers between the model and the view.
	 */
	public Controller() {
		green = new Player("green");
		yellow = new Player("yellow");
		// set yellow as the first player
		current = yellow;

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

		roundOne();

	}

	/**
	 * Receives info from parse class and create a token for current player
	 * depending on char
	 * 
	 * @param obj
	 */
	public boolean create(String input) {
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
			throw new GameError("Not correct input");
		// make it lowercase, because all map keys are lowercase
		c = Character.toLowerCase(c);
		Token token = current.getPlayerMap().getMap().get(c);
		// check if already on board
		if (token.getState().equals(State.ALIVE)) {
			frame.getOutput(">>>Token already on Board! Cannot be created. Please try again");
		}
		// else put token on board
		// set the location of the token
		if (current.equals(green))
			token.setLocation(new Location(2, 2));
		else
			token.setLocation(new Location(7, 7));
		// add token
		board.addToken(token);
		frame.drawBoard();
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
			throw new GameError("Not correct input");

		// move the token
		c = Character.toLowerCase(c);
		Token token = current.getPlayerMap().getMap().get(c);
		token.setLocation(dir);
		board.moveToken(token);
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
	public void rotate(String input) {
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
		} else if (!cmd.equals("pass")) {
			throw new GameError("Entered wrong command format");
		}
		if (!fourth)
			roundThree(true);
		else {
			if (current.equals(green))
				current = yellow;
			else
				current = green;
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
			//make sure name is either <a> or a
			if(name.toCharArray().length == 1) letter = name.toCharArray()[0];
			else if(name.toCharArray().length == 3) letter = name.toCharArray()[1];
			else throw new GameError("Not correct letter");
			
			//check if the given letter matches one of the potential reactions
			for (Token tok : reactions) {
				if(tok.getName().equals(letter)){
					toReact = tok;
				}
			}
			if(toReact.equals(null)) throw new GameError("Not a correct letter");
			
		}
		else if (reactions.size() == 1){
			toReact = reactions.get(0);
		}
		
		if(!toReact.equals(null)) react(t,toReact);
	}
	
	/**
	 * Performs the reaction with the given token
	 * @param t
	 */
	public void react (Token one,Token two){
		
	}

}
