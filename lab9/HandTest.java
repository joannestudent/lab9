package lab9;

import org.junit.*;
import java.lang.reflect.*;
import static org.junit.Assert.*;

import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class HandTest {
  private static boolean headersValid = true;


  // ----------------------
  // 1. Method header check
  // ----------------------
  @Test
  public void test01_MethodHeaders() {
    String[] expectedSignatures = { "public java.lang.String lab9.Hand.toString()",
        "public lab9.Card[] lab9.Hand.getCards()",
              "public void lab9.Hand.replaceCard(int,lab9.Card)",
              "public int lab9.Hand.findNumFaceCards()",
              "public boolean lab9.Hand.searchCard(lab9.Card)",
              "public int lab9.Hand.findLowCard()",
              "public void lab9.Hand.replaceLowCard(lab9.Card)" };

    String[] methodNames = { "toString", "getCards", "replaceCard", "findNumFaceCards", 
        "searchCard", "findLowCard", "replaceLowCard" };

    try {
      Method[] methods = Hand.class.getDeclaredMethods();
      java.util.List<String> allMethods = new java.util.ArrayList<>();
      for (Method m : methods) {
        allMethods.add(m.toString());
      }

      for (int i = 0; i < expectedSignatures.length; i++) {
        if (!allMethods.contains(expectedSignatures[i])) {
          headersValid = false;
          fail("❌ The method header for " + methodNames[i]
              + " is incorrect. Check the return type, name, and parameters.");
        }
      }

    } catch (Throwable e) {
      headersValid = false;
      fail("❌ Unexpected reflection error while checking method headers: " + e);
    }
  }

    // ----------------------
    // 2. Instance variables
    // ----------------------
    @Test
    public void test02_InstanceVariables() throws Exception {
        Hand hand = new Hand();

        Field[] fields = Hand.class.getDeclaredFields();
        assertEquals("Hand should have exactly 2 instance variables.", 2, fields.length);

        Field cardsField = Hand.class.getDeclaredField("cards");
        cardsField.setAccessible(true);
        Card[] cards = (Card[]) cardsField.get(hand);
        assertEquals("cards array should have size 10.", 10, cards.length);

        Field numCardsField = Hand.class.getDeclaredField("numCards");
        numCardsField.setAccessible(true);
        int numCards = (Integer) numCardsField.get(hand);
        assertEquals("numCards should initialize to 0.", 0, numCards);
    }

    // ----------------------
    // Helper setup
    // ----------------------
    private Hand setupHand(Card[] cards, int numCards) throws Exception {
    // instantiate a Hand object
    Class<?> c = Class.forName("lab9.Hand");
      Hand hand = (Hand)c.getDeclaredConstructor().newInstance(); 		    

        Field cardsField = Hand.class.getDeclaredField("cards");
        cardsField.setAccessible(true);
        cardsField.set(hand, cards);

        Field numCardsField = Hand.class.getDeclaredField("numCards");
        numCardsField.setAccessible(true);
        numCardsField.set(hand, numCards);

        return hand;
    }

    // ----------------------
    // 3. findLowCard
    // ----------------------
    @Test
    public void test03_FindLowCard() throws Exception {
        Card[] cards = {
          // 8 of hearts
          // 11 of spades
          // 1 of hearts
          // 12 of hearts
          // 1 of spades
          // 7 of clubs
          // 7 of spades
            new Card(34), new Card(50), new Card(27),
            new Card(38), new Card(40), new Card(7),
            new Card(46), null, null, null
        };

        Hand hand = setupHand(cards, 7);

    // call findLowCard
    Method method = hand.getClass().getMethod("findLowCard", (Class<?>[])null);
    int lowCard = (Integer)method.invoke(hand);

        assertEquals(2, lowCard);

        // swap positions
        Card temp = cards[0];
        // 1 of hearts
        cards[0] = cards[2];
        // 8 of hearts
        cards[2] = temp;

        // call findLowCard
        lowCard = (Integer)method.invoke(hand);

        assertEquals(0, lowCard);

    temp = cards[0];
      // 7 of spades
    cards[0] = cards[6];

    cards[6] = temp;
    cards[4] = new Card(45);

        // call findLowCard
        lowCard = (Integer)method.invoke(hand);
        assertEquals(6, lowCard);
    }

    // ----------------------
    // 4. findNumFaceCards
    // ----------------------
    @Test
    public void test04_FindNumFaceCards() throws Exception {
        Card[] cards = {
            new Card(34), new Card(50), new Card(3),
            new Card(38), new Card(40), new Card(10),
            new Card(46), null, null, null
        };

        Hand hand = setupHand(cards, 7);

    // call findNumFaceCards
    Method method = hand.getClass().getMethod("findNumFaceCards", (Class<?>[])null);
    int numFaceCards = (Integer)method.invoke(hand);

        assertEquals("Should detect 2 face cards.", 2, numFaceCards);
    }

    // ----------------------
    // 5. getCards (deep copy)
    // ----------------------
    @Test
    public void test05_GetCards() throws Exception {
        Card[] cards = {
            new Card(34), new Card(50), new Card(3),
            new Card(38), new Card(40), new Card(7),
            new Card(46), null, null, null
        };

        Hand hand = setupHand(cards, 7);

    // call getCards
    Method method = hand.getClass().getMethod("getCards", (Class<?>[])null);
    Card[] copy = (Card[])method.invoke(hand);

        assertNotSame("Should return a copy, not same reference.", cards, copy);
        assertEquals("Returned array size should match numCards.", 7, copy.length);

        for (int i = 0; i < 7; i++) {
            assertTrue("Card mismatch at index " + i,
                cards[i].equalCard(copy[i]));
        }
    }

    // ----------------------
    // 6. replaceCard
    // ----------------------

    @Test
    public void test06_ReplaceCard() throws Exception {
        Card[] cards = {
            new Card(34), new Card(49), new Card(3),
            new Card(38), new Card(40), new Card(7),
            new Card(46), null, null, null
        };

        Hand hand = setupHand(cards, 7);

    // check bounds
    Method method = hand.getClass().getMethod("replaceCard", int.class, Card.class);
    method.invoke(hand, -1, new Card(0));
    method.invoke(hand, 7, new Card(0));

    // put 2 of diamonds in position 2
    Card twoDiamonds = new Card(15);
    method.invoke(hand, 2, twoDiamonds);

    // call getCards
    Method method2 = hand.getClass().getMethod("getCards", (Class<?>[])null);
    Card[] updated = (Card[])method2.invoke(hand);

        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in.", updated[2].equalCard(twoDiamonds));

        // ensure others unchanged
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[0].equalCard(cards[0]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[1].equalCard(cards[1]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[3].equalCard(cards[3]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[4].equalCard(cards[4]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[5].equalCard(cards[5]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[6].equalCard(cards[6]));

        // 6 of hearts at position 0
    // put 6 of hearts in position 0
        Card sixHearts = new Card(32);
    method.invoke(hand, 0, sixHearts);

    // call getCards
    method2 = hand.getClass().getMethod("getCards", (Class<?>[])null);
    updated = (Card[])method2.invoke(hand);

        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in.", updated[0].equalCard(sixHearts));

        // ensure others unchanged
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[1].equalCard(cards[1]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[2].equalCard(cards[2]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[3].equalCard(cards[3]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[4].equalCard(cards[4]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[5].equalCard(cards[5]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[6].equalCard(cards[6]));

        // 7 of hearts at position 6
    // put 7 of hearts in position 0
        Card sevenHearts = new Card(33);
    method.invoke(hand, 6, sevenHearts);

    // call getCards
    method2 = hand.getClass().getMethod("getCards", (Class<?>[])null);
    updated = (Card[])method2.invoke(hand);

        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in.", updated[6].equalCard(sevenHearts));

        // ensure others unchanged
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[0].equalCard(cards[0]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[1].equalCard(cards[1]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[2].equalCard(cards[2]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[3].equalCard(cards[3]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[4].equalCard(cards[4]));
        assertTrue("The replaceCard method should replace the Card at the array position passed in with the new Card reference passed in. The rest of the cards array should remain unchanged.", updated[5].equalCard(cards[5]));

    }

    // ----------------------
    // 7. replaceLowCard
    // ----------------------
    @Test
    public void test07_ReplaceLowCard() throws Exception {
    Card[] cards = {new Card(34), new Card(50), new Card(3), 
        new Card(38), new Card(40),new Card(7), 
        new Card(46), null, null, null};

        Hand hand = setupHand(cards, 7);

    // call replaceLowCard to replace the Ace of spaces in position 4 with 9 of clubs
    Card nineClubs = new Card(9);
    Method method = hand.getClass().getMethod("replaceLowCard", Card.class);
    method.invoke(hand, nineClubs);

    // call getCards
    Method method2 = hand.getClass().getMethod("getCards", (Class<?>[])null);
    Card[] updated = (Card[])method2.invoke(hand);

    assertTrue("The replaceLowCard method should find the position of the lowest card in the hand and replace the card at that position with the Card passed into the method.",updated[4].equalCard(nineClubs));

    // ensure others unchanged
        assertTrue("The replaceLowCard method should find the position of the lowest card in the hand and replace the card at that position with the Card passed into the method. The rest of the cards array should remain unchanged.", updated[0].equalCard(cards[0]));
        assertTrue("The replaceLowCard method should find the position of the lowest card in the hand and replace the card at that position with the Card passed into the method. The rest of the cards array should remain unchanged.", updated[1].equalCard(cards[1]));    
        assertTrue("The replaceLowCard method should find the position of the lowest card in the hand and replace the card at that position with the Card passed into the method. The rest of the cards array should remain unchanged.", updated[2].equalCard(cards[2]));    
        assertTrue("The replaceLowCard method should find the position of the lowest card in the hand and replace the card at that position with the Card passed into the method. The rest of the cards array should remain unchanged.", updated[3].equalCard(cards[3]));    
        assertTrue("The replaceLowCard method should find the position of the lowest card in the hand and replace the card at that position with the Card passed into the method. The rest of the cards array should remain unchanged.", updated[5].equalCard(cards[5]));    
        assertTrue("The replaceLowCard method should find the position of the lowest card in the hand and replace the card at that position with the Card passed into the method. The rest of the cards array should remain unchanged.", updated[6].equalCard(cards[6]));    
     }


    // ----------------------
    // 8. searchCard
    // ----------------------
    @Test
    public void test08_SearchCard() throws Exception {
        Card[] cards = {
            new Card(34), new Card(50), new Card(3),
            new Card(38), new Card(40), new Card(7),
            new Card(46), null, null, null
        };

        Hand hand = setupHand(cards, 7);

        // look for the queen of hearts
    Method method = hand.getClass().getMethod("searchCard", Card.class);
    boolean found = (Boolean)method.invoke(hand, new Card(38));

        assertTrue("The searchCard method should return true if the card passed into the method is in the Hand.", found);

    // look for the 6 of hearts
        found = (Boolean)method.invoke(hand, new Card(32));

    assertFalse("The searchCard method should return false if the card passed into the method is not in the Hand.", found);
    }

    // ----------------------
    // 9. toString
    // ----------------------
    @Test
    public void test09_ToString() throws Exception {
        Card[] cards = {
            new Card(34), new Card(50), new Card(3),
            new Card(38), new Card(40), new Card(7),
            new Card(46), null, null, null
        };

        Hand hand = setupHand(cards, 7);

    // call toString
    Method method = hand.getClass().getMethod("toString",(Class<?>[])null);
    String str = (String)method.invoke(hand);
    str = str.toLowerCase().replaceAll("\\s+", "");

    String correct = "Card 1: 8 of hearts\nCard 2: Jack of spades\nCard 3: 3 of clubs\nCard 4: Queen of hearts\nCard 5: Ace of spades\nCard 6: 7 of clubs\nCard 7: 7 of spades";
    correct = correct.toLowerCase().replaceAll("\\s+", "");

        assertEquals("The toString method should output 7 cards in the hand preceded by 'Card 1:', Card 2:', etc. See expected output from the lab handout.", correct, str);
    }

}