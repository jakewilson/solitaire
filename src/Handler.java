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
  
  private static final int FRAME_WIDTH  = 700;
  private static final int FRAME_HEIGHT = 700;
  
  public static void main(String[] args) {
    loadGame();
    runGame();
  }
  
  /**
   * Runs the game. There is currently no point in having a game loop, since the only
   * time the game needs to be re-painted is when the user moves a card.
   */
  private static void runGame() {
    gamePanel.repaint();
//    while (true) {
//    }
  }
  
  /**
   * Initializes game objects
   */
  private static void loadGame() {
    gameFrame = new JFrame("Solitaire");
    gamePanel = new GamePanel();
    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gameFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    gameFrame.setLocationRelativeTo(null);
    gameFrame.add(gamePanel);
    gameFrame.setVisible(true);
  }

}
