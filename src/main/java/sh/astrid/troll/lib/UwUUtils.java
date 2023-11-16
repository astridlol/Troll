package sh.astrid.troll.lib;

import java.util.Random;

public class UwUUtils {
    public static String uwuify(String text) {
        String toReturn = text;

        toReturn = replaceCaseInsensitive(toReturn, "\\. ", "~ ");
        toReturn = replaceCaseInsensitive(toReturn, ", ", "~ ");
        toReturn = replaceCaseInsensitive(toReturn, "- ", "~ ");
        toReturn = replaceCaseInsensitive(toReturn, "\\? ", "~ ");
        toReturn = replaceCaseInsensitive(toReturn, "you", "you<3");
        toReturn = replaceCaseInsensitive(toReturn, "r", "w");
        toReturn = replaceCaseInsensitive(toReturn, "l", "w");
        toReturn = replaceCaseInsensitive(toReturn, "uwu", "UWU");
        toReturn = replaceCaseInsensitive(toReturn, "owo", "OWO");
        toReturn = replaceCaseInsensitive(toReturn, ";-;", "(-_-)");
        toReturn = replaceCaseInsensitive(toReturn, "-_-", "(-_-)");
        toReturn = replaceCaseInsensitive(toReturn, ":o", "※(^o^)/※");
        toReturn = replaceCaseInsensitive(toReturn, ":0", "※(^o^)/※");
        toReturn = replaceCaseInsensitive(toReturn, ":\\)", "(｡◕‿‿◕｡)");
        toReturn = replaceCaseInsensitive(toReturn, ":>", "(｡◕‿‿◕｡)");
        toReturn = replaceCaseInsensitive(toReturn, ":\\(", "(︶︹︶)");
        toReturn = replaceCaseInsensitive(toReturn, ":<", "(︶︹︶)");
        toReturn = replaceCaseInsensitive(toReturn, ":3", "(・3・)");
        toReturn = replaceCaseInsensitive(toReturn, ":D", "(ﾉ◕ヮ◕)ﾉ*:・ﾟ✧");
        toReturn = replaceCaseInsensitive(toReturn, "\\._\\.", "(っ´ω`c)");
        toReturn = replaceCaseInsensitive(toReturn, "fuck", "fwick");
        toReturn = replaceCaseInsensitive(toReturn, "shit", "*poops*");

        final String[] SUFFIXES = {
                "~ uwu *nuzzles you*",
                "~ owo whats this",
                "~ *blushes*",
                "~ *hehe*",
                "~ meow",
                "~ owo",
                "~ uwu",
                " ;3",
                "~"
        };

        Random random = new Random();
        toReturn += SUFFIXES[random.nextInt(SUFFIXES.length)];

        return toReturn;
    }

    private static String replaceCaseInsensitive(String input, String target, String replacement) {
        return input.replaceAll("(?i)" + target, replacement);
    }
}
