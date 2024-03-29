package controller;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import java.util.Scanner;
import java.util.Stack;

import model.Board;
import model.GameError;
import model.Location;
import model.Player;
import model.State;
import model.Token;
import view.BoardView;
import Command.Command;
import Command.CreateCommand;
import Command.RotateCommand;

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
		cmds.add("undo");

	}

	/**
	 * Receives info from parse class and create a token for current player
	 * depending on char
	 * 
	 * @param obj
	 */
	public boolean create(String input) {
		Stack<Command> create = new Stack<Command>();

		String exe = parseExpression(input);
		Character name = parseChar(input);
		int num = parseNum(input);

		if (name.equals('y') || !exe.equals("create") || !(num == 0 || num == 90 || num == 180 || num == 270))
			return false;
		// make it lowercase, because all map keys are lowercase
		name = Character.toLowerCase(name);
		Token token = current.getPlayerMap().getMap().get(name);
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
		// rotate the token
		token.rotate(num);
		// add command to stack
		create.push(new CreateCommand(this, token));
		commands.push(create);
		// add token
		if(!board.addToken(token)) return false;
		frame.drawBoard();
		checkReaction(token);
		return true;
	}

	/**
	 * Undo's the last create command
	 * 
	 * @param t
	 *            token to undo
	 */
	public void undoCreate(Token t) {
		board.removeToken(t);
		t.setState(State.INACTIVE);
	}

	/**
	 * Receives info from parse class and moves the given letter in the given
	 * direction
	 * 
	 * @param obj
	 */
	public boolean move(String input) {

		String exe = parseExpression(input);
		Character name = parseChar(input);
		String dir = parseDir(input);

		if (name.equals('y') || !exe.equals("move")
				|| !(dir.equals("up") || dir.equals("down") || dir.equals("left") || dir.equals("right")))
			return false;

		// move the token
		name = Character.toLowerCase(name);
		Token token = current.getPlayerMap().getMap().get(name);
		// move token
		if (!token.setLocation(dir))
			return false;
		if (!(board.moveToken(token, this)))
			return false;
		;
		commands.push(board.getStack());
		frame.drawBoard();
		if (token.getLocation().getIsOnBoard())
			checkReaction(token);
		return true;
	}

	/**
	 * Undos the most recent move, and its consequent moves
	 */
	public void undoMove(Token t) {
		if (t.getState().equals(State.ALIVE))
			board.removeToken(t);
		t.setOldLocation();
		board.addToken(t);
	}

	/**
	 * Receives info from parse class and rotates the given letter by the given
	 * degrees.
	 * 
	 * @param input
	 *            expression to parse
	 */
	public boolean rotate(String input) {
		Stack<Command> rotate = new Stack<Command>();

		String exe = parseExpression(input);
		Character name = parseChar(input);
		int num = parseNum(input);

		if (name.equals('y') || !exe.equals("rotate") || !(num == 0 || num == 90 || num == 180 || num == 270))
			return false;

		Token token = current.getPlayerMap().getMap().get(name);
		token.rotate(num);
		// add command to stack
		rotate.push(new RotateCommand(this, token));
		commands.push(rotate);
		frame.drawBoard();
		return true;
	}

	/**
	 * Undo's a rotate command
	 * 
	 * @param t
	 *            the token to rotate
	 */
	public void undoRotate(Token t) {
		int dir = t.getDirection();
		if (dir == 90)
			dir = 270;
		else if (dir == 270)
			dir = 270;
		t.rotate(dir);
		frame.drawBoard();
	}

	/**
	 * The first part of a trick - can only add or rotate
	 */
	public void firstTrick() {
		String output = (current.getName().toUpperCase() + "'S TURN: ADD LETTER TO BOARD OR PASS");
		String input = frame.getOutput(output);
		boolean success = roundOne(input);
		while (!success) {
			output = ("ENTERED WRONG COMMAND. TRY AGAIN: " + current.getName().toUpperCase()
					+ "'S TURN: ADD LETTER TO BOARD OR PASS");
			input = frame.getOutput(output);
			success = roundOne(input);
		}
	}

	/**
	 * The second part of a trick - can undo, pass, rotate or move
	 * 
	 * @param array
	 */
	public void secondTrick(ArrayList<Token> array) {
		boolean success;
		String input;
		String output;
		ArrayList<Token> active = array;
		int passCount = 0;
		boolean tryAgain = false;
		boolean first = true;

		// keep going until all the pieces have been moved, rotated or passed
		while (!active.isEmpty()) {
			success = false;
			// checks that the input is correct - will ask the player to try
			// again if its not
			while (!success) {
				String s = "";
				if (tryAgain)
					s = "ENTERED WRONG COMMAND. TRY AGAIN: ";

				output = (s + current.getName().toUpperCase() + "'S TURN: MOVE, ROTATE, PASS OR UNDO");
				input = frame.getOutput(output);

				tryAgain = true;

				// check that the given input does not match one that has
				// already been done
				if (active.contains(current.getPlayerMap().getMap().get(parseChar(input)))
						|| parseExpression(input).equals("pass") || parseExpression(input).equals(("undo"))) {
					success = roundTwo(input);

					if (success) {
						if (first && parseExpression(input).equals("undo")) {
							trick();
							return;
						}
						if (parseExpression(input).equals("pass"))
							passCount++;
						Character c = parseChar(input);
						Token t = current.getPlayerMap().getMap().get(c);
						active.remove(t);
						tryAgain = false;
						first = false;
					}
				}
			}
			if (active.size() - passCount <= 0)
				break;
		}
	}

	/**
	 * The third part of a round - can undo or pass
	 * 
	 * @return
	 */
	public boolean thirdTrick() {
		String output = (current.getName().toUpperCase() + "'S TURN: CAN UNDO OR PASS");
		String input = frame.getOutput(output);
		boolean success = roundThree(input);
		// ensures the input is correct
		while (!success) {
			output = ("ENTERED WRONG COMMAND. TRY AGAIN: " + current.getName().toUpperCase()
					+ "'S TURN: CAN UNDO OR PASS");
			input = frame.getOutput(output);
			success = roundThree(input);
			if (success)
				if (parseExpression(input).equals("undo"))
					return false;
		}
		return true;

	}

	/**
	 * Controlls the trick
	 */
	public void trick() {
		boolean success = false;

		// round one - may only create or pass
		firstTrick();
		// count how many active token the player has
		while (!success) {
			ArrayList<Token> active = new ArrayList<Token>();
			// add all active tokens to the array
			for (Token t : current.getPlayerMap().getMap().values())
				if (t.getState().equals(State.ALIVE))
					active.add(t);

			// round two - may move, rotate, pass, undo
			secondTrick(active);
			// round three - can choose undo
			if (thirdTrick())
				success = true;
		}
		// change player
		if (current.equals(yellow))
			current = green;
		else
			current = yellow;
		// clear the commands - so the next player cant undo the current players
		// turn
		commands.clear();
		// start again
		trick();

	}

	/**
	 * The first part of a players turn - must create or pass
	 * 
	 * @param input
	 *            command
	 */
	public boolean roundOne(String input) {

		String cmd = parseExpression(input);
		// must be create or pass
		if (cmd.equals("create")) {
			if (create(input))
				return true;
		} else if (cmd.equals("pass")) {
			return true;
		} else if (cmd.equals("undo")) {
			if (undo())
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
		} else if (cmd.equals("pass"))
			return true;
		else if (cmd.equals("undo"))

		{
			if (undo())
				return true;
		}
		return false;
	}

	/**
	 * controls the third round
	 * 
	 * @param input
	 * @return true if pass or undo is parsed successfully
	 */
	public boolean roundThree(String input) {
		String cmd = parseExpression(input);
		// must be undo or pass
		if (cmd.equals("pass"))
			return true;
		else if (cmd.equals("undo"))

		{
			if (undo())
				return true;
		}
		return false;

	}

	/**
	 * Gets the first word (the command) from the players input
	 * 
	 * @param input
	 * @return the command
	 */
	public String parseExpression(String input) {
		try {
			Scanner s = new Scanner(input);

			String cmd = s.next();
			String toReturn;
			if (cmds.contains(cmd)) {
				if (cmd.equals("create") || cmd.equals("move") || cmd.equals("pass") || cmd.equals("rotate")
						|| cmd.equals("undo"))
					return cmd;
			}
			// if none of the above, wrong input
			return toReturn = "null";
		} catch (IndexOutOfBoundsException | NoSuchElementException e) {
			return "null";
		}
	}

	/**
	 * Finds the char
	 * 
	 * @param input
	 * @return letter
	 */
	public char parseChar(String input) {
		try {
			Scanner s = new Scanner(input);
			// first command
			s.next();
			// get char
			char c = Character.toLowerCase(s.next().charAt(1));
			return c;
		} catch (IndexOutOfBoundsException | NoSuchElementException e) {
			return 'y';
		}
	}

	/**
	 * Finds the number for rotation
	 * 
	 * @param input
	 * @return degrees to rotate
	 */
	public int parseNum(String input) {
		try {
			Scanner s = new Scanner(input);
			// command
			s.next();
			// letter
			s.next();
			// number
			String num = s.next();
			int i = Integer.parseInt(num.substring(1, num.length() - 1));
			return i;
		} catch (IndexOutOfBoundsException | NoSuchElementException e) {
			return 0;
		}
	}

	/**
	 * Gets the direction from the users input - if the command is move
	 * 
	 * @param input
	 * @return direction
	 */
	public String parseDir(String input) {
		try {
			Scanner s = new Scanner(input);
			// command
			s.next();
			// letter
			s.next();
			// direction
			String dir = s.next();
			dir = dir.substring(1, dir.length() - 1);
			return dir;
		} catch (IndexOutOfBoundsException | NoSuchElementException e) {
			return "null";
		}
	}

	/**
	 * Fills the invalid sqaures - the ones behind the faces
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
		if (loc.getY() != 9 && !(loc.getY() == 7 && loc.getX() == 9)) {
			if (array[loc.getX()][loc.getY() + 1] != null) {
				// check if either has a sword
				if (t.getBottom().equals('-') || array[loc.getX()][loc.getY() + 1].getTop().equals('-')) {
					reactions.add(array[loc.getX()][loc.getY() + 1]);
				}
			}
		}

		// below
		if (loc.getY() != 0 && !(loc.getY() == 2 && loc.getX() == 0)) {
			if (array[loc.getX()][loc.getY() - 1] != null) {
				// check if either has a sword
				if (t.getTop().equals('-') || array[loc.getX()][loc.getY() - 1].getBottom().equals('-')) {
					reactions.add(array[loc.getX()][loc.getY() - 1]);
				}
			}
		}
		// token one is right of token two
		if (loc.getX() != 0 && !(loc.getY() == 0 && loc.getX() == 2)) {
			if (array[loc.getX() - 1][loc.getY()] != null) {
				// check if either has a sword
				if (t.getLeft().equals('-') || array[loc.getX() - 1][loc.getY()].getRight().equals('-')) {
					reactions.add(array[loc.getX() - 1][loc.getY()]);
				}
			}
		}
		// token one is left of token 2
		if (loc.getX() != 9 && !(loc.getY() == 9 && loc.getX() == 7)) {

			if (array[loc.getX() + 1][loc.getY()] != null) {
				if (t.getRight().equals('-') || array[loc.getX() + 1][loc.getY()].getLeft().equals('-')) {

					reactions.add(array[loc.getX() + 1][loc.getY()]);
				}
			}
		}
		//make sure none of them are invalid squares
		ArrayList<Token> invalid = new ArrayList<Token>();
		for(Token r : reactions){
			if(r.getState().equals(State.DEAD)) invalid.add(t);
		}
		
		for(Token i : invalid){
			reactions.remove(i);
		}
		 
		//if there is more than one possible reaction, ask user which one to analyse first
		if (reactions.size() > 1) {

			StringBuilder s = new StringBuilder("WHICH TOKEN DO YOU WANT TO REACT WITH: ");
			for (Token tok : reactions) {
				s.append(tok.getName() + " ,");
			}
			String line = s.toString();
			line = line.substring(0, line.length() - 1);
			String input = frame.getOutput(line);
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

		if (toReact != null)
			react(t, toReact);

	}

	/**
	 * Performs the reaction with the two tokens
	 * @param one first token
	 * @param two second token
	 */
	public void react(Token one, Token two) {
		String dir = "";
		// first check where token two is in relation to token one

		// on top
		if (one.getLocation().getY() == two.getLocation().getY() + 1) {
			dir = "up";
			char charOne = one.getTop();
			char charTwo = two.getBottom();
			analyseReaction(one, charOne, two, charTwo, dir);

		}
		// below
		else if (one.getLocation().getY() == two.getLocation().getY() - 1) {
			dir = "down";
			char charOne = one.getBottom();
			char charTwo = two.getTop();
			analyseReaction(one, charOne, two, charTwo, dir);
		}
		// left
		else if (one.getLocation().getX() == two.getLocation().getX() + 1) {
			dir = "left";
			char charOne = one.getLeft();
			char charTwo = two.getRight();
			analyseReaction(one, charOne, two, charTwo, dir);
		}
		// right
		else if (one.getLocation().getX() == two.getLocation().getX() - 1) {
			dir = "right";
			char charOne = one.getRight();
			char charTwo = two.getLeft();
			analyseReaction(one, charOne, two, charTwo, dir);
		}
		frame.drawBoard();
		if (one.getState().equals(State.ALIVE))
			checkReaction(one);
		if (two.getState().equals(State.ALIVE))
			checkReaction(two);

	}

	/**
	 * Checks all four cases of the reaction
	 * 
	 * @param one
	 * @param charOne
	 * @param two
	 * @param charTwo
	 */
	public void analyseReaction(Token one, char charOne, Token two, char charTwo, String dir) {
		String alternateDir = "";
		if (dir.equals("up"))
			alternateDir = "down";
		else if (dir.equals("down"))
			alternateDir = "up";
		else if (dir.equals("left"))
			alternateDir = "right";
		else if (dir.equals("right"))
			alternateDir = "left";

		// sword against sword
		if (charOne == '-' && charTwo == '-') {
			removeToken(one);
			removeToken(two);
		}
		// sword against nothing
		else if (charOne == '.' && charTwo == '-') {
			removeToken(one);
		} else if (charOne == '-' && charTwo == '.') {
			removeToken(two);
		}
		// sword against shield
		else if (charOne == '#' && charTwo == '-') {
			pushTokenBack(two, dir);
		} else if (charOne == '-' && charTwo == '#') {
			pushTokenBack(one, alternateDir);
		}
	}

	/**
	 * Removes a token from the board and sets state to null
	 * 
	 * @param t
	 */
	public void removeToken(Token t) {
		if (t.getName().equals('1')) {
			board.removeToken(t);
			frame.drawBoard();
			System.out.println("YELLOW WINS");
			System.exit(0);
		} else if (t.getName().equals('0')) {
			board.removeToken(t);
			frame.drawBoard();
			System.out.println("GREEN WINS");
			System.exit(0);
		}
		// check if the token is 0 or 1
		board.removeToken(t);
		t.setState(State.DEAD);
	}

	/**
	 * Pushes a token back, in direction opposite from the shield
	 * 
	 * @param t
	 * @param dir
	 */
	public void pushTokenBack(Token t, String dir) {
		board.removeToken(t);
		t.setLocation(dir);
		board.addToken(t);
	}

	/**
	 * Returns the board
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * Calls the appropriate undo function
	 * 
	 * @return true if undo works
	 */
	public boolean undo() {
		if (commands.isEmpty())
			return false;
		Stack<Command> stack = commands.pop();
		// temp - to reverse order
		Stack<Command> temp = new Stack<Command>();
		for (Command c : stack) {
			temp.push(c);
		}

		// undo all the commands
		for (Command c : temp) {
			c.undo();
		}
		frame.drawBoard();
		return true;
	}

	/**
	 * Returns the current player
	 * 
	 * @return current
	 */
	public Player getCurrentPlayer() {
		return current;
	}

}
