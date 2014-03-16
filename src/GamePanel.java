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
  
  Card a, b;
  
  public GamePanel() {
    setBackground(Color.GREEN);
    a = new Card("A", "S", 50, 50);
    b = new Card("A", "H", 200, 200);
  }
  
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    a.draw(g);
    b.draw(g);
  }

}
