package tk.manf.pvpsuite;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import org.bukkit.plugin.java.JavaPlugin;
import tk.manf.pvpsuite.manager.ChatManager;
import tk.manf.pvpsuite.manager.DataManager;
import tk.manf.pvpsuite.manager.PeaceManager;

public class PvPSuitePlugin extends JavaPlugin {
    @Getter(AccessLevel.PACKAGE)
    private static File languageFile;
    
    @Override
    public void onEnable() {
        try {
            languageFile = new File(getDataFolder(), "language.yml");
            languageFile.getParentFile().mkdirs();
            if(languageFile.createNewFile()) {
                Language.save();
            }
            DataManager.getInstance().initialise(this);
            DataManager.getInstance().load(getLogger());
            PeaceManager.getInstance().initialise(this);
            ChatManager.getInstance().initialise(this);
        } catch (IOException ex) {
            Logger.getLogger(PvPSuitePlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onDisable() {
        DataManager.getInstance().save(getLogger());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        if (Permission.check(Permission.USER_USE, sender)) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase(Language.COMMAND_ACCEPT.toString())) {
                    if (Permission.check(Permission.USER_ACCEPT, sender)) {
                        PeaceManager.getInstance().reply(true, (Player) sender);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase(Language.COMMAND_DECLINE.toString())) {
                    if (Permission.check(Permission.USER_DECLINE, sender)) {
                        PeaceManager.getInstance().reply(false, (Player) sender);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase(Language.COMMAND_DISBAND.toString())) {
                    if (args.length > 1) {
                        if (Permission.check(Permission.USER_DISBAND, sender)) {
                            PeaceManager.getInstance().removePeace((Player) sender, args[1]);
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase(Language.COMMAND_LIST.toString())) {
                    if (Permission.check(Permission.USER_LIST, sender)) {
                        sender.sendMessage(DataManager.getInstance().getPlayer(sender.getName()).getPeaceList());
                    }
                    return true;
                } else {
                    if (Permission.check(Permission.USER_SEND, sender)) {
                        PeaceManager.getInstance().offer((Player) sender, args[0]);
                    }
                    return true;
                }
            }
            Language.HELP.sendMessage(sender, label, Language.COMMAND_ACCEPT, Language.COMMAND_DECLINE, Language.COMMAND_DISBAND, Language.COMMAND_LIST);
        }
        return true;
    }
}
