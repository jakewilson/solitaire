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
  
  private static final int FRAME_WIDTH  = 900;
  private static final int FRAME_HEIGHT = 700;
  
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
    gameFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    gameFrame.add(gamePanel);
    gameFrame.setVisible(true);
  }

}
