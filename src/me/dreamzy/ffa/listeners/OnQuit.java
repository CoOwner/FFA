package me.dreamzy.ffa.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;
import me.dreamzy.ffa.commands.mods.ModCommand;
import me.dreamzy.ffa.scoreboards.CustomScoreboard;

public class OnQuit implements Listener {

	@EventHandler
	public void onQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		Player player = event.getPlayer();
		FPlayer fplayer = FFA.getInstance().players.get(player.getUniqueId());
		
	    for (CustomScoreboard cs : FFA.getInstance().scoreboards.values()) {
	        cs.refreshSb(true);
	      }
		
		/**
		if (Duels.get().inDuel.contains(player)) {
			Player target = Duels.get().opponent.get(player);
			FPlayer ftarget = FFA.getInstance().players.get(target.getUniqueId());
			byte playerPotions = OnDeath.calculHeals(player);
			byte targetPotions = OnDeath.calculHeals(target);
			target.sendMessage("§c" + player.getName() + " §7has disconnected");
			CustomScoreboard cs = FFA.getInstance().scoreboards.get(ftarget);
			cs.sendSb();
			Duels.get().inDuel.remove(target);
			Duels.get().opponent.remove(target);
			new BukkitRunnable() {
				@Override
				public void run() {
					ftarget.spawnInitialisation();
					ftarget.giveMainItems();
					target.teleport(FFA.getInstance().getSpawn());
					OnDeath.sendEndMessage(target, player, targetPotions, playerPotions);
				}
			}.runTaskLater(FFA.getInstance(), 3 * 20L);
		} else {
			 */
		
		if (ModCommand.inmod.contains(player))return;

		if (player.getLocation().distance(FFA.getInstance().getSpawn()) > 15) {
			fplayer.getStatsManager().addStats("deaths", 1);
			for (Player players : FFA.getInstance().getOnlinePlayers()) {
				players.sendMessage("§c" + player.getName() + "§4[" + fplayer.getStatsManager().getStats("kills")+ "] §ehas disconnected in arena");
			}
		}
		FFA.getInstance().scoreboards.remove(player);
	}
}