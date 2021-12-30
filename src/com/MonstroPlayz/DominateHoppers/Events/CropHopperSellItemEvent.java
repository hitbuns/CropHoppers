package com.MonstroPlayz.DominateHoppers.Events;

import com.MonstroPlayz.DominateHoppers.HopperSystem.CropHopper;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CropHopperSellItemEvent extends Event implements Cancellable,iPlayerEvent {

    public static HandlerList handlerList = new HandlerList();
    private boolean cancel = false;
    private OfflinePlayer offlinePlayer;
    private final CropHopper cropHopper;
    private final Material material;
    private double amount;


    public CropHopperSellItemEvent(CropHopper cropHopper, Material material, double amount) {
        this.cropHopper = cropHopper;
        this.material = material;
        setAmount(amount);
        evaluate();
    }

    public CropHopper getCropHopper() {
        return cropHopper;
    }

    public Material getMaterial() {
        return material;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = Math.max(0,amount);
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
        cancel = b;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public OfflinePlayer getPlayer() {
        return offlinePlayer != null ? offlinePlayer : (this.offlinePlayer = evaluate());
    }

    OfflinePlayer evaluate() {
        return cropHopper != null && cropHopper.getCachedPlayer() != null ? cropHopper.getCachedPlayer() :
                null;
    }
}
