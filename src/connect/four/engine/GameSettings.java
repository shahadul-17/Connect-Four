package connect.four.engine;

public class GameSettings {
	
	private static final byte[] DEPTHS = {
		3, 4, 6, 8
	};
	
	public byte mode, depth, player;
	
	public GameSettings() {
		mode = 1;
		depth = DEPTHS[1];
		player = Board.PLAYERS[0];
	}
	
	public void setDepth(byte index) {
		depth = DEPTHS[index];
	}
	
	public static byte getDefaultDepth() {
		return DEPTHS[1];
	}
	
}