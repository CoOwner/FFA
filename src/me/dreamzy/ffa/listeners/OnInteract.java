package me.dreamzy.ffa.listeners;

import org.bukkit.WeatherType;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;
import me.dreamzy.ffa.duels.Duels;
import me.dreamzy.ffa.gui.PlayGui;
import me.dreamzy.ffa.kits.DuelsKit;
import me.dreamzy.ffa.kits.editkit.EditKit;

public class OnInteract implements Listener {

	@EventHandler
	public void onItemClick(PlayerInteractEvent event) {
		FPlayer player = FFA.getInstance().players.get(event.getPlayer().getUniqueId());

		if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				event.setCancelled(true);
				if (FFA.getInstance().isInFFA(player))
					return;
				Sign sign = (Sign) event.getClickedBlock().getState();
				String[] l = sign.getLines();

				if (EditKit.get().getInEditKit().contains(player) || FFA.getInstance().inFFA.contains(player)) {
					FFA.getInstance().inFFA.remove(player);
					EditKit.get().getInEditKit().remove(player);
					player.teleport(FFA.getInstance().getSpawn());
					player.spawnInitialisation();
					player.sendMessage("§cError, try again.");
				}
				
				for (String lignes : l) {
						if (lignes.toLowerCase().contains("ffa")) {
							PlayGui.get().openGui(player);
					} else if (lignes.toLowerCase().contains("1v1")) {
							for (Player players : Duels.get().inDuel) {
								FFA.getInstance().hidePlayerForObserver(player, players);
							}
							Duels.get().in1v1Lobby.add(player);
							player.sendMessage(new String[] { "","§7You have been teleported to §c1v1 arena§7. §aRight-click on a player with your sword to request him into duel." });
							DuelsKit.giveKit(player);
							for (Player players : FFA.getInstance().getOnlinePlayers()) {
								player.showPlayer(players);
								FFA.getInstance().hideModeratorsForObserver(player);
							}
							player.teleport(Duels.get().get1v1Spawn());
					}
				}
			}
		}
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		FPlayer fplayer = FFA.getInstance().players.get(player.getUniqueId());
		Action action = event.getAction();

		if (FFA.getInstance().isInFFA(fplayer))
			return;
		if (fplayer.getLocation().distance(Duels.get().get1v1Spawn()) > 20)
			return;

		if (((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK))
				&& (fplayer.getItemInHand().getItemMeta() != null)
				&& (fplayer.getItemInHand().getItemMeta().getDisplayName() != null)) {
			if (fplayer.getItemInHand().getItemMeta().getDisplayName().contains("Back to spawn")) {
				event.setCancelled(true);
				Duels.get().in1v1Lobby.remove(player);
				fplayer.teleport(FFA.getInstance().getSpawn());
				fplayer.spawnInitialisation();
				fplayer.giveMainItems();
				fplayer.sendMessage(new String[] { "", "§7You have been teleported to spawn." });
			}
		}
	}

	@EventHandler
	public void onItemClick(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		FPlayer fplayer = FFA.getInstance().players.get(player.getUniqueId());

		if (event.getItemDrop() == null && event.getItemDrop().getType() == null)
			return;

		if (Duels.get().inDuel.contains(player))
			return;
		if (player.isOp())
			return;
		event.setCancelled(!FFA.getInstance().isInFFA(fplayer));
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		event.setCancelled(true);
		for (Player players : FFA.getInstance().getOnlinePlayers()) {
			players.setPlayerWeather(WeatherType.CLEAR);
		}
	}
}