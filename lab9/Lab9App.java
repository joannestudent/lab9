package lab9;

/**
 * <p>Title: The Lab9App class</p>
 *
 * <p>Description: COMPLETE THIS</p>
 * 
 * @author your name(s) here
 */
public class Lab9App {

  public static void main(String[] args)
  {
    // create and shuffle the deck
    Deck theDeck = new Deck();
    theDeck.shuffleDeck();

    // instantiate a Hand object
    Hand myHand = new Hand();

    // insert 7 cards in the hand
    Card theCard;
    for (int i = 0; i < 7; i++)
    {
      theCard = theDeck.dealCard();
      myHand.insertCard(theCard);
    }
  }

}
