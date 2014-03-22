import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;


/**
 * CardListener.java
 *
 * @author  Jake
 * @version Mar 18, 2014
 */
public class CardListener extends MouseInputAdapter {
  
  private GamePanel panel;
  
  private Deck deck;
  
  private Pile[] mainPiles, suitPiles;
  private Pile   deckPile; // the pile of the cards the user has drawn from the deck
  
  private Pile origPile;
  
  private int lastX, lastY;
  
  /**
   * Constructor for a Card Listener
   * @param panel the game panel in which to manipulate when the user clicks/drags/drops cards
   */
  public CardListener(GamePanel panel) {
    this.panel   = panel;
    deck = panel.getDeck();
    mainPiles = panel.getMainPiles();
    suitPiles = panel.getSuitPiles();
    deckPile  = panel.getDeckPile();
    lastX = 0;
    lastY = 0;
    origPile = null;
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    
  }
  
  @Override
  /**
   * Selects a card when it is clicked
   */
  public void mousePressed(MouseEvent e) {
    panel.selectedPile = getPileClicked(e);
    if (panel.selectedPile != null) {
      lastX = e.getX();
      lastY = e.getY();
    } else { // if no pile was clicked, check if the deck was
      if (deck.hasBeenClicked(e)) {
        if (deck.size() == 0) {
          deck.addToDeck(deckPile);
        } else {
          for (int i = 0; i < 3; i++) {
            Card c = deck.getCardOnTop();
            if (c != null) {
              deckPile.addToPile(c);
              deck.removeCardOnTop();
            }
          }
          deckPile.turnAllCardsUp();
        }
      }
      return;
    }
    panel.repaint();
  }
  
  @Override
  /**
   * Moves the card as it is dragged by the mouse
   */
  public void mouseDragged(MouseEvent e) {
    if (panel.selectedPile != null) {
      int newX = panel.selectedPile.getX() + (e.getX() - lastX);
      int newY = panel.selectedPile.getY() + (e.getY() - lastY);
      panel.selectedPile.setLocation(newX, newY);
      lastX = e.getX();
      lastY = e.getY();
    }
    panel.repaint();
  }
  
  @Override
  /**
   * Drops a card on a pile only if it has the right face and color
   */
  public void mouseReleased(MouseEvent e) {
    Pile p = panel.selectedPile;
    if (p != null) {
      boolean validDrop = false;
      // check to see if the selectedPile has been dropped on a main pile
      for (int i = 0; i < mainPiles.length; i++) {
        if (mainPiles[i].droppedOnPile(p)) {
          if (mainPiles[i].isEmpty()) {
            if (p.getCardOnBottom().getFace().equals("K")) {
              mainPiles[i].addToPile(p);
              origPile.turnTopCardUp();
              validDrop = true;
            }
          } else { // if not empty
            // only add if the colors are NOT the same
            if (!p.getCardOnBottom().getColor().equals(mainPiles[i].getCardOnTop().getColor())) {
              // now ensure the faces are descending
              if (Card.getFaceIndex(p.getCardOnBottom().getFace()) + 1 == Card.getFaceIndex(mainPiles[i].getCardOnTop().getFace())) {
                mainPiles[i].addToPile(p);
                origPile.turnTopCardUp();
                validDrop = true;
                break;
              }
            }
          } // end isEmpty() condition
        }
      }
      
      // if the drop is still invalid, check if it's been dropped on a suit pile instead
      if (!validDrop) {
        for (int i = 0; i < suitPiles.length; i++) {
          if (suitPiles[i].droppedOnPile(p)) {
            if (suitPiles[i].isEmpty()) {
              if (p.size() == 1) {
                if (p.getCardOnBottom().getFace().equals("A")) {
                  suitPiles[i].addToPile(p.getCardOnBottom());
                  origPile.turnTopCardUp();
                  validDrop = true;
                }
              }
            } else {
              if (p.size() == 1) { // only single cards can be added to suit piles
                // the suits must be the same for cards being added
                if (Card.getSuitIndex(p.getCardOnBottom().getSuit()) == Card.getSuitIndex(suitPiles[i].getCardOnTop().getSuit())) {
                  // the faces must be in ascending order
                  if (Card.getFaceIndex(p.getCardOnBottom().getFace()) == Card.getFaceIndex(suitPiles[i].getCardOnTop().getFace()) + 1) {
                    suitPiles[i].addToPile(p.getCardOnBottom());
                    origPile.turnTopCardUp();
                    validDrop = true;
                  }
                }
              }
            } // end isEmpty() condition
            
            
          }
        }
      }
      
      if (!validDrop) {
        if (p.size() == 1) {
          origPile.addToPile(p.getCardOnBottom());
        } else {
          origPile.addToPile(p);
        }
      }
    }
    
    panel.selectedPile = null;
    origPile = null;
    panel.repaint();
  }
  
  @Override
  public void mouseMoved(MouseEvent e) {
    
  }
  
  /**
   * Returns the card that was clicked or null if no card was clicked
   * @param e the mouse event to check
   * @return the card that was clicked or null if no card was clicked
   */
  private Pile getPileClicked(MouseEvent e) {
    Pile clicked = null;
    origPile     = null;
    // check the main piles and then the suit piles
    for (int i = 0; i < mainPiles.length; i++) {
      if ((clicked = mainPiles[i].pileHasBeenClicked(e)) != null) {
        origPile = mainPiles[i];
        return clicked;
      }
    }
    
    for (int i = 0; i < suitPiles.length; i++) {
      if ((clicked = suitPiles[i].pileHasBeenClicked(e)) != null) {
        origPile = suitPiles[i];
        break;
      }
    }
    return clicked;
  }
  
}