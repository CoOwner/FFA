package me.dreamzy.ffa.duels.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.dreamzy.ffa.duels.Duels;

public class OnPlayerRightclick implements Listener {

	@EventHandler
	public void onClickPlayer(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		Player t = (Player) e.getRightClicked();
		if (t != null && t != p) {
			if (p.getItemInHand().getItemMeta() != null && p.getItemInHand().getItemMeta().getDisplayName() != null
					&& p.getItemInHand().getItemMeta().getDisplayName().contains("Duel players")) {
				Duels.get().requestPlayerIntoDuel(p, t);
			}
		}
	}
}
