import javax.swing.JFrame;

public class GameOfLifeTest extends JFrame {
	public GameOfLifeTest(GameOfLifeDisplay theGame,
												GameOfLifeMenu theMenu) {
		super("Conway's Game of Life");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setJMenuBar(theMenu);
		this.add(theGame);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

  public static void main(String[] args) {
		GameOfLifeDisplay theGame = new GameOfLifeDisplay(10, 20);
		GameOfLifeMenu theMenu = new GameOfLifeMenu(theGame);
		GameOfLifeTest warghbl = new GameOfLifeTest(theGame, theMenu);
		theGame.run();
  }
}
