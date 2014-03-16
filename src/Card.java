import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * Card.java
 * A card has a suit (spade, heart, diamond, club) and a face (ace, 2, 3, ..., 10, Jack, Queen, King).
 * This class provides methods for drawing and constructing a card
 *
 * @author  Jake Wilson
 * @version Mar 15, 2014
 */
public class Card {
  
  private String suit;
  private String face;
  
  private BufferedImage suitImg;
  private BufferedImage faceImg;
  
  private Color color;
  private Font  font;
  
  private int cornerX, cornerY;
  
  private final int height = 200,  width = 120;
  
  public static final String[] SUITS = {"S", "H", "D", "C"};
  public static final String[] FACES = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
  
  /**
   * No-arg constructor that initializes the card to the Ace of Spades
   */
  public Card() {
    this("A", "S");
  }
  
  /**
   * Constructor that suits the card suit to s and the card face to f and the location to (0, 0)
   * @param s the suit of the card
   * @param f the face of the card
   */
  public Card(String s, String f) {
    this(s, f, 0, 0);
  }
  
  /**
   * Constructor that initializes all variables to passed in parameters
   * 
   * @param f the face of the card
   * @param s the suit of the card
   * @param x the top-left x coordinate of the card
   * @param y the top-left y coordinate of the card
   */
  public Card(String f, String s, int x, int y) {
    setFace(f);
    setSuit(s);
    setLocation(x, y);
    if (!initImage()) {
      // TODO: quit the game here somehow
    }
    font = new Font("Courier New", Font.BOLD, 28);
  }
  
  /**
   * Draws the card to a graphics context
   * @param g the graphics context to draw the card on
   */
  public void draw(Graphics g) {
    g.setColor(Color.white);
    g.fillRoundRect(cornerX, cornerY, width, height, 10, 10);
    g.setColor(Color.black);
    g.drawRoundRect(cornerX, cornerY, width, height, 10, 10);
    g.setColor(color);
    g.setFont(font);
    g.drawString(face  , cornerX + (width / 4), cornerY + (height / 2));
    g.drawImage(suitImg, cornerX + (width / 3) + (width / 10), cornerY + (height / 2) - suitImg.getHeight() + 5, null);
  }
  
  /**
   * Initializes the suit and face images of the card depending on what suit and face the card is
   * @return whether the image initialization was successful
   */
  private boolean initImage() {
    boolean success = true;
    try {
      switch (suit) {
      case "S":
        suitImg = ImageIO.read(new File("images/spade.png"));
        color   = Color.black;
        break;
      case "H":
        suitImg = ImageIO.read(new File("images/heart.png"));
        color   = Color.red;
        break;
      case "D":
        suitImg = ImageIO.read(new File("images/diamond.png"));
        color   = Color.red;
        break;
      case "C":
        suitImg = ImageIO.read(new File("images/club.png"));
        color   = Color.black;
        break;
      default: // should be impossible
        success = false;
      }
    } catch (IOException ioex) {
      System.out.println("Error reading image.");
      success = false;
    }
    
    return success;
  }
  
  /**
   * Sets the suit to s if it's valid
   * @param s the new suit
   */
  private void setSuit(String s) {
    if (isValidSuit(s))
      suit = s;
  }
  
  /**
   * Sets the face to s if it's valid
   * @param s the new face
   */
  private void setFace(String s) {
    if (isValidFace(s))
      face = s;
  }
  
  /**
   * Determines if the passed in suit is valid
   * @param s the suit
   * @return whether s is valid
   */
  private boolean isValidSuit(String s) {
    boolean valid = false;
    for (int i = 0; i < SUITS.length; i++) {
      if (s.equalsIgnoreCase(SUITS[i])) {
        valid = true;
        break;
      }
    }
    
    return valid;
  }
  
  /**
   * Determines if the passed in face is valid
   * @param s the face
   * @return whether s is valid
   */
  private boolean isValidFace(String s) {
    boolean valid = false;
    for (int i = 0; i < FACES.length; i++) {
      if (s.equalsIgnoreCase(FACES[i])) {
        valid = true;
        break;
      }
    }
    
    return valid;
  }
  
  /**
   * @return the card suit
   */
  public String getSuit() {
    return suit;
  }
  
  /**
   * @return the card face
   */
  public String getFace() {
    return face;
  }
  
  /**
   * Sets the top left corner of the card to (x, y)
   * @param x the top-left x coordinate of the card
   * @param y the top-left y coordinate of the card
   */
  public void setLocation(int x, int y) {
    cornerX = x;
    cornerY = y;
  }
  
  /**
   * @return the string: "<face> of <suit>"
   */
  public String toString() {
    return face + " of " + suit;
  }

}
