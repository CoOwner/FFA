package me.dreamzy.ffa.utils;

import org.bukkit.entity.Player;

import me.dreamzy.ffa.FFA;

public class FilterUtils {
	public static void Alert(Player p, String msg) {
		for (Player staff : FFA.getInstance().getOnlinePlayers()) {
			if (staff.hasPermission("mod.*")) {
				staff.sendMessage("§c[Filtered] " +  msg);
			}
		}
	}
	
	public static void AlertMP(Player p, Player t, String msg) {
		for (Player staff : FFA.getInstance().getOnlinePlayers()) {
			if (staff.hasPermission("mod.*")) {
				staff.sendMessage("§c[Filtered] §7(" + DataUtils.getPrefix(p) + p.getName() + " §7-> " + DataUtils.getPrefix(t) + t.getName() + "§7)§e" + msg);
			}
		}
	}
}