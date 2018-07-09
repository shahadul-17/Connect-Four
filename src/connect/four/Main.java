package connect.four;

import connect.four.ui.Frame;

public class Main {
	
	public static volatile boolean running;
	public static final byte DEFAULT = -1, DISC_SIZE = 80, BOARD_HEIGHT = 6,
			BOARD_WIDTH = 7, FALLING_DISC_SPEED = 40, DISC_TIMER_DELAY = 15;
	
	private static final String TITLE = "Connect Four";
	
	public static void main(String[] args) {
		running = true;
		
		new Frame(1000, 660, TITLE).setVisible(true);
	}
	
}