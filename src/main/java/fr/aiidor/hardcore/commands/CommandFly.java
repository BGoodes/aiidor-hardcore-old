package fr.aiidor.hardcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.Plugin;

public class CommandFly implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			System.out.println("[Aiidor] Vous devez etre un jouer pour effectuer cette commande");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length != 0) {
			player.sendMessage(Plugin.prefix + "§cErreur, la commande s'écrit /fly.");
			return true;
		}
		
		if (!player.getAllowFlight()) {
			
			player.setAllowFlight(true);
			player.sendMessage(Plugin.prefix + "§aVous pouvez désormais voler !");
			
		} else {
			
			player.setAllowFlight(false);
			player.sendMessage(Plugin.prefix + "§aVous ne pouvez désormais plus voler !");
		}
		
		return true;
	}

}
