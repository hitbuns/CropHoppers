package com.MonstroPlayz.DominateHoppers;

import com.MenuAPI.Config;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PriceConfig extends Config {

    public PriceConfig(JavaPlugin javaPlugin) {
        super(javaPlugin, javaPlugin.getDataFolder(), "item-prices.yml", "default.yml");
    }

    public boolean exists(Material material) {
        return this.contains("prices."+material.name());
    }

    public double getSellPrice(Material material) {
        return this.getDouble("prices."+material.name(),0);
    }

    public boolean isOfflineFarming() {
        return this.getBoolean("offline-farming",true);
    }

    public boolean isSellActionBarEnabled() {
        return this.getBoolean("actionbar.enabled",false);
    }

    public String getSellActionBarMessage(Player player,double amount) {
        return this.getString("actionbar.message","&a+$%amount%").replace("%amount%",String.valueOf(amount))
                .replace("%player%",player.getName());
    }

}
