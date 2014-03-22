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
  
  /**
   * A pile that the user may draw cards from
   */
  public static final int DECK_PILE = 3;
  
  /**
   * The type of pile: either SUIT, MAIN, TEMP, or DECK
   */
  private int type;
  
  /**
   * The x and y locations of the pile. Though these are not final, they should NEVER change
   */
  private int xLoc, yLoc;
  
  /**
   * The width of the pile. It will always be the width of a card
   */
  private int width;
  
  /**
   * The height of a pile. It will change as cards are added to the pile
   */
  private int height;
  
  /**
   * Vertical displacement between cards in a pile. This only applies for cards in a MAIN_PILE or TEMP_PILE
   */
  public static final int VERT_DISPL = 22;
  
  /**
   * Horizontal displacement between cards. This only applies for cards in a DECK_PILE
   */
  public static final int HORI_DISPL = 22;
  
  /**
   * The top 3 cards in a deck pile. This is necessary because a maximum of three cards can
   * be drawn in a deck pile. Also, if one card is removed, we must draw two, not three.
   */
  private Card[] top3;
  
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
    width  = Card.WIDTH;
    height = Card.HEIGHT;
    if (t != SUIT_PILE && t != MAIN_PILE && t != DECK_PILE)
      type = TEMP_PILE;
    else
      type = t;
    // the top 3 are only used by DECK_PILE's
    top3 = (type == DECK_PILE) ? new Card[3] : null;
  }
  
  /**
   * Draws the pile of cards
   * @param g the graphics context to draw on
   */
  public void draw(Graphics g) {
    if (pile.size() == 0 && type != DECK_PILE) {
      g.setColor(Color.white);
      g.drawRoundRect(xLoc, yLoc, Card.WIDTH, Card.HEIGHT, 10, 10);
      return;
    }
    
    if (type == DECK_PILE) {
      for (int i = 0; i < top3.length; i++) {
        if (top3[i] != null) {
          top3[i].draw(g);
        }
      }
    } else {
      for (int i = 0; i < pile.size(); i++) {
        pile.get(i).draw(g);
      }
    }
    
  }
  
  /**
   * Adds a card to the pile and sets its location appropriately
   * @param c the card to add
   */
  public void addToPile(Card c) {
    if (c != null) pile.add(c);
    if (type != SUIT_PILE && type != DECK_PILE) {
      c.setLocation(xLoc, yLoc + pile.indexOf(c) * VERT_DISPL);
      if (pile.size() > 1) height += VERT_DISPL;
    } else if (type == SUIT_PILE){
      c.setLocation(xLoc, yLoc);
    } else if (type == DECK_PILE) {
      c.setLocation(xLoc, yLoc);
      updateTop3();
    }
  }
  
  /**
   * Adds a pile to the pile and sets its location appropriately
   * TODO: this might need to work for TEMP_PILE's as well but we'll see
   * @param p the pile to add
   */
  public void addToPile(Pile p) {
    if (this.type == MAIN_PILE) {
      for (int i = 0; i < p.size(); i++) {
        this.addToPile(p.getCardAt(i));
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
      return this;
    
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
    while (i < this.size()) {
      p.addToPile(this.getCardAt(i));
      this.removeCardAt(i);
    }
    
    return p;
  }
  
  /**
   * Removes the card at index i from the deck or does nothing if the index is out of bounds
   * @param i the index to remove
   */
  public void removeCardAt(int i) {
    if (withinBounds(i))
      pile.remove(i);
  }
  
  /**
   * Removes c from the pile
   * @param c the card to remove
   */
  public void removeCard(Card c) {
    if (c != null)
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
      // because dropping a pile is really like dropping the bottom card on that pile
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
    // should be impossible for type to be anything except SUIT, MAIN, TEMP, or DECK but just in case...
    return isValidPile(type) ? type : TEMP_PILE;
  }
  
  /**
   * Sets the type to either MAIN, SUIT, or TEMP. If t is not any of these, it is set to TEMP
   * @param t the new type
   */
  public void setType(int t) {
    type = isValidPile(t) ? t : TEMP_PILE;
  }
  
  /**
   * Returns whether the value t is a valid pile: either MAIN, SUIT, TEMP, or DECK
   * @param t
   * @return
   */
  public boolean isValidPile(int t) {
    return (t == MAIN_PILE || t == SUIT_PILE || t == TEMP_PILE || t == DECK_PILE);
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
  
  /**
   * Turns the top card of the pile up, or does nothing if the size is 0 
   */
  public void turnTopCardUp() {
    if (this.size() > 0)
      this.getCardOnTop().faceDown = false;
  }
  
  /**
   * Turns all cards in the pile face up
   */
  public void turnAllCardsUp() {
    for (int i = 0; i < size(); i++) {
      pile.get(i).faceDown = false;
    }
  }
  
  /**
   * Updates the top 3 cards in the deck pile and adjusts their location
   */
  public void updateTop3() {
    if (type == DECK_PILE) {
      // first clear the top 3
      for (int i = 0; i < top3.length; i++)
        top3[i] = null;
      
      if (this.size() >= 3) {
        for (int i = this.size() - 3, j = 0; i < this.size(); i++, j++) {
          top3[j] = this.getCardAt(i);
          top3[j].setLocation(xLoc + (j * HORI_DISPL), yLoc);
        }
      } else {
        for (int i = 0; i < this.size(); i++) {
          top3[i] = this.getCardAt(i);
          top3[i].setLocation(xLoc + (i * HORI_DISPL), yLoc);
        }
      }
    }
  }

}
