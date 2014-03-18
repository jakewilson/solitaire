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
  
  public static final int VERT_DISPL = 5; // vertical displacement between cards in the same pile
  
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
    pile = new ArrayList<Card>();
    xLoc = x;
    yLoc = y;
  }
  
  /**
   * Adds a card to the pile and sets it's location appropriately
   * @param c the card to add
   */
  public void addCardToPile(Card c) {
    if (c != null) pile.add(c);
    c.setLocation(xLoc, yLoc + pile.indexOf(c) * VERT_DISPL);
  }
  
  /**
   * @return the card at index i or null if i is out of bounds
   */
  public Card getCardAtIndex(int i) {
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

}
