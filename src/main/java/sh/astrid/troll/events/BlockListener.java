package sh.astrid.troll.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import sh.astrid.troll.lib.Storage;

public class BlockListener implements Listener {
    @EventHandler
    private static void onBreak(BlockBreakEvent e) {
        Storage storage = Storage.getInstance();
        if(storage.getNoDrop(e.getPlayer().getUniqueId())) {
            if(e.isDropItems()) e.setDropItems(false);
        }
    }
}
