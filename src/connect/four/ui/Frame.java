package connect.four.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import connect.four.Main;
import connect.four.ai.ArtificialIntelligence;
import connect.four.ai.ArtificialIntelligenceListener;
import connect.four.engine.Board;
import connect.four.engine.GameSettings;

public class Frame extends JFrame implements ActionListener, MouseListener, MouseMotionListener,
											ComponentListener, WindowListener, DiscListener, ArtificialIntelligenceListener {
	
	private byte player = Board.PLAYERS[0];		// setting Player-1 as first player...
	private static final long serialVersionUID = 6210472781464718706L;
	
	public static final Color[] TRANSPARENT_BLACK_COLORS = {
		new Color(0, 0, 0, 80), new Color(0, 0, 0, 98)
	};
	
	private Disc pointer;
	private DiscController discController;
	private JPanel panelBoard;
	
	private NewGameDialog newGameDialog;
	private Board board;
	private ArtificialIntelligence[] artificialIntelligence;
	
	public Frame(int width, int height, String title) {
		board = new Board();
		artificialIntelligence = new ArtificialIntelligence[Board.PLAYERS.length];
		
		for (byte i = 0; i < artificialIntelligence.length; i++) {
			artificialIntelligence[i] = new ArtificialIntelligence(Board.PLAYERS[i], GameSettings.getDefaultDepth(), board);
			artificialIntelligence[i].addArtificialIntelligenceListener(this);
			
			new Thread(artificialIntelligence[i]).start();
		}
		
		initialize(width, height, title);
	}
	
	private void initialize(int width, int height, String title) {
		setIconImage(new ImageIcon(this.getClass().getResource("/images/icon.png")).getImage());
		setTitle(title);
		setSize(width, height);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentListener(this);
		addWindowListener(this);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menuGame = new JMenu("Game");
		menuBar.add(menuGame);
		
		JMenuItem menuItemNewGame = new JMenuItem("New Game");
		menuItemNewGame.addActionListener(this);
		menuGame.add(menuItemNewGame);
		
		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.addActionListener(this);
		menuGame.add(menuItemExit);
		
		newGameDialog = new NewGameDialog(375, 185, getTitle() + " - New Game", this);
		
		PanelBackground contentPane = new PanelBackground("background.jpg");
		setContentPane(contentPane);
		
		panelBoard = new JPanel(new GridLayout(Main.BOARD_HEIGHT, Main.BOARD_WIDTH));
		panelBoard.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelBoard.setSize(new Dimension(Main.BOARD_WIDTH * Main.DISC_SIZE, Main.BOARD_HEIGHT * Main.DISC_SIZE));
		panelBoard.setLocation((getWidth() / 2) - (panelBoard.getWidth() / 2), (getHeight() / 2) - (panelBoard.getHeight() / 2) - 30);
		panelBoard.setBackground(Frame.TRANSPARENT_BLACK_COLORS[0]);
		panelBoard.addMouseMotionListener(this);
		contentPane.add(panelBoard);
		
		pointer = new Disc(Board.PLAYERS[0]);
		pointer.setSize(Main.DISC_SIZE, Main.DISC_SIZE);
		pointer.setLocation(panelBoard.getX() + 6, panelBoard.getY() - pointer.getHeight());
		contentPane.add(pointer);
		
		discController = new DiscController();
		discController.addDiscListener(this);
		discController.initializePool(contentPane);		// initializes all the discs...
		
		for (byte i = 0; i < Main.BOARD_HEIGHT; i++) {
			for (byte j = 0; j < Main.BOARD_WIDTH; j++) {
				Disc hollowDisc = new Disc(true, i, j);
				hollowDisc.addMouseListener(this);
				hollowDisc.addMouseMotionListener(this);
				panelBoard.add(hollowDisc);
			}
		}
	}
	
	private void makeArtificiallyIntelligentMove() {
		if ((newGameDialog.gameSettings.mode == 1 && newGameDialog.gameSettings.player != player) ||
				newGameDialog.gameSettings.mode == 2) {
			artificialIntelligence[player].makeMove();
		}
	}
	
	private void startNewGame() {
		if (newGameDialog.makeVisible()) {
			for (byte i = 0; i < artificialIntelligence.length; i++) {
				artificialIntelligence[i].reset();		// set makeMove -> false...
				artificialIntelligence[i].setDepth(newGameDialog.gameSettings.depth);
			}
			
			player = Board.PLAYERS[0];
			pointer.changeColor(player);
			board.reset();
			discController.reset();
			
			getContentPane().repaint();
			makeArtificiallyIntelligentMove();		// AI will make a move only when needed...
		}
	}
	
	private void makeVisualMove(byte rowIndex, byte columnIndex) {
		discController.fall(player, rowIndex, columnIndex, board, panelBoard.getBounds());
		
		player = Board.PLAYERS[player];
		
		pointer.changeColor(player);
		
		if (board.isGameOver()) {		// match drawn is not being informed...
			board.gameOver = true;
			
			if (board.winner == Main.DEFAULT) {
				JOptionPane.showMessageDialog(this, "Match is drawn.", getTitle() + " - Game Over", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				discController.markWinner(board);
				
				// this portion needs to be greatly modified... (GARBAGE CODE) :(
				if (newGameDialog.gameSettings.mode == 0) {
					int player = this.player;
					
					if (player == Board.PLAYERS[0]) {
						player = 2;
					}
					else {
						player = 1;
					}
					
					JOptionPane.showMessageDialog(this, "Player " + player + " is the winner.", getTitle() + " - Winner", JOptionPane.INFORMATION_MESSAGE);
				}
				else if (newGameDialog.gameSettings.mode == 1) {
					String winningMessage;
					
					if (player == newGameDialog.gameSettings.player) {
						winningMessage = "Artificial Intelligence is";
					}
					else {
						winningMessage = "You are";
					}
					
					JOptionPane.showMessageDialog(this, winningMessage + " the winner.", getTitle() + " - Winner", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					int player = this.player;
					
					if (player == Board.PLAYERS[0]) {
						player = 2;
					}
					else {
						player = 1;
					}
					
					JOptionPane.showMessageDialog(this, "Artificial Intelligence " + player + " is the winner.", getTitle() + " - Winner", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		if (newGameDialog != null) {
			startNewGame();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String text = ((JMenuItem)event.getSource()).getText();
		
		if (text.equals("New Game")) {
			startNewGame();
		}
		else if (text.equals("Exit")) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		if (board.gameOver || (newGameDialog.gameSettings.mode == 1 && newGameDialog.gameSettings.player != player) ||
				newGameDialog.gameSettings.mode == 2) {
			return;
		}
		
		byte row;
		
		Disc disc = (Disc)event.getSource();
		
		if ((row = board.makeMove(disc.columnIndex, player)) != Main.DEFAULT) {
			makeVisualMove(row, disc.columnIndex);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent event) {
		pointer.setLocation(event.getLocationOnScreen().x - (pointer.getLocationOnScreen().x - pointer.getX()) - pointer.getWidth() / 2, pointer.getY());
	}
	
	@Override
	public void componentResized(ComponentEvent event) {
		panelBoard.setLocation((getWidth() / 2) - (panelBoard.getWidth() / 2), (getHeight() / 2) - (panelBoard.getHeight() / 2) - 30);
		pointer.setLocation(pointer.getX(), panelBoard.getY() - pointer.getHeight());		// might be optional...
		discController.updateDiscLocation(panelBoard.getBounds());
	}
	
	@Override
	public void windowClosing(WindowEvent event) {
		Main.running = false;
	}
	
	@Override
	public void discHasFallen(Disc disc) {
		makeArtificiallyIntelligentMove();
	}
	
	@Override
	public void decisionTaken(byte rowIndex, byte columnIndex, ArtificialIntelligence artificialIntelligence) {
		board.makeMove(rowIndex, columnIndex, player);
		makeVisualMove(rowIndex, columnIndex);
	}
	
	@Override
	public void mousePressed(MouseEvent event) { }

	@Override
	public void mouseReleased(MouseEvent event) { }

	@Override
	public void mouseEntered(MouseEvent event) { }

	@Override
	public void mouseExited(MouseEvent event) { }
	
	@Override
	public void componentMoved(ComponentEvent event) { }

	@Override
	public void componentShown(ComponentEvent event) { }

	@Override
	public void componentHidden(ComponentEvent event) { }

	@Override
	public void mouseDragged(MouseEvent event) { }
	
	@Override
	public void windowOpened(WindowEvent event) { }

	@Override
	public void windowClosed(WindowEvent event) { }

	@Override
	public void windowIconified(WindowEvent event) { }

	@Override
	public void windowDeiconified(WindowEvent event) { }

	@Override
	public void windowActivated(WindowEvent event) { }

	@Override
	public void windowDeactivated(WindowEvent event) { }
	
}