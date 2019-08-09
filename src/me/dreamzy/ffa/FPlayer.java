package me.dreamzy.ffa;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.WeatherType;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.dreamzy.ffa.files.StatsConfig;

public class FPlayer extends CraftPlayer {
		
	private StatsConfig eloConfig;
	
	public FPlayer(Player p) {
		super(((CraftServer) Bukkit.getServer()), ((CraftPlayer) p).getHandle());
		eloConfig = new StatsConfig(this);
	}

	public void spawnInitialisation() {
		setPlayerWeather(WeatherType.CLEAR);
		setGameMode(GameMode.SURVIVAL);
		setPlayerTime(6000, false);
		setFlying(false);
		setAllowFlight(false);
		setFoodLevel(20);
		setSaturation(20);
		setHealth(20.0);
		setMaxHealth(20.0);
		setMaximumNoDamageTicks(20);
		setNoDamageTicks(10);
		setWalkSpeed(0.2f);
		setFireTicks(0);
		for (PotionEffect potion : getActivePotionEffects()) {
			removePotionEffect(potion.getType());
		}
	}

	public void giveMainItems() {
		clearInventory(true);
		updatePInventory();
	}

	public Integer getPing() {
			return getHandle().ping;
	}

	public void clearInventory(Boolean armor) {
		getInventory().clear();
		if (armor) {
			getInventory().setArmorContents(new ItemStack[] { null, null, null, null });
		}
	}

	@SuppressWarnings("deprecation")
	public void updatePInventory() {
		updateInventory();
	}
	
	public StatsConfig getStatsManager() {
		return eloConfig;
	}
	
	public boolean isAuthorized(String permission){
		return hasPermission(permission);
	}
	
}
