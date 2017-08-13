package Command;

import controller.Controller;
import model.Token;

public class MoveCommand implements Command{
	
	private Controller cont;
	private String input;
	private Token t;
	
	public MoveCommand(Controller c, Token t){
		this.cont = c;
		this.t = t;
	}

	public void undo(){
		System.out.println(t.getName());
		cont.undoMove(t);
	}

}
