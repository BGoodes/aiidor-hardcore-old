package fr.aiidor.hardcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.managers.EconManager;
import fr.aiidor.hardcore.managers.EconManager.Account;

public class CommandPay implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			System.out.println("[Money] Seul un joueur peut effectuer cette commande !");
			return true;
		}
		
		EconManager econManager = Plugin.getInstance().getEconManager();
		Player player = (Player) sender;
		
		if (!econManager.hasAccount(player.getUniqueId())) {
			player.sendMessage(Plugin.prefix + "§cVous n'avez pas de compte !");
			return true;
		}
		
		if (args.length == 0) {
			player.sendMessage(Plugin.prefix + "§eLa commande s'écrit /pay <joueur> <montant>");
			return true;
		}
		
		if (args.length != 2) {
			player.sendMessage(Plugin.prefix + "§cErreur, La commande s'écrit /pay <joueur> <montant>");
			return true;
		}
		
		if (args.length == 2) {
			String target = args[0];
			
			Double montant = 0.0;
			
			try {
				 montant = Double.valueOf(args[1].replace(",", "."));	  
			} catch (NumberFormatException ex){
				player.sendMessage(Plugin.prefix + "§cErreur, La commande s'écrit /pay <joueur> <montant>");
				return true;
			}
			
			if (!econManager.hasAccount(target)) {
				player.sendMessage(Plugin.prefix + "§cCe joueur n'a pas de compte ou n'existe pas !");
				return true;
			}
			
			montant = (double)Math.round(montant * 100d) / 100d;
			
			if (!isConnected(target)) {
				player.sendMessage(Plugin.prefix + "§cLe joueur n'est pas connecté !");
				return true;
			}
			
			Player Ptarget = getPlayer(target);
			if (player.getLocation().distance(Ptarget.getLocation()) > 20) {
				player.sendMessage(Plugin.prefix + "§cVous devez être à moins de 20 bloc de " + target + " pour effectuer le paiement.");
				return true;
			}
			
			Account account = econManager.getAccount(player);
			
			if (!account.hasMoney(montant)) {
				player.sendMessage(Plugin.prefix + "§cVous n'avez pas assez de " + EconManager.devise + " pour effectuer le paiement.");
				return true;
			}
			
			account.removeMoney(montant);
			player.sendMessage(Plugin.prefix + "§aVous venez de donner §e" + montant + " " + EconManager.devise + " §aà " + Ptarget.getName());
			
			econManager.getAccount(Ptarget).addMoney(montant);
			Ptarget.sendMessage(Plugin.prefix + "§bVous venez de recevoir §e" + montant + " " + EconManager.devise + " §bde la part de " + player.getName());
			
			Ptarget.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 0.5f, 1f);
			return true;
		}
		
		return false;
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
