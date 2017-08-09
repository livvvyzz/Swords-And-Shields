package controller;

import java.util.ArrayList;

/**
 * Parses the expressions sent to the controller
 * @author olivia
 *
 */
public class Parser {

	//holds the four expressions that can be parsed
	private ArrayList<String> expr = new ArrayList<>(); 
	
	/**
	 * initialses parses
	 */
	public Parser(){
		expr.add("create");
		expr.add("rotate");
		expr.add("move");
		expr.add("pass");
	}
	
	/**
	 * Analyses input and calls the appropriate method depending on the command ("move", "create", "rotate", "pass"
	 */
	public void ParseExpressions(String input){
		
	}

	
	/**
	 * Analyses create command
	 */
	public void ParseCreate(){
		
	}
	
	/**
	 * Analyses rotate command
	 */
	public void ParseMove(){
		
	}
	
	/**
	 * Analyses move command
	 */
	public void ParseRotate(){
		
	}
	
	public void ParsePass(){
		
	}
}
