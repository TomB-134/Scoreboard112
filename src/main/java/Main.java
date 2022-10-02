import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.*;
import types.Player;
import types.ScoreboardEntry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    static String worldSave = "C:\\Users\\Thoma\\OneDrive\\Desktop\\Minecraft\\Worlds\\112SurvivalWorld\\world";


    public static void main(String[] args) throws IOException {
        System.out.println("Scoreboard Generator for 1.12");

        StatHandler statHandler = new StatHandler(worldSave);
        types.Player[] players = statHandler.generatePlayers();

        ScoreboardHandler scHandler = new ScoreboardHandler();
        ArrayList<ScoreboardEntry> scoreboardEntries = scHandler.createScoreboards();

        createScoreboardFile(scoreboardEntries, players);
    }

    public static NamedTag createScoreboardFile(ArrayList<ScoreboardEntry> scoreboardEntries, Player[] players) throws IOException {

        CompoundTag rootTag = new CompoundTag();

        CompoundTag data = new CompoundTag();
        rootTag.put("data", data);

        CompoundTag displaySlots = new CompoundTag();
        data.put("DisplaySlots", displaySlots);

        ListTag<CompoundTag> objectives = new ListTag<>(CompoundTag.class); //Create Objectives
        for (ScoreboardEntry scoreboardEntry : scoreboardEntries) {
            StringTag criteriaName = new StringTag(scoreboardEntry.criteriaName);
            StringTag displayName = new StringTag(scoreboardEntry.displayName);
            StringTag name = new StringTag(scoreboardEntry.name);
            StringTag renderType = new StringTag("integer");

            CompoundTag scoreboardEntryTag = new CompoundTag();
            scoreboardEntryTag.put("CriteriaName", criteriaName);
            scoreboardEntryTag.put("DisplayName", displayName);
            scoreboardEntryTag.put("Name", name);
            scoreboardEntryTag.put("RenderType", renderType);

            objectives.add(scoreboardEntryTag);
        }

        data.put("Objectives", objectives);

        ListTag<CompoundTag> playerScores = new ListTag<>(CompoundTag.class);
        for (Player player : players) {
            for (ScoreboardEntry scoreboardEntry : scoreboardEntries) {
                ByteTag locked = new ByteTag((byte) 0);
                StringTag name = new StringTag(player.username);
                StringTag objective = new StringTag(scoreboardEntry.name);

                IntTag score = new IntTag(); //Apply score from saved statistics
                for (String statistic : player.statistics) {
                    String[] splitString = statistic.split("=");
                    if (Objects.equals(scoreboardEntry.criteriaName, splitString[0])) {
                        score.setValue(Integer.parseInt(splitString[1]));
                    }
                }

                CompoundTag scoreboardScoreEntryTag = new CompoundTag();

                scoreboardScoreEntryTag.put("Name", name);
                scoreboardScoreEntryTag.put("Score", score);
                scoreboardScoreEntryTag.put("Objective", objective);
                scoreboardScoreEntryTag.put("Locked", locked);

                playerScores.add(scoreboardScoreEntryTag);
            }
        }

        data.put("PlayerScores", playerScores);

        ListTag<CompoundTag> teams = new ListTag<>(CompoundTag.class);
        data.put("Teams", teams);

        NBTUtil.write(rootTag, worldSave + "\\data\\scoreboard.dat");

        return null;
    }
}
