package view;

import model.Board;
import model.Token;

public class BoardView {

	private Board board;

	private int BOARD_SIZE = 40;

	/**
	 * Draws the board into the console
	 * 
	 * @param board
	 */
	public BoardView(Board board) {
		this.board = board;
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
		System.out.println("-----------------------------------------");

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
				System.out.println(line);

			}
			System.out.println("-----------------------------------------");
		}

	}
}
