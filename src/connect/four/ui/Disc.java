package connect.four.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import javax.swing.JLabel;
import javax.swing.Timer;

import connect.four.Main;

public class Disc extends JLabel implements ActionListener {
	
	private boolean hollow = false, markWinner = false;
	public byte rowIndex = Main.DEFAULT, columnIndex = Main.DEFAULT;
	public int preferredY = Main.DEFAULT;		// WARNING: DO NOT CHANGE THE VALUES OF THESE VARIABLES...!!! READ ONLY ACCESS...!!!
	private static final int BOARD_OFFSET = 6, DISC_SIZE = Main.DISC_SIZE - (BOARD_OFFSET * 2);
	private static final long serialVersionUID = -493280532824123771L;
	
	private DiscListener discListener;
	
	private Color color;
	private static final Color[] DISC_COLORS = {
		new Color(23, 232, 38), new Color(30, 144, 255)
	};
	private Timer timer;
	
	public Disc(byte player) {
		hollow = false;
		color = DISC_COLORS[player];
		
		timer = new Timer(Main.DISC_TIMER_DELAY, this);
		timer.setRepeats(true);
	}
	
	public Disc(boolean hollow, byte rowIndex, byte columnIndex) {
		this.hollow = hollow;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		
		color = Frame.TRANSPARENT_BLACK_COLORS[1];
	}
	
	public void reset() {
		markWinner = false;
		preferredY = rowIndex = columnIndex = Main.DEFAULT;
	}
	
	public void addDiscListener(DiscListener discListener) {
		this.discListener = discListener;
	}
	
	public void calculateLocation(Rectangle boardBounds) {
		int y = getY(), tempPreferredY = (boardBounds.y + 6) + ((getHeight() - 2) * rowIndex);
		
		if (preferredY < tempPreferredY) {
			preferredY = tempPreferredY;
			
			timer.start();
		}
		else if (preferredY > tempPreferredY) {
			y = preferredY = tempPreferredY;
		}
		
		setLocation((boardBounds.x + 7) + ((getWidth() - 2) * columnIndex), y);
	}
	
	public void changeColor(byte colorState) {
		color = DISC_COLORS[colorState];
		
		paintComponent(getGraphics());
	}
	
	public void markWinner() {
		markWinner = true;
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		Graphics2D graphics2D = (Graphics2D)graphics.create();
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setColor(color);
		
		if (hollow) {		// hollow
			Area hollowRegionArea = new Area(new Ellipse2D.Float(BOARD_OFFSET, BOARD_OFFSET, DISC_SIZE, DISC_SIZE));
			Area totalDiscArea = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
			
            totalDiscArea.subtract(hollowRegionArea);
            graphics2D.fill(totalDiscArea);
		}
		else {		// fill
			graphics2D.fillOval(BOARD_OFFSET, BOARD_OFFSET, DISC_SIZE, DISC_SIZE);
			
			byte size, halfSize;
			
			if (markWinner) {
				size = (byte) (DISC_SIZE / 1.5);
				
				graphics2D.setColor(Color.YELLOW);
			}
			else {
				size = DISC_SIZE / 3;
				
				graphics2D.setColor(Color.WHITE);
			}
			
			halfSize = (byte)(size / 2);
			
			graphics2D.fillOval((getWidth() / 2) - halfSize, (getHeight() / 2) - halfSize, size, size);
		}
	}
	
	@Override
	public String toString() {
		return "[ " + rowIndex + ", " + columnIndex + " ]";		// position of the disc in the board...
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (getY() < preferredY) {
			int difference = preferredY - getY();
			
			if (difference < Main.FALLING_DISC_SPEED) {
				setLocation(getX(), getY() + difference);
			}
			else {
				setLocation(getX(), getY() + Main.FALLING_DISC_SPEED);
			}
			
			getParent().revalidate();
			getParent().repaint();
		}
		else {
			timer.stop();
			discListener.discHasFallen(this);
		}
	}
	
}