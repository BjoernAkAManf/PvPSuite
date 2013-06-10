package tk.manf.pvpsuite;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tk.manf.serialisation.SerialisationType;
import tk.manf.serialisation.annotations.Identification;
import tk.manf.serialisation.annotations.InitiationConstructor;
import tk.manf.serialisation.annotations.Parameter;
import tk.manf.serialisation.annotations.Property;
import tk.manf.serialisation.annotations.Unit;

@Unit(isStatic = false, name = "data", type = SerialisationType.FLATFILE_YAML)
public class PvPPlayer {
    @Identification
    @Property
    @Getter
    private final String name;
    @Property
    private final List<String> peace;

    @InitiationConstructor
    public PvPPlayer(@Parameter(name = "name") String name, @Parameter(name = "peace") List<String> peace) {
        this.name = name;
        Collections.sort(peace);
        this.peace = peace;
    }

    public boolean isPlayerPeace(PvPPlayer player) {
        return this.isPlayerPeace(player.getName()) && player.isPlayerPeace(this.getName());
    }

    public boolean isPlayerPeace(String player) {
        return name.equalsIgnoreCase(player) || peace.contains(player.toLowerCase());
    }

    public void addPlayerPeace(PvPPlayer player) {
        this.addPlayerPeace(player.getName());
        player.addPlayerPeace(this.getName());
    }

    public void removePlayerPeace(PvPPlayer player) {
        this.removePlayerPeace(player.getName());
        player.removePlayerPeace(this.getName());
    }


    public String getPeaceList() {
        if (peace.isEmpty()) {
            return Language.ERROR_LIST_NOPEACE.toString();
        }
        StringBuilder sb = new StringBuilder();
        for (String player : peace) {
            Player p = Bukkit.getPlayerExact(player);
            if (p == null) {
                sb.append(ChatColor.RED).append(player);
            } else {
                sb.append(ChatColor.GREEN).append(p.getName());
            }
            sb.append(ChatColor.WHITE).append(", ");
        }
        return sb.toString().substring(0, sb.length() - 2);
    }

    private void removePlayerPeace(String player) {
        peace.remove(player.toLowerCase());
    }

    private void addPlayerPeace(String player) {
        if (isPlayerPeace(player)) {
            return;
        }
        peace.add(player.toLowerCase());
        Collections.sort(peace);
    }
}
