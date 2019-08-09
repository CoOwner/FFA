package me.dreamzy.ffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dreamzy.ffa.utils.DataUtils;

public class HelpopCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ((sender instanceof Player)) {
			Player p = (Player) sender;
			if (args.length >= 1) {
				String message = "";
				for (String part : args) {
					if (message != "")
						message += " ";
					message += part;
				}
				p.sendMessage("§aMessage sent to admins.");
				Player[] arrayOfPlayer;
				@SuppressWarnings("deprecation")
				int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
				for (int i = 0; i < j; i++) {
					Player staff = arrayOfPlayer[i];
					if (staff.hasPermission("admin.helpop.receive")) {
						staff.sendMessage(	"§8[§4Helpop§8] §a" + DataUtils.getPrefix(p) + p.getName() + "§f: " + message);
					}
				}
			} else {
				p.sendMessage("§cUse /helpop <message>");
			}
		}
		return false;
	}
}