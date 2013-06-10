package tk.manf.pvpsuite.manager;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import tk.manf.pvpsuite.Language;
import tk.manf.pvpsuite.PvPPlayer;
import tk.manf.pvpsuite.config.PeaceConfig;
import tk.manf.pvpsuite.modules.TagAPIModule;
import tk.manf.serialisation.SerialisationException;
import tk.manf.util.DualHashMap;
import tk.manf.util.DualMap;

public class PeaceManager implements Listener {
    private PeaceConfig config;
    private final DualMap<String, String> offers;
    private TagAPIModule tagAPIModule;
    private static final Logger logger = Logger.getLogger("Minecraft");

    private PeaceManager() {
        offers = new DualHashMap<String, String>(0);
    }

    public void initialise(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        try {
            try {
                config = DataManager.getInstance().getSerialiser().loadStatic(PeaceConfig.class);
            } catch (SerialisationException ex) {
                config = new PeaceConfig();
            }
            if (config.isTagAPI()) {
                tagAPIModule = new TagAPIModule(config);
                plugin.getServer().getPluginManager().registerEvents(tagAPIModule, plugin);
            }
            DataManager.getInstance().getSerialiser().save(config, false);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }


    public void reply(boolean accept, Player p) {
        if (!offers.containsValue(p.getName())) {
            Language.ERROR_NOOFFER.sendMessage(p);
            return;
        }
        Player o = Bukkit.getPlayerExact(offers.getValueMap().get(p.getName()));
        if (o == null) {
            Language.ERROR_NOOFFER.sendMessage(p);
        } else {
            if (accept) {
                DataManager.getInstance().getPlayer(p.getName()).addPlayerPeace(DataManager.getInstance().getPlayer(o.getName()));
                Language.OFFER_ACCEPTED_SELF.sendMessage(o, p.getName());
                Language.OFFER_ACCEPTED_OTHER.sendMessage(p, o.getName());
                if (tagAPIModule != null) {
                    tagAPIModule.refreshPlayer(o, p);
                }
            } else {
                Language.OFFER_DECLINED_SELF.sendMessage(o, p.getName());
                Language.OFFER_DECLINED_OTHER.sendMessage(p, o.getName());
            }
        }
        offers.removeValue(p.getName());
    }

    public void offer(Player p, String client) {
        Player target = Bukkit.getPlayer(client);
        if (target == null) {
            Language.ERROR_NOPLAYER.sendMessage(p, client);
            return;
        }
        if (canOffer(p.getName())) {
            if (canOffer(target.getName())) {
                if (DataManager.getInstance().getPlayer(p.getName()).isPlayerPeace(target.getName())) {
                    Language.ERROR_ALREADYPEACE.sendMessage(p);
                } else {
                    Language.OFFER_RECIEVED.sendMessage(target, p.getName());
                    Language.OFFER_SENDED.sendMessage(p, target.getName());
                    offers.put(p.getName(), target.getName());
                }
            } else {
                Language.ERROR_BUSY_OTHER.sendMessage(p);
            }
        } else {
            Language.ERROR_BUSY_SELF.sendMessage(p);
        }
    }

    public void removePeace(Player sender, String t) {
        Player target = Bukkit.getPlayer(t);
        if (target == null) {
            Language.ERROR_DISBAND_NOPLAYER.sendMessage(sender);
            return;
        }

        PvPPlayer o = DataManager.getInstance().getPlayer(target.getName());
        PvPPlayer p = DataManager.getInstance().getPlayer(sender.getName());
        if (p.isPlayerPeace(o) && !sender.getName().equals(target.getName())) {
            p.removePlayerPeace(o);
            Language.DISBAND_SELF.sendMessage(sender, target.getName());
            Language.DISBAND_OTHER.sendMessage(target, sender.getName());
            tagAPIModule.refreshPlayer(sender, target);
        } else {
            Language.ERROR_NOPEACE.sendMessage(sender);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent ev) {
        if (ev.getEntityType() == EntityType.PLAYER && ev.getDamager().getType() == EntityType.PLAYER) {
            PvPPlayer player = DataManager.getInstance().getPlayer(((Player) ev.getEntity()).getName());
            ev.setCancelled(player.isPlayerPeace(((Player) ev.getDamager()).getName()));
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent ev) {
        offers.remove(ev.getPlayer().getName());
        offers.removeValue(ev.getPlayer().getName());
    }

    private boolean canOffer(String name) {
        return !(offers.containsKey(name) || offers.containsValue(name));
    }

    public static PeaceManager getInstance() {
        return instance;
    }
    private static final PeaceManager instance = new PeaceManager();
}