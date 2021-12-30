package com.MonstroPlayz.DominateHoppers.Commands;

import com.MenuAPI.Configs.InventoryConfig;
import com.MenuAPI.ItemAdder;
import com.MenuAPI.Utils;
import com.MonstroPlayz.DominateHoppers.ItemSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CmdCropHopper implements CommandExecutor {

    InventoryConfig inventoryConfig;

    public CmdCropHopper(JavaPlugin javaPlugin) {
        inventoryConfig = new InventoryConfig(javaPlugin,"inventory-default.yml");
        javaPlugin.getCommand("crophopper").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!commandSender.hasPermission("cropHopper.admin")) {
            commandSender.sendMessage(Utils.color("&cYou cannot use this command"));
            return true;
        }

        Player target = commandSender instanceof Player player ? player :
                strings.length > 0 ? Bukkit.getPlayer(strings[0]) : null;

        try {
            int amount = strings.length > 1 ? Integer.parseInt(strings[1]) : 1;

            if (target == null) {
                sendUsage(commandSender);
                return true;
            }

            ItemStack itemStack = inventoryConfig.getItem(ItemSystem.CROP_HOPPER_ITEM);
            itemStack.setAmount(amount);

            ItemAdder.addItem(target,itemStack);


            target.sendMessage(Utils.color("&eYou have been given "+amount+"x CropHoppers"));
            commandSender.sendMessage(Utils.color("&eYou have given "+target.getName()+" "+amount+"x CropHoppers"));

        } catch (Exception e) {
            e.printStackTrace();
            sendUsage(commandSender);
        }



        return true;
    }

    public InventoryConfig getInventoryConfig() {
        return inventoryConfig;
    }

    void sendUsage(CommandSender commandSender) {
        commandSender.sendMessage(Utils.color("&cUsage: /crophopper [player] [amount]"));
    }


}
