package me.dreamzy.ffa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;
import me.dreamzy.ffa.duels.Duels;

public class OnDamage implements Listener {
	@EventHandler
	public void onDamageEvent(EntityDamageByEntityEvent event) {
		if ((event.getEntity() instanceof Player)) {
			FPlayer fVictim = (FPlayer) FFA.getInstance().players.get(event.getEntity().getUniqueId());
			Player victim = (Player) event.getEntity();
			Player attacker = victim.getKiller();
			
			if (Duels.get().in1v1Lobby.contains(attacker)) {
				event.setCancelled(!Duels.get().inDuel.contains(attacker));
				
				return;
			}
			if (Duels.get().in1v1Lobby.contains(victim)) {
				event.setCancelled(!Duels.get().inDuel.contains(victim));
				return;
			}

			event.setCancelled(!FFA.getInstance().isInFFA(fVictim));
		}
	}

	@EventHandler
	public void onDamageEvent(EntityDamageEvent event) {
		if ((event.getEntity() instanceof Player)) {
			FPlayer fVictim = (FPlayer) FFA.getInstance().players.get(event.getEntity().getUniqueId());
			Player victim = (Player) event.getEntity();
			
			if (Duels.get().in1v1Lobby.contains(victim)) {
				event.setCancelled(!Duels.get().inDuel.contains(victim));
				return;
			}
			
			event.setCancelled(!FFA.getInstance().isInFFA(fVictim));
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	@EventHandler
	public void onFoodChangeEvent(FoodLevelChangeEvent event) {
		FPlayer player = (FPlayer) FFA.getInstance().players.get(event.getEntity().getUniqueId());

		event.setCancelled(!FFA.getInstance().isInFFA(player));
		if (event.isCancelled()) {
			player.setFoodLevel(20);
			player.setSaturation(20.0F);
		}
	}
}