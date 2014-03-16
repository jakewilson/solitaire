import java.awt.Graphics;
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
   * No-arg constructor that adds 52 cards to the deck
   */
  public Deck() {
    deck = new ArrayList<Card>();
    for (int i = 0; i < Card.SUITS.length; i++) {
      for (int j = 0; j < Card.FACES.length; j++) {
        deck.add(new Card(Card.FACES[j], Card.SUITS[i]));
      }
    }
    try {
      
      printDeck();
      System.out.println();
      shuffleDeck();
      printDeck();
      System.out.println("size: " + deck.size());
    } catch (Exception e) {}

//    for (int i = 0; i < LENGTH; i++) {
//      for (int j = 0; j < LENGTH; j++) {
//        if (getCardAt(i).equals(getCardAt(j))) {
//          System.out.println(i + "uh oh...");
//        }
//      }
//    }
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
   * Shuffles the deck of cards. Private until I find a reason to make it public
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
    removeCardAt(i1);
    insertCardAt(i1, this.getCardAt(i2 - 1));
    removeCardAt(i2);
    insertCardAt(i2, temp);
  }
  
  /**
   * Returns the card at the specified index
   * @param index the index of the card to return
   * @return the card at the index or null if the index is out of bounds
   */
  public Card getCardAt(int index) {
    if (validIndex(index)) {
      return deck.get(index);
    }
    
    return null;
  }
  
  /**
   * Removes the card at the specified index if the index is not out of bounds
   * @param index the index of the card to remove
   */
  private void removeCardAt(int index) {
    if (validIndex(index)) {
      deck.remove(index);
    }
  }
  
  /**
   * Inserts a card c at the specified index
   * @param index the index to insert c at
   * @param c the card to insert
   */
  private void insertCardAt(int index, Card c) {
    if (validIndex(index)) {
      deck.add(index, c);
    }
  }
  
  /**
   * Returns whether i is a valid index in the deck
   * @param i the index to check
   * @return whether i is in bounds
   */
  private boolean validIndex(int i) {
    return (i >= 0 && i < this.LENGTH);
  }
  
  /**
   * Prints the deck for debugging purposes
   */
  private void printDeck() {
    for (int i = 0; i < this.LENGTH; i++) {
      System.out.println(this.getCardAt(i));
    }
  }

}
