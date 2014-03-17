import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

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
    CardListener listener = new CardListener(this);
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);
    this.setFocusable(true);
  }
  
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    deck.draw(g);
  }
  
  public Deck getDeck() {
    return deck;
  }

}

class CardListener extends MouseInputAdapter {
  
  private GamePanel panel;
  
  private boolean pressed;
  private Card selectedCard;
  
  public CardListener(GamePanel panel) {
    this.panel   = panel;
    pressed      = false;
    selectedCard = null;
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    
  }
  
  @Override
  public void mousePressed(MouseEvent e) {
    selectedCard = panel.getDeck().cardHasBeenClicked(e);
    if (selectedCard != null) {
      pressed = true;
    }
  }
  
  @Override
  public void mouseReleased(MouseEvent e) {
    pressed      = false;
    selectedCard = null;
  }
  
  @Override
  public void mouseMoved(MouseEvent e) {
    
  }
  
  @Override
  public void mouseDragged(MouseEvent e) {
    if (pressed && selectedCard != null) {
      selectedCard.setLocation(e.getX(), e.getY());
    }
  }
  
}
