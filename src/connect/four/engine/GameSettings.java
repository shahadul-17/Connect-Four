package connect.four.engine;

public class GameSettings {
	
	private static final short[] DEPTHS = {
		1024, 2048, 4096, 8192
	};
	
	private byte mode, player;
	private short depth;
	
	public GameSettings() {
		mode = 1;
		depth = DEPTHS[1];
		player = Board.PLAYERS[0];
	}
	
	public byte getMode() {
		return mode;
	}

	public void setMode(byte mode) {
		this.mode = mode;
	}

	public byte getPlayer() {
		return player;
	}

	public void setPlayer(byte player) {
		this.player = player;
	}
	
	public short getDepth() {
		return depth;
	}
	
	public void setDepth(byte index) {
		depth = DEPTHS[index];
	}
	
	public static short getDefaultDepth() {
		return DEPTHS[1];
	}
	
}