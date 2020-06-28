package fr.aiidor.hardcore.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.managers.EconManager;

public class CommandEcon implements CommandExecutor, TabCompleter {

	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0) {
			sendMessage(sender, "[Aiidor]§2Les commandes :");
			sendMessage(sender, "  §6/econ set §c<joueur> §e<montant>");
			sendMessage(sender, "  §6/econ add §c<joueur> §e<montant>");
			sendMessage(sender, "  §6/econ remove §c<joueur> §e<montant>");
			sendMessage(sender, "  §6/econ destroy §c<joueur> ");
			sendMessage(sender, " ");
			return true;
		}
		
		EconManager econManager = Plugin.getInstance().getEconManager();
		
		if (args[0].equalsIgnoreCase("set")) {
			if (args.length == 1) {
				sendMessage(sender, "[Aiidor]§eLa commande : /money set <joueur> <montant>");
				return true;
			}
			
			if (args.length != 3) {
				sendMessage(sender, "[Aiidor]§cErreur, la commande s'écrit /money set <joueur> <montant>");
				return true;
			}
			
			if (!econManager.hasAccount(args[1])) {
				sendMessage(sender, "[Aiidor]§cErreur, ce joueur n'a pas de compte !");
				return true;
			}
			
			Double montant;
			
			try {
				
				montant = Double.valueOf(args[2].replace(",", "."));
				
			} catch (Exception e) {
				sendMessage(sender, "[Aiidor]§cErreur, la commande s'écrit /money set <joueur> <montant>");
				return true;
			}
			
			econManager.getAccount(args[1]).setBalance(montant);
			sendMessage(sender, "[Aiidor]§6Vous venez de mettre la solde du compte de §c" + args[1] + " §6à §e" + montant + " " + EconManager.devise + " §6!");
			return true;
		}
		
		
		if (args[0].equalsIgnoreCase("add")) {
			if (args.length == 1) {
				sendMessage(sender, "[Aiidor]§eLa commande : /money add <joueur> <montant>");
				return true;
			}
			
			if (args.length != 3) {
				sendMessage(sender, "[Aiidor]§cErreur, la commande s'écrit /money add <joueur> <montant>");
				return true;
			}
			
			if (!econManager.hasAccount(args[1])) {
				sendMessage(sender, "[Aiidor]§cErreur, ce joueur n'a pas de compte !");
				return true;
			}
			
			Double montant;
			
			try {
				
				montant = Double.valueOf(args[2].replace(",", "."));
				
			} catch (Exception e) {
				sendMessage(sender, "[Aiidor]§cErreur, la commande s'écrit /money add <joueur> <montant>");
				return true;
			}
			
			econManager.getAccount(args[1]).addMoney(montant);
			sendMessage(sender, "[Aiidor]§6Vous venez d'ajouter §e" + montant + " " + EconManager.devise + "§c à " + args[1]);
			return true;
		}
		
		
		
		
		if (args[0].equalsIgnoreCase("remove")) {
			if (args.length == 1) {
				sendMessage(sender, "[Aiidor]§eLa commande : /money remove <joueur> <montant>");
				return true;
			}
			
			if (args.length != 3) {
				sendMessage(sender, "[Aiidor]§cErreur, la commande s'écrit /money remove <joueur> <montant>");
				return true;
			}
			
			if (!econManager.hasAccount(args[1])) {
				sendMessage(sender, "[Aiidor]§cErreur, ce joueur n'a pas de compte !");
				return true;
			}
			
			Double montant;
			
			try {
				
				montant = Double.valueOf(args[2].replace(",", "."));
				
			} catch (Exception e) {
				sendMessage(sender, "[Aiidor]§cErreur, la commande s'écrit /money remove <joueur> <montant>");
				return true;
			}
			
			econManager.getAccount(args[1]).removeMoney(montant);
			sendMessage(sender, "[Aiidor]§6Vous venez de retirer §e" + montant + " " + EconManager.devise + " §6à §c" + args[1]);
			return true;
		}
		
		
		
		
		if (args[0].equalsIgnoreCase("destroy")) {
			if (args.length == 1) {
				sendMessage(sender, "[Aiidor]§eLa commande : /money remove <joueur> <montant>");
				return true;
			}
			
			if (args.length != 2) {
				sendMessage(sender, "[Aiidor]§cErreur, la commande s'écrit /money destroy <joueur>");
				return true;
			}
			
			if (!econManager.hasAccount(args[1])) {
				sendMessage(sender, "[Aiidor]§cErreur, ce joueur n'a pas de compte !");
				return true;
			}
			
			econManager.destroyAccount(args[1]);
			sendMessage(sender, "[Aiidor]§6Vous venez de suprimer le compte de §c" + args[1]);
			return true;
		}
		
		
		sendMessage(sender, "[Aiidor]§cCette commande n'existe pas !");
		return false;
	}
	
	private void sendMessage(CommandSender sender, String msg) {
		if (sender instanceof Player) {
			
			Player p = (Player) sender;
			p.sendMessage(msg.replace("[Aiidor]", Plugin.prefix));
			
		} else {
			System.out.println(msg.replace("[Aiidor]", "[Aiidor] ")
						.replace("§a", "")
						.replace("§e", "")
						.replace("§c", "")
						.replace("§6", "")
						.replace("§2", "")
						.replace("§9", "")
						.replace("§f", "")
						.replace("§d", "")
						
						.replace("é", "e")
						.replace("è", "e")
						.replace("à", "a")
						.replace("ê", "e")
						.replace("û", "u")
			);
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label,  String[] args) {
		
		if (label.equalsIgnoreCase("econ")) {
			 
			List<String> completion = new ArrayList<String>();
			
			if (args.length == 1) {
				
				for (String string : Arrays.asList("set", "add", "remove", "destroy")) {
						
					if (args[0].equals("") || string.toLowerCase().startsWith(args[0].toLowerCase())) {
						completion.add(string);
					}
				}
				
				Collections.sort(completion);
			}
			
			if (!completion.isEmpty()) return completion;
		}
		
		return null;
	}

}
