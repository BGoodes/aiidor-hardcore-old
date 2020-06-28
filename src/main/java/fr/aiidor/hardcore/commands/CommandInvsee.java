package fr.aiidor.hardcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.Plugin;

public class CommandInvsee implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			System.out.println("[Aiidor] Vous devez etre un jouer pour effectuer cette commande");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
			player.sendMessage(Plugin.prefix + "§eLa commande s'écrit /invsee <pseudo>.");
			return true;
		}
		
		if (args.length != 1) {
			player.sendMessage(Plugin.prefix + "§cErreur, la commande s'écrit /invsee <pseudo>.");
			return true;
		}
		
		String targetName = args[0];
		
		if (!isConnected(targetName)) {
			player.sendMessage(Plugin.prefix + "§cCe joueur n'est pas connecté ou n'existe pas !");
			return true;
		}
		
		player.openInventory(getPlayer(targetName).getInventory());
		
		return true;
	}

	private Boolean isConnected(String name) {
		return getPlayer(name) != null;
	}
	
	private Player getPlayer(String name) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().equalsIgnoreCase(name)) return p;
		}
		return null;
	}
}
