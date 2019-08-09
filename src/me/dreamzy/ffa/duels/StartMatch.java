package me.dreamzy.ffa.duels;

import org.bukkit.entity.Player;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.kits.editkit.EditKit;

public class StartMatch{
	
	public static void start(Player player1, Player player2){
		if (player1 != null && player2 != null && player1.isOnline() && player2.isOnline()) {
			for (Player players : FFA.getInstance().getOnlinePlayers()) {
				if (Duels.get().get1v1Spawn().distance(players.getLocation()) < 20) {
					player1.hidePlayer(players);
					player2.hidePlayer(players);
				}
			}
			player1.showPlayer(player2);
			player2.showPlayer(player1);
			
			Duels.get().opponent.put(player1, player2);
			Duels.get().opponent.put(player2, player1);
			Duels.get().inDuel.add(player1);
			Duels.get().inDuel.add(player2);
			player1.teleport(Duels.get().getArenaSpawn1());
			player2.teleport(Duels.get().getArenaSpawn2());
			//FPlayer fplayer1 = FFA.getInstance().players.get(player1.getUniqueId());
			//FPlayer fplayer2 = FFA.getInstance().players.get(player2.getUniqueId());
			//CustomScoreboard cs1 = FFA.getInstance().scoreboards.get(fplayer1);
			//CustomScoreboard cs2 = FFA.getInstance().scoreboards.get(fplayer2);
			//cs1.sendFightSb(fplayer2);
			//cs2.sendFightSb(fplayer1);
			EditKit.get().sendBook(player1, "NoDebuff");
			EditKit.get().sendBook(player2, "NoDebuff");
			player1.sendMessage("§6Starting match against §a" + player2.getName() + "§6.");
			player2.sendMessage("§6Starting match against §a" + player1.getName() + "§6.");
			new DuelCountdown(player1, player2, 4, FFA.getInstance());
		}
	}
}