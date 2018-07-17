package connect.four.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import connect.four.engine.Board;
import connect.four.engine.GameSettings;

public class NewGameDialog extends JDialog implements ActionListener, WindowListener {
	
	private boolean flag;
	private static final long serialVersionUID = 4892735653878993210L;
	
	private static final String[] GAME_MODES = {
		"Player vs. Player",
		"Player vs. AI",
		"AI vs. AI"
	},
	DIFFICULTY_LEVELS = {
		"Easy", "Normal", "Hard", "Very Hard"
	},
	PLAYERS = {
		"Player 1", "Player 2"
	};
	
	public GameSettings gameSettings;
	
	private JComboBox<String> comboBoxGameModes, comboBoxDifficultyLevels, comboBoxPlayAs;
	private JButton buttonCancel, buttonStart;
	
	public NewGameDialog(int width, int height, String title, Component component) {
		gameSettings = new GameSettings();
		
		initialize(width, height, title, component);
	}
	
	private void initialize(int width, int height, String title, Component component) {
		setIconImage(((JFrame)component).getIconImage());
		setTitle(title);
		setSize(width, height);
		setResizable(false);
		setLocationRelativeTo(component);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setModal(true);
		addWindowListener(this);
		
		JPanel contentPane = new JPanel(null);
		setContentPane(contentPane);
		
		JLabel labelGameMode = new JLabel("Game Mode");
		labelGameMode.setBounds(10, 10, 100, 25);
		contentPane.add(labelGameMode);
		
		comboBoxGameModes = new JComboBox<String>(GAME_MODES);
		comboBoxGameModes.setBounds(130, 10, 230, 25);
		comboBoxGameModes.setSelectedIndex(1);
		comboBoxGameModes.addActionListener(this);
		contentPane.add(comboBoxGameModes);
		
		JLabel labelDifficultyLevel = new JLabel("Difficulty Level");
		labelDifficultyLevel.setBounds(10, 45, 120, 25);
		contentPane.add(labelDifficultyLevel);
		
		comboBoxDifficultyLevels = new JComboBox<String>(DIFFICULTY_LEVELS);
		comboBoxDifficultyLevels.setBounds(130, 45, 230, 25);
		comboBoxDifficultyLevels.setSelectedIndex(1);
		contentPane.add(comboBoxDifficultyLevels);
		
		JLabel labelPlayAs = new JLabel("Play As");
		labelPlayAs.setBounds(10, 80, 120, 25);
		contentPane.add(labelPlayAs);
		
		comboBoxPlayAs = new JComboBox<String>(PLAYERS);
		comboBoxPlayAs.setBounds(130, 80, 230, 25);
		contentPane.add(comboBoxPlayAs);
		
		buttonCancel = new JButton("Cancel");
		buttonCancel.setBounds(180, 115, 85, 25);
		buttonCancel.addActionListener(this);
		contentPane.add(buttonCancel);
		
		buttonStart = new JButton("Start");
		buttonStart.setBounds(275, 115, 85, 25);
		buttonStart.addActionListener(this);
		contentPane.add(buttonStart);
	}
	
	public boolean makeVisible() {
		setVisible(true);
		
		return flag;
	}
	
	public String getGameMode() {
		return GAME_MODES[gameSettings.mode];
	}
	
	public GameSettings getGameSettings() {
		return gameSettings;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		
		if (source.equals(comboBoxGameModes)) {
			switch (comboBoxGameModes.getSelectedIndex()) {
			case 0:
				comboBoxDifficultyLevels.setEnabled(false);
				comboBoxPlayAs.setEnabled(false);
				
				break;
			case 1:
				comboBoxDifficultyLevels.setEnabled(true);
				comboBoxPlayAs.setEnabled(true);
				
				break;
			case 2:
				comboBoxDifficultyLevels.setEnabled(true);
				comboBoxPlayAs.setEnabled(false);
				
				break;
			default:
				break;
			}
		}
		else if (source.equals(buttonCancel)) {
			flag = false;
			
			setVisible(false);
		}
		else if (source.equals(buttonStart)) {
			flag = true;
			
			gameSettings.mode = (byte)comboBoxGameModes.getSelectedIndex();
			
			if (gameSettings.mode == 1) {
				gameSettings.player = Board.PLAYERS[comboBoxPlayAs.getSelectedIndex()];
			}
			else {
				gameSettings.player = Board.PLAYERS[0];		// set as player 1...
			}
			
			gameSettings.setDepth((byte)comboBoxDifficultyLevels.getSelectedIndex());
			
			setVisible(false);
		}
	}
	
	@Override
	public void windowClosing(WindowEvent event) {
		flag = false;
	}
	
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