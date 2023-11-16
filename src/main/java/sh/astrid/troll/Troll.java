package sh.astrid.troll;

import me.vaperion.blade.Blade;
import me.vaperion.blade.bukkit.BladeBukkitPlatform;
import org.bukkit.plugin.java.JavaPlugin;
import sh.astrid.troll.events.BlockListener;
import sh.astrid.troll.events.ChatListener;
import sh.astrid.troll.lib.SQL;
import com.moandjiezana.toml.Toml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

public final class Troll extends JavaPlugin {
    public static Troll getInstance() {
        return JavaPlugin.getPlugin(Troll.class);
    }

    @Override
    public void onEnable() {
        Blade.forPlatform(new BladeBukkitPlatform(this)).build()
                .registerPackage(Troll.class, "sh.astrid.troll.commands");

        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);

        if(!this.getDataFolder().exists()) {
            boolean created = this.getDataFolder().mkdir();
            if(!created) {
                this.getLogger().warning("Failed to create data folder!");
            }
        }
    }

    public Toml getFile(String tomlFile) {
        File config = new File(Troll.getInstance().getDataFolder(), tomlFile);

        if (!config.exists()) {
            Troll.getInstance().saveResource(tomlFile, false);
        }

        try {
            List<String> lines = Files.readAllLines(Path.of(config.getAbsolutePath()));
            return new Toml().read(String.join("\n", lines));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        try {
            SQL.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
