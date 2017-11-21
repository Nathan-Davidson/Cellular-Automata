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

  public void run() {
    while (running) {
      while(paused) System.out.println("PAUSED"); //Busywaiting is fun!
      System.out.println("CONWAY");
      tick();
      wait(sleepTime);
    }
  }

  public void tick() {
    theBoard.tick();
    repaint();
  }

  public void wait(int sleepTime) {
    try {
      Thread.sleep(sleepTime);
    } catch (InterruptedException ie) {
      ie.printStackTrace();
    }
  }

  @Override
  public void paintComponent(Graphics g) {
		for (int row = 0; row < height; row++)
			for (int col = 0; col < width; col++) {
				if (theBoard.isLive(row, col)) {
					g.setColor(Color.BLACK);
				} else {
					g.setColor(this.getBackground());
				}
				g.fillRect(col * CELL_SIZE,
                   row * CELL_SIZE,
                   CELL_SIZE,
                   CELL_SIZE);
			}
	}

  public int getWidthPixels() {
    return width * CELL_SIZE;
  }

  public boolean paused() {
    return paused;
  }

  public void start() {
    paused = false;
  }

  public void stop() {
    paused = true;
  }

  public int getSleepTime() {
    return sleepTime;
  }

  public void setSleepTime(int sleepTime) {
    this.sleepTime = sleepTime;
  }

  public void reset() {
    theBoard.reset();
    repaint();
  }

  public class GameOfLifeListener implements MouseListener {
    private GameOfLifeBoard theBoard;

    public GameOfLifeListener(GameOfLifeBoard theBoard) {
      this.theBoard = theBoard;
    }

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
