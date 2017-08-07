package controller;

import model.Board;
import model.Location;
import model.Token;
import view.BoardView;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Board b = new Board();
		BoardView view = new BoardView(b);
		Token t = new Token('a', "a#.-.", false);
		t.setLocation(new Location(2,4));
		b.addToken(t);
		view.drawBoard();
	}

}
