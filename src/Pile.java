import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


/**
 * Pile.java
 * This class represents a pile of cards in solitaire. Unlike the deck, a pile does not
 * necessarily contain 52 cards, it can hold anywhere from 0 to 52 cards. Piles can have cards
 * added and removed at runtime.
 * 
 * Note: the Pile class does not care how or in what order cards are added. The CardListener class
 * is responsible for ensuring that cards are added to piles in red black red black order and that
 * the faces are descending.
 *
 * @author  Jake Wilson
 * @version Mar 17, 2014
 */
public class Pile {
  
  private ArrayList<Card> pile;
  
  /**
   * The x and y locations of the pile. Though these are not final, they should NEVER change
   */
  private int xLoc, yLoc;
  
  /**
   * The width of the pile. It will always be the width of a card
   */
  private final int width = Card.WIDTH;
  
  /**
   * The height of a pile. It will change as cards are added to the pile
   */
  private int height;
  
  public static final int VERT_DISPL = 20; // vertical displacement between cards in the same pile
  
  /**
   * No-arg constructor that sets the piles coordinates to (0,0)
   */
  public Pile() {
    this(0, 0);
  }
  
  /**
   * Constructs a new pile of cards with an x location and a y location of (x,y)
   * @param x the x location of the pile
   * @param y the y location of the pile
   */
  public Pile(int x, int y) {
    pile   = new ArrayList<Card>();
    xLoc   = x;
    yLoc   = y;
    height = Card.HEIGHT;
  }
  
  /**
   * Draws the pile of cards
   * @param g the graphics context to draw on
   */
  public void draw(Graphics g) {
    if (pile.size() == 0) {
      g.setColor(Color.white);
      g.drawRoundRect(xLoc, yLoc, Card.WIDTH, Card.HEIGHT, 10, 10);
      return;
    }
    for (int i = 0; i < pile.size(); i++) {
      pile.get(i).draw(g);
    }
  }
  
  /**
   * Adds a card to the pile and sets it's location appropriately
   * @param c the card to add
   */
  public void addCardToPile(Card c) {
    if (c != null) pile.add(c);
    c.setLocation(xLoc, yLoc + pile.indexOf(c) * VERT_DISPL);
    height += VERT_DISPL;
  }
  
  /**
   * @return the card at index i or null if i is out of bounds
   */
  public Card getCardAt(int i) {
    return withinBounds(i) ? pile.get(i) : null;
  }
  
  /**
   * Returns whether i is a valid index of the pile
   * @param i the index to check
   * @return whether i is a valid index of the pile
   */
  private boolean withinBounds(int i) {
    return (i >= 0 && i < pile.size());
  }
  
  /**
   * Returns the card that has been clicked, if any
   * TODO: this should return an array of all cards clicked. If only one was clicked, the array will have
   * just one card (obviously)
   * @param e the mouse event to check
   * @return the card that was clicked or null if no cards were clicked
   */
  public Card cardHasBeenClicked(MouseEvent e) {
    for (int i = 0; i < pile.size(); i++) {
      Card c = this.getCardAt(i);
      if ((e.getX() >= c.getX() && e.getX() <= (c.getX() + width)) &&
          (e.getY() >= c.getY() && e.getY() <= (c.getY() + height)) && (isOnTop(c))) {
        // TODO: we still want a card that's not on top to be clicked if we want to move a whole pile
        return c;
      }
    }
    
    return null;
  }
  
  /**
   * Removes the card at index i from the deck or does nothing if the index is out of bounds
   * TODO: only the top card can be removed by itself. If a card other than the top is removed, all the cards
   *       above it are removed as well
   * @param i the index to remove
   */
  public void removeCardAt(int i) {
    if (!withinBounds(i)) return;
    
    pile.remove(i);
  }
  
  /**
   * Removes c from the pile
   * @param c the card to remove
   */
  public void removeCard(Card c) {
    if (c == null) return;
    pile.remove(c);
  }
  
  /**
   * Determines if the passed in card is on top of the pile (it's the most recent addition to the pile)
   * @param c the card to check
   * @return whether the card is on top of its pile
   */
  public boolean isOnTop(Card c) {
    if (withinBounds(pile.indexOf(c)))
      return (pile.indexOf(c) == pile.size() - 1);
    
    return false;
  }
  
  /**
   * @return the card on top of the pile or null if there are no cards in the pile
   */
  public Card getCardOnTop() {
    if (pile.size() > 0)
      return pile.get(pile.size() - 1);
    
    return null;
  }
  
  /**
   * Returns whether a card has been dropped on the pile
   * @param c the card to check
   * @return whether the card has been dropped on the pile
   */
  public boolean cardDroppedOnPile(Card c) {
    // this checks to see if any of the cards corners is on a pile
    return (((c.getX() >= xLoc && c.getX() <= xLoc + width)           && (c.getY() >= yLoc && c.getY() <= yLoc + height))             ||
            ((c.getRightX() >= xLoc && c.getRightX() <= xLoc + width) && (c.getY() >= yLoc && c.getY() <= yLoc + height))             ||
            ((c.getX() >= xLoc && c.getX() <= xLoc + width)           && (c.getBottomY() >= yLoc && c.getBottomY() <= yLoc + height)) ||
            ((c.getRightX() >= xLoc && c.getRightX() <= xLoc + width) && (c.getBottomY() >= yLoc && c.getBottomY() <= yLoc + height)));
  }
  
  /**
   * @return the size of the pile
   */
  public int size() {
    return pile.size();
  }

}
