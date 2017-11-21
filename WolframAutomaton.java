import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Random;

public class WolframAutomaton extends JPanel {
	//Allows for a fairly large number of iterations, but fits comfortably
	//on most displays. Could use something like
	//Toolkit.getDefaultToolkit().getScreenSize() to be more exact. but that
	//seems like more effor than this warrants.
	private static final int TARGET_WINDOW_HEIGHT = 720;
	//Sixty is the smallest multiple of 10 such that the full title displays
	//for 1-iteration automaton
	private static final int MAX_CELL_SIZE = 60;

	private int height;
	private int width;
	private int cellSize;
	private boolean[][] board;
	private int rule;
	private int waitSecs;

	public WolframAutomaton(boolean randomStart) {
		//The rules governing a Wolfram Cellular Automaton are given my an
		//8-bit value, corresponding to the eight possible values of the three
		//"parent" cells in a cell's neighborhood
		//30 is the default because I think it looks kinda neat
		this.rule = getBoundedInput("Rule", 0, 255, 30);

		//Running for zero iterations is boring. Running for more iterations
		//than the window height is possible, but involves looping back around
		//when the bottom of the window is reached, which is not yet supported
		int iterations = getBoundedInput("Iterations",
																		 1,
																		 TARGET_WINDOW_HEIGHT,
																		 300);
		//Leave room for the "base/start" state
		this.height = iterations + 1;
		//Many automata "fan out" one square in each direction on each iteration
		//A width of twice height facilitates this
		this.width = height * 2;

		//This may look somewhat strange
		//Why not just set dimensions directly to the target height?
		//Because the number of iterations may not evenly divide the target window
		//height, which would result in borders that are (in my opinion) unsightly
		//Therefore, we compute a cell size, then set the window to fit the
		//appropriate number of cells of that size.
		this.cellSize = TARGET_WINDOW_HEIGHT / iterations;
		if (this.cellSize > MAX_CELL_SIZE) {
			this.cellSize = MAX_CELL_SIZE;
		}
		this.setPreferredSize(new Dimension(width * cellSize, height * cellSize));
		this.setBackground(Color.WHITE);

		this.board = new boolean[height][width];

		//Wolfram used two start states for his automata
		//One seeds with a single "on" cell in the center
		//The other uses a random set of seed cells
		if (!randomStart) {
			this.board[0][(width / 2)] = true;
		} else {
			Random RNG = new Random();
			for (int col = 0; col < width; col++) {
				this.board[0][col] = RNG.nextBoolean();
			}
		}

		//Honestly, the (upper) bounds on this are kind of arbitrary
		//So is the default. The default is 0 mostly because that makes
		//behavior checking easier because you don't have to wait for the display
		//to update.
		this.waitSecs = getBoundedInput("Wait Time (ms)", 0, 10000, 0);
	}

	/**
	 * Retrieves integer input via a graphical prompt.
	 * Ensures that the return value falls in a specified range.
	 * If the value the user provides does not, a default is returned instead.
	 * The default is also returned in the user enters a non-integer value.
	 *
	 * @param msg a message to be displayed on the input prompt
	 * @param min the lower bound of the input range
	 * @param max the upper bound of the input range
	 * @param def the default value
	 * @return the input value
	 */
  public static int getBoundedInput(String msg,
																		int min,
																		int max,
																		int def) {
		String inputString =
			JOptionPane.showInputDialog(null, "Enter " + msg);
		try {
			int input = Integer.parseInt(inputString);
			if (input >= min && input <= max) {
				return input;
			}
		} catch(NumberFormatException nfe) {}
		JOptionPane.showMessageDialog(null, "Using Default (" + def + ").");
		return def;
	}

	/**
	 * Runs the automaton, calculating the contents of each row, except the
	 * top/seed row.
   */
	public void run() {
		for (int i = 1; i < height; i++) {
			updateRow(i);

			repaint();

			try {
				Thread.sleep(waitSecs);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}

	/**
	 * Iterate over the backing array, color the areas corresponding to on/true
	 * cells black.
	 * Ideally, this should only update the new row, but that requires global
	 * state to work properly, which is not a great design.
	 */
	@Override
	public void paintComponent(Graphics g) {
		for (int row = 0; row < height; row++)
			for (int col = 0; col < width; col++) {
				if (board[row][col]) {
					g.setColor(Color.BLACK);
				} else {
					g.setColor(Color.WHITE);
				}
				g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
			}
	}

	/**
	 * Compute the new values for each cell in a row
	 *
	 * @param row the row to compute new values for
	 */
	private void updateRow(int row) {
		for (int col = 0; col < width; col++) {
			updateCell(row, col);
		}
	}

	/**
	 * Computes the new value of a particular cell
	 * A Wolfram Automaton's transition functions are specified by a 8-bit bit
	 * string. The bits of this bit string correspond to whether a given set of
	 * parent cells yield an on or off state.
	 * getValue computes the value (a power of 2 on the range 1 - 128)
	 * this value is based on the state of parent nodes
	 * Therefore, by taking the bitwise and of these two values, we can
	 * determine whether a cell should be live or dead in the next iteration.
	 * If the value correspond to the cell's parent cells is in the rule
	 * the and yields a non-zero value, and the cell should be alive in the
	 * next iteration.
	 *
	 * @param row the row of the cell to update
	 * @param col the column of the cell to update
	 */
	private void updateCell(int row, int col) {
		int value = getParentCellsValue(row, col);
		board[row][col] = ((value & rule) > 0);
	}

	/**
	 * Computes the value of the parents of a given cell.
	 * Wraps the edges of the display
	 * (note that, as of now, bottom-to-top wrapping is not used)
	 *
	 * @param row the row of the cell to update
	 * @param col the column of the cell to update
	 * @return the value of the cell's parents
	 */
	private int getParentCellsValue(int row, int col) {
		if(row == 0) {
			row = height;
		}
		int val = 1;
		//Wrap edges back around
		if ((col == (width - 1) && board[row - 1][0])
			  || (col != (width - 1) && board[row - 1][col + 1])) {
			val *= 2;
		}
		if (board[row - 1][col]) {
			val *= 4;
		} if ((col == 0 && board[row - 1][width - 1])
			  ||(col != 0 && board[row - 1][col - 1])) {
			val *= 16;
		}
		return val;
	}

	public static void main(String[] args) {
		WolframAutomaton theAutomaton;
		//There are two modifications I think are potentially interesting
		//1. Start with a random set of seeds.
		//2. Loop endlessly through the board, restarting at the top.
		//Currently, only check for the first.
 		if (args.length == 1 && args[0].equals("--random-start")) {
			theAutomaton = new WolframAutomaton(true);
		} else {
			theAutomaton = new WolframAutomaton(false);
		}

		JFrame theFrame =
			new JFrame("Wolfram Celluar Automaton");

		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.add(theAutomaton);
		theFrame.pack();
		theFrame.setVisible(true);
		theFrame.setLocationRelativeTo(null);
		theFrame.setResizable(false);
		theAutomaton.run();
	}
}
