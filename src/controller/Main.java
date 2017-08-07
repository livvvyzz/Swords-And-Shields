package controller;

import model.Board;
import model.Location;
import model.PlayerMap;
import model.Token;
import view.BoardView;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Board b = new Board();
		PlayerMap y = new PlayerMap(true);
		PlayerMap g = new PlayerMap(false);
		BoardView view = new BoardView(b,y,g);
		Token t = new Token('a', "a#.-.", false);
		t.setLocation(new Location(2,4));
		b.addToken(t);
		view.drawBoard();
	}

}
