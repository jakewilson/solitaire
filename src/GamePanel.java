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
  
  public static final int HORI_DISPL = 35; // horizontal displacement between piles of cards
  
  /**
   * X locations of every solitaire pile
   */
  public static final int[] PILE_X_LOCS = {(HORI_DISPL*1) + (Card.WIDTH * 0),
                                           (HORI_DISPL*2) + (Card.WIDTH * 1),
                                           (HORI_DISPL*3) + (Card.WIDTH * 2),
                                           (HORI_DISPL*4) + (Card.WIDTH * 3),
                                           (HORI_DISPL*5) + (Card.WIDTH * 4),
                                           (HORI_DISPL*6) + (Card.WIDTH * 5),
                                           (HORI_DISPL*7) + (Card.WIDTH * 6)};
  
  /**
   * Y locations of every solitaire pile
   */
  public static final int[] PILE_Y_LOCS = {150 + (Pile.VERT_DISPL * 0),
                                           150 + (Pile.VERT_DISPL * 1),
                                           150 + (Pile.VERT_DISPL * 2),
                                           150 + (Pile.VERT_DISPL * 3),
                                           150 + (Pile.VERT_DISPL * 4),
                                           150 + (Pile.VERT_DISPL * 5),
                                           150 + (Pile.VERT_DISPL * 6)};
  
  /**
   * X locations of each suit pile
   */
  public static final int[] SUIT_PILE_X_LOCS = {PILE_X_LOCS[3],
                                                PILE_X_LOCS[4],
                                                PILE_X_LOCS[5],
                                                PILE_X_LOCS[6]};
  
  /**
   * Y locations of each suit pile
   */
  public static final int SUIT_PILE_Y_LOC = 20;
  
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
  private Deck deck;
  
  private int lastX, lastY;
  
  public CardListener(GamePanel panel) {
    this.panel   = panel;
    cardPressed  = false;
    selectedCard = null;
    deck = panel.getDeck();
    lastX = 0;
    lastY = 0;
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    
  }
  
  @Override
  public void mousePressed(MouseEvent e) {
    selectedCard = deck.cardHasBeenClicked(e);
    if (selectedCard != null) {
      cardPressed = true;
      lastX = e.getX();
      lastY = e.getY();
    }
    panel.repaint();
  }
  
  @Override
  public void mouseReleased(MouseEvent e) {
//    if (selectedCard != null) {
//      if ()
//    }
    cardPressed  = false;
    selectedCard = null;
    panel.repaint();
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
    panel.repaint();
  }
  
}
