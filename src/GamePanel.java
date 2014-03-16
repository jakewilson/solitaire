import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * GamePanel.java
 *
 * @author  Jake Wilson
 * @version Mar 15, 2014
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {
  
  Deck deck;
  
  public GamePanel() {
    setBackground(new Color(0, 200, 0));
    deck = new Deck();
  }
  
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    deck.draw(g);
  }

}
