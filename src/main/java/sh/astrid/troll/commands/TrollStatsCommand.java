package sh.astrid.troll.commands;

import me.vaperion.blade.annotation.argument.Name;
import me.vaperion.blade.annotation.argument.Sender;
import me.vaperion.blade.annotation.command.Command;
import me.vaperion.blade.annotation.command.Description;
import me.vaperion.blade.annotation.command.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import sh.astrid.troll.lib.Storage;
import sh.astrid.troll.lib.TrollType;

import java.util.UUID;

import static sh.astrid.troll.lib.MiniMessage.color;

@SuppressWarnings("unused")
public class TrollStatsCommand {
    private static String prettify(String input) {
        String str = input.replaceAll("[-_]", " ").toLowerCase();
        if (!str.isEmpty()) {
            str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
        }
        return str;
    }

    @Command("trollstats")
    @Description("View trolling statistics")
    @Permission("op")
    @SuppressWarnings("unused")
    public static void stats(
            @Sender Player sender,
            @Name("player") Player player
    )  {
        Storage storage = Storage.getInstance();
        JSONArray trollData = storage.getTrollData(player.getUniqueId());

        if(trollData == null) {
            sender.sendMessage(color("<red>This player has never been trolled!"));
            return;
        }

        StringBuilder msg = new StringBuilder();

        for (int i = 0; i < trollData.length(); i++) {
            JSONObject obj = trollData.getJSONObject(i);
            int rawType = obj.getInt("type");
            int count = obj.getInt("count");
            String lastTrollUUID = obj.getString("lastTrolled");
            OfflinePlayer lastTroller = Bukkit.getOfflinePlayer(UUID.fromString(lastTrollUUID));

            TrollType type = TrollType.values()[rawType];
            String prettyType = prettify(type.name());

            msg.append("<p>")
                    .append(prettyType)
                    .append(": <s>")
                    .append(count)
                    .append(" <p>( <s>Last by: ")
                    .append(lastTroller.getName())
                    .append(" </s>)")
                    .append("\n");
        }

        sender.sendMessage(color("\n" + msg));
    }
}
