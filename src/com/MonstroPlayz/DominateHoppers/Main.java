package com.MonstroPlayz.DominateHoppers;

import com.MonstroPlayz.DominateHoppers.EventListeners.CropHopperListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    Econ econ;
    static Main Instance;

    @Override
    public void onEnable() {
        econ = new Econ(Instance = this);
        registerListeners();
    }

    void registerListeners() {
        new CropHopperListener(this);
    }

    public static Main getInstance() {
        return Instance;
    }

    public Econ getEconomy() {
        return econ;
    }
}
