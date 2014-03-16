import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


/**
 * GamePanel.java
 *
 * @author  Jake Wilson
 * @version Mar 15, 2014
 */
public class GamePanel extends JPanel {
  
  Deck deck;
  
  public GamePanel() {
    setBackground(Color.GREEN);
    deck = new Deck();
  }
  
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
  }

}
