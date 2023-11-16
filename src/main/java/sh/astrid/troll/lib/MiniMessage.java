package sh.astrid.troll.lib;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Objects;

public class MiniMessage {
    public static Component color(String input) {
        net.kyori.adventure.text.minimessage.MiniMessage mm = net.kyori.adventure.text.minimessage.MiniMessage.miniMessage();
        TextColor primary = Objects.requireNonNull(TextColor.fromHexString("#ffd4e3"));
        TextColor secondary = Objects.requireNonNull(TextColor.fromHexString("#ffb5cf"));
        return mm.deserialize(
                        input, TagResolver.resolver("p", Tag.styling(primary)), TagResolver.resolver("s", Tag.styling(secondary)))
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

}
