import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


/**
 * Deck.java
 * A deck is a list of 52 cards.
 *
 * @author  Jake Wilson
 * @version Mar 15, 2014
 */
public class Deck {
  
  private ArrayList<Card> deck;
  
  /**
   * The length of the card deck. This should always be used when iterating through the deck
   */
  public final int LENGTH = 52;
  
  /**
   * No-arg constructor that adds 52 cards to the deck and shuffles it
   */
  public Deck() {
    deck = new ArrayList<Card>();
    for (int i = 0; i < Card.SUITS.length; i++) {
      for (int j = 0; j < Card.FACES.length; j++) {
        deck.add(new Card(Card.FACES[j], Card.SUITS[i]));
      }
    }
    
    shuffleDeck();
  }
  
  /**
   * Draws each card in the deck
   * @param g the graphics context to draw the deck on
   */
  public void draw(Graphics g) {
    for (int i = 0; i < this.LENGTH; i++) {
      this.getCardAt(i).draw(g);
    }
  }
  
  /**
   * Shuffles the deck of cards.
   */
  private void shuffleDeck() {
    for (int i = 0; i < this.LENGTH; i++) {
      int index = (int)(Math.random() * this.LENGTH);
      swap(i, index);
    }
  }
  
  /**
   * Swaps two cards in a deck
   * @param i1 the first index
   * @param i2 the second index
   */
  private void swap(int i1, int i2) {
    Card temp = this.getCardAt(i1);
    this.setCardAt(i1, this.getCardAt(i2));
    this.setCardAt(i2, temp);
  }
  
  /**
   * Returns the card at the specified index
   * @param index the index of the card to return
   * @return the card at the index or null if the index is out of bounds
   */
  public Card getCardAt(int index) {
    if (withinBounds(index)) {
      return deck.get(index);
    }
    
    return null;
  }
  
  /**
   * Removes the card at the specified index if the index is not out of bounds
   * @param index the index of the card to remove
   */
  private void removeCardAt(int index) {
    if (withinBounds(index)) {
      deck.remove(index);
    }
  }
  
  /**
   * Sets the card at index to c
   * @param index the index to set
   * @param c the card to set
   */
  private void setCardAt(int index, Card c) {
    if (withinBounds(index)) {
      deck.set(index, c);
    }
  }
  
  /**
   * Inserts a card c at the specified index
   * @param index the index to insert c at
   * @param c the card to insert
   */
  private void insertCardAt(int index, Card c) {
    if (withinBounds(index)) {
      deck.add(index, c);
    }
  }
  
  /**
   * Returns whether i is a valid index in the deck
   * @param i the index to check
   * @return whether i is in bounds
   */
  private boolean withinBounds(int i) {
    return (i >= 0 && i < this.LENGTH);
  }
  
  /**
   * Prints the deck for debugging purposes
   */
  private void printDeck() {
    for (int i = 0; i < this.LENGTH; i++) {
      System.out.println(i + ": " + this.getCardAt(i));
    }
  }
  
  /**
   * Returns the card that has been clicked, if any
   * @param e the mouse event to check
   * @return the card that was clicked or null if no cards were clicked
   */
  public Card cardHasBeenClicked(MouseEvent e) {
    for (int i = 0; i < this.LENGTH; i++) {
      Card c = this.getCardAt(i);
      if ((e.getX() >= c.getX() && e.getX() <= (c.getX() + Card.WIDTH)) &&
          (e.getY() >= c.getY() && e.getY() <= (c.getY() + Card.HEIGHT)) && (!c.faceDown)) {
        return c;
      }
    }
    
    return null;
  }
  
  /**
   * Determines if a card is at a valid location (on another card with appropriate color and face or empty position
   * IF the card is a King) or not
   * @param c the card to determine
   * @return whether the card is at a valid location or not
   */
  public boolean validLocation(Card c) {
    for (int i = 0; i < this.LENGTH; i++) {
      //if (c.getX() >= )
    }
    
    return true;
  }

}
