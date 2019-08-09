package me.dreamzy.ffa.listeners;

import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class OnDrop implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrop(final PlayerDropItemEvent e) {
        final Player p = e.getPlayer();
        if (e.getItemDrop().getItemStack() != null && p.getGameMode() != GameMode.CREATIVE) {
            if (e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD) {
                e.setCancelled(true);
        }
    }
    }
}
