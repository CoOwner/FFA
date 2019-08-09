package me.dreamzy.ffa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;

public class OnThrowPotions implements Listener {
	
	@EventHandler
	public void onPotionSplash(final PotionSplashEvent event) {
		@SuppressWarnings("deprecation")
		FPlayer player = FFA.getInstance().players.get(((Player) event.getEntity().getShooter()).getUniqueId());
		if (player.isSprinting() && event.getIntensity(player) > 0.6 && event.getIntensity(player) < 0.95) {
			event.setIntensity(player, 0.9);
		}
	}
}