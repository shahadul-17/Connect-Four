package connect.four.ai;

import java.util.Random;

import connect.four.Main;
import connect.four.engine.Board;
import connect.four.engine.Move;

public class Minimax {
	
	private byte player;
	private short maximumDepth;
	private int alpha, beta;
	
	private Random random;
	
	public Minimax(byte player, short maximumDepth) {
		this.maximumDepth = maximumDepth;
		this.player = player;
		
		random = new Random();
	}
	
	public void setMaximumDepth(short maximumDepth) {
		this.maximumDepth = maximumDepth;
	}
	
	public Move takeDecision(Board board) {
		alpha = Integer.MIN_VALUE;
		beta = Integer.MAX_VALUE;
		
		return takeDecision(player, (short)0, board);
	}
	
	private Move takeDecision(byte player, short depth, Board board) {
		Move move = new Move();
		
		if (!Main.running || depth == maximumDepth || alpha > beta || board.isGameOver()) {
			move.setMove(board.evaluate(), board.move);
			
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
			
			if (tempMove.value == move.value && random.nextInt(Board.PLAYERS.length) == 0) {
				move.setMove(tempMove.value, boards[i].move);
			}
			else if (player == Board.PLAYERS[0] && tempMove.value > move.value) {
				move.setMove(tempMove.value, boards[i].move);
				
				if (alpha < move.value) {
					alpha = move.value;
				}
			}
			else if (player == Board.PLAYERS[1] && tempMove.value < move.value) {
				move.setMove(tempMove.value, boards[i].move);
				
				if (beta > move.value) {
					beta = move.value;
				}
			}
		}
		
		return move;
	}
	
}