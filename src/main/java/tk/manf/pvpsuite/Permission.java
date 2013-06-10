package tk.manf.pvpsuite;

import lombok.Getter;
import org.bukkit.command.CommandSender;

public enum Permission {
    USER("user"),
    USER_USE(USER, "use"),
    USER_ACCEPT(USER, "accept"),
    USER_DECLINE(USER, "decline"),
    USER_LIST(USER, "list"),
    USER_DISBAND(USER, "disband"),
    USER_SEND(USER, "send");
 
    private Permission(String perm) {
        this("PvPSuite", perm);
    }
    
    private Permission(Permission parent, String perm) {
        this(parent.getPerm(), perm);
    }
    
    private Permission(String parent, String perm) {
        this.parent = parent;
        this.perm = parent + "." + perm;
    }
    
    public static boolean check(Permission perm, CommandSender a) {
        if(a.hasPermission(perm.getPerm()) || a.hasPermission(perm.getParent() + ".*")){
            return true;
        }
        Language.ERROR_NOPERMISSION.sendMessage(a, perm.toString());
        return false;
    }
    
    @Getter
    private String parent;
    @Getter
    private String perm;
}
