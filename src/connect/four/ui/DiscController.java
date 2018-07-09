package connect.four.ui;

import java.awt.Rectangle;

import javax.swing.JPanel;

import connect.four.Main;
import connect.four.engine.Board;

public class DiscController {
	
	private byte[] poolIndices;
	
	private DiscListener discListener;
	
	private Disc[][] pool;
	
	public void addDiscListener(DiscListener discListener) {
		this.discListener = discListener;
	}
	
	public void initializePool(JPanel discContainer) {
		poolIndices = new byte[2];		// index 0: for PLAYER 2 Color, index 1: for PLAYER 1 color...
		pool = new Disc[poolIndices.length][(Main.BOARD_WIDTH * Main.BOARD_HEIGHT) / 2];		// total 42 hollow space... 21 for Red, 21 for Yellow...
		
		for (byte i = 0; i < pool.length; i++) {
			for (byte j = 0; j < pool[i].length; j++) {
				Disc disc = new Disc(i);
				disc.setBounds(0, -Main.DISC_SIZE, Main.DISC_SIZE, Main.DISC_SIZE);
				disc.addDiscListener(discListener);
				discContainer.add(disc);
				
				pool[i][j] = disc;
			}
		}
	}
	
	public void updateDiscLocation(Rectangle boardBounds) {
		for (byte i = 0; i < pool.length; i++) {
			for (byte j = 0; j < pool[i].length; j++) {
				Disc disc = pool[i][j];
				
				if (disc.rowIndex != -1 || disc.columnIndex != -1) {
					disc.calculateLocation(boardBounds);
				}
			}
		}
	}
	
	public void reset() {
		for (byte i = 0; i < poolIndices.length; i++) {
			poolIndices[i] = 0;
			
			for (byte j = 0; j < pool[i].length; j++) {
				pool[i][j].setBounds(0, -Main.DISC_SIZE, Main.DISC_SIZE, Main.DISC_SIZE);
				pool[i][j].reset();
			}
		}
	}
	
	/*
	 * poorly written method... needs to be improved...
	 */
	public void markWinner(Board board) {		// needs to be optimized...
		byte i = -1, j = -1, counter = 0;
		int winningDiscs = board.winningDiscs * (-1);
		
		do {
			int digit = winningDiscs % 10;
			
			if (counter == 0) {
				j = (byte)digit;
			}
			else {
				i = (byte)digit;
			}
			
			winningDiscs /= 10;
			
			if (i != -1 && j != -1) {
				for (int y = 0; y < pool[board.lastMove.player].length; y++) {
					if (pool[board.lastMove.player][y].rowIndex == i && pool[board.lastMove.player][y].columnIndex == j) {
						pool[board.lastMove.player][y].markWinner();
						
						break;
					}
				}
			}
			
			counter++;
			
			if (counter == 2) {
				i = j = -1;
				counter = 0;
			}
		}
		while (winningDiscs != 0);
		
		pool[0][0].getParent().repaint();
	}
	
	public void fall(byte player, byte rowIndex, byte columnIndex, Board board, Rectangle boardBounds) {
		if (poolIndices[player] < pool[player].length) {
			Disc disc = pool[player][poolIndices[player]];
			disc.rowIndex = rowIndex;
			disc.columnIndex = columnIndex;
			disc.calculateLocation(boardBounds);
			
			poolIndices[player]++;
		}
	}
	
}