package tk.manf.pvpsuite.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import tk.manf.pvpsuite.Language;

public class Anti7Module implements Listener {
    public Anti7Module() {
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent ev) {
        if (ev.getMessage().startsWith("7")) {
            ev.setCancelled(true);
            final String cmd = "/" + ev.getMessage().substring(1);
            ev.getPlayer().chat(cmd);
            Language.CHAT_ANTI7.sendMessage(ev.getPlayer(), ev.getMessage(), cmd);
        }
    }
}
