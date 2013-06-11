package tk.manf.pvpsuite.manager;

import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import tk.manf.pvpsuite.config.ChatConfig;
import tk.manf.pvpsuite.modules.Anti7Module;
import tk.manf.serialisation.SerialisationException;

public class ChatManager {
    private Anti7Module anti7;
    private ChatConfig config;
    
    private ChatManager() {}

    public void initialise(JavaPlugin plugin) {
         try {
            try {
                config = DataManager.getInstance().getSerialiser().loadStatic(ChatConfig.class);
            } catch (SerialisationException ex) {
                config = new ChatConfig();
            }
            if(config.isAnti7()) {
                anti7 = new Anti7Module();
                plugin.getServer().getPluginManager().registerEvents(anti7, plugin);
            }
            DataManager.getInstance().getSerialiser().save(config, false);
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
        }
    }
    
    @Getter
    private static ChatManager instance = new ChatManager();
}
