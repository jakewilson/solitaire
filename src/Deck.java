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
   * The location of the deck at (xLoc, yLoc)
   */
  private int xLoc, yLoc;
  
  /**
   * No-arg constructor that adds 52 cards to the deck and shuffles it
   */
  public Deck() {
    deck = new ArrayList<Card>();
    for (int i = 0; i < Card.SUITS.length; i++) {
      for (int j = 0; j < Card.FACES.length; j++) {
        deck.add(new Card(Card.FACES[j], Card.SUITS[i]));
        //deck.add(new Card(Card.FACES[j % 4], Card.SUITS[i % 2]));
      }
    }
    
    shuffleDeck();
  }
  
  /**
   * Draws each card in the deck
   * @param g the graphics context to draw the deck on
   */
  public void draw(Graphics g) {
    for (int i = 0; i < deck.size(); i++) {
      this.getCardAt(i).draw(g);
    }
  }
  
  /**
   * Shuffles the deck of cards
   */
  private void shuffleDeck() {
    for (int i = 0; i < deck.size(); i++) {
      int index = (int)(Math.random() * deck.size());
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
  public void removeCardAt(int index) {
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
   * Returns whether i is a valid index in the deck
   * @param i the index to check
   * @return whether i is in bounds
   */
  private boolean withinBounds(int i) {
    return (i >= 0 && i < deck.size());
  }
  
  /**
   * Prints the deck for debugging purposes
   */
  private void printDeck() {
    for (int i = 0; i < deck.size(); i++) {
      System.out.println(i + ": " + this.getCardAt(i));
    }
  }
  
  /**
   * Returns the card that has been clicked, if any
   * @param e the mouse event to check
   * @return the card that was clicked or null if no cards were clicked
   */
  public boolean hasBeenClicked(MouseEvent e) {
    if ((e.getX() >= xLoc && e.getX() <= xLoc + Card.WIDTH) && 
        (e.getY() >= yLoc && e.getY() <= yLoc + Card.HEIGHT))
      return true;
    
    return false;
  }
  
  /**
   * Wrapper class for ArrayList.size()
   * @return the size of the deck of cards
   */
  public int size() {
    return deck.size();
  }
  
  /**
   * Returns the card at the top of the deck (index size() - 1)
   * @return the top card
   */
  public Card getCardOnTop() {
    if (size() > 0)
      return this.getCardAt(size() - 1);
    
    return null;
  }
  
  /**
   * Removes the card at the top of the deck
   */
  public void removeCardOnTop() {
    if (size() > 0) 
      this.removeCardAt(size() - 1);
  }
  
  /**
   * Sets the location of the deck to (x, y). Doing so also sets every card in the deck to this location.
   * @param x the x coordinate of the deck
   * @param y the y coordinate of the deck
   */
  public void setLocation(int x, int y) {
    xLoc = x; yLoc = y;
    for (int i = 0; i < deck.size(); i++) {
      deck.get(i).setLocation(x,  y);
    }
  }
  
  /**
   * Returns the x location of the deck
   * @return the x location of the deck
   */
  public int getX() {
    return xLoc;
  }
  
  /**
   * Returns the y location of the deck
   * @return the y location of the deck
   */
  public int getY() {
    return yLoc;
  }
  
  /**
   * Turns all cards in the deck face down
   */
  public void turnAllCardsDown() {
    for (int i = 0; i < this.size(); i++) {
      deck.get(i).faceDown = true;
    }
  }
  
  /**
   * Adds a card to the deck
   * @param c the card to add
   */
  private void addToDeck(Card c) {
    this.deck.add(c);
    c.setLocation(xLoc, yLoc);
  }
  
  /**
   * Adds the pile p to the deck and removes the cards from p only if the deck has no cards in it
   * @param p the pile to add
   */
  public void addToDeck(Pile p) {
    if (this.size() == 0) {
      while (!p.isEmpty()) {
        this.addToDeck(p.getCardAt(p.size() - 1));
        p.removeCardAt(p.size() - 1);
      }
      turnAllCardsDown();
    }
  }

}
