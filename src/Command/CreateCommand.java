package Command;

import controller.Controller;
import model.Token;

public class CreateCommand implements Command{
	
	private Controller cont;
	private String input;
	private Token t;
	
	public CreateCommand(Controller c, Token t){
		this.cont = c;
		this.t = t;
	}
	public void undo(){
		cont.undoCreate(t);
	}

}
