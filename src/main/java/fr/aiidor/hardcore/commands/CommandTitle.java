package fr.aiidor.hardcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.Plugin;

public class CommandTitle implements CommandExecutor {
	
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
		
		StringBuilder line_1 = new StringBuilder();
		StringBuilder line_2 = new StringBuilder();
		
		Boolean next = false;
		
		for (int i = 0; i != args.length; i ++) {
			
			String word = args[i];
			
			if (!next) {
				if (word.equals("//")) {
					next = true;
				} else {
					line_1.append(word.replace("&", "§"));
					if (i < args.length - 1) {
						
						if (!args[i + 1].equals("//")) {
							line_1.append(" ");
						}
					}
				}
			} else {
				line_2.append(word.replace("&", "§"));
				if (i != args.length - 1) line_2.append(" ");
			}
		
		}
		
		if (line_1.length() > 32 || line_2.length() > 32) {
			player.sendMessage(Plugin.prefix + "§cVous ne pouvez pas dépasser plus de 32 caractères par ligne !");
			return true;
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			
			if (!next) line_2.append(" ");
			p.sendTitle(line_1.toString(), line_2.toString(), 0, 60, 60);
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, SoundCategory.MASTER, 0.5f, 1);  
		}

		
		return true;
	}

}
