import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

/**
 * GamePanel.java
 *
 * @author  Jake Wilson
 * @version Mar 15, 2014
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {
  
  private Deck deck;
  
  public GamePanel() {
    setBackground(new Color(0, 200, 0));
    deck = new Deck();
    deck.setInitialLayout();
    this.addMouseListener(new CardListener(this));
  }
  
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    deck.draw(g);
  }
  
  public Deck getDeck() {
    return deck;
  }

}

class CardListener implements MouseListener {
  
  GamePanel panel;
  
  public CardListener(GamePanel panel) {
    this.panel = panel;
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    
  }
  
  @Override
  public void mousePressed(MouseEvent e) {
    Card c = panel.getDeck().cardHasBeenClicked(e);
    if (c != null) {
      System.out.println(c + " has been pressed.");
    }
  }
  
  @Override
  public void mouseReleased(MouseEvent e) {
    
  }
  
  @Override
  public void mouseEntered(MouseEvent e) {
    
  }
  
  @Override
  public void mouseExited(MouseEvent e) {
    
  }
  
}
