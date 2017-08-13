package Command;

import java.util.Stack;

public class CommandStack {
	
	//holds all command
	Stack<Command> cmds;
	
	public CommandStack(){
		cmds = new Stack<Command>();
	}
	
	/**
	 * Returns the most recent command
	 */
	public Command get(){
		
		//check not empty
		if(!cmds.isEmpty()){
			return cmds.pop();
		}
		return null;
	}
	
	public void put(Command cmd){
		cmds.push(cmd);
	}
}
