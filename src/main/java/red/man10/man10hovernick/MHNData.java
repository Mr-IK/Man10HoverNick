package red.man10.man10hovernick;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import red.man10.man10vaultapiplus.JPYBalanceFormat;
import red.man10.man10vaultapiplus.enums.TransactionCategory;
import red.man10.man10vaultapiplus.enums.TransactionType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MHNData {
    static Man10HoverNick plugin;
    public static void loadConfig(Man10HoverNick plugin){
        MHNData.plugin = plugin;
        plugin.saveDefaultConfig();
        plugin.config = plugin.getConfig();
        File userdata = new File(plugin.getDataFolder(), File.separator + "Users");
        if(!userdata.exists()){
            userdata.mkdir();
        }
        File[] files = userdata.listFiles();  // (a)
        if(files != null){
            for (File f : files) {
                if (f.isFile()){  // (c)
                    String filename = f.getName();

                    if(filename.substring(0,1).equalsIgnoreCase(".")){
                        continue;
                    }

                    int point = filename.lastIndexOf(".");
                    if (point != -1) {
                        filename =  filename.substring(0, point);
                    }

                    if(loadNick(UUID.fromString(filename)).isNull()){
                        continue;
                    }

                    plugin.list.put(UUID.fromString(filename),new NameTagManager.NameTagData(UUID.fromString(filename),loadNick(UUID.fromString(filename)).prefix,loadNick(UUID.fromString(filename)).suffix));
                }
            }
        }
    }

    public static void register(String name,String prefix,String suffix,Mode mode,double money){
        if(plugin.config.contains(name)){
            return;
        }
        plugin.config.set(name+".prefix",prefix);
        plugin.config.set(name+".suffix",suffix);
        if(mode == Mode.COMMAND) {
            plugin.config.set(name+".mode","command");
        }else if(mode == Mode.BUY) {
            plugin.config.set(name+".mode","BUY");
            plugin.config.set(name+".money",money);
        }else if(mode == Mode.FREE) {
            plugin.config.set(name+".mode","FREE");
        }else{
            plugin.config.set(name+".mode","PEX");
        }
        plugin.saveConfig();
    }

    public static void unregister(String name){
        if(!plugin.config.contains(name)){
            return;
        }
        plugin.config.set(name,null);
        plugin.saveConfig();
    }

    public static List<String> registlist(){
        return new ArrayList<>(plugin.config.getKeys(false));
    }

    public static Datas getRegist(String name){
        if(!plugin.config.contains(name)){
            return null;
        }
        Mode mode = null;
        if(plugin.config.getString(name+".mode").equalsIgnoreCase("pex")) {
            mode = Mode.PEX;
        }else if(plugin.config.getString(name+".mode").equalsIgnoreCase("buy")){
            mode = Mode.BUY;
        }else if(plugin.config.getString(name+".mode").equalsIgnoreCase("free")){
            mode = Mode.FREE;
        }else{
            mode = Mode.COMMAND;
        }
        return new Datas(name,plugin.config.getString(name+".prefix"),plugin.config.getString(name+".suffix"),mode);
    }

    public static void createUser(Player p){
        String fileName = p.getUniqueId().toString();
        File userdata = new File(plugin.getDataFolder(), File.separator + "Users");
        File f = new File(userdata, File.separator + fileName + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);
        if (!f.exists()) {
            try {
                data.set("name", p.getName());
                data.set("list", new ArrayList<>());
                data.save(f);
            } catch (IOException exception) {
                return;
            }
        }
    }

    public static boolean containUser(Player p){
        String fileName = p.getUniqueId().toString();
        File userdata = new File(plugin.getDataFolder(), File.separator + "Users");
        File f = new File(userdata, File.separator + fileName + ".yml");
        return f.exists();
    }

    public static void addNick(Player p,String id){
        String fileName = p.getUniqueId().toString();
        File userdata = new File(plugin.getDataFolder(), File.separator + "Users");
        File f = new File(userdata, File.separator + fileName + ".yml");
        if(!containUser(p)){
            createUser(p);
        }
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);
        f.delete();

        if (!f.exists()) {
            try {
                List<String> list = data.getStringList("list");
                list.add(id);
                data.set("list", list);
                data.save(f);
            } catch (IOException ignored) {
            }
        }
    }

    public static void saveNick(Player p,Datas datas){
        String fileName = p.getUniqueId().toString();
        File userdata = new File(plugin.getDataFolder(), File.separator + "Users");
        File f = new File(userdata, File.separator + fileName + ".yml");
        if(!containUser(p)){
            createUser(p);
        }
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);
        f.delete();

        if (!f.exists()) {
            try {
                data.set("id", datas.name);
                data.save(f);
            } catch (IOException ignored) {
            }
        }
    }

    public static Datas loadNick(UUID uuid){
        String fileName = uuid.toString();
        File userdata = new File(plugin.getDataFolder(), File.separator + "Users");
        File f = new File(userdata, File.separator + fileName + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);
        if(data.contains("id")) {
            Datas datas = getRegist(data.getString("id"));
            removeNick(uuid);
            return datas;
        }
        return new Datas(null,null,null,null);
    }

    public static void removeNick(UUID uuid){
        String fileName = uuid.toString();
        File userdata = new File(plugin.getDataFolder(), File.separator + "Users");
        File f = new File(userdata, File.separator + fileName + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);
        try {
            data.set("id", null);
            data.save(f);
        } catch (IOException ignored) {
        }
    }

    public static void removeNick(Player p,String id){
        String fileName = p.getUniqueId().toString();
        File userdata = new File(plugin.getDataFolder(), File.separator + "Users");
        File f = new File(userdata, File.separator + fileName + ".yml");
        if(!containUser(p)){
            createUser(p);
        }
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);
        f.delete();
        if (!f.exists()) {
            try {
                List<String> list = data.getStringList("list");
                list.remove(id);
                data.set("list", list);
                data.save(f);
            } catch (IOException ignored) {
            }
        }
    }

    public static List<String> listNick(Player p){
        String fileName = p.getUniqueId().toString();
        File userdata = new File(plugin.getDataFolder(), File.separator + "Users");
        File f = new File(userdata, File.separator + fileName + ".yml");
        if(!containUser(p)){
            createUser(p);
        }
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);
        return data.getStringList("list");
    }

    public static boolean buyNick(Player p,String id){
        plugin.vault.transferMoneyPlayerToCountry(p.getUniqueId(),plugin.config.getDouble(id+".money"),TransactionCategory.TAX,TransactionType.FEE,"buy nickname");
        p.sendMessage(plugin.prefix+"§e"+new JPYBalanceFormat(plugin.config.getDouble(id+".money")).getString()+"円支払い 『"+id+"』 を購入しました");
        addNick(p,id);
        return true;
    }



    static class Datas{
        String name;
        String prefix;
        String suffix;
        Mode mode;
        Datas(String name,String prefix,String suffix,Mode mode){
            this.name = name;
            this.prefix = prefix;
            this.suffix = suffix;
            this.mode = mode;
        }
        boolean isNull(){
            return name == null && prefix == null && suffix == null && mode == null;
        }
    }

    enum Mode {
        BUY,PEX,FREE,COMMAND
    }

}
