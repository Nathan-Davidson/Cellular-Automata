import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GameOfLife extends JPanel {
  private static int DEFAULT_BUTTON_SIZE = 20;

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
    this.setPreferredSize(new Dimension(width * DEFAULT_BUTTON_SIZE,
                                        height * DEFAULT_BUTTON_SIZE));
    this.theBoard = new GameOfLifeBoard(height, width);
    this.theCells = initCells(height, width, this.theBoard);
    this.sleepTime = 0;
    this.running = true;
    this.paused = true;
  }

  public JButton[][] initCells(int height,
                               int width,
                               GameOfLifeBoard theBoard) {
    JButton[][] theCells = new JButton[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        theCells[row][col] = new JButton();
        GameOfLifeListener listener = new GameOfLifeListener(theBoard,
                                                             row,
                                                             col);
        theCells[row][col].addActionListener(listener);
        theCells[row][col].setBounds(col * DEFAULT_BUTTON_SIZE,
                                     row * DEFAULT_BUTTON_SIZE,
                                     DEFAULT_BUTTON_SIZE,
                                     DEFAULT_BUTTON_SIZE);
        theCells[row][col].setBackground(Color.WHITE);
        this.add(theCells[row][col]);
      }
    }

    return theCells;
  }

  public void run() {
    while (running) {
      while(paused); //Busywaiting is fun
      tick();
      wait(sleepTime);
    }
  }

  public void tick() {
    theBoard.tick();
    updateCells();
  }

  public void updateCells() {
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        updateCell(row, col);
      }
    }
  }

  public void wait(int sleepTime) {
    try {
      Thread.sleep(sleepTime);
    } catch (InterruptedException ie) {
      ie.printStackTrace();
    }
  }

  public void updateCell(int row, int col) {
    if (theBoard.getCell(row, col)) {
      theCells[row][col].setBackground(Color.BLACK);
    } else {
      theCells[row][col].setBackground(Color.WHITE);
    }
  }

  public int getWidthPixels() {
    return width * DEFAULT_BUTTON_SIZE;
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

  public class GameOfLifeListener implements ActionListener {
    private GameOfLifeBoard theBoard;
    private int cellRow;
    private int cellCol;

    public GameOfLifeListener(GameOfLifeBoard theBoard, int row, int col) {
      this.theBoard = theBoard;
      this.cellRow = row;
      this.cellCol = col;
    }

    public void actionPerformed(ActionEvent e) {
			theBoard.flipCell(cellRow, cellCol);
      updateCell(cellRow, cellCol);
		}
  }
}
