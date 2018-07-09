package connect.four.engine;

import connect.four.Main;

public class Move {
	
	public byte rowIndex, columnIndex, player;
	public int value;
	
	public Move() {
		value = rowIndex = columnIndex = player = Main.DEFAULT;		// initializing...
	}
	
	public Move(byte rowIndex, byte columnIndex, byte player) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		this.player = player;
	}
	
	public Move(int value, Move move) {
		this.value = value;
		this.rowIndex = move.rowIndex;
		this.columnIndex = move.columnIndex;
	}
	
	public void setMove(int value, Move move) {
		this.value = value;
		this.rowIndex = move.rowIndex;
		this.columnIndex = move.columnIndex;
	}
}