package com.MonstroPlayz.DominateHoppers.HopperSystem;

import com.MonstroPlayz.DominateHoppers.MetaDataHandlerList;
import com.MonstroPlayz.DominateHoppers.MetaDataKey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;

import java.util.UUID;

public class CropHopper {

    private final UUID uuid;
    private OfflinePlayer cachedPlayer;
    private Location location;
    private Hopper hopper;

    public Location getLocation() {
        return location;
    }

    public void setHopper(Hopper hopper) {
        this.location = (this.hopper = hopper) != null ? hopper.getLocation() : null;
    }

    public Hopper getHopper() {
        if (hopper != null) return hopper;
        Block block;
        return location != null && (block = location.getWorld().getBlockAt(location)) != null &&
        block.getState() instanceof Hopper
                 hopper ? (this.hopper = hopper) : null;
    }

    public boolean isHopperSet() {
        return getHopper() != null && MetaDataHandlerList.getValue(getHopper(), MetaDataKey.IS_A_CROP_HOPPER, boolean.class) &&
                MetaDataHandlerList.getValue(getHopper(),MetaDataKey.CROP_HOPPER_OWNER_UUID,String.class) != null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public OfflinePlayer getCachedPlayer() {
        return cachedPlayer != null ? cachedPlayer : uuid != null ? (this.cachedPlayer = Bukkit.getOfflinePlayer(uuid))
                : null;
    }

    public CropHopper(UUID uuid,Hopper hopper) {
        this.uuid = uuid;
        setHopper(hopper);
    }

    public CropHopper(OfflinePlayer offlinePlayer,Hopper hopper) {
        this(offlinePlayer.getUniqueId(),hopper);
    }



}
