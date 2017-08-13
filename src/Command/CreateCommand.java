package Command;

import controller.Controller;

public class CreateCommand implements Command{
	
	private Controller cont;
	private String input;
	private char c;
	
	public CreateCommand(Controller c, String input){
		this.cont = c;
		this.input = input;
	}
	public void execute() {
		this.c = cont.create(input);
	}
	
	public void undo(){
		cont.undoCreate(c);
	}

}
