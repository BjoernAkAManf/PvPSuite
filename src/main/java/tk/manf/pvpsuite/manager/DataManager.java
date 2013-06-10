package tk.manf.pvpsuite.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import tk.manf.pvpsuite.PvPPlayer;
import tk.manf.serialisation.ObjectSerialiser;

public class DataManager implements Listener {
    private ObjectSerialiser serial;
    private final HashMap<String, PvPPlayer> players;
    private static final Logger logger = Logger.getLogger("Minecraft");

    private DataManager() {
        players = new HashMap<String, PvPPlayer>();
    }

    public void initialise(JavaPlugin plugin) {
        serial = new ObjectSerialiser(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void load() {
        try {
            List<PvPPlayer> tmp = serial.load(PvPPlayer.class);
            for (PvPPlayer player : tmp) {
                players.put(player.getName().toLowerCase(), player);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void save() {
        try {
            for (PvPPlayer player : players.values()) {
                serial.save(player, false);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public PvPPlayer getPlayer(String name) {
        if (containsKey(name)) {
            return players.get(name.toLowerCase());
        }
        throw new IllegalArgumentException("Player does not exists");
    }

    public ObjectSerialiser getSerialiser() {
        return serial;
    }
    
    @EventHandler
    public final void onPlayerJoin(final PlayerJoinEvent ev) {
        if (!containsKey(ev.getPlayer())) {
            putEmptyPlayer(ev.getPlayer());
        }
    }

    private boolean containsKey(Player p) {
        return containsKey(p.getName());
    }

    private boolean containsKey(String name) {
        return players.containsKey(name.toLowerCase());
    }

    private void putEmptyPlayer(Player p) {
        put(p, new PvPPlayer(p.getName(), new ArrayList<String>(0)));
    }

    private void put(Player p, PvPPlayer player) {
        players.put(p.getName().toLowerCase(), player);
    }

    public static DataManager getInstance() {
        return instance;
    }
    private static final DataManager instance = new DataManager();
}
