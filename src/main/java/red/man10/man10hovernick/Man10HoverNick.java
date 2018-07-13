package red.man10.man10hovernick;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import red.man10.man10vaultapiplus.JPYBalanceFormat;
import red.man10.man10vaultapiplus.Man10VaultAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public final class Man10HoverNick extends JavaPlugin implements Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if(args.length == 3){
                if(args[0].equalsIgnoreCase("give")) {
                    if (!MHNData.registlist().contains(args[1])) {
                        return true;
                    }
                    Player pp = Bukkit.getPlayer(args[2]);
                    if (pp == null) {
                        return true;
                    }
                    MHNData.addNick(pp, args[1]);
                    return true;
                }else if(args[0].equalsIgnoreCase("take")) {
                    if (!MHNData.registlist().contains(args[1])) {
                        return true;
                    }
                    Player pp = Bukkit.getPlayer(args[2]);
                    if (pp == null) {
                        return true;
                    }
                    MHNData.removeNick(pp, args[1]);
                    return true;
                }
            }
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("mhn.use")) {
            p.sendMessage(prefix + "§cあなたには権限がありません！");
            return true;
        }
        if(args.length == 0){
            Inventory inv;
            if (list.containsKey(p.getUniqueId())) {
                inv = Bukkit.createInventory(null,9,"§0現在のニックネーム: "+list.get(p.getUniqueId()).getPrefix() + p.getName() + list.get(p.getUniqueId()).getSuffix());
            } else {
                inv = Bukkit.createInventory(null,9,"§0現在のニックネーム: なし");
            }
            ItemStack item = new ItemStack(Material.NAME_TAG);
            ItemMeta itemm = item.getItemMeta();
            itemm.setDisplayName("§a§l二つ名設定");
            itemm.setUnbreakable(true);
            itemm.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            List<String> lore = new ArrayList<>();
            lore.add("§e二つ名を設定します");
            itemm.setLore(lore);
            item.setItemMeta(itemm);
            inv.setItem(2,item);
            ItemStack item3 = new ItemStack(Material.GLASS);
            ItemMeta itemm3 = item3.getItemMeta();
            itemm3.setDisplayName("§c§l二つ名リセット");
            itemm3.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
            itemm3.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            List<String> lore3 = new ArrayList<>();
            lore3.add("§a二つ名をリセットします");
            itemm3.setLore(lore3);
            item3.setItemMeta(itemm3);
            inv.setItem(4,item3);
            ItemStack item2 = new ItemStack(Material.CHEST);
            ItemMeta itemm2 = item2.getItemMeta();
            itemm2.setDisplayName("§e§l二つ名ショップ");
            itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
            itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            List<String> lore2 = new ArrayList<>();
            lore2.add("§aショップを開きます！");
            itemm2.setLore(lore2);
            item2.setItemMeta(itemm2);
            inv.setItem(6,item2);
            pstats.put(p.getUniqueId(),"main");
            p.openInventory(inv);
            return true;
        }else if(args.length == 1) {
            if (args[0].equalsIgnoreCase("unset")) {
                NameTagManager.setTag(p, "", "");
                list.remove(p.getUniqueId());
                MHNData.removeNick(p.getUniqueId());
                p.sendMessage(prefix + "§aアンセットに成功しました。");
                return true;
            } else if (args[0].equalsIgnoreCase("list")) {
                Inventory inv = Bukkit.createInventory(null,54,"§0二つ名リスト page:1");
                ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
                ItemMeta itemm = item.getItemMeta();
                itemm.setDisplayName("§a§l前のページへ戻る");
                item.setItemMeta(itemm);
                for(int i = 45; i < 49; i++){
                    inv.setItem(i,item);
                }
                ItemStack items = new ItemStack(Material.EMERALD_BLOCK);
                ItemMeta itemsm = items.getItemMeta();
                itemsm.setDisplayName("§a§l次のページへ");
                items.setItemMeta(itemsm);
                for(int i = 50; i < 54; i++){
                    inv.setItem(i,items);
                }
                ItemStack itemss = new ItemStack(Material.COMPASS);
                ItemMeta itemssm = itemss.getItemMeta();
                itemssm.setDisplayName("1");
                itemss.setItemMeta(itemssm);
                inv.setItem(49,itemss);
                int i = 0;
                for (String str : MHNData.registlist()) {
                    if(i==44){
                        break;
                    }
                    MHNData.Datas data = MHNData.getRegist(str);
                    if (data == null) {
                        continue;
                    }
                    if (data.mode == MHNData.Mode.PEX) {
                        if (!p.hasPermission("mhn." + str)) {
                            if (!p.hasPermission("mhn.op")) {
                                continue;
                            } else {
                                ItemStack item2 = new ItemStack(Material.NETHERRACK);
                                ItemMeta itemm2 = item2.getItemMeta();
                                itemm2.setDisplayName("§c"+data.name);
                                itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
                                itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                List<String> lore2 = new ArrayList<>();
                                lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                                lore2.add("§cOP権限で見ています");
                                itemm2.setLore(lore2);
                                item2.setItemMeta(itemm2);
                                inv.setItem(i,item2);
                                i++;
                                continue;
                            }
                        }
                    } else if (data.mode == MHNData.Mode.BUY || data.mode == MHNData.Mode.COMMAND) {
                        if (!MHNData.listNick(p).contains(str)) {
                            if (!p.hasPermission("mhn.op")) {
                                continue;
                            } else {
                                ItemStack item2 = new ItemStack(Material.NETHERRACK);
                                ItemMeta itemm2 = item2.getItemMeta();
                                itemm2.setDisplayName("§c"+data.name);
                                itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
                                itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                List<String> lore2 = new ArrayList<>();
                                lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                                lore2.add("§cOP権限で見ています");
                                itemm2.setLore(lore2);
                                item2.setItemMeta(itemm2);
                                inv.setItem(i,item2);
                                i++;
                                continue;
                            }
                        }
                    }
                    ItemStack item2 = new ItemStack(Material.PAPER);
                    ItemMeta itemm2 = item2.getItemMeta();
                    itemm2.setDisplayName("§a"+data.name);
                    itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
                    itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    List<String> lore2 = new ArrayList<>();
                    lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                    itemm2.setLore(lore2);
                    item2.setItemMeta(itemm2);
                    inv.setItem(i,item2);
                    i++;
                }
                pstats.put(p.getUniqueId(),"list");
                p.openInventory(inv);
                return true;
            } else if (args[0].equalsIgnoreCase("shop")) {
                Inventory inv = Bukkit.createInventory(null,54,"§0二つ名ショップ page:1");
                ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
                ItemMeta itemm = item.getItemMeta();
                itemm.setDisplayName("§a§l前のページへ戻る");
                item.setItemMeta(itemm);
                for(int i = 45; i < 49; i++){
                    inv.setItem(i,item);
                }
                ItemStack items = new ItemStack(Material.EMERALD_BLOCK);
                ItemMeta itemsm = items.getItemMeta();
                itemsm.setDisplayName("§a§l次のページへ");
                items.setItemMeta(itemsm);
                for(int i = 50; i < 54; i++){
                    inv.setItem(i,items);
                }
                ItemStack itemss = new ItemStack(Material.COMPASS);
                ItemMeta itemssm = itemss.getItemMeta();
                itemssm.setDisplayName("1");
                itemss.setItemMeta(itemssm);
                inv.setItem(49,itemss);
                int i = 0;
                for (String str : MHNData.registlist()) {
                    if(i==44){
                        break;
                    }
                    MHNData.Datas data = MHNData.getRegist(str);
                    if (data == null) {
                        continue;
                    }
                    if (data.mode != MHNData.Mode.BUY) {
                        continue;
                    }
                    if (MHNData.listNick(p).contains(str)) {
                        ItemStack item2 = new ItemStack(Material.STAINED_GLASS,1,(short)5);
                        ItemMeta itemm2 = item2.getItemMeta();
                        itemm2.setDisplayName("§c"+data.name);
                        itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
                        itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        List<String> lore2 = new ArrayList<>();
                        lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                        lore2.add("§e" + new JPYBalanceFormat(config.getDouble(str + ".money")).getString() + "円 §a購入済み");
                        itemm2.setLore(lore2);
                        item2.setItemMeta(itemm2);
                        inv.setItem(i,item2);
                        i++;
                    } else {
                        ItemStack item2 = new ItemStack(Material.STAINED_GLASS,1,(short)14);
                        ItemMeta itemm2 = item2.getItemMeta();
                        itemm2.setDisplayName("§a"+data.name);
                        itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
                        itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        List<String> lore2 = new ArrayList<>();
                        lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                        lore2.add("§e" + new JPYBalanceFormat(config.getDouble(str + ".money")).getString() + "円 §c未購入");
                        itemm2.setLore(lore2);
                        item2.setItemMeta(itemm2);
                        inv.setItem(i,item2);
                        i++;
                    }
                }
                pstats.put(p.getUniqueId(),"shop");
                p.openInventory(inv);
                return true;
            } else if (args[0].equalsIgnoreCase("help")) {
                p.sendMessage("§6========" + prefix + "§6=========");
                if (p.hasPermission("mhn.op")) {
                    sendSuggestCommand(p, "§c/mhn register [id] [prefix] [suffix] [mode] (金額) §f: ニックネームをレジスタする", "クリックでチャットに打ち込む", "/mhn register ");
                    sendSuggestCommand(p, "§c/mhn unregister [id] §f: ニックネームをアンレジスタする", "クリックでチャットに打ち込む", "/mhn unregister ");
                    sendSuggestCommand(p, "§c/mhn unset [user名] §f: ニックネームを強制アンセットする", "クリックでチャットに打ち込む", "/mhn unset ");
                    sendSuggestCommand(p, "§c/mhn give [user名] [id] §f: ニックネームをギブする", "クリックでチャットに打ち込む", "/mhn give ");
                    sendSuggestCommand(p, "§c/mhn take [user名] [id] §f: ニックネームをテイクする", "クリックでチャットに打ち込む", "/mhn take ");
                    p.sendMessage("§cモードリスト: command buy free pex");
                }
                sendSuggestCommand(p, "§e/mhn set [id] §f: ニックネームをセットする", "クリックでチャットに打ち込む", "/mhn set ");
                sendHoverText(p, "§e/mhn list §f: 使えるニックネームリストを表示", "クリックでリストを見る", "/mhn list");
                sendHoverText(p, "§e/mhn shop §f: 使えるニックネームリストを表示", "クリックでショップを開く", "/mhn shop");
                sendSuggestCommand(p, "§e/mhn buy [id] §f: ニックネームを買う", "クリックでチャットに打ち込む", "/mhn buy ");
                sendHoverText(p, "§e/mhn unset §f: ニックネームをリセットする", "クリックでリセット", "/mhn unset");
                if (list.containsKey(p.getUniqueId())) {
                    p.sendMessage("§e現在のニックネーム: " + list.get(p.getUniqueId()).getPrefix() + p.getName() + list.get(p.getUniqueId()).getSuffix());
                } else {
                    p.sendMessage("§e現在のニックネーム: なし");
                }
                p.sendMessage("§6=============================");
                return true;
            }
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("unset")) {
                if (!p.hasPermission("mhn.op")) {
                    p.sendMessage(prefix + "§cあなたには権限がありません！");
                    return true;
                }
                Player pp = Bukkit.getPlayer(args[1]);
                if (pp == null) {
                    p.sendMessage(prefix + "§cplayerがオフラインです");
                    return true;
                }
                NameTagManager.setTag(pp, "", "");
                list.remove(pp.getUniqueId());
                MHNData.removeNick(pp.getUniqueId());
                p.sendMessage(prefix + "§aアンセットに成功しました。");
                return true;
            }else if(args[0].equalsIgnoreCase("set")) {
                if(!MHNData.registlist().contains(args[1])){
                    p.sendMessage(prefix + "§cそのidは存在しません");
                    return true;
                }
                MHNData.Datas data = MHNData.getRegist(args[1]);
                if(data==null){
                    p.sendMessage(prefix + "§cそのidは存在しません");
                    return true;
                }
                if(data.mode == MHNData.Mode.PEX){
                    if(!p.hasPermission("mhn."+args[1])){
                        p.sendMessage(prefix + "§c権限がありません！");
                        return true;
                    }
                }else if(data.mode == MHNData.Mode.BUY||data.mode == MHNData.Mode.COMMAND){
                    if(!MHNData.listNick(p).contains(args[1])){
                        p.sendMessage(prefix + "§cそのニックネームを使用できません！");
                        return true;
                    }
                }
                String pfix = data.prefix;
                String dfix = data.suffix;
                NameTagManager.setTag(p,pfix,dfix);
                list.put(p.getUniqueId(),new NameTagManager.NameTagData(p.getUniqueId(),pfix,dfix));
                MHNData.saveNick(p,data);
                p.sendMessage(prefix + "§aセットに成功しました。");
                return true;
            }else if(args[0].equalsIgnoreCase("buy")) {
                if(!MHNData.registlist().contains(args[1])){
                    p.sendMessage(prefix + "§cそのidは存在しません");
                    return true;
                }
                if(!config.getString(args[1]+".mode").equalsIgnoreCase("buy")) {
                    p.sendMessage(prefix + "§cこのidは買うタイプのものではありません");
                    return true;
                }
                if(MHNData.listNick(p).contains(args[1])){
                    p.sendMessage(prefix + "§cあなたはすでに 『"+args[1]+"』 を購入しています");
                    return true;
                }
                if(vault.getBalance(p.getUniqueId()) < config.getDouble(args[1]+".money")){
                    p.sendMessage(prefix + "§cお金が足りません! (必要: "+new JPYBalanceFormat(config.getDouble(args[1]+".money")).getString()+")");
                    return true;
                }
                MHNData.buyNick(p,args[1]);
                return true;
            }else if(args[0].equalsIgnoreCase("unregister")) {
                if (!p.hasPermission("mhn.op")) {
                    p.sendMessage(prefix + "§cあなたには権限がありません！");
                    return true;
                }
                if(!MHNData.registlist().contains(args[1])){
                    p.sendMessage(prefix + "§cそのidは存在しません");
                    return true;
                }
                MHNData.unregister(args[1]);
                p.sendMessage(prefix + "§a"+args[1]+"をアンレジスタしました。");
                return true;
            }
        }else if(args.length == 3){
            if(args[0].equalsIgnoreCase("give")) {
                if (!p.hasPermission("mhn.op")) {
                    p.sendMessage(prefix + "§cあなたには権限がありません！");
                    return true;
                }
                if (!MHNData.registlist().contains(args[1])) {
                    p.sendMessage(prefix + "§cそのidは存在しません");
                    return true;
                }
                Player pp = Bukkit.getPlayer(args[2]);
                if (pp == null) {
                    p.sendMessage(prefix + "§cplayerがオフラインです");
                    return true;
                }
                MHNData.addNick(pp, args[1]);
                p.sendMessage(prefix + "§a" + pp.getName() + "に§e" + args[1] + "をギブしました。");
                pp.sendMessage(prefix + "§a" + p.getName() + "から§e『" + args[1] + "』が送られてきました!");
                return true;
            }else if(args[0].equalsIgnoreCase("take")) {
                if (!p.hasPermission("mhn.op")) {
                    p.sendMessage(prefix + "§cあなたには権限がありません！");
                    return true;
                }
                if (!MHNData.registlist().contains(args[1])) {
                    p.sendMessage(prefix + "§cそのidは存在しません");
                    return true;
                }
                Player pp = Bukkit.getPlayer(args[2]);
                if (pp == null) {
                    p.sendMessage(prefix + "§cplayerがオフラインです");
                    return true;
                }
                MHNData.removeNick(pp, args[1]);
                p.sendMessage(prefix + "§a" + pp.getName() + "から§e" + args[1] + "をテイクしました。");
                return true;
            }
        }else if(args.length == 5){
            if(args[0].equalsIgnoreCase("register")) {
                if (!p.hasPermission("mhn.op")) {
                    p.sendMessage(prefix + "§cあなたには権限がありません！");
                    return true;
                }
                if(MHNData.registlist().contains(args[1])){
                    p.sendMessage(prefix + "§cそのidは存在します");
                    return true;
                }
                if(args[2].length() >= 17){
                    p.sendMessage(prefix + "§cprefixが16文字以上です。");
                    return true;
                }
                if(args[3].length() >= 17){
                    p.sendMessage(prefix + "§csuffixが16文字以上です。");
                    return true;
                }
                MHNData.Mode mode = null;
                if(args[4].equalsIgnoreCase("pex")) {
                    mode = MHNData.Mode.PEX;
                }else if(args[4].equalsIgnoreCase("buy")){
                    mode = MHNData.Mode.BUY;
                }else if(args[4].equalsIgnoreCase("free")){
                    mode = MHNData.Mode.FREE;
                }else{
                    mode = MHNData.Mode.COMMAND;
                }
                String pfix = ChatColor.translateAlternateColorCodes('&',args[2]);
                if(pfix.equalsIgnoreCase("null")){
                    pfix = "";
                }
                String dfix = ChatColor.translateAlternateColorCodes('&',args[3]);
                if(dfix.equalsIgnoreCase("null")){
                    dfix = "";
                }
                MHNData.register(args[1],pfix,dfix,mode,0);
                p.sendMessage(prefix + "§aレジスタに成功しました。");
                return true;
            }
        }else if(args.length == 6){
            if(args[0].equalsIgnoreCase("register")) {
                if (!p.hasPermission("mhn.op")) {
                    p.sendMessage(prefix + "§cあなたには権限がありません！");
                    return true;
                }
                if(MHNData.registlist().contains(args[1])){
                    p.sendMessage(prefix + "§cそのidは存在します");
                    return true;
                }
                if(args[2].length() >= 17){
                    p.sendMessage(prefix + "§cprefixが16文字以上です。");
                    return true;
                }
                if(args[3].length() >= 17){
                    p.sendMessage(prefix + "§csuffixが16文字以上です。");
                    return true;
                }
                double setbal;
                try{
                    setbal = Double.parseDouble(args[5]);
                }catch (NumberFormatException e){
                    p.sendMessage(prefix + "§c数字を入力してください");
                    return true;
                }
                String pfix = ChatColor.translateAlternateColorCodes('&',args[2]);
                if(pfix.equalsIgnoreCase("null")){
                    pfix = "";
                }
                String dfix = ChatColor.translateAlternateColorCodes('&',args[3]);
                if(dfix.equalsIgnoreCase("null")){
                    dfix = "";
                }
                MHNData.register(args[1],pfix,dfix, MHNData.Mode.BUY,setbal);
                p.sendMessage(prefix + "§aレジスタに成功しました。");
                return true;
            }
        }
        Inventory inv;
        if (list.containsKey(p.getUniqueId())) {
            inv = Bukkit.createInventory(null,9,"§0現在のニックネーム: "+list.get(p.getUniqueId()).getPrefix() + p.getName() + list.get(p.getUniqueId()).getSuffix());
        } else {
            inv = Bukkit.createInventory(null,9,"§0現在のニックネーム: なし");
        }
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta itemm = item.getItemMeta();
        itemm.setDisplayName("§a§l二つ名設定");
        itemm.setUnbreakable(true);
        itemm.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        List<String> lore = new ArrayList<>();
        lore.add("§e二つ名を設定します");
        itemm.setLore(lore);
        item.setItemMeta(itemm);
        inv.setItem(2,item);
        ItemStack item3 = new ItemStack(Material.GLASS);
        ItemMeta itemm3 = item3.getItemMeta();
        itemm3.setDisplayName("§c§l二つ名リセット");
        itemm3.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
        itemm3.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore3 = new ArrayList<>();
        lore3.add("§a二つ名をリセットします");
        itemm3.setLore(lore3);
        item3.setItemMeta(itemm3);
        inv.setItem(4,item3);
        ItemStack item2 = new ItemStack(Material.CHEST);
        ItemMeta itemm2 = item2.getItemMeta();
        itemm2.setDisplayName("§e§l二つ名ショップ");
        itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
        itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore2 = new ArrayList<>();
        lore2.add("§aショップを開きます！");
        itemm2.setLore(lore2);
        item2.setItemMeta(itemm2);
        inv.setItem(6,item2);
        pstats.put(p.getUniqueId(),"main");
        p.openInventory(inv);
        return true;
    }

    String prefix = "§e[§d§lM§f§lHover§6§lNick§e]§r";
    HashMap<UUID,NameTagManager.NameTagData> list;
    HashMap<UUID,String> pstats;
    FileConfiguration config;
    Man10VaultAPI vault;

    @Override
    public void onEnable() {
        // Plugin startup logic
        list = new HashMap<>();
        pstats = new HashMap<>();
        vault = new Man10VaultAPI("Man10HoverNick");
        MHNData.loadConfig(this);
        getCommand("mhn").setExecutor(this);
        NameTagManager.EnableLoad(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        for(NameTagManager.NameTagData data:list.values()){
            if(data.getUUID().equals(uuid)){
                NameTagManager.setTag(e.getPlayer(),data.getPrefix(),data.getSuffix());
            }else{
                if(Bukkit.getPlayer(data.getUUID())!=null) {
                    NameTagManager.joinLoad(e.getPlayer(), Bukkit.getPlayer(data.getUUID()), data.getPrefix(), data.getSuffix());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(pstats.containsKey(e.getWhoClicked().getUniqueId())){
            e.setCancelled(true);
            if(e.getClickedInventory()==e.getWhoClicked().getInventory()){
                return;
            }
            if(pstats.get(e.getWhoClicked().getUniqueId()).equalsIgnoreCase("main")) {
                Player p = (Player) e.getWhoClicked();
                if (e.getSlot() == 2) {
                    p.closeInventory();
                    p.chat("/mhn list");
                } else if (e.getSlot() == 4) {
                    p.closeInventory();
                    p.chat("/mhn unset");
                } else if (e.getSlot() == 6) {
                    p.closeInventory();
                    p.chat("/mhn shop");
                }
            }else if(pstats.get(e.getWhoClicked().getUniqueId()).equalsIgnoreCase("list")){
                Player p = (Player) e.getWhoClicked();
                int ii = Integer.parseInt(e.getClickedInventory().getItem(49).getItemMeta().getDisplayName());
                if (e.getSlot() >= 45&&e.getSlot() <= 48) {
                    if(ii==1){
                        return;
                    }
                    p.closeInventory();
                    Inventory inv = Bukkit.createInventory(null,54,"§0二つ名リスト page:"+(ii-1));
                    ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
                    ItemMeta itemm = item.getItemMeta();
                    itemm.setDisplayName("§a§l前のページへ戻る");
                    item.setItemMeta(itemm);
                    for(int i = 45; i < 49; i++){
                        inv.setItem(i,item);
                    }
                    ItemStack items = new ItemStack(Material.EMERALD_BLOCK);
                    ItemMeta itemsm = items.getItemMeta();
                    itemsm.setDisplayName("§a§l次のページへ");
                    items.setItemMeta(itemsm);
                    for(int i = 50; i < 54; i++){
                        inv.setItem(i,items);
                    }
                    ItemStack itemss = new ItemStack(Material.COMPASS);
                    ItemMeta itemssm = itemss.getItemMeta();
                    itemssm.setDisplayName(ii-1 +"");
                    itemss.setItemMeta(itemssm);
                    inv.setItem(49,itemss);
                    int i = 0;
                    int iii = (ii - 2) * 44;
                    for (String str : MHNData.registlist()) {
                        if(iii > 0){
                            iii--;
                            continue;
                        }
                        if(i==44){
                            break;
                        }
                        MHNData.Datas data = MHNData.getRegist(str);
                        if (data == null) {
                            continue;
                        }
                        if (data.mode == MHNData.Mode.PEX) {
                            if (!p.hasPermission("mhn." + str)) {
                                if (!p.hasPermission("mhn.op")) {
                                    continue;
                                } else {
                                    ItemStack item2 = new ItemStack(Material.NETHERRACK);
                                    ItemMeta itemm2 = item2.getItemMeta();
                                    itemm2.setDisplayName("§c"+data.name);
                                    itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
                                    itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    List<String> lore2 = new ArrayList<>();
                                    lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                                    lore2.add("§cOP権限で見ています");
                                    itemm2.setLore(lore2);
                                    item2.setItemMeta(itemm2);
                                    inv.setItem(i,item2);
                                    i++;
                                    continue;
                                }
                            }
                        } else if (data.mode == MHNData.Mode.BUY || data.mode == MHNData.Mode.COMMAND) {
                            if (!MHNData.listNick(p).contains(str)) {
                                if (!p.hasPermission("mhn.op")) {
                                    continue;
                                } else {
                                    ItemStack item2 = new ItemStack(Material.NETHERRACK);
                                    ItemMeta itemm2 = item2.getItemMeta();
                                    itemm2.setDisplayName("§c"+data.name);
                                    itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
                                    itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    List<String> lore2 = new ArrayList<>();
                                    lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                                    lore2.add("§cOP権限で見ています");
                                    itemm2.setLore(lore2);
                                    item2.setItemMeta(itemm2);
                                    inv.setItem(i,item2);
                                    i++;
                                    continue;
                                }
                            }
                        }
                        ItemStack item2 = new ItemStack(Material.PAPER);
                        ItemMeta itemm2 = item2.getItemMeta();
                        itemm2.setDisplayName("§a"+data.name);
                        itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
                        itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        List<String> lore2 = new ArrayList<>();
                        lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                        itemm2.setLore(lore2);
                        item2.setItemMeta(itemm2);
                        inv.setItem(i,item2);
                        i++;
                    }
                    pstats.put(p.getUniqueId(),"list");
                    p.openInventory(inv);
                } else if (e.getSlot() >= 50&&e.getSlot() <= 53) {
                    p.closeInventory();
                    Inventory inv = Bukkit.createInventory(null,54,"§0二つ名リスト page:"+(ii+1));
                    ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
                    ItemMeta itemm = item.getItemMeta();
                    itemm.setDisplayName("§a§l前のページへ戻る");
                    item.setItemMeta(itemm);
                    for(int i = 45; i < 49; i++){
                        inv.setItem(i,item);
                    }
                    ItemStack items = new ItemStack(Material.EMERALD_BLOCK);
                    ItemMeta itemsm = items.getItemMeta();
                    itemsm.setDisplayName("§a§l次のページへ");
                    items.setItemMeta(itemsm);
                    for(int i = 50; i < 54; i++){
                        inv.setItem(i,items);
                    }
                    ItemStack itemss = new ItemStack(Material.COMPASS);
                    ItemMeta itemssm = itemss.getItemMeta();
                    itemssm.setDisplayName(ii+1 +"");
                    itemss.setItemMeta(itemssm);
                    inv.setItem(49,itemss);
                    int i = 0;
                    int iii = (ii) * 44;
                    for (String str : MHNData.registlist()) {
                        if(iii != 0){
                            iii--;
                            continue;
                        }
                        if(i==44){
                            break;
                        }
                        MHNData.Datas data = MHNData.getRegist(str);
                        if (data == null) {
                            continue;
                        }
                        if (data.mode == MHNData.Mode.PEX) {
                            if (!p.hasPermission("mhn." + str)) {
                                if (!p.hasPermission("mhn.op")) {
                                    continue;
                                } else {
                                    ItemStack item2 = new ItemStack(Material.NETHERRACK);
                                    ItemMeta itemm2 = item2.getItemMeta();
                                    itemm2.setDisplayName("§c"+data.name);
                                    itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
                                    itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    List<String> lore2 = new ArrayList<>();
                                    lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                                    lore2.add("§cOP権限で見ています");
                                    itemm2.setLore(lore2);
                                    item2.setItemMeta(itemm2);
                                    inv.setItem(i,item2);
                                    i++;
                                    continue;
                                }
                            }
                        } else if (data.mode == MHNData.Mode.BUY || data.mode == MHNData.Mode.COMMAND) {
                            if (!MHNData.listNick(p).contains(str)) {
                                if (!p.hasPermission("mhn.op")) {
                                    continue;
                                } else {
                                    ItemStack item2 = new ItemStack(Material.NETHERRACK);
                                    ItemMeta itemm2 = item2.getItemMeta();
                                    itemm2.setDisplayName("§c"+data.name);
                                    itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
                                    itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    List<String> lore2 = new ArrayList<>();
                                    lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                                    lore2.add("§cOP権限で見ています");
                                    itemm2.setLore(lore2);
                                    item2.setItemMeta(itemm2);
                                    inv.setItem(i,item2);
                                    i++;
                                    continue;
                                }
                            }
                        }
                        ItemStack item2 = new ItemStack(Material.PAPER);
                        ItemMeta itemm2 = item2.getItemMeta();
                        itemm2.setDisplayName("§a"+data.name);
                        itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,1,true);
                        itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        List<String> lore2 = new ArrayList<>();
                        lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                        itemm2.setLore(lore2);
                        item2.setItemMeta(itemm2);
                        inv.setItem(i,item2);
                        i++;
                    }
                    pstats.put(p.getUniqueId(),"list");
                    p.openInventory(inv);
                } else if (e.getSlot() >=0&&e.getSlot() <= 44) {
                    if(e.getClickedInventory().getItem(e.getSlot())==null){
                        return;
                    }
                    if(e.getClickedInventory().getItem(e.getSlot()).getType()==Material.NETHERRACK){
                        return;
                    }
                    p.closeInventory();
                    p.chat("/mhn set "+e.getClickedInventory().getItem(e.getSlot()).getItemMeta().getDisplayName().replace("§a",""));
                }
            }else if(pstats.get(e.getWhoClicked().getUniqueId()).equalsIgnoreCase("shop")){
                Player p = (Player) e.getWhoClicked();
                int ii = Integer.parseInt(e.getClickedInventory().getItem(49).getItemMeta().getDisplayName());
                if (e.getSlot() >= 45&&e.getSlot() <= 48) {
                    if (ii == 1) {
                        return;
                    }
                    p.closeInventory();
                    Inventory inv = Bukkit.createInventory(null, 54,"§0二つ名ショップ page:"+(ii-1));
                    ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
                    ItemMeta itemm = item.getItemMeta();
                    itemm.setDisplayName("§a§l前のページへ戻る");
                    item.setItemMeta(itemm);
                    for (int i = 45; i < 49; i++) {
                        inv.setItem(i, item);
                    }
                    ItemStack items = new ItemStack(Material.EMERALD_BLOCK);
                    ItemMeta itemsm = items.getItemMeta();
                    itemsm.setDisplayName("§a§l次のページへ");
                    items.setItemMeta(itemsm);
                    for (int i = 50; i < 54; i++) {
                        inv.setItem(i, items);
                    }
                    ItemStack itemss = new ItemStack(Material.COMPASS);
                    ItemMeta itemssm = itemss.getItemMeta();
                    itemssm.setDisplayName(ii-1 +"");
                    itemss.setItemMeta(itemssm);
                    inv.setItem(49, itemss);
                    int i = 0;
                    int iii = (ii - 2) * 44;
                    for (String str : MHNData.registlist()) {
                        if(iii > 0){
                            iii--;
                            continue;
                        }
                        if (i == 44) {
                            break;
                        }
                        MHNData.Datas data = MHNData.getRegist(str);
                        if (data == null) {
                            continue;
                        }
                        if (data.mode != MHNData.Mode.BUY) {
                            continue;
                        }
                        if (MHNData.listNick(p).contains(str)) {
                            ItemStack item2 = new ItemStack(Material.STAINED_GLASS, 1, (short) 5);
                            ItemMeta itemm2 = item2.getItemMeta();
                            itemm2.setDisplayName("§c" + data.name);
                            itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                            itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            List<String> lore2 = new ArrayList<>();
                            lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                            lore2.add("§e" + new JPYBalanceFormat(config.getDouble(str + ".money")).getString() + "円 §a購入済み");
                            itemm2.setLore(lore2);
                            item2.setItemMeta(itemm2);
                            inv.setItem(i, item2);
                            i++;
                        } else {
                            ItemStack item2 = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
                            ItemMeta itemm2 = item2.getItemMeta();
                            itemm2.setDisplayName("§a" + data.name);
                            itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                            itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            List<String> lore2 = new ArrayList<>();
                            lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                            lore2.add("§e" + new JPYBalanceFormat(config.getDouble(str + ".money")).getString() + "円 §c未購入");
                            itemm2.setLore(lore2);
                            item2.setItemMeta(itemm2);
                            inv.setItem(i, item2);
                            i++;
                        }
                    }
                    pstats.put(p.getUniqueId(), "shop");
                    p.openInventory(inv);
                } else if (e.getSlot() >= 50&&e.getSlot() <= 53) {
                    p.closeInventory();
                    Inventory inv = Bukkit.createInventory(null, 54,"§0二つ名ショップ page:"+(ii+1));
                    ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
                    ItemMeta itemm = item.getItemMeta();
                    itemm.setDisplayName("§a§l前のページへ戻る");
                    item.setItemMeta(itemm);
                    for (int i = 45; i < 49; i++) {
                        inv.setItem(i, item);
                    }
                    ItemStack items = new ItemStack(Material.EMERALD_BLOCK);
                    ItemMeta itemsm = items.getItemMeta();
                    itemsm.setDisplayName("§a§l次のページへ");
                    items.setItemMeta(itemsm);
                    for (int i = 50; i < 54; i++) {
                        inv.setItem(i, items);
                    }
                    ItemStack itemss = new ItemStack(Material.COMPASS);
                    ItemMeta itemssm = itemss.getItemMeta();
                    itemssm.setDisplayName(ii+1 +"");
                    itemss.setItemMeta(itemssm);
                    inv.setItem(49, itemss);
                    int i = 0;
                    int iii = (ii) * 44;
                    for (String str : MHNData.registlist()) {
                        if(iii != 0){
                            iii--;
                            continue;
                        }
                        if (i == 44) {
                            break;
                        }
                        MHNData.Datas data = MHNData.getRegist(str);
                        if (data == null) {
                            continue;
                        }
                        if (data.mode != MHNData.Mode.BUY) {
                            continue;
                        }
                        if (MHNData.listNick(p).contains(str)) {
                            ItemStack item2 = new ItemStack(Material.STAINED_GLASS, 1, (short) 5);
                            ItemMeta itemm2 = item2.getItemMeta();
                            itemm2.setDisplayName("§c" + data.name);
                            itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                            itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            List<String> lore2 = new ArrayList<>();
                            lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                            lore2.add("§e" + new JPYBalanceFormat(config.getDouble(str + ".money")).getString() + "円 §a購入済み");
                            itemm2.setLore(lore2);
                            item2.setItemMeta(itemm2);
                            inv.setItem(i, item2);
                            i++;
                        } else {
                            ItemStack item2 = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
                            ItemMeta itemm2 = item2.getItemMeta();
                            itemm2.setDisplayName("§a" + data.name);
                            itemm2.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                            itemm2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                            List<String> lore2 = new ArrayList<>();
                            lore2.add("§a表示『" + data.prefix + p.getName() + data.suffix + "§a』");
                            lore2.add("§e" + new JPYBalanceFormat(config.getDouble(str + ".money")).getString() + "円 §c未購入");
                            itemm2.setLore(lore2);
                            item2.setItemMeta(itemm2);
                            inv.setItem(i, item2);
                            i++;
                        }
                    }
                    pstats.put(p.getUniqueId(), "shop");
                    p.openInventory(inv);
                } else if (e.getSlot() >=0&&e.getSlot() <= 44) {
                    if(e.getClickedInventory().getItem(e.getSlot())==null){
                        return;
                    }
                    if(e.getClickedInventory().getItem(e.getSlot()).getDurability() == 5){
                        return;
                    }
                    p.chat("/mhn buy "+e.getClickedInventory().getItem(e.getSlot()).getItemMeta().getDisplayName().replace("§a",""));
                    p.closeInventory();
                    p.chat("/mhn shop");
                }
            }
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        pstats.remove(p.getUniqueId());
    }



    ////////////////////////////////////////////////////////////////////////////////////////////
    //  マインクラフトチャットに、ホバーテキストや、クリックコマンドを設定する関数
    // [例1] sendHoverText(player,"ここをクリック",null,"/say おはまん");
    // [例2] sendHoverText(player,"カーソルをあわせて","ヘルプメッセージとか",null);
    // [例3] sendHoverText(player,"カーソルをあわせてクリック","ヘルプメッセージとか","/say おはまん");
    public static void sendHoverText(Player p,String text,String hoverText,String command){
        //////////////////////////////////////////
        //      ホバーテキストとイベントを作成する
        HoverEvent hoverEvent = null;
        if(hoverText != null){
            BaseComponent[] hover = new ComponentBuilder(hoverText).create();
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
        }

        //////////////////////////////////////////
        //   クリックイベントを作成する
        ClickEvent clickEvent = null;
        if(command != null){
            clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND,command);
        }

        BaseComponent[] message = new ComponentBuilder(text).event(hoverEvent).event(clickEvent). create();
        p.spigot().sendMessage(message);
    }

    //  マインクラフトチャットに、ホバーテキストや、クリックコマンドサジェストを設定する
    public static void sendSuggestCommand(Player p,String text,String hoverText,String command){

        //////////////////////////////////////////
        //      ホバーテキストとイベントを作成する
        HoverEvent hoverEvent = null;
        if(hoverText != null){
            BaseComponent[] hover = new ComponentBuilder(hoverText).create();
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
        }

        //////////////////////////////////////////
        //   クリックイベントを作成する
        ClickEvent clickEvent = null;
        if(command != null){
            clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND ,command);
        }

        BaseComponent[] message = new ComponentBuilder(text). event(hoverEvent).event(clickEvent). create();
        p.spigot().sendMessage(message);
    }
}
