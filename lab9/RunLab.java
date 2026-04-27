package lab9;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class RunLab {

    public static void main(String[] args) {

        // =======================================
        // HOW TO USE:
        // 1. To run your program → mode = "APP"
        // 2. To run tests → mode = "TEST"
        // =======================================

        // 🔁 CHANGE THIS LINE ONLY
        String mode = "APP";  // "APP" or "TEST"


        // ANSI Color Constants
        final String RESET = "\u001B[0m";
        final String RED = "\u001B[31m";
        final String GREEN = "\u001B[32m";
        final String YELLOW = "\u001B[33m";
        final String BOLD = "\u001B[1m";


        if (mode.equals("APP")) {
            System.out.println("--- Running Lab9App (Application Mode) ---");
            lab9.Lab9App.main(args);

        } else if (mode.equals("TEST")) {
            System.out.println("\n==============================");
            System.out.println(" RUNNING LAB 9 UNIT TESTS");
            System.out.println("==============================\n");

            Result result = JUnitCore.runClasses(
                    lab9.HandTest.class,
                    lab9.Lab9AppTest.class
                );

            if (result.wasSuccessful()) {
                System.out.println("\n" + GREEN + BOLD + "✅ SUCCESS: All " + result.getRunCount() + " tests passed!" + RESET);
            } else {
                System.out.println("\n" + RED + BOLD + "❌ FAILED: " + result.getFailureCount() + " tests failed." + RESET);
                System.out.println("--------------------------------------------------");

                for (org.junit.runner.notification.Failure failure : result.getFailures()) {
                    System.out.println("\n🚩 TEST: " + failure.getTestHeader());

                    Throwable rootCause = failure.getException();

                    if (rootCause instanceof java.lang.reflect.InvocationTargetException) {
                        rootCause = rootCause.getCause();
                    }

                    if (rootCause instanceof java.lang.NoSuchMethodException) {
                        System.out.println(YELLOW + "   MESSAGE: 🛠️ Missing Method! It looks like you haven't written the method correctly yet." + RESET);
                        System.out.println("   DETAILS: " + rootCause.getMessage());
                    } else if (rootCause instanceof java.lang.NoSuchFieldException) {
                        System.out.println(YELLOW + "   MESSAGE: 🛠️ Missing Variable! Check your variable names." + RESET);
                    } else if (rootCause instanceof AssertionError) {
                        // Standard assertion failure in Red to grab attention
                        System.out.println(RED + "   MESSAGE: " + failure.getMessage() + RESET);
                    } else {
                        // Crashes in Red
                        System.out.println(RED + "   MESSAGE: ⚠️ Your code crashed with a " + rootCause.getClass().getSimpleName() + RESET);
                        System.out.println("   DETAILS: " + rootCause.getMessage());
                    }
                    System.out.println("--------------------------------------------------");
                }
            }
        }
    }
}