package fr.aiidor.hardcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.Plugin;

public class CommandBroadcast implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			System.out.println("[Aiidor] Vous devez etre un jouer pour effectuer cette commande");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
			player.sendMessage(Plugin.prefix + "§eLa commande s'écrit /" + label.toLowerCase() + " <message>.");
			return true;
		}
		
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i != args.length; i ++) {
			message.append(args[i].replace("&", "§"));
			
			if (i != args.length - 1) message.append(" ");
		}
		
		Bukkit.broadcastMessage(Plugin.prefix + "§d" + message.toString());
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.playSound(p.getLocation(), Sound.ENTITY_EVOKER_PREPARE_WOLOLO, SoundCategory.MASTER, 2, 1);  
		}
		
		return true;
	}

}
