import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

public class GameOfLifeMenu extends JMenuBar {
  GameOfLife theGame;

  public GameOfLifeMenu(GameOfLife theGame) {
    this.theGame = theGame;
    this.setPreferredSize(new Dimension(theGame.getWidthPixels(), 30));
    JButton speedButton = new JButton("0 ms delay");
    speedButton.addActionListener(new SpeedListener(theGame));
    this.add(speedButton);
    JButton startButton = new JButton("Start");
    startButton.addActionListener(new PlayingListener(theGame));
    this.add(startButton);
    JButton resetButton = new JButton("Reset");
    resetButton.addActionListener(new ResetListener(theGame));
    this.add(resetButton);
  }

  public class SpeedListener implements ActionListener {
    GameOfLife theGame;

    public SpeedListener(GameOfLife theGame) {
      this.theGame = theGame;
    }

    public void actionPerformed(ActionEvent e) {
      JButton speedButton = (JButton) e.getSource();
      int newSpeed = getBoundedInput("Tick delay in ms", 0, 10000, 100);
      theGame.setSleepTime(newSpeed);
      speedButton.setText(newSpeed + " ms delay");
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

    public ResetListener(GameOfLife theGame) {
      this.theGame = theGame;
    }

    public void actionPerformed(ActionEvent e) {
      theGame.reset();
    }
  }
}
