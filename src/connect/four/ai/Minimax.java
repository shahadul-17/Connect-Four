package connect.four.ai;

import java.util.Random;

import connect.four.Main;
import connect.four.engine.Board;
import connect.four.engine.Move;

public class Minimax {
	
	private byte player, maximumDepth;
	
	private Random random;
	
	public Minimax(byte player, byte maximumDepth) {
		this.maximumDepth = maximumDepth;
		this.player = player;
		
		random = new Random();
	}
	
	public void setMaximumDepth(byte maximumDepth) {
		this.maximumDepth = maximumDepth;
	}
	
	public Move takeDecision(Board board) {
		return takeDecision(player, (byte)0, board);
	}
	
	private Move takeDecision(byte player, byte depth, Board board) {
		Move move = new Move();
		
		if (!Main.running || depth == maximumDepth || board.isGameOver()) {
			move.setMove(board.evaluate(), board.lastMove);
			
			return move;
		}
		
		Board[] boards = board.getAllPossibleBoards(player);
		
		if (player == Board.PLAYERS[0]) {
			move.value = Integer.MIN_VALUE;
		}
		else {
			move.value = Integer.MAX_VALUE;
		}
		
		for (int i = 0; i < boards.length; i++) {
			if (boards[i] == null) {
				continue;		// skipping null objects...
			}
			
			Move tempMove = takeDecision(Board.PLAYERS[player], (byte)(depth + 1), boards[i]);
			
			if ((tempMove.value == move.value && random.nextInt(Board.PLAYERS.length) == 0) ||
					(player == Board.PLAYERS[0] && tempMove.value > move.value) ||
					(player == Board.PLAYERS[1] && tempMove.value < move.value)) {
				move.setMove(tempMove.value, boards[i].lastMove);
			}
		}
		
		return move;
	}
	
}