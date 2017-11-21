import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

public class GameOfLifeMenu extends JMenuBar {
  GameOfLife theGame;

  /**
   * Initializes a menu bar with buttons that:
   * 1. Allow the user to set the delay between ticks.
   * 2. Allow the user to start or stop the simulation.
   * 3. Allow the user to reset the simulation.
   * 4. Allow the user to advance the simulation one tick.
   *
   * @param theGame the instance of the Game of Life for which the menu is
   * being created.
   */
  public GameOfLifeMenu(GameOfLife theGame) {
    this.theGame = theGame;
    this.setPreferredSize(new Dimension(theGame.getWidthPixels(), 30));
    JButton speedButton = new JButton("0 ms delay"); //Default delay
    speedButton.addActionListener(new SpeedListener(theGame));
    this.add(speedButton);
    JButton startButton = new JButton("Start");
    startButton.addActionListener(new PlayingListener(theGame));
    this.add(startButton);
    JButton resetButton = new JButton("Reset");
    resetButton.addActionListener(new ResetListener(theGame, startButton));
    this.add(resetButton);
    JButton tickButton = new JButton("Tick");
    tickButton.addActionListener(new TickListener(theGame));
    this.add(tickButton);
  }

  public class SpeedListener implements ActionListener {
    GameOfLife theGame;

    public SpeedListener(GameOfLife theGame) {
      this.theGame = theGame;
    }

    /**
     * When the speed button is clicked, you change the tick delay of the
     * simulation based on user input. Get a delay from the user, between 0
     * and 10000 (ten seconds). Then set it as the new speed. Also, update
     * the button's text.
     */
    public void actionPerformed(ActionEvent e) {
      JButton speedButton = (JButton) e.getSource();
      theGame.stop();
      int newSpeed = getBoundedInput("Tick delay in ms", 0, 10000, 100);
      theGame.setSleepTime(newSpeed);
      speedButton.setText(newSpeed + " ms delay");
      theGame.start();
    }

    /**
     * Taken from WolframAutomaton. I should probably create a library.
     *
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
    public int getBoundedInput(String msg,
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
  }

  public class PlayingListener implements ActionListener {
    GameOfLife theGame;

    public PlayingListener(GameOfLife theGame) {
      this.theGame = theGame;
    }

    /**
     * If the game is paused, start it. If the game is running, stop it.
     * Update the button text appropriately.
     */
    public void actionPerformed(ActionEvent e) {
      JButton startButton = (JButton) e.getSource();
      if (theGame.paused()) {
        theGame.start();
        startButton.setText("Stop");
      } else {
        theGame.stop();
        startButton.setText("Start");
      }
    }
  }

  public class ResetListener implements ActionListener {
    GameOfLife theGame;
    JButton startButton;

    public ResetListener(GameOfLife theGame, JButton startButton) {
      this.theGame = theGame;
      this.startButton = startButton;
    }

    /**
     * Reset the game. This sets it to paused, so the start button should be
     * changed as well.
     */
    public void actionPerformed(ActionEvent e) {
      theGame.reset();
      startButton.setText("Start");
    }
  }

  public class TickListener implements ActionListener {
    GameOfLife theGame;

    public TickListener(GameOfLife theGame) {
      this.theGame = theGame;
    }

    /**
     * Clicking this button should advance the game one tick.
     */
    public void actionPerformed(ActionEvent e) {
      theGame.tick();
    }
  }
}
