package controller;

import java.util.ArrayList;
import java.util.Scanner;

import model.Player;

/**
 * Parses the expressions sent to the controller
 * 
 * @author olivia
 *
 */
public class Parser {

	// holds the four expressions that can be parsed
	private ArrayList<String> expr = new ArrayList<>();
	
	//Controller
	private Controller cont;

	/**
	 * initialses parses
	 */
	public Parser(Controller c) {
		expr.add("create");
		expr.add("rotate");
		expr.add("move");
		expr.add("pass");
		
		this.cont =c;
	}

	/**
	 * Analyses input and calls the appropriate method depending on the command
	 * ("move", "create", "rotate", "pass"
	 */
	public void ParseExpressions(String input) {
		Scanner s = new Scanner(input);
		String cmd = s.next();
		if (expr.contains(cmd)) {
			if(cmd.equals("create")) ParseCreate(s);
			else if (cmd.equals("rotate")) ParseRotate(s);
			else if(cmd.equals("move")) ParseRotate(s);
			else if(cmd.equals("pass")) ParsePass(s);
		}
	}

	/**
	 * Check if the users input is Pass, if so dont parse the expression
	 * @param input
	 * @return 		true if is pass
	 */
	public boolean checkPass(String input){
		Scanner s = new Scanner(input);
		String cmd = s.next();
		if (expr.contains(cmd)) {
			if(cmd.equals("pass")) return true;
		}
		return false;
	}
	/**
	 * Analyses create command
	 */
	public void ParseCreate(Scanner s) {
		//get the leter to be create
		s.useDelimiter("<");
		char c = s.next().toCharArray()[0];
		
		//init token for player
		cont.create(c);
	}

	/**
	 * Analyses rotate command
	 */
	public void ParseMove(Scanner input) {

	}

	/**
	 * Analyses move command
	 */
	public void ParseRotate(Scanner input) {

	}

	public void ParsePass(Scanner input) {

	}

	
}
