import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.shanerx.mojang.Mojang;
import types.Player;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class StatHandler {
    private final Mojang mojangAPI;

    private String worldSaveDir;

    public StatHandler(String worldSaveDir) {
        this.worldSaveDir = worldSaveDir;
        mojangAPI = new Mojang().connect();
    }

    public Player[] generatePlayers() throws NullPointerException {
        System.out.println("Gathering player data");
        File[] listOfFiles = new File(this.worldSaveDir + "\\stats").listFiles();
        String[] playerNames = new String[listOfFiles.length];

        System.out.println("Gathering usernames from UUIDs");
        for (int i = 0; i < listOfFiles.length; i++) {
            String UUID = listOfFiles[i].getName().split("\\.")[0];
            String username = mojangAPI.getPlayerProfile(UUID).getUsername();
            playerNames[i] = username;
        }

        System.out.println("Extracting statistics");
        ArrayList<String[]> statsList = new ArrayList<>();

        for (File statFile : listOfFiles) {
            try (FileReader fr = new FileReader(statFile)) {
                ArrayList<String> playerStats = new ArrayList<>();
                final JsonElement je = JsonParser.parseReader(fr);

                JsonObject jo = je.getAsJsonObject();

                Set<Map.Entry<String, JsonElement>> entries = jo.entrySet();
                for (Map.Entry<String, JsonElement> entry : entries) {
                    playerStats.add(entry.toString());
                }

                String[] playerStatsArray = playerStats.toArray(new String[0]);
                statsList.add(playerStatsArray);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String[][] statisticsCollated = statsList.toArray(new String[0][]);

        Player[] players = new Player[playerNames.length];

        for (int i = 0; i < playerNames.length; i++) {
            Player newPlayer = new Player(playerNames[i], statisticsCollated[i]);
            players[i] = newPlayer;
        }

        return players;
    }
}
