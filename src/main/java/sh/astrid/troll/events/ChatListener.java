package sh.astrid.troll.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import sh.astrid.troll.lib.Storage;
import sh.astrid.troll.lib.UwUUtils;

public class ChatListener implements Listener {
    @EventHandler
    private static void onChat(AsyncPlayerChatEvent e) {
        Storage storage = Storage.getInstance();
        if(storage.getUwUMode(e.getPlayer().getUniqueId())) {
            String message = e.getMessage();
            e.setMessage(UwUUtils.uwuify(message));
        }
    }
}
