package me.dreamzy.ffa.listeners;

import java.util.WeakHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.dreamzy.ffa.utils.ChatUtils;
import me.dreamzy.ffa.utils.FilterUtils;

public class OnChat implements Listener {
	private static WeakHashMap<Player, Long> cooldown = new WeakHashMap<Player, Long>();

	@EventHandler
	public void onSpamChat(AsyncPlayerChatEvent e) {
		if (e.isCancelled())return;
		if (e.getPlayer() instanceof Player) {
			Player p = e.getPlayer();

			e.setFormat(p.getDisplayName() + ": %2$s");
			
			if (p.hasPermission("youtuber.bypass")) {
				return;
			}
			if (!isInCooldown(p)) {
				applyCooldown(p);

				if (ChatUtils.hasBlacklistedWord(e.getMessage(), p)) {
					p.sendMessage(e.getFormat());
					FilterUtils.Alert(p, e.getFormat());
					e.setCancelled(true);
					return;
				}
			} else {
				e.setCancelled(true);
				p.sendMessage("§cYou need to wait 3 seconds to speak again.");
			}
		}
	}
	
	public boolean isInCooldown(Player p) {
		if (!cooldown.containsKey(p)) {
			return false;
		}
		return ((Long) cooldown.get(p)).longValue() > System.currentTimeMillis();
	}

	public long getCooldown(Player p) {
		if (cooldown.containsKey(p)) {
			return Math.max(0L, ((Long) cooldown.get(p)).longValue() - System.currentTimeMillis());
		}
		return 0L;
	}

	public void applyCooldown(Player p) {
		cooldown.put(p, Long.valueOf(System.currentTimeMillis() + 3 * 1000));
	}
}