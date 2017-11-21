import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GameOfLife extends JPanel {
  private static int CELL_SIZE = 15;

  private int height;
  private int width;
  private GameOfLifeBoard theBoard;
  private JButton[][] theCells;
  private int sleepTime;
  private boolean running;
  private boolean paused;

  public GameOfLife(int height, int width) {
    this.height = height;
    this.width = width;
    this.setLayout(null);
    this.setPreferredSize(new Dimension(width * CELL_SIZE,
                                        height * CELL_SIZE));
    this.theBoard = new GameOfLifeBoard(height, width);
    this.addMouseListener(new GameOfLifeListener(theBoard));
    this.sleepTime = 0;
    this.running = true;
    this.paused = true;
  }

  /**
   * Runs the simulation. Sleeps while paused. While not paused, alternates
   * between sleeping and advancing the board state.
   */
  public void run() {
    while (running) {
      while (paused) {
        wait(100);
      }
      tick();
      wait(sleepTime);
    }
  }

  /**
   * Update the backing array, then redraw the cells to reflect births
   * and deaths.
   */
  public void tick() {
    theBoard.tick();
    repaint();
  }

  /**
   * Sleeps the program for the provided perior (in ms).
   *
   * @param sleepTime how long to sleep
   */
  public void wait(int sleepTime) {
    try {
      Thread.sleep(sleepTime);
    } catch (InterruptedException ie) {
      ie.printStackTrace();
    }
  }

  /**
   * Iterates over the board, drawing black squares for live cells and white
   * squares for dead ones.
   *
   * @param g required by Swing
   */
  @Override
  public void paintComponent(Graphics g) {
		for (int row = 0; row < height; row++)
			for (int col = 0; col < width; col++) {
				if (theBoard.isLive(row, col)) {
					g.setColor(Color.BLACK);
				} else {
					g.setColor(this.getBackground());
				}
				g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
			}
	}

  /**
   * Returns the width of the array. Used to make sure the menu aligns with
   * the board.
   *
   * @return the width of the board
   */
  public int getWidthPixels() {
    return width * CELL_SIZE;
  }

  /**
   * Tells you if the game is paused or not.
   *
   * @return whether the array is paused
   */
  public boolean paused() {
    return paused;
  }

  /**
   * Start the game.
   */
  public void start() {
    paused = false;
  }

  /**
   * Stop the game.
   */
  public void stop() {
    paused = true;
  }

  /**
   * Returns the sleep time.
   * Yes, I acknowledge that these comments are kind of obvious, but the linter
   * wants javadoc comments everywhere, and I think just making the members
   * public is a bad design pattern (though maybe not in this case).
   *
   * @return the current sleep time
   */
  public int getSleepTime() {
    return sleepTime;
  }

  /**
   * Sets the sleep time.
   *
   * @param sleepTime the new sleep time.
   */
  public void setSleepTime(int sleepTime) {
    this.sleepTime = sleepTime;
  }

  /**
   * Resets the board.
   * First, clear the backing array.
   * Second, repaint the screen to reflect the new backing array.
   * Third, pause the game.
   */
  public void reset() {
    theBoard.reset();
    repaint();
    paused = true;
  }

  public class GameOfLifeListener implements MouseListener {
    private GameOfLifeBoard theBoard;

    public GameOfLifeListener(GameOfLifeBoard theBoard) {
      this.theBoard = theBoard;
    }

    /**
     * Flip the block where a click was made, then update the display.
     */
    public void mouseClicked(MouseEvent e) {
      int row = e.getY() / CELL_SIZE;
      int col = e.getX() / CELL_SIZE;
      theBoard.flipCell(row, col);
      repaint();
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
  }
}
