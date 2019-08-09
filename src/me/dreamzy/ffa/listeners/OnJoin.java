package me.dreamzy.ffa.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;
import me.dreamzy.ffa.PluginRunnable;
import me.dreamzy.ffa.TabList;
import me.dreamzy.ffa.scoreboards.CustomScoreboard;

public class OnJoin implements Listener {

	@EventHandler
	public void onJoinEvent(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		FPlayer player = FFA.getInstance().players.get(event.getPlayer().getUniqueId());

		FFA.getInstance().hideModeratorsForObserver(event.getPlayer());

		for (CustomScoreboard cs : FFA.getInstance().scoreboards.values()) {
			cs.refreshSb(false);
		}
		CustomScoreboard cs = new CustomScoreboard(player);
		cs.sendSb();

		player.giveMainItems();
		player.spawnInitialisation();
		player.teleport(FFA.getInstance().getSpawn());
	}

	@EventHandler
	public void onPreLoginEvent(PlayerLoginEvent event) {
		if (event.getResult() == Result.ALLOWED) {
			FPlayer player = new FPlayer(event.getPlayer());
			FFA.getInstance().players.put(player.getUniqueId(), player);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		final FPlayer fP = FFA.getInstance().players.get(e.getPlayer().getUniqueId());
		
		if (fP == null) return;
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (FFA.getInstance().tabs.containsKey(fP)) return;
				FFA.getInstance().tabs.put(fP, TabList.createTabList(fP));
				TabList tab = FFA.getInstance().tabs.get(fP);
				tab.setSlot(1, "&7&m---------------");
				tab.setSlot(2, "&c&l   FragMC FFA");
				tab.setSlot(3, "&7&m---------------");
				
				tab.setSlot(7, "&cPing: &f"  + fP.getPing());
				tab.setSlot(9, "&cOnline: &f"  + Bukkit.getOnlinePlayers().length);
				
				tab.setSlot(16, "&cName:" );
				tab.setSlot(17, "&cRatio: &f1.0" );
				tab.setSlot(18, "&cIn Spawn:" );
				
				tab.setSlot(19, "&f" + fP.getName());
				tab.setSlot(21, "&f" + ((Bukkit.getOnlinePlayers().length -  FFA.getInstance().inFFA.size()) - FFA.getInstance().inKitEditor.size()));
				
				tab.setSlot(23, "&cKills: &f0" );
				
				tab.setSlot(25, "&cRank:" );
				tab.setSlot(26, "&cDeaths: &f0" );
				tab.setSlot(27, "&cIn FFA:" );
				
				tab.setSlot(28, "&fDefault" );
				tab.setSlot(30, "&f" + FFA.getInstance().inFFA.size());
				
				tab.setSlot(52, "&7&m---------------");
				tab.setSlot(53, "&7&m---------------");
				tab.setSlot(54, "&7&m---------------");

				tab.setSlot(55, "&cWebsite:");
				tab.setSlot(56, "&cTeamspeak:");
				tab.setSlot(57, "&cStore:");

				tab.setSlot(58, "&fwww.fragmc.com");
				tab.setSlot(59, "&fts.fragmc.com");
				tab.setSlot(60, "&c&fstore.fragmc.com");

				if (tab.isClient18()) {
					tab.setSlot(68, "&cWarning!");
					tab.setSlot(69, "&cWe recommend");
					tab.setSlot(70, "&cuse version 1.7");
					tab.setSlot(71, "&cfor a better");
					tab.setSlot(72, "&cgaming experience");
					tab.setSlot(73, "&cand more fluid pvp");
				}
			}
		}.runTaskLater(FFA.getInstance(), 1L);
		
		new PluginRunnable(FFA.getInstance());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		TabList.deleteTabList(event.getPlayer());
	}
}