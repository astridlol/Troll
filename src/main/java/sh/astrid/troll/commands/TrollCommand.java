package sh.astrid.troll.commands;

import com.moandjiezana.toml.Toml;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.vaperion.blade.annotation.argument.Name;
import me.vaperion.blade.annotation.argument.Sender;
import me.vaperion.blade.annotation.command.Command;
import me.vaperion.blade.annotation.command.Description;
import me.vaperion.blade.annotation.command.Permission;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import sh.astrid.troll.Troll;
import sh.astrid.troll.lib.SkullCreator;
import sh.astrid.troll.lib.Storage;
import sh.astrid.troll.lib.TrollType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static sh.astrid.troll.lib.MiniMessage.color;

@SuppressWarnings("unused")
public class TrollCommand {
    private static ItemBuilder getQuestionSkull() {
        String questionSkull = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWFiNmVhZWMyMzdkOTRjMzY5OWVhZWQ1NjZhNTkyMzg2ZmI2Nzk1MDNhMzA4NWNhNDliM2ExMjk4NDFkY2MxMyJ9fX0=";
        ItemStack skull = SkullCreator.createSkull(questionSkull);
        return ItemBuilder.from(skull);
    }

    private static ItemBuilder createUwUModeItem(UUID uuid) {
        Storage storage = Storage.getInstance();
        boolean uwuEnabled = storage.getUwUMode(uuid);
        String statusText = "<green>Enabled";
        if(!uwuEnabled) statusText = "<red>Disabled";

        return ItemBuilder.from(Material.PINK_GLAZED_TERRACOTTA)
                .name(color("<p>UwU Mode"))
                .lore(
                        color("<s>Forces the player to talk in uwu speak"),
                        color("<s>Status: " + statusText)
                );
    }

    private static ItemBuilder createDemoModeItem() {
        return getQuestionSkull()
                .name(color("<p>Demo Mode"))
                .lore(color("<s>Shows the player the demo screen"));
    }

    private static ItemBuilder createNoDropItem(UUID uuid) {
        Storage storage = Storage.getInstance();
        boolean uwuEnabled = storage.getNoDrop(uuid);
        String statusText = "<green>Enabled";
        if(!uwuEnabled) statusText = "<red>Disabled";

        return ItemBuilder.from(Material.GRASS_BLOCK)
                .name(color("<p>No Drop"))
                .lore(
                        color("<s>Disables item dropping from blocks"),
                        color("<s>Status: " + statusText)
                );
    }

    private static ItemBuilder createExplodeItem() {
        return ItemBuilder.from(Material.CREEPER_SPAWN_EGG)
                .name(color("<p>Explosion"))
                .lore(
                        color("<s>Creates a creeper explosion")
                );
    }

    private static ItemBuilder createSoundItem() {
        ItemStack disk = new ItemStack(Material.MUSIC_DISC_5);
        disk.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);

