import types.ScoreboardEntry;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardHandler {

    public ArrayList<ScoreboardEntry> generateCreateScoreboards() throws IOException {
        URL url = this.getClass().getClassLoader().getResource("stats");
        File file = new File(url.getPath());

        List<String> statFileLines = Files.readAllLines(file.toPath());

        ArrayList<ScoreboardEntry> scoreboardEntries = new ArrayList<>();
        int id = 1;
        for (String stat : statFileLines) {
            ScoreboardEntry scoreboardEntry = new ScoreboardEntry(stat, generateName(stat), String.valueOf(id));
            scoreboardEntries.add(scoreboardEntry);
            id++;
        }

        return scoreboardEntries;
    }


    public static String generateName(String stat) {
        if (stat.contains("breakItem")) {
            String itemName = stat.split("\\.")[3];
            return formalizeWord(itemName) + " Broken";
        }
        if (stat.contains("craftItem")) {
            String itemName = stat.split("\\.")[3];
            return formalizeWord(itemName) + " Crafted";
        }
        if (stat.contains("drop")) {
            String[] splitStatString = stat.split("\\.");
            if (splitStatString.length != 2) {
                String itemName = stat.split("\\.")[3];
                return formalizeWord(itemName) + " Dropped";
            }
        }
        if (stat.contains("entityKilledBy")) {
            String itemName = stat.split("\\.")[2];
            return "Killed By " + formalizeWord(itemName);
        }
        if (stat.contains("killEntity")) {
            String itemName = stat.split("\\.")[2];
            return formalizeWord(itemName) + " Killed";
        }
        if (stat.contains("mineBlock")) {
            String itemName = stat.split("\\.")[3];
            return formalizeWord(itemName) + " Mined";
        }
        if (stat.contains("pickup")) {
            String itemName = stat.split("\\.")[3];
            return formalizeWord(itemName) + " Picked Up";
        }
        if (stat.contains("useItem")) {
            String itemName = stat.split("\\.")[3];
            return formalizeWord(itemName) + " Used";
        }
        else {
            String statName = stat.split("\\.")[1];
            return formalizeWord(sentenceCase(statName));
        }
    }

    public static String formalizeWord(String string) {
        if (string == null) {return null;}
        String removedUnderscores = string.replaceAll("_", " ");
        char[] chars = removedUnderscores.toLowerCase().toCharArray();

        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i])) {
                found = false;
            }
        }

        return String.valueOf(chars);
    }

    public static String sentenceCase(String text) {
        if (!text.equals("")) {
            String result = text.replaceAll("([A-Z])", " $1");
            return result.substring(0, 1).toUpperCase() + result.substring(1).toLowerCase();
        }
        return null;
    }
}
