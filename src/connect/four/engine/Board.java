package connect.four.engine;

import connect.four.Main;

public class Board {
	
	public volatile boolean gameOver;
	public byte winner;
	public int winningDiscs = Main.DEFAULT;
	
	public static final byte[] PLAYERS = {
		1,		// 1 = player 1
		0		// 0 = player 2
	};
	private byte[][] board;
	
	public Move lastMove;
	
	public Board() {
		board = new byte[Main.BOARD_HEIGHT][Main.BOARD_WIDTH];
		
		reset();
	}
	
	public void reset() {
		gameOver = false;
		winner = Main.DEFAULT;
		
		for (byte i = 0; i < Main.BOARD_HEIGHT; i++) {
			for (byte j = 0; j < Main.BOARD_WIDTH; j++) {
				board[i][j] = Main.DEFAULT;
			}
		}
		
		lastMove = new Move();
		lastMove.player = PLAYERS[1];		// player that played last...
	}
	
	public Board(Board board) {
		winner = board.winner;
		this.board = new byte[Main.BOARD_HEIGHT][Main.BOARD_WIDTH];
		lastMove = new Move(board.lastMove.value, board.lastMove);
		lastMove.player = board.lastMove.player;
		
		for (byte i = 0; i < Main.BOARD_HEIGHT; i++) {
			for (byte j = 0; j < Main.BOARD_WIDTH; j++) {
				this.board[i][j] = board.board[i][j];
			}
		}
	}
	
	public byte getValue(byte rowIndex, byte columnIndex) {
		return board[rowIndex][columnIndex];
	}
	
	public void setValue(byte rowIndex, byte columnIndex, byte value) {
		board[rowIndex][columnIndex] = value;
	}
	
	public boolean isColumnAvailable(byte columnIndex) {
		return board[0][columnIndex] == Main.DEFAULT;
	}
	
	public byte getAvailableRowIndex(byte columnIndex) {
		for (byte i = Main.BOARD_HEIGHT - 1; i > -1; i--) {
			if (board[i][columnIndex] == Main.DEFAULT) {
				return i;
			}
		}
		
		return Main.DEFAULT;		// returns -1...
	}
	
	public byte makeMove(byte columnIndex, byte player) {		// returns if move was successfully made...
		byte rowIndex = getAvailableRowIndex(columnIndex);
		
		if (rowIndex != Main.DEFAULT) {
			board[rowIndex][columnIndex] = player;
			lastMove = new Move(rowIndex, columnIndex, player);
		}
		
		return rowIndex;
	}
	
	public void makeMove(byte rowIndex, byte columnIndex, byte player) {		// returns if move was successfully made...
		board[rowIndex][columnIndex] = player;
		lastMove = new Move(rowIndex, columnIndex, player);
	}
	
	public Board[] getAllPossibleBoards(byte player) {
		Board[] boards = new Board[Main.BOARD_WIDTH];
		
		for (byte i = 0; i < Main.BOARD_WIDTH; i++) {		// iterating through columns and making all possible moves in one chance...
			if (isColumnAvailable(i)) {
				Board board = new Board(this);
				
				if (board.makeMove(i, player) > Main.DEFAULT) {		// checking if the move was successfully made...
					boards[i] = board;
				}
			}
		}
		
		return boards;
	}
	
