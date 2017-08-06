package testsuit;

import static org.junit.Assert.*;


import org.junit.Test;

import model.Board;
import model.Location;
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
	
	@Test
	public void testAddTokenOnToken(){
		
	}
	

	

}
