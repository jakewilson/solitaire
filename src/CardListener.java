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
  
  private boolean cardPressed;
  private Deck deck;
  
  private Pile[] mainPiles, suitPiles;
  
  private int lastX, lastY;
  private int origX, origY;
  
  /**
   * Constructor for a Card Listener
   * @param panel the game panel in which to manipulate when the user clicks/drags/drops cards
   */
  public CardListener(GamePanel panel) {
    this.panel   = panel;
    cardPressed  = false;
    deck = panel.getDeck();
    mainPiles = panel.getMainPiles();
    suitPiles = panel.getSuitPiles();
    lastX = 0;
    lastY = 0;
    origX = 0;
    origY = 0;
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    
  }
  
  @Override
  /**
   * Selects a card when it is clicked
   */
  public void mousePressed(MouseEvent e) {
    panel.selectedCard = getCardClicked(e);
    if (panel.selectedCard != null) {
      cardPressed = true;
      lastX = e.getX();
      lastY = e.getY();
      origX = panel.selectedCard.getX();
      origY = panel.selectedCard.getY();
    } else {
      return;
    }
    panel.repaint();
  }
  
  @Override
  /**
   * Drops a card on a pile only if it has the right face and color
   */
  public void mouseReleased(MouseEvent e) {
    Card c = panel.selectedCard;
    // TODO: add functionality for suit piles as well
    // determines if a card was dropped on a pile. If the card is dropped on
    // two piles, it will choose the left most one
    if (c != null) {
      boolean validDrop = false;
      for (int i = 0; i < mainPiles.length; i++) {
        if (mainPiles[i].cardDroppedOnPile(c)) {
          if (c.getMainPileNum() >= 0) {
            if (mainPiles[i].size() == 0) { // a card may be added to an empty main pile only if it's a king
              if (!c.getFace().equals("K")) {
                break;
              }
            } else {
              // a card may only be added to a pile in red - black - red - black order
              // black may not be added to black or red to red
              Card top = mainPiles[i].getCardOnTop();
              if (!(top.getColor().equals(Color.red)   && c.getColor().equals(Color.black)) &&
                  !(top.getColor().equals(Color.black) && c.getColor().equals(Color.red))) {
                break;
              } else if (Card.getFaceIndex(top.getFace()) != Card.getFaceIndex(c.getFace()) + 1) {
                // the selected cards face must also be the next index lower than the face at the top of the pile
                break;
              }
            }
            mainPiles[c.getMainPileNum()].removeCard(c);
            mainPiles[i].addCardToPile(c);
            c.setMainPileNum(i);
            c.setSuitPileNum(-1);
            validDrop = true;
            break;
          }
        }
      }
      // if the card isn't dropped on a pile, move it back to where it was picked up from, check if it
      // was dropped on a suit pile
      if (!validDrop) {
        for (int i = 0; i < suitPiles.length; i++) {
          if (suitPiles[i].cardDroppedOnPile(c)) {
            // if a card is added to an empty suit pile, it must be an ace
            if (suitPiles[i].size() == 0) {
              if (!c.getFace().equals("A"))
                break;
              else if (c.getSuitPileNum() > -1) // the card is already part of a suit pile - don't add it to another one
                break;
            } else {
              Card topCard = suitPiles[i].getCardOnTop();
              // cards must be the same suit and faces must be in ascending order
              if (!topCard.getSuit().equals(c.getSuit()))
                break;
              else if (Card.getFaceIndex(c.getFace()) != Card.getFaceIndex(topCard.getFace()) + 1) {
                break;
              }
            }
            
            mainPiles[c.getMainPileNum()].removeCard(c);
            suitPiles[i].addCardToPile(c);
            c.setMainPileNum(-1);
            c.setSuitPileNum(i);
            validDrop = true;
            break;
          }
        }
      }
      
      if (!validDrop) {
        c.setLocation(origX, origY);
      }
    }
  
    cardPressed  = false;
    c = null;
    panel.repaint();
  }
  
  @Override
  public void mouseMoved(MouseEvent e) {
    
  }
  
  @Override
  /**
   * Moves the card as it is dragged by the mouse
   */
  public void mouseDragged(MouseEvent e) {
    if (cardPressed && panel.selectedCard != null) {
      int newX = panel.selectedCard.getX() + (e.getX() - lastX);
      int newY = panel.selectedCard.getY() + (e.getY() - lastY);
      panel.selectedCard.setLocation(newX, newY);
      lastX = e.getX();
      lastY = e.getY();
    }
    panel.repaint();
  }
  
  /**
   * Returns the card that was clicked or null if no card was clicked
   * @param e the mouse event to check
   * @return the card that was clicked or null if no card was clicked
   */
  private Card getCardClicked(MouseEvent e) {
    Card clicked = null;
    // check if the card is in the deck, then the main piles, then the suit piles
    clicked = deck.cardHasBeenClicked(e);
    if (clicked == null) {
      for (int i = 0; i < mainPiles.length; i++) {
        if ((clicked = mainPiles[i].cardHasBeenClicked(e)) != null)
          break;
      }
      if (clicked == null) {
        for (int i = 0; i < suitPiles.length; i++) {
          if ((clicked = suitPiles[i].cardHasBeenClicked(e)) != null)
            break;
        }
      }
    }
    
    return clicked;
  }
  
}