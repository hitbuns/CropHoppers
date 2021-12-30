package com.MonstroPlayz.DominateHoppers.Events;

import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CropHopperPlaceEvent extends PlayerEvent implements Cancellable {

    public static HandlerList handlerList = new HandlerList();
    private boolean cancel = false;
    private final Hopper hopper;

    public CropHopperPlaceEvent(Player who, Hopper hopper) {
        super(who);
        this.hopper =hopper;
    }

    public Hopper getHopper() {
        return hopper;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
        cancel = b;
    }
}
