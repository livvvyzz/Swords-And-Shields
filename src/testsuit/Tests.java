package testsuit;

import static org.junit.Assert.*;


import org.junit.Test;

import controller.Controller;
import model.Board;
import model.GameError;
import model.Location;
import model.Player;
import model.PlayerMap;
import model.State;
import model.Token;
import view.BoardView;

public class Tests {
//-----------------------------------MODEL TESTS----------------------------------------------------------------
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
		board = b.getBoard();
		assertEquals(board[2][1],t); 
	}
	
	@Test
	public void testTokenNotOnBoardError() {
		Board b = new Board();
		assertEquals(b.moveToken(new Token('a',"a....",false)), false);
	}
	
	@Test
	public void testTokenPushesCurrent(){
		Board b = new Board();
		Token current = new Token('a', "a....", false);
		current.setLocation(new Location(6,6));
		b.addToken(current);
		Token newTok = new Token('b',"b....", false);
		newTok.setLocation(new Location(5,6));
		b.addToken(newTok);
		newTok.setLocation("right");
		b.moveToken(newTok);
		//check that newTok is in right pos
		assertEquals(new Location(6,6).getX(), newTok.getLocation().getX());
		assertEquals(new Location(6,6).getY(), newTok.getLocation().getY());
		assertEquals(new Location(7,6).getX(), current.getLocation().getX() );
		assertEquals(new Location(7,6).getY(), current.getLocation().getY() );

	}
	
	@Test
	public void testTokenPushesTwo(){
		Board b = new Board();
		Token one = new Token('a', "a....", false);
		one.setLocation(new Location(6,6));
		b.addToken(one);
		Token two = new Token('b',"b....", false);
		two.setLocation(new Location(5,6));
		b.addToken(two);
		Token three = new Token('a', "a....", false);
		three.setLocation(new Location(7,6));
		b.addToken(three);
		two.setLocation("right");
		b.moveToken(two);
		//check that newTok is in right pos
		assertEquals(new Location(6,6).getX(), two.getLocation().getX());
		assertEquals(new Location(6,6).getY(), two.getLocation().getY());
		assertEquals(new Location(7,6).getX(), one.getLocation().getX() );
		assertEquals(new Location(7,6).getY(), one.getLocation().getY() );
		assertEquals(new Location(8,6).getX(), three.getLocation().getX() );
		assertEquals(new Location(8,6).getY(), three.getLocation().getY() );
	}
	
	@Test
	public void testTokenPushesAnotherOffBoard(){
		Board b = new Board();
		Token one = new Token('a', "a....", false);
		one.setLocation(new Location(5,0));
		b.addToken(one);
		Token two = new Token('b',"b....", false);
		two.setLocation(new Location(5,1));
		b.addToken(two);
		two.setLocation("up");
		b.moveToken(two);
		//check that newTok is in right pos
		assertEquals(new Location(5,0).getX(), two.getLocation().getX());
		assertEquals(new Location(5,0).getY(), two.getLocation().getY());
		assertEquals(new Location(5,-1).getX(), one.getLocation().getX() );
		assertEquals(new Location(5,-1).getY(), one.getLocation().getY() );
		assertEquals(State.DEAD, one.getState());
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
	
	@Test
	public void testInitMap(){
		PlayerMap p = new PlayerMap(false);
		assertEquals(p.getMap().size(), 24);
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


//-----------------------------------VIEW TESTS----------------------------------------------------------------
	/**
	 * ---------------------------//BOARDVIEW TESTS //------------------------------
	 */

	@Test
	public void testDrawBoard(){
		Board b = new Board();
		BoardView view = new BoardView(b, new PlayerMap(false), new PlayerMap(true));
		view.drawBoard();
	}
	
//-----------------------------------UNDO TESTS----------------------------------------------------------------
	
	@Test
	public void testCreateUndo(){
		Controller c = new Controller();
		c.roundOne("create <a> <90>");
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('a').getState(), State.ALIVE);
		c.undo();
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('a').getState(), State.INACTIVE);
		
	}
	
	@Test
	public void testMoveUndo(){
		Controller c = new Controller();
		c.roundOne("create <a> <90>");
		c.roundTwo("move <a> <up>");
		c.roundOne("create <b> <90>");
		c.roundTwo("move <b> <up>");
		c.roundOne("create <c> <90>");
		c.roundThree("move <c> <up>");
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('a').getLocation().getY(), 4);
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('b').getLocation().getY(), 5);
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('c').getLocation().getY(), 6);
		c.undo();
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('a').getLocation().getY(), 5);
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('b').getLocation().getY(), 6);
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('c').getLocation().getY(), 7);
	}
	
	@Test
	public void testUndoPushTokenOffBoard(){
		Controller c = new Controller();
		c.roundOne("create <a> <90>");
		c.roundTwo("move <a> <down>");
		c.roundOne("create <b> <90>");
		c.roundTwo("move <b> <down>");
		c.roundTwo("move <b> <down>");
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('a').getLocation().getY(), 10);
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('a').getState(), State.DEAD);
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('b').getLocation().getY(), 9);
		c.undo();
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('a').getLocation().getY(), 9);
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('a').getState(), State.ALIVE);
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('b').getLocation().getY(), 8);
	}
	
	@Test
	public void testMultUndos(){
		Controller c = new Controller();
		c.roundOne("create <a> <90>");
		c.roundTwo("move <a> <down>");
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('a').getLocation().getY(),8);
		c.undo();
		c.undo();
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('a').getLocation().getY(),7);
		assertEquals(c.getCurrentPlayer().getPlayerMap().getMap().get('a').getState(), State.INACTIVE);
	}

//-----------------------------------CONTROLLER TESTS----------------------------------------------------------------

	@Test
	public void TestTrick(){
		Controller c = new Controller();
		assertEquals(c.roundOne("create <a> <90>"),true);
		assertEquals(c.roundTwo("move <b> <up>"),false);
		assertEquals(c.roundTwo("move <a> <up>"),true);
		assertEquals(c.roundTwo("create <b> <up>"),false);
		assertEquals(c.roundThree("pass"),true);
		assertEquals(c.roundThree("move <a> <up>"),true);
	}
}
