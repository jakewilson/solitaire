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
   * The y location of each main pile
   */
  public static final int mainPileYLoc = 150;
  
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
  
  private Pile[] mainPile, suitPile;
  
  public GamePanel() {
    setBackground(new Color(0, 200, 0));
    deck = new Deck();
    mainPile = new Pile[7];
    suitPile = new Pile[4];
    setInitialLayout(deck);
    CardListener listener = new CardListener(this);
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);
    this.setFocusable(true);
  }
  
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    deck.draw(g);
    drawSuitPiles(g);
  }
  
  /**
   * Draws each suit pile at the top right of the screen
   * @param g
   */
  private void drawSuitPiles(Graphics g) {
    g.setColor(Color.white);
    // draw an outline around each suit pile
    for (int i = 0; i < Card.SUITS.length; i++) {
      g.drawRoundRect(SUIT_PILE_X_LOCS[i], SUIT_PILE_Y_LOC, Card.WIDTH, Card.HEIGHT, 10, 10);
    }
  }
  
  /**
   * @return the deck of cards
   */
  public Deck getDeck() {
    return deck;
  }
  
  /**
   * Sets the location of all cards to their starting points
   */
  public void setInitialLayout(Deck d) {
    int cardNum = 0;
    for (int i = 0; i < mainPile.length; i++) {
      mainPile[i] = new Pile(PILE_X_LOCS[i], mainPileYLoc);
      for (int j = 0; j <= i; j++) {
        mainPile[i].addCardToPile(d.getCardAt(cardNum++));
        if (j == i) d.getCardAt(cardNum - 1).faceDown = false;
      }
    }
    
    for (int i = 0; i < Card.SUITS.length; i++) {
      suitPile[i] = new Pile(SUIT_PILE_X_LOCS[i], SUIT_PILE_Y_LOC);
    }
    
    // place the remaining cards in the deck at the top left corner
    for (int i = cardNum; i < d.LENGTH; i++) {
      d.getCardAt(i).setLocation(GamePanel.HORI_DISPL, GamePanel.SUIT_PILE_Y_LOC);
    }
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
