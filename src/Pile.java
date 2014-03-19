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
   * A suit pile is one of the four piles at the top right corner
   */
  public static final int SUIT_PILE = 0;
  
  /**
   * A main pile is one of the seven piles across the middle of the screen
   */
  public static final int MAIN_PILE = 1;
  
  /**
   * This indicates a pile that is being moved
   */
  public static final int TEMP_PILE = 2;
  
  private int type;
  
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
   * No-arg constructor that sets the piles coordinates to (0,0) and sets the pile type to MAIN_PILE
   */
  public Pile() {
    this(0, 0, TEMP_PILE);
  }
  
  /**
   * Constructs a new pile of cards with an x location and a y location of (x,y)
   * @param x the x location of the pile
   * @param y the y location of the pile
   */
  public Pile(int x, int y, int t) {
    pile   = new ArrayList<Card>();
    xLoc   = x;
    yLoc   = y;
    height = Card.HEIGHT;
    if (t != SUIT_PILE && t != MAIN_PILE)
      type = TEMP_PILE;
    else
      type = t;
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
   * Adds a card to the pile and sets its location appropriately
   * @param c the card to add
   */
  public void addToPile(Card c) {
    if (c != null) pile.add(c);
    if (type != SUIT_PILE) { 
      c.setLocation(xLoc, yLoc + pile.indexOf(c) * VERT_DISPL);
      if (pile.size() > 1) height += VERT_DISPL;
    } else {
      c.setLocation(xLoc, yLoc);
    }
  }
  
  /**
   * Adds a pile to the pile and sets its location appropriately
   * TODO: this might need to work for TEMP_PILE's as well but we'll see
   * @param p the pile to add
   */
  public void addToPile(Pile p) {
    if (this.type == MAIN_PILE) {
      while (!p.isEmpty()) {
        this.addToPile(p.getCardAt(0));
        p.removeCardAt(0);
      }
    }
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
   * Returns the pile that has been clicked, if any. If only one card has been clicked, the pile
   * will simply contain a single card
   * @param e the mouse event to check
   * @return the pile that was clicked or null if the pile was not clicked
   */
  public Pile pileHasBeenClicked(MouseEvent e) {
    // no point in checking the y coordinates if the x isn't right
    if (e.getX() < this.xLoc || e.getX() > this.xLoc + this.width)
      return null;
    
    if (this.size() == 0 && (e.getY() >= this.yLoc && e.getY() <= this.yLoc + this.height))
      return new Pile();
    
    for (int i = 0; i < this.size() - 1; i++) {
      Card c = this.getCardAt(i);
      if (e.getY() >= c.getY() && e.getY() <= c.getY() + VERT_DISPL && !c.faceDown) {
        return this.getPileAt(i);
      }
    }
    
    if (this.size() > 0) {
      Card c = this.getCardOnTop();
      if (e.getY() >= c.getY() && e.getY() <= c.getBottomY() && !c.faceDown)
        return this.getPileAt(this.size() - 1);
    }
    
    
    return null;
  }
  
  /**
   * Returns a pile of cards from the passed in index to the end of this pile
   * @param i the index to start at
   * @return a pile of cards from the index i to the end of this pile
   */
  public Pile getPileAt(int i) {
    Pile p = new Pile(xLoc, this.getCardAt(i).getY(), TEMP_PILE);
    for (; i < this.size(); i++) {
      p.addToPile(this.getCardAt(i));
    }
    
    return p;
  }
  
  /**
   * Removes the card at index i from the deck or does nothing if the index is out of bounds
   * TODO: only the top card can be removed by itself. If a card other than the top is removed, all the cards
   *       above it are removed as well
   * @param i the index to remove
   */
  public void removeCardAt(int i) {
    if (!withinBounds(i)) return;
    
    // if the card on top is removed, turn the next card face up
    if (i > 0 && isOnTop(pile.get(i)))
      pile.get(i).faceDown = false;
    
    pile.remove(i);
  }
  
  /**
   * Removes c from the pile
   * @param c the card to remove
   */
  public void removeCard(Card c) {
    if (c == null) return;
    
    // if the card on top is removed, turn the next card face up
    int index = pile.indexOf(c);
    if (isOnTop(c) && index > 0)
      pile.get(index - 1).faceDown = false;
    
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
   * @return the card at the first position of the pile
   */
  public Card getCardOnBottom() {
    if (pile.size() > 0)
      return pile.get(0);
    
    return null;
  }
  
  /**
   * Returns whether a card has been dropped on the pile
   * @param c the card to check
   * @return whether the card has been dropped on the pile
   */
  public boolean droppedOnPile(Card c) {
    // this checks to see if any of the cards corners is on a pile
    return (((c.getX() >= xLoc && c.getX() <= xLoc + width)           && (c.getY() >= yLoc && c.getY() <= yLoc + height))             ||
            ((c.getRightX() >= xLoc && c.getRightX() <= xLoc + width) && (c.getY() >= yLoc && c.getY() <= yLoc + height))             ||
            ((c.getX() >= xLoc && c.getX() <= xLoc + width)           && (c.getBottomY() >= yLoc && c.getBottomY() <= yLoc + height)) ||
            ((c.getRightX() >= xLoc && c.getRightX() <= xLoc + width) && (c.getBottomY() >= yLoc && c.getBottomY() <= yLoc + height)));
  }
  
  /**
   * Returns whether a pile has been dropped on this pile
   * @param p the pile to check
   * @return whether p has been dropped on this pile
   */
  public boolean droppedOnPile(Pile p) {
    if (p.size() > 0) {
      // because dropping a pile is really like dropping the root card on that pile
      return droppedOnPile(p.getCardAt(0));
    }
    
    return false;
  }
  
  /**
   * @return the size of the pile
   */
  public int size() {
    return pile.size();
  }
  
  /**
   * @return the pile type: either SUIT_PILE or MAIN_PILE
   */
  public int getType() {
    // should be impossible for type to be anything except SUIT, MAIN, or TEMP, but just in case...
    return (type == SUIT_PILE || type == MAIN_PILE || type == TEMP_PILE) ? type : TEMP_PILE;
  }
  
  /**
   * Sets the type to either MAIN, SUIT, or TEMP. If t is not any of these, it is set to TEMP
   * @param t the new type
   */
  public void setType(int t) {
    type = (t == MAIN_PILE || t == SUIT_PILE || t == TEMP_PILE) ? t : TEMP_PILE;
  }
  
  /**
   * @return the x location of the pile
   */
  public int getX() {
    return xLoc;
  }
  
  /**
   * @return the y location of the pile
   */
  public int getY() {
    return yLoc;
  }
  
  /**
   * Moves a TEMP_PILE. The method simply does nothing if the pile is not a TEMP_PILE.
   * It also moves the x and y location of every card in the pile
   * @param x the new x location of the pile
   * @param y the new y location of the pile
   */
  public void setLocation(int x, int y) {
    if (type == TEMP_PILE) {
      xLoc = x;
      yLoc = y;
      // move every card in the pile
      for (int i = 0; i < this.size(); i++) {
        Card c = this.getCardAt(i);
        c.setLocation(x, y + (i * VERT_DISPL));
      }
    }
  }
  
  /**
   * @return whether a pile is empty (size == 0) or not
   */
  public boolean isEmpty() {
    return this.size() == 0;
  }

}
