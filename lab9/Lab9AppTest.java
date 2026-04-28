package lab9;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class Lab9AppTest {

    private static final String[] EXPECTED_ACE = {
    		"Display the cards in the hand:",
    		"Card 1: 8 of hearts",
    		"Card 2: Jack of spades",
    		"Card 3: 3 of clubs",
    		"Card 4: 9 of spades",
    		"Card 5: Ace of spades",
    		"Card 6: 7 of clubs",
    		"Card 7: 7 of spades",
    		"Get a copy of the cards array, and display the contents of the copy:",
    		"8 of hearts",
    		"Jack of spades",
    		"3 of clubs",
    		"9 of spades",
    		"Ace of spades",
    		"7 of clubs",
    		"7 of spades",
    		"Changing the first card in the copy to the 4 of hearts:",
    		"4 of hearts",
    		"Jack of spades",
    		"3 of clubs",
    		"9 of spades",
    		"Ace of spades",
    		"7 of clubs",
    		"7 of spades",
    		"Display the cards in the hand:",
    		"Card 1: 8 of hearts",
    		"Card 2: Jack of spades",
    		"Card 3: 3 of clubs",
    		"Card 4: 9 of spades",
    		"Card 5: Ace of spades",
    		"Card 6: 7 of clubs",
    		"Card 7: 7 of spades",
    		"Replacing the card in position 0 with a deck card and display the cards in the hand:",
    		"Card 1: Queen of clubs",
    		"Card 2: Jack of spades",
    		"Card 3: 3 of clubs",
    		"Card 4: 9 of spades",
    		"Card 5: Ace of spades",
    		"Card 6: 7 of clubs",
    		"Card 7: 7 of spades",
    		"The hand has the Ace of spades.",
    		"There are 2 face card(s) in the hand.",
    		"Replacing the lowest card with a deck card and display the cards in the hand:",
    		"Card 1: Queen of clubs",
    		"Card 2: Jack of spades",
    		"Card 3: 3 of clubs",
    		"Card 4: 9 of spades",
    		"Card 5: Jack of clubs",
    		"Card 6: 7 of clubs",
    		"Card 7: 7 of spades"};

    private static final String[] EXPECTED_NO_ACE = {
    		"Display the cards in the hand:",
    		"Card 1: 8 of hearts",
    		"Card 2: Jack of spades",
    		"Card 3: 3 of clubs",
    		"Card 4: 9 of spades",
    		"Card 5: 2 of spades",
    		"Card 6: 7 of clubs",
    		"Card 7: 7 of spades",
    		"Get a copy of the cards array, and display the contents of the copy:",
    		"8 of hearts",
    		"Jack of spades",
    		"3 of clubs",
    		"9 of spades",
    		"2 of spades",
    		"7 of clubs",
    		"7 of spades",
    		"Changing the first card in the copy to the 4 of hearts:",
    		"4 of hearts",
    		"Jack of spades",
    		"3 of clubs",
    		"9 of spades",
    		"2 of spades",
    		"7 of clubs",
    		"7 of spades",
    		"Display the cards in the hand:",
    		"Card 1: 8 of hearts",
    		"Card 2: Jack of spades",
    		"Card 3: 3 of clubs",
    		"Card 4: 9 of spades",
    		"Card 5: 2 of spades",
    		"Card 6: 7 of clubs",
    		"Card 7: 7 of spades",
    		"Replacing the card in position 0 with a deck card and display the cards in the hand:",
    		"Card 1: Queen of clubs",
    		"Card 2: Jack of spades",
    		"Card 3: 3 of clubs",
    		"Card 4: 9 of spades",
    		"Card 5: 2 of spades",
    		"Card 6: 7 of clubs",
    		"Card 7: 7 of spades",
    		"The hand does not have the Ace of spades.",
    		"There are 2 face card(s) in the hand.",
    		"Replacing the lowest card with a deck card and display the cards in the hand:",
    		"Card 1: Queen of clubs",
    		"Card 2: Jack of spades",
    		"Card 3: 3 of clubs",
    		"Card 4: 9 of spades",
    		"Card 5: Jack of clubs",
    		"Card 6: 7 of clubs",
    		"Card 7: 7 of spades"};


    private static class Captured {
        final String[] rawLines;
        final String[] normalizedLines;
        Captured(String[] raw, String[] norm) { rawLines = raw; normalizedLines = norm; }
    }

    /** Run Lab9App.main and capture output lines (raw and whitespace-normalized). */
    private Captured captureOutputLines(boolean ace) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bout));
        try {
        	if(ace) {
        		Deck.test1 = true;
        		Deck.test2 = false;

        		Lab9App.main(new String[0]);
        	}
        	else {
        		Deck.test2 = true;
        		Deck.test1 = false;

        		Lab9App.main(new String[0]);
       		
        	}
        } finally {
            System.setOut(originalOut);
        }

        String out = bout.toString();
        String[] raw = out.split("\\R", -1); // keep trailing blanks
        List<String> rawList = new ArrayList<>();
        for (String s : raw) {
            String trimmed = s;
            if (trimmed != null && !trimmed.trim().isEmpty()) {
                rawList.add(trimmed);
            }
        }
        String[] rawLines = rawList.toArray(new String[0]);

        String[] norm = new String[rawLines.length];
        for (int i = 0; i < rawLines.length; i++) {
            norm[i] = rawLines[i].toLowerCase().replaceAll("\\s+", "");
        }
        return new Captured(rawLines, norm);
    }

    private String normalizeExpectedAce(int idx) {
        return EXPECTED_ACE[idx].toLowerCase().replaceAll("\\s+", "");
    }

    private String normalizeExpectedNoAce(int idx) {
        return EXPECTED_NO_ACE[idx].toLowerCase().replaceAll("\\s+", "");
    }

    private void assertRange(int fromInclusive, int toInclusive, boolean ace) {
        Captured c = captureOutputLines(ace);	
        String[] raw = c.rawLines;
        String[] norm = c.normalizedLines;

        for (int i = fromInclusive; i <= toInclusive; i++) {
            if(ace) {
                int lineIndex = i; 
                if (lineIndex >= norm.length) {
                    fail("Missing output line " + (lineIndex + 1) + ". Expected: \"" + EXPECTED_ACE[lineIndex] + "\"");
                }

                String expectedNormAce = normalizeExpectedAce(lineIndex);
            	String actualNorm = norm[lineIndex];
            	if (!expectedNormAce.equals(actualNorm)) {
            		String actualRaw = raw[lineIndex];
            		fail("Line " + (lineIndex + 1) + " mismatch.\nExpected: \"" + EXPECTED_ACE[lineIndex] + "\"\nBut was: \"" + actualRaw + "\"");
            	}
            }
            else {
                int lineIndex = i; 
                if (lineIndex >= norm.length) {
                    fail("Missing output line " + (lineIndex + 1) + ". Expected: \"" + EXPECTED_NO_ACE[lineIndex] + "\"");
                }

            	String expectedNormNoAce = normalizeExpectedNoAce(lineIndex);
            	String actualNorm = norm[lineIndex];
            	if (!expectedNormNoAce.equals(actualNorm)) {
            		String actualRaw = raw[lineIndex];
            		fail("Line " + (lineIndex + 1) + " mismatch.\nExpected: \"" + EXPECTED_NO_ACE[lineIndex] + "\"\nBut was: \"" + actualRaw + "\"");
            	}
            	
            }
        }
    }

    // --- tests 
	@Test
	public void test01_MethodCallCount()
	{
		String[] args = new String[0];

		Lab9App.main(args);
		assertEquals("The dealCard method should be called exactly 9 times in this lab.  Check if you are calling dealCard too few or too many times.", 9, Deck.cardCounter);
	}


    @Test
    public void test02_Lines1to8() {
        // lines 1 - 8 correspond to EXPECTED[0..7]
        assertRange(0, 7, true);
    }

    @Test
    public void test03_Lines9to16() {
        assertRange(8, 15, true);
    }

    @Test
    public void test04_Lines17to24() {
        assertRange(16, 23, true);
    }

    @Test
    public void test05_Lines25to32() {
        assertRange(24, 31, true);
    }

    @Test
    public void test06_Lines33to40() {
        assertRange(32, 39, true);
    }

    @Test
    public void test07_Lines41to42_with_ace() {
        assertRange(40, 41, true);
    }

    @Test
    public void test08_Lines41to42_without_ace() {
        assertRange(40, 41, false);
    }

    @Test
    public void test09_Lines43to50() {
        assertRange(42, 49, true);
    }
}
