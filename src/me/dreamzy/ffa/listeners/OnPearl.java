package me.dreamzy.ffa.listeners;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.dreamzy.ffa.FFA;

public class OnPearl implements Listener {

	private final HashMap<String, Long> lastThrow = new HashMap<>();
	private Long time = Long.valueOf(10000L);
	private String msg = "§9(Cooldown) §7You can't use §aEnderpearl §7for §a {sec} seconds§7.";

	@EventHandler
	public void onPlayerUseEP(PlayerInteractEvent e) {
		if ((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)|| (e.getItem() == null) || (e.getItem().getType() != Material.ENDER_PEARL)) {
			return;
		}
		Player p = e.getPlayer();
		Long now = Long.valueOf(System.currentTimeMillis());
		if (validthrow(p, now.longValue())) {
			lastThrow.put(p.getName(), now);
		} else {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public  void onGlitchGlass(PlayerTeleportEvent e) {
		if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {			
			Location target = e.getTo();
			target.setX(target.getBlockX() + 0.5D);
			target.setZ(target.getBlockZ() + 0.5D);
			e.setTo(target);
		}
	}

	private double remainingCooldown(Player p, long throwTime) {
		Long lastPlayerPearl = (Long) lastThrow.get(p.getName());
		return (this.time.longValue() - (throwTime - lastPlayerPearl.longValue())) / 1000.0D;
	}

	private boolean validthrow(Player p, long throwTime) {
		Long lastPlayerPearl = (Long) lastThrow.get(p.getName());
		if ((lastPlayerPearl == null) || (throwTime - lastPlayerPearl.longValue() >= this.time.longValue())) {
			return true;
		}
		sendMessageChecked(p, this.msg.replace("{sec}", String.format("%.1f", new Object[] { Double.valueOf(remainingCooldown(p, throwTime)) })));

		return false;
	}

	private void sendMessageChecked(Player p, String message) {
		p.sendMessage(message);
	}
	

	@EventHandler
	public void OnTeleportEvent(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.ENDER_PEARL) {
			event.setCancelled((event.getFrom().distance(FFA.getInstance().getSpawn()) < 15));
			if (event.isCancelled()) {
				event.getPlayer().sendMessage("§cEnderpearl teleportation cancelled.");
			}
		}
	}
}
