package me.dreamzy.ffa.commands.mods;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dreamzy.ffa.FFA;

public class ClearchatCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mod.*")) {
			for (int i = 0; i < 100; i++) {
				for (Player players : FFA.getInstance().getOnlinePlayers()) {
					if (FreezeCommand.getArray().contains(players)) continue;
					
					players.sendMessage("");
				}
			}
			sender.sendMessage("§aChat has been successfully cleared.");
		}
		return false;
	}
}