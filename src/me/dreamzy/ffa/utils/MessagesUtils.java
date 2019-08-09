package me.dreamzy.ffa.utils;

public class MessagesUtils {

	private static String errorPrefix = "§cError: ";
	
	public String NoPermissionMessage(){
		return "§cNo permission.";
	}
	
	public String AreNotInSpawnMessage(){
		return errorPrefix + "§cYou aren't in spawn.";
	}
	
	public String IsNotInSpawnMessage(){
		return errorPrefix + "§cThis player isn't in spawn.";
	}
	
	public String IsNotConnectedMessage(){
		return errorPrefix + "§cThis player isn't connected.";
	}
	
	public String SyntaxeErrorMessage(){
		return errorPrefix + "§cIncorrect syntax.";
	}
	
	public String MainColor(){
		return "§e";
	}
	
	public String SecondaryColor(){
		return "§a";
	}
	
	public String ThirdColor(){
		return "§6";
	}
	
}
