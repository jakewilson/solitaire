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
  
  private boolean cardPressed;
  private Card selectedCard;
  
  private int lastX, lastY;
  
  public CardListener(GamePanel panel) {
    this.panel   = panel;
    cardPressed  = false;
    selectedCard = null;
    lastX = 0;
    lastY = 0;
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    
  }
  
  @Override
  public void mousePressed(MouseEvent e) {
    selectedCard = panel.getDeck().cardHasBeenClicked(e);
    if (selectedCard != null) {
      cardPressed = true;
      lastX = e.getX();
      lastY = e.getY();
    }
  }
  
  @Override
  public void mouseReleased(MouseEvent e) {
    cardPressed  = false;
    selectedCard = null;
  }
  
  @Override
  public void mouseMoved(MouseEvent e) {
    
  }
  
  @Override
  public void mouseDragged(MouseEvent e) {
    if (cardPressed && selectedCard != null) {
      int newX = selectedCard.getX() + (e.getX() - lastX);
      int newY = selectedCard.getY() + (e.getY() - lastY);
      selectedCard.setLocation(newX, newY);
      lastX = e.getX();
      lastY = e.getY();
    }
  }
  
}