	// we provide the "numberOfConsecutiveDiscs" in the function and it will return the number of times it occurred... 
	private int checkConsecutiveDiscs(byte numberOfConsecutiveDiscs, byte player) {
		byte i, j, k, flag;
		int occurrence = 0;
		
		for (i = 0; i < Main.BOARD_HEIGHT; i++) {		// checking consecutive discs in a row...
			for (j = 0; j < Main.BOARD_WIDTH - numberOfConsecutiveDiscs + 1; j++) {
                flag = 0;
                
                if (player == Main.DEFAULT) {
                	occurrence = 0;
                }
                
				for (k = 0; k < numberOfConsecutiveDiscs; k++) {
                    if (player == Main.DEFAULT && player != board[i][j + k] && board[i][j] == board[i][j + k]) {
                    	occurrence = (occurrence * 10) + i;
                    	occurrence = (occurrence * 10) + (j + k);
                    	
                        flag++;
                    }
                    else if (player != Main.DEFAULT && player == board[i][j + k]) {
                    	flag++;
                    }
                }
                
                if (flag == numberOfConsecutiveDiscs) {
                	if (player == Main.DEFAULT) {
                		winner = board[i][j];
                		
                		return -occurrence;
                	}
                	else {
                		occurrence++;
                	}
                }
			}
		}
		
		for (i = Main.BOARD_HEIGHT - 1; i > numberOfConsecutiveDiscs - 2; i--) {		// checking consecutive discs in a column...
			for (j = 0; j < Main.BOARD_WIDTH; j++) {
                flag = 0;
                
                if (player == Main.DEFAULT) {
                	occurrence = 0;
                }
                
                for (k = 0; k < numberOfConsecutiveDiscs; k++) {
                    if (player == Main.DEFAULT && player != board[i - k][j] && board[i][j] == board[i - k][j]) {
                    	occurrence = (occurrence * 10) + (i - k);
                    	occurrence = (occurrence * 10) + j;
                    	
                    	flag++;
                    }
                    else if (player != Main.DEFAULT && player == board[i - k][j]) {
                    	flag++;
                    }
                }
                
                if (flag == numberOfConsecutiveDiscs) {
                	if (player == Main.DEFAULT) {
                		winner = board[i][j];
                		
                		return -occurrence;
                	}
                	else {
                		occurrence++;
                	}
                }
			}
		}
		
		for (i = 0; i < Main.BOARD_HEIGHT - numberOfConsecutiveDiscs + 1; i++) {		// checking consecutive discs in (left-to-right + top-to-bottom) diagonal...
			for (j = 0; j < Main.BOARD_WIDTH - numberOfConsecutiveDiscs + 1; j++) {
                flag = 0;
                
                if (player == Main.DEFAULT) {
                	occurrence = 0;
                }
                
                for (k = 0; k < numberOfConsecutiveDiscs; k++) {
                    if (player == Main.DEFAULT && player != board[i + k][j + k] && board[i][j] == board[i + k][j + k]) {
                    	occurrence = (occurrence * 10) + (i + k);
                    	occurrence = (occurrence * 10) + (j + k);
                    	
                    	flag++;
                    }
                    else if (player != Main.DEFAULT && player == board[i + k][j + k]) {
                    	flag++;
                    }
                }
                
                if (flag == numberOfConsecutiveDiscs) {
                	if (player == Main.DEFAULT) {
                		winner = board[i][j];
                		
                		return -occurrence;
                	}
                	else {
                		occurrence++;
                	}
                }
			}
		}
		
		for (i = Main.BOARD_HEIGHT - 1; i > numberOfConsecutiveDiscs - 2; i--) {		// checking consecutive discs in (right-to-left + bottom-to-top) diagonal...
			for (j = 0; j < Main.BOARD_WIDTH - numberOfConsecutiveDiscs + 1; j++) {
                flag = 0;
                
                if (player == Main.DEFAULT) {
                	occurrence = 0;
                }
                
                for (k = 0; k < numberOfConsecutiveDiscs; k++) {
                    if (player == Main.DEFAULT && player != board[i - k][j + k] && board[i][j] == board[i - k][j + k]) {
                    	occurrence = (occurrence * 10) + (i - k);
                    	occurrence = (occurrence * 10) + (j + k);
                    	
                        flag++;
                    }
                    else if (player != Main.DEFAULT && player == board[i - k][j + k]) {
                    	flag++;
                    }
                }
                
                if (flag == numberOfConsecutiveDiscs) {
                	if (player == Main.DEFAULT) {
                		winner = board[i][j];
                		
                		return -occurrence;
                	}
                	else {
                		occurrence++;
                	}
                }
			}
		}
		
		if (player == Main.DEFAULT) {
			return winner = Main.DEFAULT;
		}
		
		return occurrence;
	}
	
	private boolean isWinnerFound() {
		int occurrence = checkConsecutiveDiscs((byte)4, (byte)(-1));		// four consecutive discs...
		
		if (occurrence != Main.DEFAULT) {
			winningDiscs = occurrence;
			
			return true;
		}
		
		return false;
	}
	
	public int evaluate() {
		short[] scores = new short[2];
		
		if (isWinnerFound()) {
			scores[winner] += 100;		// whoever wins, gets 100 points...
		}
		
		for (int i = 0; i < PLAYERS.length; i++) {
			scores[PLAYERS[i]] += (checkConsecutiveDiscs((byte)3, PLAYERS[i]) * 10) + checkConsecutiveDiscs((byte)2, PLAYERS[i]);
		}
		
		return scores[PLAYERS[0]] - scores[PLAYERS[1]];		// will return 0 if match is drawn...
	}
	
    public boolean isGameOver() {
    	if (isWinnerFound()) {		// if someone won the game, then game over...
    		return true;
    	}
    	
    	for (byte i = 0; i < Main.BOARD_HEIGHT; i++) {		// check if all cells are full... which means it's a draw, then game over...
    		for (byte j = 0; j < Main.BOARD_WIDTH; j++) {
        		if (board[i][j] == Main.DEFAULT) {
        			return false;
        		}
        	}
    	}
    	
    	return true;
    }
    
}