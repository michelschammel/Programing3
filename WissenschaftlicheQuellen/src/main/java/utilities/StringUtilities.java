package utilities;

public class StringUtilities {

    public static boolean doesStringContain(String firstString, String secondString) {
        String firstStringLower = firstString.toLowerCase();
        String secondStringLower = secondString.toLowerCase();

        return firstStringLower.contains(secondStringLower);
    }
}
