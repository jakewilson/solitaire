import java.util.ArrayList;


/**
 * Pile.java
 * This class represents a pile of cards in solitaire. Unlike the deck, a pile does not
 * necessarily contain 52 cards, it can hold anywhere from 0 to 52 cards. Piles can have cards
 * added and removed at runtime.
 *
 * @author  Jake Wilson
 * @version Mar 17, 2014
 */
public class Pile {
  
  private ArrayList<Card> pile;
  
  private int xLoc, yLoc;
  
  public static final int VERT_DISPL = 5; // vertical displacement between cards in the same pile

}
