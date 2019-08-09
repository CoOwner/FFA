package me.dreamzy.ffa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dreamzy.ffa.FFA;

public class DebugCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			for (Player players : FFA.getInstance().getOnlinePlayers()) {
				FFA.getInstance().hidePlayerForObserver(players, player);
				FFA.getInstance().showPlayerForObserver(players, player);
				FFA.getInstance().hideModeratorsForObserver(player);
			}
			player.sendMessage("§aDone.");
		} else {
			sender.sendMessage("§cOnly executable by players.");
		}
		return false;
	}
}