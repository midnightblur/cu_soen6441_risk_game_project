/* 
 * Risk Game Team 2
 * Card.java
 * Version 1.0
 * Oct 18, 2017
 */
package shared_resources.game_entities;

/**
 * Card class contains all the different types that are available of cards in the deck in
 * cardType vector. Card type 1 is Infantry, type 2 is Cavalry, and type 3 is Artillery.
 */
public class Card {
    
    // region Attributes declaration
    
    /** The card type. */
    private CARD_TYPE cardType;
    
    /**
     * The Card sets the cardType of a card in the deck according to the index.
     *
     * @param cardType the card type
     */
    public Card(CARD_TYPE cardType) {
        this.cardType = cardType;
    }
    // endregion
    
    // region Constructors
    
    /**
     * Gets the types count.
     *
     * @return the count of card types
     */
    public static int getTypesCount() {
        return CARD_TYPE.values().length;
    }
    // endregion
    
    // region Getters & Setters
    
    /**
     * Override equals method to check if two cards are the same, depending on the
     * types of those cards.
     *
     * @param other The other card object to compare with.
     *
     * @return Boolean index that says whether or not the two objects are equal
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Card)) {
            return false;
        }
        
        Card card = (Card) other;
        if (cardType == card.getCardType()) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets the card type.
     *
     * @return the card type
     */
    public CARD_TYPE getCardType() {
        return this.cardType;
    }
    // endregion
    
    // region Public Methods
    
    /** The Enum CARD_TYPE. */
    public enum CARD_TYPE {
        INFANTRY,
        CAVALRY,
        ARTILLERY
    }
    // endregion
}
