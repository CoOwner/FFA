package me.dreamzy.ffa.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;
import me.dreamzy.ffa.commands.mods.FreezeCommand;
import me.dreamzy.ffa.commands.mods.ModCommand;
import me.dreamzy.ffa.duels.Duels;
import me.dreamzy.ffa.kits.editkit.EditKit;
import me.dreamzy.ffa.scoreboards.CustomScoreboard;
import net.minecraft.server.v1_7_R4.EnumClientCommand;
import net.minecraft.server.v1_7_R4.PacketPlayInClientCommand;

public class OnDeath implements Listener {
	private String deathMessage;

	@EventHandler
	public void onDeathEvent(PlayerDeathEvent event) {
		if (event.getEntity() instanceof Player) {

			if (Duels.get().inDuel.contains(event.getEntity())) {
				deathMessage = event.getDeathMessage();
				event.setDeathMessage(null);
				event.getDrops().clear();
				event.setDroppedExp(0);
				Player loser = event.getEntity();
				Player winner = Duels.get().opponent.get(loser);
				deathMessage = "§c" + deathMessage.replaceAll(loser.getName(), "§c" + loser.getName() + "§7").replaceAll(winner.getName(), "§a" + winner.getName() + "§7");
				winner.sendMessage(deathMessage);
				loser.sendMessage(deathMessage);
				byte winnerPotions = calculHeals(winner);
				byte loserPotions = calculHeals(winner);
				Duels.get().inDuel.remove(loser);
				Duels.get().inDuel.remove(winner);
				Duels.get().opponent.remove(loser);
				Duels.get().opponent.remove(winner);
				for (Player players : FFA.getInstance().getOnlinePlayers()) {
					winner.showPlayer(players);
					FFA.getInstance().hideModeratorsForObserver(winner);
				}
				FPlayer fwinner = FFA.getInstance().players.get(winner.getUniqueId());
				FPlayer floser = FFA.getInstance().players.get(loser.getUniqueId());
				CustomScoreboard cs1 = FFA.getInstance().scoreboards.get(fwinner);
				CustomScoreboard cs2 = FFA.getInstance().scoreboards.get(floser);
				cs1.sendSb();
				cs2.sendSb();

				new BukkitRunnable() {
					@Override
					public void run() {
						FPlayer fwinner = FFA.getInstance().players.get(winner.getUniqueId());
						fwinner.clearInventory(true);
						fwinner.spawnInitialisation();
						fwinner.giveMainItems();
						winner.teleport(FFA.getInstance().getSpawn());
						sendEndMessage(winner, loser, winnerPotions, loserPotions);
					}
				}.runTaskLater(FFA.getInstance(), 3 * 20L);
			}

			Player victim = event.getEntity();
			FPlayer fVictim = FFA.getInstance().players.get(event.getEntity().getUniqueId());
			fVictim.getStatsManager().addStats("deaths", 1);

			victim.getWorld().strikeLightningEffect(victim.getLocation());

			if (event.getEntity().getKiller() != null) {
				FPlayer attacker = FFA.getInstance().players.get(event.getEntity().getKiller().getUniqueId());

				attacker.getStatsManager().addStats("kills", 1);

				deathMessage = "§e" + event.getDeathMessage().replaceAll(fVictim.getName(),"§c" + fVictim.getName() + "§4[" + fVictim.getStatsManager().getStats("kills") + "]§e").replaceAll(attacker.getName(), "§c" + attacker.getName() + "§4["+ attacker.getStatsManager().getStats("kills") + "]§e");
				event.setDeathMessage(deathMessage);

				attacker.playSound(attacker.getLocation(), Sound.NOTE_PLING, 2, 5);
				attacker.setHealth(20.0);
			} else {
				deathMessage = "§e" + event.getDeathMessage().replaceAll(fVictim.getName(),"§c" + fVictim.getName() + "§4[" + fVictim.getStatsManager().getStats("kills") + "]§e");
				event.setDeathMessage(deathMessage);
			}

			new BukkitRunnable() {

				@Override
				public void run() {
					((CraftPlayer) victim).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
				}

			}.runTaskLater(FFA.getInstance(), 1L);
		}
	}

	public static byte calculHeals(Player player) {
		byte playerpotions = 0;
		ItemStack heal = new ItemStack(Material.POTION, 1, (short) 16421);
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null)
				continue;
			if (item.isSimilar(heal)) {
				playerpotions++;
			}
		}
		return playerpotions;
	}

	public static void sendEndMessage(Player player1, Player player2, Byte player1potions, Byte player2potions) {
		player1.sendMessage("§7§m-----------------------------------");
		player1.sendMessage("§6Winner: §e" + player1.getName());
		player1.sendMessage("§6Potions: §a" + player1.getName() + "§7[§e" + player1potions + "§7]§6, §c"
				+ player2.getName() + "§7[§e" + player2potions + "§7]");
		player1.sendMessage("§7§m-----------------------------------");

		player2.sendMessage("§7§m-----------------------------------");
		player2.sendMessage("§6Winner: §e" + player1.getName());
		player2.sendMessage("§6Potions: §a" + player1.getName() + "§7[§e" + player1potions + "§7]§6, §c"
				+ player2.getName() + "§7[§e" + player2potions + "§7]");
		player2.sendMessage("§7§m-----------------------------------");
	}

	@EventHandler
	public void onRespawnEvent(PlayerRespawnEvent event) {
		FPlayer fplayer = FFA.getInstance().players.get(event.getPlayer().getUniqueId());
		Player player = event.getPlayer();
		player.sendMessage("§aRespawned!");
		if (FreezeCommand.getArray().contains(player)) {
			event.setRespawnLocation(FreezeCommand.getFrozePoint());
		} else {
			event.setRespawnLocation(FFA.getInstance().getSpawn());
		}
		if (ModCommand.inmod.contains(player)) {
			ModCommand.inmod.remove(player);
		} else if (EditKit.get().getInEditKit().contains(player)) {
			EditKit.get().getInEditKit().remove(player);
		} else if (FFA.getInstance().inFFA.contains(player)) {
			FFA.getInstance().inFFA.remove(player);
		}
		for (Player players : FFA.getInstance().getOnlinePlayers()) {
			player.showPlayer(players);
			FFA.getInstance().hideModeratorsForObserver(player);
		}
		fplayer.spawnInitialisation();
		fplayer.giveMainItems();
	}
}