        return ItemBuilder.from(disk)
                .name(color("<p>Sounds"))
                .lore(
                        color("<s>➜ <em>Click to view!")
                );
    }

    private static ItemBuilder createCrashItem() {
        return ItemBuilder.from( new ItemStack(Material.BEDROCK))
                .name(color("<p>Crash"))
                .lore(
                        color("<s>Causes a fake crash to the player")
                );
    }

    private static void sendActionBar(Player player, String string) {
        player.sendActionBar(color(string));
    }

    @Command("troll")
    @Description("Troll players")
    @Permission("op")
    @SuppressWarnings("unused")
    public static void troll(
            @Sender Player sender,
            @Name("player") Player player
    )  {
        Storage storage = Storage.getInstance();

        Gui gui = Gui.gui()
                .title(Component.text("Trolling " + player.getName()))
                .rows(1)
                .create();

        GuiItem uwuMode = createUwUModeItem(player.getUniqueId()).asGuiItem(event -> {
            event.setCancelled(true);
            boolean toggle = storage.toggleUwUMode(player.getUniqueId());

            sender.closeInventory();
            storage.incrementTrollCount(player.getUniqueId(), sender.getUniqueId(), TrollType.UWU);

            String text = "Enabled";
            if(!toggle) text = "Disabled";
            sender.sendMessage(color("<green> " + text + " UwU mode for " + player.getName()));
        });

        GuiItem demoMode = createDemoModeItem().asGuiItem(event -> {
            event.setCancelled(true);
            player.showDemoScreen();
            storage.incrementTrollCount(player.getUniqueId(), sender.getUniqueId(), TrollType.DEMO);

            sender.sendMessage(color("<green>Showed the demo screen to " + player.getName() + "!"));
        });

        GuiItem noDrop = createNoDropItem(player.getUniqueId()).asGuiItem(event -> {
            event.setCancelled(true);
            boolean toggle = storage.toggleNoDrop(player.getUniqueId());
            sender.closeInventory();
            storage.incrementTrollCount(player.getUniqueId(), sender.getUniqueId(), TrollType.NO_DROP);

            String text = "Enabled";
            if(!toggle) text = "Disabled";
            sender.sendMessage(color("<green> " + text + " no-drops mode for " + player.getName()));
        });

        GuiItem explosion = createExplodeItem().asGuiItem(event -> {
            event.setCancelled(true);
            player.getLocation().createExplosion(5F);
            sender.sendMessage(color("<green>Faked a creeper explosion for " + player.getName() + "!"));
            storage.incrementTrollCount(player.getUniqueId(), sender.getUniqueId(), TrollType.EXPLOSION);
        });

        gui.setItem(0, uwuMode);
        gui.setItem(2, demoMode);
        gui.setItem(4, noDrop);
        gui.setItem(6, explosion);

        gui.setItem(8, ItemBuilder.from(new ItemStack(Material.ARROW))
                .name(color("<s>➜ <em>Next page"))
                .asGuiItem(event -> player.chat("/troll 2 " + player.getName())));

        gui.open(sender);
    }

    @Command("troll 2")
    @Description("Troll players")
    @Permission("op")
    @SuppressWarnings("unused")
    public static void troll2(
            @Sender Player sender,
            @Name("player") Player player
    )  {
        Storage storage = Storage.getInstance();

        Gui gui = Gui.gui()
                .title(Component.text("Trolling " + player.getName()))
                .rows(1)
                .create();

        gui.setItem(0, ItemBuilder.from(new ItemStack(Material.ARROW))
                .name(color("<s>← <em>Previous page"))
                .asGuiItem(event -> player.chat("/troll " + player.getName())));

        GuiItem sounds = createSoundItem().asGuiItem(event -> {
            event.setCancelled(true);
            sender.chat("/troll sounds " + player.getName());
        });

        GuiItem crash = createCrashItem().asGuiItem(event -> {
            event.setCancelled(true);
            player.kick(color("Internal Exception: java.lang.StringIndexOutOfBoundsException: String index out of range: -15"));
            storage.incrementTrollCount(player.getUniqueId(), sender.getUniqueId(), TrollType.CRASH);
            sender.sendMessage(color("<green>Faked a crash for " + player.getName() + "!"));
        });

        gui.setItem(2, crash);
        gui.setItem(4, sounds);

        gui.open(sender);
    }

    @Command("troll sounds")
    @Description("Play sounds to the player")
    @Permission("op")
    @SuppressWarnings("unused")
    public static void playSounds(
            @Sender Player sender,
            @Name("player") Player player
    )  {
        Storage storage = Storage.getInstance();

        Gui gui = Gui.gui()
                .title(Component.text("Trolling " + player.getName()))
                .rows(3)
                .create();

        Toml soundConfig = Troll.getInstance().getFile("sounds.toml");
        List<HashMap<String, String>> sounds = soundConfig.getList("sounds");

        AtomicInteger index = new AtomicInteger();
        sounds.forEach((sound) -> {
            String soundName = sound.get("name");
            String soundKey = sound.get("key");
            String prettyName = soundName.toLowerCase();

            ItemBuilder item = ItemBuilder.from(new ItemStack(Material.JUKEBOX))
                    .name(color("<p>" + soundName))
                    .lore(color("<s>Plays the " + prettyName + " sound effect"));
            GuiItem guiItem = item.asGuiItem(event -> {
                event.setCancelled(true);
                try {
                    Sound specificSound = Sound.sound(Key.key(soundKey), Sound.Source.AMBIENT, 1f, 1f);
                    player.playSound(specificSound);
                    storage.incrementTrollCount(player.getUniqueId(), sender.getUniqueId(), TrollType.SOUND);
                    sender.sendMessage(color(
                            new StringBuilder()
                            .append("<green>Played the sound of ")
                            .append(prettyName)
                            .append(" to ")
                            .append(player.getName())
                            .toString())
                    );
                } catch (Exception exception) {
                    player.sendMessage("<red>Failed to play sound of " + soundKey);
                }
            });

            gui.setItem(index.get(), guiItem);

            index.getAndIncrement();
        });

        gui.open(sender);
    }
}
