package connect.four.ai;

import connect.four.Main;
import connect.four.engine.Board;
import connect.four.engine.Move;

public class ArtificialIntelligence implements Runnable {
	
	private volatile boolean makeMove;
	
	private ArtificialIntelligenceListener artificialIntelligenceListener;
	
	private Minimax minimax;
	private Board board;
	
	public ArtificialIntelligence(byte player, short depth, Board board) {
		minimax = new Minimax(player, depth);
		this.board = board;
	}
	
	public void setDepth(short depth) {
		minimax.setMaximumDepth(depth);
	}
	
	public void reset() {
		makeMove = false;
	}
	
	public void makeMove() {
		makeMove = true;
	}
	
	public void addArtificialIntelligenceListener(ArtificialIntelligenceListener artificialIntelligenceListener) {
		this.artificialIntelligenceListener = artificialIntelligenceListener;
	}
	
	@Override
	public void run() {
		while (Main.running) {
			if (!board.gameOver && makeMove) {
				Move move = minimax.takeDecision(board);
				
				artificialIntelligenceListener.decisionTaken(move.rowIndex, move.columnIndex, this);
				
				makeMove = false;
			}
			
			try {
				Thread.sleep(1000);
			}
			catch (Exception exception) {
				// don't need to handle this exception...
			}
		}
	}
	
}