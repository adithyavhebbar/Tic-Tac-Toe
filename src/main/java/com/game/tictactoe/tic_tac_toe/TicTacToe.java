package com.game.tictactoe.tic_tac_toe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TicTacToe implements ActionListener {
	JFrame frame = new JFrame();
	JPanel textPanel = new JPanel();
	JLabel textField = new JLabel();
	JPanel buttonPanel = new JPanel();
	JButton[][] buttons = new JButton[3][3];
	private JButton resetButton = new JButton();

	public boolean playerXturn = true;

	private String playerX = "X", playerO = "O";

	public String currentPlayer = playerX;

	private String[][] btnValues = new String[3][3];

	public TicTacToe() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(600, 600);
		frame.getContentPane().setBackground(Color.BLACK);

		textField.setBackground(Color.BLACK);
		textField.setForeground(new Color(25, 255, 0));
		textField.setFont(new Font("Ink Free", Font.BOLD, 40));
		textField.setHorizontalAlignment(JLabel.CENTER);
		textField.setText("TIC TAC TOE");

		resetButton.setSize(30, 30);
		resetButton.setFocusable(false);
		resetButton.setFont(new Font("MV Boli", Font.BOLD, 15));
		resetButton.setText("RESET");
		resetButton.setName("reset");
		resetButton.addActionListener(this);

		textPanel.setLayout(new BorderLayout());
		textPanel.setBounds(0, 0, 800, 100);
		textPanel.setBackground(Color.BLACK);
		textPanel.add(textField, BorderLayout.CENTER);
		textPanel.add(resetButton, BorderLayout.EAST);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setFocusable(false);
				buttons[i][j].addActionListener(this);
				buttons[i][j].setName(i + "," + j);
				buttons[i][j].setFont(new Font("MV Boli", Font.BOLD, 25));
				buttonPanel.add(buttons[i][j]);
			}
		}

		frame.add(textPanel, BorderLayout.NORTH);

		buttonPanel.setLayout(new GridLayout(3, 3));
		buttonPanel.setBackground(new Color(0, 0, 50));

		frame.add(buttonPanel);
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);

		textField.setText("AI's" + " Turn");
		initbtnValues();

		callAIPlayer();

	}

	public void callAIPlayer() {
		Map<String, Integer> bestMov = bestMove(btnValues);
		int row = bestMov.get("row");
		int col = bestMov.get("col");
		buttons[row][col].setText(getCurrentPlayer());
		setStringValues(row, col);
		WinStates state = checkWinner(btnValues);
		buttons[row][col].setText(getCurrentPlayer());
		System.out.println("AI Player result:" + state);
		if (state.compareTo(WinStates.WIN) == 0) {
			textField.setText("AI" + " WON");
			disableButtons();
		} else if (state.compareTo(WinStates.TIE) == 0) {
			textField.setText("MATCH TIED");
			disableButtons();
		} else {
			setNextPlayer();
		}
	}

	private void initbtnValues() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				btnValues[i][j] = "";
			}
		}
	}

	private void setStringValues(int row, int col) {
		btnValues[row][col] = getCurrentPlayer();
	}

	public String getCurrentPlayer() {
		return currentPlayer.equalsIgnoreCase(playerX) ? playerX : playerO;
	}

	private String getNextPlayer() {
		return currentPlayer.equalsIgnoreCase(playerX) ? playerO : playerX;
	}

	public void setNextPlayer() {
		this.currentPlayer = getNextPlayer();
		if (getCurrentPlayer().equalsIgnoreCase(playerO))
			textField.setText("Your" + " Turn");
		else
			textField.setText("AI's" + " Turn");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clickedButton = (JButton) e.getSource();
		if (!clickedButton.getName().equalsIgnoreCase("reset")) {
			String[] rowcol = clickedButton.getName().split(",");
			int row = Integer.parseInt(rowcol[0]);
			int col = Integer.parseInt(rowcol[1]);
			if (buttons[row][col].getText().equalsIgnoreCase("")) {
				buttons[row][col].setText(getCurrentPlayer());
				setStringValues(row, col);
				WinStates state = checkWinner(btnValues);
				if (state.compareTo(WinStates.WIN) == 0) {
					textField.setText("YOU" + " WON");
					disableButtons();
				} else if (state.compareTo(WinStates.TIE) == 0) {
					textField.setText("MATCH TIED");
					disableButtons();
				}
				setNextPlayer();
				callAIPlayer();
			}
		} else if (clickedButton.getName().equalsIgnoreCase("reset")) {
			resetBoard();
		}
	}

	public WinStates checkWinner(String[][] buttons) {

		for (int i = 0; i < 3; i++) {
			String r1 = buttons[i][0];
			String r2 = buttons[i][1];
			String r3 = buttons[i][2];

			String c1 = buttons[0][i];
			String c2 = buttons[1][i];
			String c3 = buttons[2][i];

			boolean rowCheck = false;
			if (!r1.equalsIgnoreCase("") && !r2.equalsIgnoreCase("") && !r3.equalsIgnoreCase("")) {
				rowCheck = r1.equalsIgnoreCase(r2) && r1.equalsIgnoreCase(r3);
				if (rowCheck) {
					return WinStates.WIN;
				}
			}
			boolean colCheck = false;
			if (!c1.equalsIgnoreCase("") && !c2.equalsIgnoreCase("") && !c3.equalsIgnoreCase("")) {
				colCheck = c1.equalsIgnoreCase(c2) && c1.equalsIgnoreCase(c3);
//				System.out.println("COl Check:"+colCheck);
				if (colCheck) {
					return WinStates.WIN;
				}
			}
		}

		boolean d1Check = false;
		String d1 = buttons[1][1];
		String d2 = buttons[1][1];
		String d3 = buttons[2][2];

		if (!d1.equalsIgnoreCase("") && !d2.equalsIgnoreCase("") && !d3.equalsIgnoreCase("")) {
			d1Check = d1.equalsIgnoreCase(d2) && d2.equalsIgnoreCase(d3);
		}

		if (d1Check) {
			return WinStates.WIN;
		}

		d1 = buttons[0][2];
		d2 = buttons[1][1];
		d3 = buttons[2][0];

		boolean antiDCheck = false;

		if (!d1.equalsIgnoreCase("") && !d2.equalsIgnoreCase("") && !d3.equalsIgnoreCase("")) {
			antiDCheck = d1.equalsIgnoreCase(d2) && d2.equalsIgnoreCase(d3);
		}
		if (antiDCheck) {
			return WinStates.WIN;
		}

		boolean isTie = true;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (buttons[i][j].equalsIgnoreCase("")) {
					isTie = false;
					return WinStates.CONT;
				}
			}
		}

		if (isTie) {
			return WinStates.TIE;
		}

		return WinStates.CONT;
	}

	public Map<String, Integer> bestMove(String[][] buttons) {

		int bestScore = Integer.MIN_VALUE;
		Map<String, Integer> bestPos = new HashMap<>(2);
		bestPos.put("row", Integer.MAX_VALUE);
		bestPos.put("col", Integer.MAX_VALUE);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (buttons[i][j].equalsIgnoreCase("")) {
					buttons[i][j] = playerX;
					int score = minimax(buttons, false, playerX);
					if (score > bestScore) {
						bestScore = score;
						bestPos.put("row", i);
						bestPos.put("col", j);
					}
					buttons[i][j] = "";
				}
			}
		}
		return bestPos;
	}

	private int minimax(String[][] btns, boolean isMaximizer, String lastPlayer) {
		WinStates state = checkWinner(btns);
//		System.out.println("State in minimax:" + state + " for player:" + lastPlayer);
		if (state.compareTo(WinStates.WIN) == 0 && lastPlayer.equalsIgnoreCase(playerX)) {
			return 1;
		} else if (state.compareTo(WinStates.WIN) == 0 && lastPlayer.equalsIgnoreCase(playerO)) {
			return -1;
		} else if (state.compareTo(WinStates.TIE) == 0) {
			return 0;
		}

		int bestScore;
		if (isMaximizer) {
			bestScore = Integer.MIN_VALUE;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (btns[i][j].compareTo("") == 0) {
						btns[i][j] = (playerX);
						int score = minimax(btns, false, playerX);
//						System.out.println("[" + i + "," + j + "] = " + score);
						if (score > bestScore) {
							bestScore = score;
						}
						btns[i][j] = "";
					}
				}
			}
		} else {
			bestScore = Integer.MAX_VALUE;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (btns[i][j].compareTo("") == 0) {
						btns[i][j] = playerO;
						int score = minimax(btns, true, playerO);
						if (score < bestScore) {
							bestScore = score;
						}
						btns[i][j] = "";
					}
				}
			}
		}
		return bestScore;
	}

	private void disableButtons() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.buttons[i][j].setEnabled(false);
			}
		}
	}

	private void resetBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.buttons[i][j].setEnabled(true);
				this.buttons[i][j].setText("");
				this.btnValues[i][j] = "";
			}
		}

		currentPlayer = playerX;
		callAIPlayer();
	}

}
