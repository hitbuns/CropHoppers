package com.MonstroPlayz.DominateHoppers.HopperSystem;

import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Hopper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CropHopperHandlerList {

    private final Map<Chunk,CropHopper> chunkHopperList = new HashMap<>();

    public Map<Chunk, CropHopper> getChunkHopperList() {
        return chunkHopperList;
    }

    public void register(CropHopper cropHopper,boolean force) {
        if (cropHopper != null && cropHopper.isHopperSet()) {
            Chunk chunk = cropHopper.getLocation().getChunk();
            if (force || !chunkHopperList.containsKey(chunk))
                chunkHopperList.put(cropHopper.getLocation().getChunk(),cropHopper);
        }
    }

    public void register(Hopper hopper, UUID uuid, boolean force) {
        if (hopper != null) register(new CropHopper(uuid,hopper),force);
    }

    public void register(Hopper hopper, OfflinePlayer player, boolean force) {
        if (hopper != null && player != null) register(new CropHopper(player,hopper),force);
    }

    public void deregister(Chunk chunk) {
        chunkHopperList.remove(chunk);
    }

    public void deregister(UUID uuid) {
        chunkHopperList.values().removeIf(cropHopper -> cropHopper.isHopperSet()||cropHopper.getUuid()
        == uuid);
    }

    public void deregister (OfflinePlayer player) {
        if (player != null) deregister(player.getUniqueId());
    }

}
