package me.dreamzy.ffa.duels;

import org.bukkit.entity.Player;

public class MatchUtils{
	private Player player1;
	private boolean isInMatch;
	
	public MatchUtils(Player p1){
		p1 = player1;
		isInMatch = false;
	}
		
	public boolean isInMatch(){
		return isInMatch;
	}
	
	public void toggleInMatch(){
		isInMatch = (isInMatch() ? false : true);
	}
}