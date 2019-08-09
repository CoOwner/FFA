package me.dreamzy.ffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;

public class StatsCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return true;

		FPlayer player = FFA.getInstance().players.get(((Entity) sender).getUniqueId());
		player.sendMessage(new String[] { "§7§m-*-----------------------------*-",
				"§6" + player.getName() + "'s stats:",  "§6Kills: §e" + player.getStatsManager().getStats("kills"),
				"§6Deaths: §e" + player.getStatsManager().getStats("deaths"),
				"§7§m-*-----------------------------*-" });
		return false;
	}
}