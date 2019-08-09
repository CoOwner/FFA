package me.dreamzy.ffa.duels;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DuelCountdown extends BukkitRunnable {

	private Player one, two;
	private int count;
	private Plugin plugin;

	public DuelCountdown(Player p, Player target, Integer count, Plugin plugin) {
		this.one = p;
		this.two = target;
		this.count = count;
		this.plugin = plugin;
		this.runTaskTimer(this.plugin, 0L, 20L);
	}

	@Override
	public void run() {
		if (!one.isOnline() || !two.isOnline()) {
			cancel();
			return;
		}
		switch (count) {
		case 4:
		case 3:
		case 2:
		case 1:
			one.sendMessage("§6Match starts in §a" + count + " §6second" + (count > 1 ? "s" : "") + ".");
			two.sendMessage("§6Match starts in §a" +  count +  " §6second" + (count > 1 ? "s" : "") + ".");
			one.playSound(one.getLocation(), Sound.NOTE_PLING, 5.0F, 1.0F);
			two.playSound(two.getLocation(), Sound.NOTE_PLING, 5.0F, 1.0F);
			break;
		case 0:
			one.showPlayer(two);
			two.showPlayer(one);
			one.playSound(one.getLocation(), Sound.NOTE_PLING, 5.0F, 2.0F);
			one.sendMessage("§aMatch started!");
			two.playSound(two.getLocation(), Sound.NOTE_PLING, 5.0F, 2.0F);
			two.sendMessage("§aMatch started!");
			cancel();
			return;

		}
		count--;
	}
}
