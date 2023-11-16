package sh.astrid.troll.lib;

import org.json.JSONArray;
import org.json.JSONObject;
import sh.astrid.troll.Troll;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Storage {
    private static Storage instance;
    private Map<UUID, Boolean> uwuMode;
    private Map<UUID, Boolean> noDrop;

    private Storage() {
        uwuMode = new HashMap<>();
        noDrop = new HashMap<>();
    }

    public static Storage getInstance() {
        if (instance == null) instance = new Storage();
        return instance;
    }

    public boolean toggleUwUMode(UUID uuid) {
        boolean currentState = getUwUMode(uuid);
        boolean newState = !currentState;
        uwuMode.put(uuid, newState);
        return newState;
    }

    public boolean getUwUMode(UUID uuid) {
        return uwuMode.getOrDefault(uuid, false);
    }

    public boolean toggleNoDrop(UUID uuid) {
        boolean currentState = getNoDrop(uuid);
        boolean newState = !currentState;
        noDrop.put(uuid, newState);
        return newState;
    }

    public boolean getNoDrop(UUID uuid) {
        return noDrop.getOrDefault(uuid, false);
    }

    public void incrementTrollCount(UUID uuid, UUID troller, TrollType type) {
        System.out.println("Received update of " + type.ordinal() + " ( " + type + " ) troll count");
        try {
            ResultSet currentStatistics = SQL.query("SELECT * FROM statistics WHERE player_uuid = ?", uuid.toString());

            if (currentStatistics == null || !currentStatistics.next()) {
                createTrollData(uuid, type, troller);
            } else {
                updateTrollData(uuid, troller, type, currentStatistics);
            }

        } catch (Exception e) {
            Troll.getInstance().getLogger().warning(e.getMessage());
        }
    }

    private void createTrollData(UUID uuid, TrollType type, UUID troller) {
        try {
            JSONArray newTrollData = new JSONArray();
            JSONObject newTrollType = new JSONObject();
            newTrollType.put("type", type.ordinal());
            newTrollType.put("count", 1);
            newTrollType.put("lastTrolled", troller.toString());

            newTrollData.put(newTrollType);

            SQL.execute(
                    "INSERT INTO statistics(player_uuid, trolls) VALUES(?, ?)",
                    uuid.toString(),
                    newTrollData.toString()
            );
        } catch (Exception e) {
            Troll.getInstance().getLogger().warning(e.getMessage());
        }
    }

    private void updateTrollData(UUID uuid, UUID troller, TrollType type, ResultSet currentStatistics) {
        try {
            JSONArray trollData = new JSONArray(currentStatistics.getString("trolls"));
            boolean foundType = false;

            for (int i = 0; i < trollData.length(); i++) {
                JSONObject troll = trollData.getJSONObject(i);
                if (troll.getInt("type") == type.ordinal()) {
                    int currentCount = troll.getInt("count");
                    troll.put("count", currentCount + 1);
                    troll.put("lastTrolled", troller.toString());
                    foundType = true;
                    break;
                }
            }

            if (!foundType) {
                JSONObject newTroll = new JSONObject();
                newTroll.put("type", type.ordinal());
                newTroll.put("count", 1);
                newTroll.put("lastTrolled", troller.toString());
                trollData.put(newTroll);
            }

            SQL.execute(
                    "UPDATE statistics SET trolls = ? WHERE player_uuid = ?",
                    trollData.toString(),
                    uuid.toString()
            );
        } catch (Exception e) {
            Troll.getInstance().getLogger().warning(e.getMessage());
        }
    }

    public JSONArray getTrollData(UUID uuid) {
        try {
            ResultSet currentStatistics = SQL.query("SELECT * FROM statistics WHERE player_uuid = ?", uuid.toString());
            String rawJSON = currentStatistics.getString("trolls");
            return new JSONArray(rawJSON);
        } catch (Exception e) {
            Troll.getInstance().getLogger().warning(e.getMessage());
        }
        return null;
    }

}
