import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public class WolframAutomaton extends Canvas {
  private static final int SQ_SIZE = 1;
	private static final int HEIGHT = 800 / SQ_SIZE;
	private static final int WIDTH = HEIGHT * 2 + 1;
	private static final boolean START = true;

	private boolean[][] board;
	private int rule;

	public WolframAutomaton(int rule) {
		super();
		if(rule < 0 || rule > 255)
			throw new IllegalArgumentException();
		this.board = new boolean[HEIGHT][WIDTH];
		this.board[0][(WIDTH / 2) + 1] = START;
		this.rule = rule;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for(int row = 0; row < HEIGHT; row++) {
			updateRow(row);
			for(int col = 0; col < WIDTH; col++) {
				if(board[row][col]) {
					g.setColor(Color.BLACK);
				} else {
					g.setColor(this.getBackground());
				}
				g.fillRect(col * SQ_SIZE, row * SQ_SIZE, SQ_SIZE, SQ_SIZE);
			}
		}
	}

	private void updateRow(int row) {
		for(int i = 0; i <= row; i++) {
			updateSquare(row, (WIDTH / 2) - i);
			updateSquare(row, (WIDTH / 2) + i);
		}
	}

	private void updateSquare(int row, int col) {
		if(row == 0) return;
		int value = getValue(row, col);
		board[row][col] = ((value & rule) > 0);
	}

	private int getValue(int row, int col) {
		int val = 1;
		if(board[row - 1][col - 1]) val *= 16;
		if(board[row - 1][col]) val *= 4;
		if(board[row - 1][col + 1]) val *= 2;
		return val;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Wolfram Automaton for Rule " + args[0]);
		frame.setSize(WIDTH * SQ_SIZE, HEIGHT * SQ_SIZE);
		frame.setResizable(false);
		frame.add(new WolframAutomaton(Integer.parseInt(args[0])));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
