package tk.manf.pvpsuite.modules;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;
import org.kitteh.tag.TagAPI;
import tk.manf.pvpsuite.PvPPlayer;
import tk.manf.pvpsuite.config.PeaceConfig;
import tk.manf.pvpsuite.manager.DataManager;

public class TagAPIModule implements Listener {
    private final PeaceConfig config;

    public TagAPIModule(PeaceConfig config) {
        this.config = config;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onNameTag(PlayerReceiveNameTagEvent ev) {
        PvPPlayer player = DataManager.getInstance().getPlayer(ev.getPlayer().getName());
        if (player.isPlayerPeace(ev.getNamedPlayer().getName())) {
            ev.setTag(ChatColor.getByChar(config.getPeaceTAG()) + ev.getNamedPlayer().getName());
        }
    }

    public void refreshPlayer(Player o, Player p) {
        TagAPI.refreshPlayer(p, o);
        TagAPI.refreshPlayer(o, p);
    }
}