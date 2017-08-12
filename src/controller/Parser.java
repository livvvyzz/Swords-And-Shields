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
	public String ParseExpressions(String input) {
		Scanner s = new Scanner(input);
		String cmd = s.next();
		String toReturn;
		if (expr.contains(cmd)) {
			if(cmd.equals("create")) return toReturn ="create";
			else if (cmd.equals("rotate")) return toReturn = "rotate";
			else if(cmd.equals("move")) return toReturn ="move";
			else if(cmd.equals("pass")) return toReturn ="pass";
		}
		
		//if none of the above, wrong input
		return toReturn = "null";
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
	

	}
}