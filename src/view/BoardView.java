package view;

import java.util.Scanner;

import model.Board;
import model.PlayerMap;
import model.State;
import model.Token;

public class BoardView {

	private Board board;
	private PlayerMap yellow;
	private PlayerMap green;

	private int BOARD_SIZE = 40;

	/**
	 * Draws the board into the console
	 * 
	 * @param board
	 */
	public BoardView(Board board, PlayerMap y, PlayerMap g) {
		this.board = board;
		this.green = g;
		this.yellow = y;
	}

	/**
	 * Updates the board in the console every time a change occurs
	 */
	public void update() {
		drawBoard();
	}

	/**
	 * Draws the board in the console (40*40) plus the inactive tokens
	 */
	public void drawBoard() {
		Token[][] b = board.getBoard();

		// top boarder
		System.out.println("");
		System.out.println("-----------------------------------------          Yellows Tokens");

		// rows of the board
		for (int row = 0; row < b.length; row++) {
			// levels of the rows
			for (int level = 0; level < 3; level++) {
				// iterate through colums of the board
				StringBuilder line = new StringBuilder();
				line.append("|");
				for (int col = 0; col < b.length; col++) {
					if (b[row][col] == null) {
						line.append("   ");
					} else {
						Token t = b[row][col];
						Character[][] c = t.getCode();
						// iterate through name per row
						for (int j = 0; j < 3; j++) {
							if (c[level][j] == null) {
								line.append(" ");
							} else {
								if (c[level][j].equals('.')) {
									line.append(" ");
								} else {
									line.append(c[level][j]);
								}
							}
						}
					}
					line.append("|");
				}
				line.append("      ");
				// display pieces that are not on board
				Token[][] t = yellow.getTokensArray();
				if (row < 5)
					t = yellow.getTokensArray();
				else
					t = green.getTokensArray();

				int newRow = row;
				if (row >= 5) {
					newRow = row - 5;
				} 
				for (int col = 0; col < t.length; col++) {
					if (t[newRow][col] != null) {
						Token token = t[newRow][col];
						if (token.getState().equals(State.INACTIVE)) {
							
							// check if there is a char in that spot
							Character[][] code = token.getCode();
							for (int j = 0; j < 3; j++) {
								if (code[level][j] == null) {
									line.append(" ");
								} else {
									if (code[level][j].equals('.')) {
										line.append(" ");
									} else {
										line.append(code[level][j]);
									}
								}
							}
							line.append("  ");
						}
					} else {
						line.append(" ");
					}
				}

				System.out.println(line);

			}
			if (row == 4)
				System.out.println("-----------------------------------------          Green Tokens");
			else
				System.out.println("-----------------------------------------");
		}

	}
	
	/**
	 * Receives output from controller to be pritned
	 * @param output 	text to be sent to the console
	 */
	public String getOutput(String output){
		Scanner input = new Scanner(System.in);

		System.out.println(output);
		String s = input.next(); // getting a String value
		return s;
	}
}
