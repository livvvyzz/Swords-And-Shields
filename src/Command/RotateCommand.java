package Command;

import controller.Controller;
import model.Token;

public class RotateCommand implements Command{
	
	private Controller cont;
	private String input;
	private Token t;
	
	public RotateCommand(Controller c, Token t){
		this.cont = c;
		this.t = t;
	}

	public void undo(){
		cont.undoRotate(t);
	}

} 
