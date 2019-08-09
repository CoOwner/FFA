package me.dreamzy.ffa;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PluginRunnable extends BukkitRunnable{
	
	private Plugin plugin;

	public PluginRunnable(Plugin plugin) {
		this.plugin = plugin;
		this.runTaskTimer(this.plugin, 0L, 2*20L);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		for (Player players : FFA.getInstance().getOnlinePlayers()){
			if (!FFA.getInstance().tabs.containsKey(players)) continue;
			if (!players.isOnline()) continue;
			
			FPlayer fP = FFA.getInstance().players.get(players.getUniqueId());
			TabList tab = FFA.getInstance().tabs.get(fP);;
			tab.setSlot(1, "&7&m---------------");
			tab.setSlot(2, "&b&l   Vexus FFA");
			tab.setSlot(3, "&7&m---------------");
		
			tab.setSlot(7, "&3Ping: &f"  + fP.getPing());
			tab.setSlot(9, "&3Online: &f"  + Bukkit.getOnlinePlayers().length);
			
			tab.setSlot(16, "&3Name:" );
			tab.setSlot(17, "&3Ratio: &f1.0" );
			tab.setSlot(18, "&3In Spawn:" );
			
			tab.setSlot(19, "&f" + fP.getName());
			tab.setSlot(21, "&f" + ((Bukkit.getOnlinePlayers().length -  FFA.getInstance().inFFA.size()) - FFA.getInstance().inKitEditor.size()));
			
			tab.setSlot(23, "&3Kills: &f" + fP.getStatsManager().getStats("kills") );
			
			tab.setSlot(25, "&3Rank:" );
			tab.setSlot(26, "&3Deaths: &f" + fP.getStatsManager().getStats("deaths") );
			tab.setSlot(27, "&3In FFA:" );
			
			tab.setSlot(28, "&fDefault" );
			tab.setSlot(30, "&f" + FFA.getInstance().inFFA.size());
			
			tab.setSlot(52, "&7&m---------------");
			tab.setSlot(53, "&7&m---------------");
			tab.setSlot(54, "&7&m---------------");

			tab.setSlot(55, "&3Website:");
			tab.setSlot(56, "&3Teamspeak:");
			tab.setSlot(57, "&3Store:");

			tab.setSlot(58, "&fwww.vexus.rip");
			tab.setSlot(59, "&fts.vexus.rip");
			tab.setSlot(60, "&3&fstore.vexus.rip");

			if (tab.isClient18()) {
				tab.setSlot(68, "&3Warning!");
				tab.setSlot(69, "&3We recommend");
				tab.setSlot(70, "&3use version 1.7");
				tab.setSlot(71, "&3for a better");
				tab.setSlot(72, "&3gaming experience");
				tab.setSlot(73, "&3and more fluid pvp");
			}
		}
	}
}