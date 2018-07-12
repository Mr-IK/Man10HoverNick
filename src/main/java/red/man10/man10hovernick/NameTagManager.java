package red.man10.man10hovernick;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class NameTagManager {

    static Man10HoverNick plugin;
    static Scoreboard scoreboard;

    static class NameTagData{
        private UUID uuid;
        private String prefix;
        private String suffix;
        NameTagData(UUID uuid,String prefix,String suffix){
           this.uuid = uuid;
           this.prefix = prefix;
           this.suffix = suffix;
        }
        UUID getUUID(){
            return uuid;
        }
        String getPrefix(){
            return prefix;
        }
        String getSuffix(){
            return suffix;
        }
    }

    public static void EnableLoad(Man10HoverNick plugin){
        NameTagManager.plugin = plugin;
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public static void setTag(Player tagreceiver,String prefix,String suffix) {
        List<Player> admins = Collections.singletonList(tagreceiver);
        ArrayList<Player> packetereceivers = new ArrayList<>(Bukkit.getOnlinePlayers());
        NameTag.getter.send(admins, prefix,suffix, packetereceivers);
    }

    public static void joinLoad(Player joiner,Player player,String prefix,String suffix){
        List<Player> admins = Collections.singletonList(player);
        List<Player> packetereceivers = Collections.singletonList(joiner);
        NameTag.getter.send(admins, prefix,suffix, packetereceivers);
    }


}
