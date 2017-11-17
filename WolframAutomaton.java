import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

public class WolframAutomaton extends Canvas {
	private static final int HEIGHT = 800;
	private static final int WIDTH = HEIGHT * 2 + 1;
	private static final int SQ_SIZE = 1;
	private static final int START = 1;

	private int[][] board;
	private int rule;

	public WolframAutomaton(int rule) {
		super();
		if(rule < 0 || rule > 255)
			throw new IllegalArgumentException();
		this.board = new int[HEIGHT][WIDTH];
		this.board[0][(WIDTH / 2) + 1] = START;
		this.rule = rule;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for(int row = 0; row < HEIGHT; row++) {
			updateBoard(row);
			for(int col = 0; col < WIDTH; col++) {
				if(board[row][col] == 1) {
					g.setColor(Color.BLACK);
				} else {
					g.setColor(this.getBackground());
				}
				g.fillRect(col * SQ_SIZE, row * SQ_SIZE, SQ_SIZE, SQ_SIZE);
			}
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Wolfram Automaton for Rule " + args[0]);
		frame.setSize(WIDTH * SQ_SIZE, HEIGHT * SQ_SIZE);
		frame.setResizable(false);
		frame.add(new WolframAutomaton(Integer.parseInt(args[0])));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void updateBoard(int row) {
		for(int i = 0; i <= row; i++) {
			int value = getValue(row, (WIDTH / 2) - i);
			if((value & rule) > 0)
				board[row][((WIDTH / 2) - i)] = 1;
			value = getValue(row, (WIDTH / 2) + i);
			if((value & rule) > 0)
				board[row][((WIDTH / 2) + i)] = 1;
		}
	}

	private int getValue(int row, int col) {
		if(row == 0) return board[row][col];
		int val = 1;
		if(board[row - 1][col - 1] == 1) val *= 16;
		if(board[row - 1][col] == 1) val *= 4;
		if(board[row - 1][col + 1] == 1) val *= 2;
		return val;
	}
}
