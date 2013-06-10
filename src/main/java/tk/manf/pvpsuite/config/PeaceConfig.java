package tk.manf.pvpsuite.config;

import lombok.Getter;
import org.bukkit.ChatColor;
import tk.manf.serialisation.SerialisationType;
import tk.manf.serialisation.annotations.Property;
import tk.manf.serialisation.annotations.Unit;

@Unit(name = "peace.yml", type = SerialisationType.FLATFILE_YAML)
public class PeaceConfig {
    @Property
    @Getter
    private boolean TagAPI = true;
    @Property
    @Getter
    private char peaceTAG = ChatColor.GREEN.getChar();
}