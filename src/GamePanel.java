import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

/**
 * GamePanel.java
 * The panel that Solitaire is painted on. It is responsible for drawing
 * and initializing all game objects (the deck and all piles).
 *
 * @author  Jake Wilson
 * @version Mar 15, 2014
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {
  
  private Deck deck;
  
  /**
   * Horizontal displacement between rows of cards
   */
  public static final int HORI_DISPL = 35;
  
  /**
   * X locations of every solitaire pile
   */
  public static final int[] MAIN_PILE_X_LOCS = {(HORI_DISPL*1) + (Card.WIDTH * 0),
                                                (HORI_DISPL*2) + (Card.WIDTH * 1),
                                                (HORI_DISPL*3) + (Card.WIDTH * 2),
                                                (HORI_DISPL*4) + (Card.WIDTH * 3),
                                                (HORI_DISPL*5) + (Card.WIDTH * 4),
                                                (HORI_DISPL*6) + (Card.WIDTH * 5),
                                                (HORI_DISPL*7) + (Card.WIDTH * 6)};
  
  /**
   * The y location of each main pile
   */
  public static final int MAIN_PILE_Y_LOC = 150;
  
  /**
   * X locations of each suit pile
   */
  public static final int[] SUIT_PILE_X_LOCS = {MAIN_PILE_X_LOCS[3],
                                                MAIN_PILE_X_LOCS[4],
                                                MAIN_PILE_X_LOCS[5],
                                                MAIN_PILE_X_LOCS[6]};
  
  /**
   * Y locations of each suit pile
   */
  public static final int SUIT_PILE_Y_LOC = 20;
  
  private Pile[] mainPiles, suitPiles;
  
  /**
   * The selected card is always drawn last (so it is on top of everything else)
   */
  public Card selectedCard;
  
  /**
   * No-arg constructor for a game panel. Adds mouse listeners and initializes deck and piles
   */
  public GamePanel() {
    setBackground(new Color(0, 200, 0));
    deck = new Deck();
    mainPiles = new Pile[7];
    suitPiles = new Pile[4];
    selectedCard = null;
    setInitialLayout(deck);
    CardListener listener = new CardListener(this);
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);
    this.setFocusable(true);
  }
  
  /**
   * Paints the screen on a graphics context
   * @param g the graphics context to paint on
   */
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    // draw all piles and the remaining cards left in the deck
    for (int i = 0; i < mainPiles.length; i++) {
      mainPiles[i].draw(g);
    }
    for (int i = 0; i < suitPiles.length; i++) {
      suitPiles[i].draw(g);
    }
    deck.draw(g);
    
    if (selectedCard != null) selectedCard.draw(g);
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
    for (int i = 0; i < mainPiles.length; i++) {
      mainPiles[i] = new Pile(MAIN_PILE_X_LOCS[i], MAIN_PILE_Y_LOC, Pile.MAIN_TYPE);
      for (int j = 0; j <= i; j++) {
        mainPiles[i].addCardToPile(d.getCardAt(cardNum));
        d.getCardAt(cardNum).setMainPileNum(i);
        if (j == i) d.getCardAt(cardNum).faceDown = false;
        d.removeCardAt(cardNum);
      }
    }
    
    for (int i = 0; i < Card.SUITS.length; i++) {
      suitPiles[i] = new Pile(SUIT_PILE_X_LOCS[i], SUIT_PILE_Y_LOC, Pile.SUIT_TYPE);
    }
    
    // place the remaining cards in the deck at the top left corner
    for (int i = cardNum; i < d.size(); i++) {
      d.getCardAt(i).setLocation(GamePanel.HORI_DISPL, GamePanel.SUIT_PILE_Y_LOC);
    }
  }
  
  /**
   * @return the mainPiles array
   */
  public Pile[] getMainPiles() {
    return mainPiles;
  }
  
  /**
   * @return the suitPiles array
   */
  public Pile[] getSuitPiles() {
    return suitPiles;
  }

}
