import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class GameOfLifeMenu extends JMenuBar {
  GameOfLifeDisplay theGame;

  public GameOfLifeMenu(GameOfLifeDisplay theGame) {
    this.theGame = theGame;
    this.setPreferredSize(new Dimension(theGame.getWidthPixels(), 30));
    JMenu options = new OptionsMenu(theGame);
    this.add(options);
    JButton startButton = new JButton("Start");
    startButton.addActionListener(new PlayingListener(theGame));
    this.add(startButton);
  }

  public class OptionsMenu extends JMenu {
    GameOfLifeDisplay theGame;

    public OptionsMenu(GameOfLifeDisplay theGame) {
      super("Options");
      this.theGame = theGame;
    }
  }

  public class PlayingListener implements ActionListener {
    GameOfLifeDisplay theGame;

    public PlayingListener(GameOfLifeDisplay theGame) {
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
}
