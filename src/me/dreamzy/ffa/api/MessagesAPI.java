package me.dreamzy.ffa.api;

import org.bukkit.entity.Player;

public class MessagesAPI {
	
	private Player sender;
	private Player target;
	
	public MessagesAPI(Player sender, Player target) {
		this.sender = sender;
		this.target = target;
	}
	
	public Player getSender() {
		return this.sender;
	}
	
	public Player getTarget() {
		return this.target;
	}
	
	public void setSender(Player p) {
		this.sender = p;
	}
	
	public void setTarget(Player p) {
		this.target = p;
	}

}