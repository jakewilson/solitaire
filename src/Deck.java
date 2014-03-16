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
  
  private final int LENGTH = 52;
  
  /**
   * No-arg constructor that adds 52 cards to the deck
   */
  public Deck() {
    deck = new ArrayList<Card>();
    for (int i = 0; i < LENGTH; i++) {
      deck.add(new Card(Card.FACES[i % 13], Card.SUITS[i % 4]));
      System.out.println(i + ": " + deck.get(i));
    }
  }

}
