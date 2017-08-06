package testsuit;

import static org.junit.Assert.*;


import org.junit.Test;

import model.Board;
import model.GameError;
import model.Location;
import model.Player;
import model.PlayerMap;
import model.Token;

public class Tests {

	/**
	 * ---------------------------//BOARD TESTS //------------------------------
	 */
	@Test
	public void testGetBoard(){
		Board b = new Board();
		Token[][] board = b.getBoard();
		assertEquals(board.length, 10);
	}
	
	@Test 
	public void testAddToken(){
		Board b = new Board();
		Token[][] board = b.getBoard();
		Token t = new Token('a', "a....", true);
		t.setLocation(new Location(4,4));
		b.addToken(t);
		board = b.getBoard();
		assertEquals(board[4][4],t);
		t.setLocation(new Location(2,1));
		b.moveToken(t);
		assertEquals(board[2][1],t); 
	}
	
	@Test(expected = AssertionError.class)
	public void testTokenNotOnBoardError() {
		Board b = new Board();
		try {
			b.moveToken(new Token('a', "a....", false));
		} catch (GameError e) {
			fail(e.getMessage());
		}

	}
	
	/**
	 * ---------------------------//TOKEN TESTS //------------------------------
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testGetCode(){
		Token t = new Token('a', "a.#.-", false);
		Character[][] array = t.getCode();
		Character[][] myarray = new Character[3][3];
		myarray[1][1] = 'a';
		myarray[1][0]  = '.'; //top
		myarray[2][1]  = '#'; //right
		myarray[1][2]  = '.'; //bottom
		myarray[0][1]  = '-'; //left
		assertEquals(array,myarray);
		
	}
	
	/**
	 * ---------------------------//PLAYER TESTS //------------------------------
	 */
	@Test(expected = AssertionError.class)
	public void testWrongNameError() {
		try {
			Player p = new Player("hi");
		} catch (GameError e) {
			fail(e.getMessage());
		}

	}
	
	@Test
	public void testGetName(){
		Player p = new Player("yellow");
		assertEquals(p.getName(), "yellow");
	}
	
	/**
	 * ---------------------------//LOCATION TESTS //------------------------------
	 */
	
	@Test
	public void testGetLocation(){
		Location l = new Location(2,4);
		assertEquals(2, l.getLocation().getX());
		assertEquals(4, l.getLocation().getY());

	}
}


