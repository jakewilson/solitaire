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

class CardListener extends MouseInputAdapter {
  
  private GamePanel panel;
  
  private boolean cardPressed;
  private Deck deck;
  
  private Pile[] mainPiles, suitPiles;
  
  private int lastX, lastY;
  private int origX, origY;
  
  /**
   * Constructor for a Card Listener
   * @param panel the game panel in which to manipulate when the user clicks/drags/drops cards
   */
  public CardListener(GamePanel panel) {
    this.panel   = panel;
    cardPressed  = false;
    deck = panel.getDeck();
    mainPiles = panel.getMainPiles();
    suitPiles = panel.getSuitPiles();
    lastX = 0;
    lastY = 0;
    origX = 0;
    origY = 0;
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    
  }
  
  @Override
  /**
   * Selects a card when it is clicked
   */
  public void mousePressed(MouseEvent e) {
    panel.selectedCard = getCardClicked(e);
    if (panel.selectedCard != null) {
      cardPressed = true;
      lastX = e.getX();
      lastY = e.getY();
      origX = panel.selectedCard.getX();
      origY = panel.selectedCard.getY();
    } else {
      return;
    }
    panel.repaint();
  }
  
  @Override
  /**
   * Drops a card on a pile only if it has the right face and color
   */
  public void mouseReleased(MouseEvent e) {
    Card c = panel.selectedCard;
    // TODO: add functionality for suit piles as well
    // determines if a card was dropped on a pile. If the card is dropped on
    // two piles, it will choose the left most one
    if (c != null) {
      boolean validDrop = false;
      for (int i = 0; i < mainPiles.length; i++) {
        if (mainPiles[i].cardDroppedOnPile(c)) {
          if (c.getMainPileNum() >= 0) {
            if (mainPiles[i].size() == 0) { // a card may be added to an empty main pile only if it's a king
              if (!c.getFace().equals("K")) {
                break;
              }
            } else {
              // a card may only be added to a pile in red - black - red - black order
              // black may not be added to black or red to red
              Card top = mainPiles[i].getCardOnTop();
              if (!(top.getColor().equals(Color.red)   && c.getColor().equals(Color.black)) &&
                  !(top.getColor().equals(Color.black) && c.getColor().equals(Color.red))) {
                break;
              } else if (Card.getFaceIndex(top.getFace()) != Card.getFaceIndex(c.getFace()) + 1) {
                // the selected cards face must also be the next index lower than the face at the top of the pile
                break;
              }
            }
            mainPiles[c.getMainPileNum()].removeCard(c);
            mainPiles[i].addCardToPile(c);
            c.setMainPileNum(i);
            c.setSuitPileNum(-1);
            validDrop = true;
            break;
          }
        }
      }
      // if the card isn't dropped on a pile, move it back to where it was picked up from, check if it
      // was dropped on a suit pile
      if (!validDrop) {
        for (int i = 0; i < suitPiles.length; i++) {
          if (suitPiles[i].cardDroppedOnPile(c)) {
            // if a card is added to an empty suit pile, it must be an ace
            if (suitPiles[i].size() == 0) {
              if (!c.getFace().equals("A"))
                break;
              else if (c.getSuitPileNum() > -1) // the card is already part of a suit pile - don't add it to another one
                break;
            } else {
              Card topCard = suitPiles[i].getCardOnTop();
              // cards must be the same suit and faces must be in ascending order
              if (!topCard.getSuit().equals(c.getSuit()))
                break;
              else if (Card.getFaceIndex(c.getFace()) != Card.getFaceIndex(topCard.getFace()) + 1) {
                break;
              }
            }
            
            mainPiles[c.getMainPileNum()].removeCard(c);
            suitPiles[i].addCardToPile(c);
            c.setMainPileNum(-1);
            c.setSuitPileNum(i);
            validDrop = true;
            break;
          }
        }
      }
      
      if (!validDrop) {
        c.setLocation(origX, origY);
      }
    }
  
    cardPressed  = false;
    c = null;
    panel.repaint();
  }
  
  @Override
  public void mouseMoved(MouseEvent e) {
    
  }
  
  @Override
  /**
   * Moves the card as it is dragged by the mouse
   */
  public void mouseDragged(MouseEvent e) {
    if (cardPressed && panel.selectedCard != null) {
      int newX = panel.selectedCard.getX() + (e.getX() - lastX);
      int newY = panel.selectedCard.getY() + (e.getY() - lastY);
      panel.selectedCard.setLocation(newX, newY);
      lastX = e.getX();
      lastY = e.getY();
    }
    panel.repaint();
  }
  
  /**
   * Returns the card that was clicked or null if no card was clicked
   * @param e the mouse event to check
   * @return the card that was clicked or null if no card was clicked
   */
  private Card getCardClicked(MouseEvent e) {
    Card clicked = null;
    // check if the card is in the deck, then the main piles, then the suit piles
    clicked = deck.cardHasBeenClicked(e);
    if (clicked == null) {
      for (int i = 0; i < mainPiles.length; i++) {
        if ((clicked = mainPiles[i].cardHasBeenClicked(e)) != null)
          break;
      }
      if (clicked == null) {
        for (int i = 0; i < suitPiles.length; i++) {
          if ((clicked = suitPiles[i].cardHasBeenClicked(e)) != null)
            break;
        }
      }
    }
    
    return clicked;
  }
  
}
