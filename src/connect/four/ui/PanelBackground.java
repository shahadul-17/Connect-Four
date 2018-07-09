package connect.four.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PanelBackground extends JPanel {
	
	private static final long serialVersionUID = 3330510769063731974L;
	
	private Image background;
	
	public PanelBackground(String imageName) {
		background = new ImageIcon(this.getClass().getResource("/images/" + imageName)).getImage();
		
		setLayout(null);
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		Graphics2D graphics2D = (Graphics2D)graphics.create();
        graphics2D.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        graphics2D.drawImage(background, 0, 0, getWidth(), getHeight(), null);
	}
	
}