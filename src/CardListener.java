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
      
      // find the pile the selected card was moved from
      for (int i = 0; i < mainPiles.length; i++)
        if (mainPiles[i].getX() == panel.selectedPile.getX())
          origPile = mainPiles[i];
      if (origPile == null) {
        // if it's not a main pile, it has to be a suit pile
        for (int i = 0; i < suitPiles.length; i++)
          if (suitPiles[i].getX() == panel.selectedPile.getX())
            origPile = suitPiles[i];
      }
    } else {
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
          if (mainPiles[i].size() == 0) {
            if (p.getCardOnBottom().getFace().equals("K")) {
              mainPiles[i].addToPile(p);
              origPile.turnTopCardUp();
              validDrop = true;
            }
            break;
          }
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
        }
      }
      
      // TODO: check if it's been dropped on a suit pile
      
      if (!validDrop) {
        //p.setLocation(origX, origY);
        origPile.addToPile(p);
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
    // check the main piles and then the suit piles
    // TODO: move this to it's own method that gets called if this one returns a pile of size 0
    //clicked = deck.cardHasBeenClicked(e);
    for (int i = 0; i < mainPiles.length; i++) {
      if ((clicked = mainPiles[i].pileHasBeenClicked(e)) != null) {
        return clicked;
      }
    }
    
    for (int i = 0; i < suitPiles.length; i++) {
      if ((clicked = suitPiles[i].pileHasBeenClicked(e)) != null) {
        break;
      }
    }
    return clicked;
  }
  
}