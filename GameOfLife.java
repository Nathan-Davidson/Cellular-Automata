import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class GameOfLife extends JFrame {
	private static final int B_SIZE = 10;
	private static final int HEIGHT = 480 / B_SIZE;
	private static final int WIDTH = 720 / B_SIZE;

	private int[][] currLive;
	private int[][] nextLive;
	private JButton[][] board;

	public GameOfLife() {
		super("Game of Life");
		this.setLayout(null);
		this.setSize(WIDTH * B_SIZE, HEIGHT * B_SIZE);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.currLive = new int[HEIGHT][WIDTH];
		this.nextLive = new int[HEIGHT][WIDTH];
		this.board = new JButton[HEIGHT][WIDTH];
		for(int h = 0; h < HEIGHT; h++)
			for(int w = 0; w < WIDTH; w++) {
				this.board[h][w] = new JButton();
				this.board[h][w].addActionListener(new GameOfLifeListener(h, w));
				this.board[h][w].setBounds(w * B_SIZE, h * B_SIZE, B_SIZE, B_SIZE);
				this.board[h][w].setBackground(Color.WHITE);
				this.add(this.board[h][w]);
			}
		this.setVisible(true);
	}

	public void updateState() {
		for(int h = 0; h < HEIGHT; h++) {
			for(int w = 0; w < WIDTH; w++) {
				int neighbors = getNeighbors(h, w);
				if(currLive[h][w] == 1 && (neighbors < 2 || neighbors > 3)) {
						nextLive[h][w] = 0;
				} else if(currLive[h][w] == 0 && neighbors == 3) {
						nextLive[h][w] = 1;
				} else {
					nextLive[h][w] = currLive[h][w];
				}
			}
		}

		int[][] temp = currLive;
		currLive = nextLive;
		nextLive = temp;
	}

	public void displayState() {
		for(int h = 0; h < HEIGHT; h++) {
			for(int w = 0; w < WIDTH; w++) {
				if(currLive[h][w] == 1) {
					board[h][w].setBackground(Color.BLACK);
				} else {
					board[h][w].setBackground(Color.WHITE);
				}
			}
		}
	}

	public int getNeighbors(int height, int width) {
		int num = 0;
		num += isLive(height + 1, width + 1);
		num += isLive(height + 1, width);
		num += isLive(height + 1, width - 1);

		num += isLive(height, width + 1);
		num += isLive(height, width - 1);

		num += isLive(height - 1, width + 1);
		num += isLive(height - 1, width);
		num += isLive(height - 1, width - 1);
		return num;
	}

	public int isLive(int height, int width) {
		if(height < 0)
			height = HEIGHT - 1;
		if(width < 0)
			width = WIDTH - 1;
		if(height == HEIGHT)
			height = 0;
		if(width == WIDTH)
			width = 0;
		return currLive[height][width];
	}

	public static void main(String[] args) throws InterruptedException {
		GameOfLife theGame = new GameOfLife();
		Thread.sleep(20000);
		while(true) {
			theGame.updateState();
			theGame.displayState();
			Thread.sleep(100);
		}
	}

	public class GameOfLifeListener implements ActionListener {
		int height;
		int width;

		public GameOfLifeListener(int height, int width) {
			this.height = height;
			this.width = width;
		}

		public void actionPerformed(ActionEvent e) {
			if(currLive[height][width] == 0) {
				currLive[height][width] = 1;
				board[height][width].setBackground(Color.BLACK);
			} else {
				currLive[height][width] = 0;
				board[height][width].setBackground(Color.WHITE);
			}
		}
	}
}
