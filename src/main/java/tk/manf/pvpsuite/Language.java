package tk.manf.pvpsuite;

import java.io.IOException;
import java.text.MessageFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Language {
    COMMAND_ACCEPT("annehmen"),
    COMMAND_DECLINE("ablehnen"),
    COMMAND_DISBAND("auflösen"),
    COMMAND_LIST("list"),
    DISBAND_OTHER("&6{0} hat den Frieden mit dir beendet."),
    DISBAND_SELF("&6Du hast den Frieden mit {0} beendet."),
    OFFER_ACCEPTED_SELF("&6{0} hat dein Angebot angenommen!"),
    OFFER_ACCEPTED_OTHER("&6Angebot von {0} angenommen."),
    OFFER_DECLINED_SELF("&6Dein Angebot wurde von {0} abgelehnt!"),
    OFFER_DECLINED_OTHER("&6Angebot von {0} abgelehnt."),
    OFFER_SENDED("&6Du hast {0} den Frieden angeboten."),
    OFFER_RECIEVED("&6Der Spieler {0} bietet dir den Frieden an!(N)Du kannst das Angebot mit '/frieden [annehmen|ablehnen]' beantworten"),
    ERROR_NOOFFER("&6Du hast kein Angebot erhalten oder es ist bereits ungültig."),
    ERROR_NOPLAYER("&6Dieser Spieler konnte nicht gefunden werden: {0}"),
    ERROR_ALREADYPEACE("&6Du hast mit dieser Person bereits Frieden!"),
    ERROR_BUSY_OTHER("&6Dieser Spieler ist gerade beschäftigt."),
    ERROR_BUSY_SELF("&6Du bist bereits mit einem anderem Angebot beschäftigt."),
    ERROR_DISBAND_NOPLAYER("&6Du kannst die Beziehung nicht beenden, wenn der Spieler nicht online ist!"),
    ERROR_NOPEACE("&6Du hast keinen Frieden mit dieser Person!"),
    ERROR_NOPERMISSION("&cDir fehlt die Berechtigung: &6{0}"),
    ERROR_LIST_NOPEACE("&6Keinen Frieden geschlossen!"),
    HELP("&1------------------------&9Frieden&1-----------------------(N)"
    + "&a/{0} <Playername>-Friedensanfrage senden.(N)"
    + "&a/{0} {1} <Player>-&rFriedensanfrage annehmen.(N)"
    + "&a/{0} {2} <Player>-&rFriedensanfrage ablehnen(N)"
    + "&a/{0} {3} <Player>-&rFrieden mit Spielern auflösen.(N)"
    + "&a/{0} {4}-&rAlle anzeigen mit den man frieden hat.(N)"
    + "&1-----------------------------------------------------");

    private Language(String pattern) {
        this.format = new MessageFormat(getLanguage().contains(this.name()) ? getLanguage().getString(name()) : pattern);
    }

    public void sendMessage(CommandSender sender, Object... args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', format.format(args)).split("\\(N\\)"));
    }

    @Override
    public String toString() {
        return format.toPattern();
    }

    public static void save() throws IOException {
        for (Language lang : values()) {
            getLanguage().set(lang.name(), lang.toString());
        }
        getLanguage().save(PvPSuitePlugin.getLanguageFile());
    }

    /**
     * Returns Language Configuration loads Config if not already loaded
     * <p/>
     * @return Language
     */
    private static FileConfiguration getLanguage() {
        if (language == null) {
            language = YamlConfiguration.loadConfiguration(PvPSuitePlugin.getLanguageFile());
        }
        return language;
    }
    private static FileConfiguration language;
    private MessageFormat format;
}
