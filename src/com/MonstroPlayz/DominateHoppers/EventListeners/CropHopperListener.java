package com.MonstroPlayz.DominateHoppers.EventListeners;

import com.MenuAPI.BukkitEventCaller;
import com.MenuAPI.ItemAdder;
import com.MenuAPI.Utils;
import com.MonstroPlayz.DominateHoppers.*;
import com.MonstroPlayz.DominateHoppers.Commands.CmdCropHopper;
import com.MonstroPlayz.DominateHoppers.Events.CropHopperPlaceEvent;
import com.MonstroPlayz.DominateHoppers.Events.CropHopperSellItemEvent;
import com.MonstroPlayz.DominateHoppers.HopperSystem.CropHopper;
import com.MonstroPlayz.DominateHoppers.HopperSystem.CropHopperHandlerList;
import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CropHopperListener implements Listener {

    CropHopperHandlerList cropHopperHandlerList = new CropHopperHandlerList();
    Econ economy = Main.getInstance().getEconomy();
    PriceConfig priceConfig;
    CmdCropHopper cmdCropHopper;

    public CropHopperListener(JavaPlugin javaPlugin) {
        javaPlugin.getServer().getPluginManager().registerEvents(this,javaPlugin);
        priceConfig = new PriceConfig(javaPlugin);
        cmdCropHopper = new CmdCropHopper(javaPlugin);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {


        Item item = e.getEntity();
        Material material = item.getItemStack().getType();
        if (!priceConfig.exists(material)) return;

        Chunk chunk = item.getLocation().getChunk();
        CropHopper cropHopper = cropHopperHandlerList.getChunkHopperList().get(chunk);


        if (cropHopper == null || !cropHopper.isHopperSet()) {
            List<BlockState> bs = Arrays.stream(chunk.getTileEntities()).filter(blockState -> {
                return blockState instanceof Hopper &&
                        MetaDataHandlerList.hasMetaData(blockState.getBlock(), MetaDataKey.IS_A_CROP_HOPPER) &&
                        MetaDataHandlerList.getValue(blockState.getBlock(), MetaDataKey.IS_A_CROP_HOPPER, Boolean.class);
            }).collect(Collectors.toList());
            if (bs.size() <= 0) return;


            Hopper hopper = (Hopper) bs.get(0);

            UUID uuid = UUID.fromString(MetaDataHandlerList.getValue(hopper, MetaDataKey.CROP_HOPPER_OWNER_UUID,String.class));


            cropHopperHandlerList.getChunkHopperList().put(chunk,cropHopper = new CropHopper(uuid,hopper));
        }


        boolean b = cropHopper.isHopperSet();
        if (b) {
            double amount = priceConfig.getSellPrice(material) * item.getItemStack().getAmount();

            CropHopperSellItemEvent cropHopperSellItemEvent = new CropHopperSellItemEvent(cropHopper,material,amount);
            if (!BukkitEventCaller.callEvent(cropHopperSellItemEvent)) {
                e.setCancelled(true);
                OfflinePlayer offlinePlayer = cropHopper.getCachedPlayer();
                if (offlinePlayer != null && (priceConfig.isOfflineFarming() ||
                        offlinePlayer.isOnline())) {
                    economy.getEconomy().depositPlayer(cropHopper.getCachedPlayer(), cropHopperSellItemEvent.getAmount());

                }
            }

        } else {
            cropHopperHandlerList.deregister(chunk);
        }



    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onCropHopperSell(CropHopperSellItemEvent cropHopperSellItemEvent) {

        if (cropHopperSellItemEvent.isCancelled()) return;

        OfflinePlayer offlinePlayer = cropHopperSellItemEvent.getPlayer();
        if (priceConfig.isSellActionBarEnabled() && offlinePlayer != null && offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    Utils.color(priceConfig.getSellActionBarMessage(player, cropHopperSellItemEvent.getAmount()))
            ));
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent e) {
        if (e.isCancelled() || !e.canBuild()) return;

        Block block = e.getBlockPlaced();

        ItemStack itemStack = e.getItemInHand();
        if (itemStack == null || !new NBTItem(itemStack).getBoolean("isaCropHopper")) return;

        if (block != null && block.getState() instanceof Hopper hopper) {
            e.setCancelled(cropHopperHandlerList.getChunkHopperList().containsKey(block.getLocation().getChunk()));
            Player p = e.getPlayer();
            if (!(e.isCancelled() && BukkitEventCaller.callEvent(new CropHopperPlaceEvent(p,hopper)))) {
                MetaDataHandlerList.setValue(block, MetaDataKey.IS_A_CROP_HOPPER, true);
                MetaDataHandlerList.setValue(block, MetaDataKey.CROP_HOPPER_OWNER_UUID, String.valueOf(e.getPlayer().getUniqueId()));
                return;
            }
            p.sendMessage(Utils.color("&cYou cannot place down the crop hopper here as another crop hopper exists this spot!"));
        }

    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        boolean b = MetaDataHandlerList.hasMetaData(e.getBlock(),MetaDataKey.IS_A_CROP_HOPPER) && MetaDataHandlerList.getValue(e.getBlock(),MetaDataKey.IS_A_CROP_HOPPER,Boolean.class);
        e.setCancelled(b);
        if (b && !e.getPlayer().isSneaking())
            e.getPlayer().sendMessage(Utils.color("&cYou must shift left click the block to obtain the hopper!"));
    }

    @EventHandler
    public void onCombust(EntityExplodeEvent e) {
        filter(e.blockList());
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent e) {
        filter(e.blockList());
    }

    void filter(List<Block> blocks) {
        blocks.removeIf(block -> MetaDataHandlerList.hasMetaData(block,MetaDataKey.IS_A_CROP_HOPPER)
                && MetaDataHandlerList.getValue(block,MetaDataKey.IS_A_CROP_HOPPER,Boolean.class));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (e.getAction() == Action.LEFT_CLICK_BLOCK && e.getPlayer().isSneaking() && block != null &&
        block.getState() instanceof Hopper hopper && MetaDataHandlerList.hasMetaData(hopper,MetaDataKey.IS_A_CROP_HOPPER)
                && MetaDataHandlerList.getValue(
                block,MetaDataKey.IS_A_CROP_HOPPER,Boolean.class
        )) {
            ItemAdder.addItem(e.getPlayer(),cmdCropHopper.getInventoryConfig().getItem(ItemSystem.CROP_HOPPER_ITEM));
            block.setType(Material.AIR);
            block.getState().update(true);
        }
    }

}