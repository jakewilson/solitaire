
import javax.swing.JFrame;



/**
 * Handler.java
 *
 * @author  Jake Wilson
 * @version Mar 15, 2014
 */
public class Handler {
  
  private static JFrame gameFrame;
  private static GamePanel gamePanel;
  
  public static void main(String[] args) {
    gameFrame = new JFrame("Solitaire");
    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gameFrame.setSize(900, 500);
    gamePanel = new GamePanel();
    gameFrame.add(gamePanel);
    gameFrame.setVisible(true);
    gamePanel.repaint();
  }

}
