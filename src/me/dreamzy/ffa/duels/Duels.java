package me.dreamzy.ffa.duels;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;

public class Duels {
	private static Duels instance;

	public ArrayList<Player> in1v1Lobby = new ArrayList<>();
	
	public ArrayList<Player> inDuel = new ArrayList<>();

	public HashMap<Player, Player> hasDueled = new HashMap<>();
	public HashMap<Player, Player> opponent = new HashMap<>();

	public Duels() {
		instance = this;
	}

	public static Duels get() {
		return instance;
	}

	public void requestPlayerIntoDuel(Player requester, Player receiver) {
		if (isAbleToDuels(requester) && isAbleToDuels(receiver)) {
			if (hasDueled.get(receiver) != null && hasDueled.containsKey(receiver)
					&& hasDueled.get(receiver) == requester) {
				acceptDuelRequest(requester, receiver);
				return;
			}
				if (hasDueled.containsKey(requester) && hasDueled.get(requester) == receiver) {
					requester.sendMessage("§cYou have already requested " + receiver.getName() + " into duel.");
					return;
				}
				hasDueled.put(requester, receiver);
				requester.sendMessage("§7You have requested §c" + receiver.getName() + " §7into duel.");
				receiver.sendMessage("§c" + requester.getName()+ " §7has requested you into duel. §aRight-click with sword on him to accept.");
		}
	}

	public void acceptDuelRequest(Player accepter, Player opponent) {
		if (hasDueled.containsKey(accepter)) {
			hasDueled.remove(accepter);
		}
		if (hasDueled.containsKey(opponent)) {
			hasDueled.remove(opponent);
		}
		StartMatch.start(accepter, opponent);
	}

	public boolean isAbleToDuels(Player player) {
		if (player != null) {
			if ((!inDuel.contains(player)) && (!opponent.containsKey(player))) {
				FPlayer player2 = FFA.getInstance().players.get((player).getUniqueId());
				if ((!FFA.getInstance().isInFFA(player2))) {
					if (in1v1Lobby.contains(player)) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public Location get1v1Spawn() {
		return new Location(Bukkit.getWorld("overworld"),	-64.5, 101.5, 0.5, 0, 0);
	}
	
	public Location getArenaSpawn1(){
		return new Location(Bukkit.getWorld("overworld"), -43.5,101.5,0.5,90,0);
	}
	
	public Location getArenaSpawn2(){
		return new Location(Bukkit.getWorld("overworld"), -85.5,101.5,0.5,-90,0);
	}

}
