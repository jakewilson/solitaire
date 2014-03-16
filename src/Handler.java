
import javax.swing.JFrame;



/**
 * Handler.java
 *
 * @author  Jake Wilson
 * @version Mar 15, 2014
 */
public class Handler {
  
  private static JFrame    gameFrame;
  private static GamePanel gamePanel;
  
  public static void main(String[] args) {
    loadGame();
    runGame();
  }
  
  private static void runGame() {
    while (true) {
      gamePanel.repaint();
    }
  }
  
  /**
   * Initializes game objects
   */
  private static void loadGame() {
    gameFrame = new JFrame("Solitaire");
    gamePanel = new GamePanel();
    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gameFrame.setSize(900, 500);
    gameFrame.add(gamePanel);
    gameFrame.setVisible(true);
  }

}
