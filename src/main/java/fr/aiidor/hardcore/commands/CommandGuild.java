package fr.aiidor.hardcore.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.managers.EconManager;
import fr.aiidor.hardcore.managers.EconManager.Account;
import fr.aiidor.hardcore.managers.GuildManager;
import fr.aiidor.hardcore.managers.GuildManager.Guild;
import fr.aiidor.hardcore.tasks.Invitation;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandGuild implements CommandExecutor, TabCompleter {
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			System.out.println("[Aiidor] Seul un joueur peut effectuer cette commande !");
			return true;
		}  
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
			player.sendMessage(Plugin.prefix + "§2Les commmandes :");
			
			player.sendMessage("  §6/" + label.toLowerCase() + " create <nom> : §ePermet de créer sa guilde.");
			player.sendMessage("  §6/" + label.toLowerCase() + " disband : §ePermet de supprimer sa guilde.");
			player.sendMessage("  §6/" + label.toLowerCase() + " invite §c<joueur> : §eInviter un membre dans la guilde.");
			player.sendMessage("  §6/" + label.toLowerCase() + " leave : §eQuitter sa guilde.");
			player.sendMessage("  §6/" + label.toLowerCase() + " bank §c<add|get> <money> : §ePermet d'intéragir avec la banque de sa guilde.");
			player.sendMessage("  §6/" + label.toLowerCase() + " kick §c<joueur> : §eVirer un membre de sa guilde.");
			player.sendMessage("  §6/" + label.toLowerCase() + " give §c<joueur> : §ePermet de donner sa guilde.");

			return true;
		}
		
		GuildManager guildManager = Plugin.getInstance().getGuildManager();
		EconManager econManager = Plugin.getInstance().getEconManager();
		
		if (args[0].equalsIgnoreCase("disband")) {
		
			if (args.length != 1) {
				player.sendMessage(Plugin.prefix + "§eErreur, la commande s'écrit : /" + label.toLowerCase() + " " + args[0].toLowerCase());
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("create")) {
			
			if (args.length != 2) {
				player.sendMessage(Plugin.prefix + "§cErreur, la commande s'écrit : /" + label.toLowerCase() + " create <nom>");
				return true;
			}
			
			String guildName = args[1];
			
			if (guildManager.hasGuild(player.getUniqueId())) {
				player.sendMessage(Plugin.prefix + "§cErreur, vous êtes déjà dans une Guilde !");
				return true;
			}
			
			if (guildManager.isGuild(guildName)) {
				player.sendMessage(Plugin.prefix + "§cErreur, une guilde portant le nom " + guildName + " existe déjà !");
				return true;
			}
			
			if (guildName.length() > 12) {
				player.sendMessage(Plugin.prefix + "§cVotre nom ne peut pas dépasser les 12 caractères !");
				return true;
			}
			
			if (!econManager.hasAccount(player)) {
				player.sendMessage(Plugin.prefix + "§cErreur, vous n'avez pas de compte bancaire.");
				return true;
			}
			
			Account account = econManager.getAccount(player);
			
			if (!account.hasMoney(guildManager.guild_price)) {
				player.sendMessage(Plugin.prefix + "§cErreur, vous n'avez pas assez d'argent pour créer une guilde. (prix : " + guildManager.guild_price + " " + EconManager.devise + ")");
				return true;
			}
			
			account.removeMoney(guildManager.guild_price);
			
			guildManager.createGuild(guildName, player.getUniqueId());
			player.sendMessage(Plugin.prefix + "§aVous venez de créer votre Guilde !");
			
			Bukkit.broadcastMessage(Plugin.prefix + "§e" + player.getName() + " §evient de fonder la guilde : §6" + guildName + " §e!");
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE, SoundCategory.PLAYERS, 0.5f, 1f);
			}
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("disband")) {
			
			if (args.length != 1) {
				player.sendMessage(Plugin.prefix + "§cErreur, la commande s'écrit : /" + label.toLowerCase() + " disband");
				return true;
			}
			
			if (!guildManager.hasGuild(player)) {
				player.sendMessage(Plugin.prefix + "§cErreur, vous n'avez pas de guilde.");
				return true;
			}
			
			Guild guild = guildManager.getPlayerGuild(player);
			
			if (!player.getUniqueId().equals(guild.getMaster())) {
				player.sendMessage(guild.getTag() + "§cErreur, vous devez être le Maître de la Guilde pour effectuer cette commande.");
				return true;
			}
			
			guildManager.destroyGuild(guild);
			
			Bukkit.broadcastMessage(Plugin.prefix + "§e" + player.getName() + " §evient de supprimer la guilde : §6" + guild.getName() + " §e!");
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE, SoundCategory.PLAYERS, 0.5f, 1f);
			}
			
			
			return true;
		}
		
		//KICK, INVITE, GIVE
		if (args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("give")) {
			if (args.length == 1) {
				player.sendMessage(Plugin.prefix + "§eLa commande s'écrit : /" + label.toLowerCase() + " " + args[0].toLowerCase() + " <joueur>");
				return true;
			}
			
			if (args.length != 2) {
				player.sendMessage(Plugin.prefix + "§Erreur, la commande s'écrit : /" + label.toLowerCase() + " " + args[0].toLowerCase() + " <joueur>");
				return true;
			}
			
			if (!guildManager.hasGuild(player.getUniqueId())) {
				player.sendMessage(Plugin.prefix + "§cErreur, vous devez avoir une guilde pour effectuer cette commande.");
				return true;
			}
			
			Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
		
			
			String targetname = args[1];
			
			if (args[0].equalsIgnoreCase("invite")) {
				
				if (targetname.equals(player.getName())) {
					player.sendMessage(guild.getTag() + "§cErreur, s'inviter sois-même c'est ce qu'on apelle s'incruster !");
					return true;
				}
				
				if (!isConnected(targetname)) {
					player.sendMessage(guild.getTag() + "§cErreur, le joueur " + targetname + " n'est pas connecté ou n'existe pas !");
					return true;
				}
				
				Player Target = getPlayer(targetname);
				
				if (guildManager.hasGuild(Target)) {
					player.sendMessage(guild.getTag() + "§cErreur, le joueur " + targetname + " est déjà dans une Guilde !");
					return true;
				}
				
				if (hasInvite(Target.getUniqueId(), guild)) {
					player.sendMessage(guild.getTag() + "§cCe joueur à déjà reçu une invitation en cours de la Guilde !");
					return true;
				}
				
				player.sendMessage(guild.getTag() + "§aVotre invitation à été envoyée !");
				
				Target.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 0.6f, 1f);
				
				TextComponent info = new TextComponent(Plugin.prefix + "§6La Guilde §2" + guild.getName() + "§6 vous invite à rejoindre leurs rangs.");
				TextComponent rep = new TextComponent("§6Faite §e/guild join " + guild.getName() + " §6pour accepter.");
				
				rep.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/guild join " + guild.getName()));
				rep.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eRejoindre ?").create()));
				
				Target.spigot().sendMessage(info, rep);
				addInvite(Target.getUniqueId(), guild);
				return true;
			}
			
			//PERM
			if (!player.getUniqueId().equals(guild.getMaster())) {
				player.sendMessage(guild.getTag() + "§cErreur, vous devez être le Maître de la Guilde pour effectuer cette commande.");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("kick")) {
				
				if (targetname.equals(player.getName())) {
					player.sendMessage(guild.getTag() + "§cVous n'allez pas vous kick vous-même ! Quand même !");
					return true;
				}
				
				if (!guildManager.hasGuild(targetname)) {
					player.sendMessage(guild.getTag() + "§cErreur, le joueur " + targetname + " n'a pas de Guilde ou n'existe pas !");
					return true;
				}
				
				if (!guild.isMember(targetname)) {
					player.sendMessage(guild.getTag() + "§cErreur, " + targetname + " n'est pas dans la Guilde !");
					return true;
				}
				
				if (targetname.equals(guild.getMemberName(guild.getMaster()))) {
					player.sendMessage(guild.getTag() + "§cVous ne pouvez pas faire de coup d'état ! "+ targetname +" restera le Maître de cette Guilde !");
					return true;
				}
				
				guild.removeMember(targetname);
				guild.broadcast(guild.getTag() + "§4Le joueur §c" + targetname + " §4vient d'être expulsé de la Guilde !");
				
				if (isConnected(targetname)) {
					getPlayer(targetname).sendMessage(Plugin.prefix + "§4Vous venez d'être expulsé de votre Guilde !");
				}
				return true;
			}
			
			//GIVE
			if (args[0].equalsIgnoreCase("give")) {
				
				if (targetname.equals(player.getName())) {
					player.sendMessage(guild.getTag() + "§cVous ne pouvez pas vous choisir vous même !");
					return true;
				}
				
				if (!guildManager.hasGuild(targetname)) {
					player.sendMessage(guild.getTag() + "§cErreur, le joueur " + targetname + " n'a pas de Guilde ou n'existe pas !");
					return true;
				}
				
				if (!guild.isMember(targetname)) {
					player.sendMessage(guild.getTag() + "§cErreur, " + targetname + " n'est pas dans la Guilde !");
					return true;
				}
				
				if (!isConnected(targetname)) {
					player.sendMessage(guild.getTag() + "§cCe joueur n'est pas connecté !");
					return true;
				}
				
				Player target = getPlayer(targetname);
				guild.setMaster(target.getUniqueId());
				
				guild.broadcast(guild.getTag() + "§b"+ player.getName() + " vient de laisser ses permissions de Maître à " + targetname + " !");
				
				target.sendMessage(guild.getTag() + "§cVous êtes le nouveau Maître de la Guilde !");
				target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.5f, 1f);
				
				return true;
			}
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("join")) {
			
			if (args.length == 1) {
				player.sendMessage(Plugin.prefix + "§eLa commande s'écrit : /" + label.toLowerCase() + " join <guilde>");
				return true;
			}
			
			if (args.length != 2) {
				player.sendMessage(Plugin.prefix + "§cErreur, la commande s'écrit : /" + label.toLowerCase() + " join <guilde>");
				return true;
			}
			
			if (guildManager.hasGuild(player.getUniqueId())) {
				player.sendMessage(Plugin.prefix + "§cErreur, vous êtes déjà dans une Guilde !");
				return true;
			}
			
			if (!guildManager.isGuild(args[1])) {
				player.sendMessage(Plugin.prefix + "§cErreur, cette Guilde n'existe pas ou n'existe plus !");
				return true;
			}
			
			Guild guild = guildManager.getGuild(args[1]);
			
			if (!hasInvite(player.getUniqueId(), guild)) {
				player.sendMessage(Plugin.prefix + "§cErreur, vous n'avez pas reçut d'invitation de cette Guilde ou elle a expiré !");
				return true;
			}
			
			guild.addMember(player);
			player.sendMessage(guild.getTag() + "§aBienvenue dans la Guilde " + guild.getName() + " !");
			
			guild.broadcast(guild.getTag() + "§a" + player.getName() + "§2 fait désormais partie de la Guilde !");

			
			guildManager.invites.remove(player.getUniqueId());
			return true;
		}
		
		if (args[0].equalsIgnoreCase("leave")) {
			if (args.length != 1) {
				player.sendMessage(Plugin.prefix + "§Erreur, la commande s'écrit : /" + label.toLowerCase() + " leave");
				return true;
			}
			
			if (!guildManager.hasGuild(player.getUniqueId())) {
				player.sendMessage(Plugin.prefix + "§cErreur, vous devez avoir une guilde pour effectuer cette commande.");
				return true;
			}
			
			Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
			
			if (player.getUniqueId().equals(guild.getMaster())) {
				player.sendMessage(guild.getTag() + "§cErreur, vous êtes le maître de la Guilde ! Pour la quitter, vous devez choisir un nouveau maître OU supprimer celle ci !");
				return true;
			}
			
			guild.removeMember(player.getUniqueId());
			player.sendMessage(Plugin.prefix + "§4Vous venez de quitter la Guilde §c" + guild.getName() + "§4 !");
			
			guild.broadcast(guild.getTag() + "§c" + player.getName() + "§4 ne fait désormais plus partie de la Guilde !");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("bank")) {
			
			if (!guildManager.hasGuild(player.getUniqueId())) {
				player.sendMessage(Plugin.prefix + "§cErreur, vous devez avoir une guilde pour effectuer cette commande.");
				return true;
			}
			
			Guild guild = guildManager.getPlayerGuild(player.getUniqueId());
			
			if (args.length == 1) {
				player.sendMessage(guild.getTag() + "§6La guilde détient §e" + guild.getBalance() + " " + EconManager.devise);
				return true;
			}
			
			if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("get")) {
				
				if (args.length != 3) {
					player.sendMessage(Plugin.prefix + "§eLa commande s'écrit : /" + label.toLowerCase() + " bank " + args[1].toLowerCase() + " <nombre>");
					return true;
				}
				
				Double money = 0.0;
				
				try {
					money = Double.valueOf(args[2].replace(',', '.'));
					  
				} catch (NumberFormatException ex){
					 player.sendMessage(Plugin.prefix + "§cErreur, vous devez entrer un chiffre !");
					 
					return true;
				}
				
				money = (double)Math.round(money * 100d) / 100d;
				
				if (money == 0) {
					 player.sendMessage(guild.getTag() + "§aRien ne se passe ...");
					return true;
				}
				
				if (!econManager.hasAccount(player.getUniqueId())) {
					 player.sendMessage(Plugin.prefix + "§cErreur, vous n'avez pas de compte !");
					return true;
				}
				
				Account account = econManager.getAccount(player);
				
				if (args[1].equalsIgnoreCase("add")) {
					
					if (!account.hasMoney(money)) {
						 player.sendMessage(Plugin.prefix + "§cErreur, vous n'avez pas assez d'argent pour faire ça !");
						return true;
					}
					
					account.removeMoney(money);
					guild.addMoney(money);
					
					player.sendMessage(guild.getTag() + "§aVous venez de déposer " + money + " " + EconManager.devise);
					return true;
				}
				
				if (args[1].equalsIgnoreCase("get")) {
					
					
					if (!guild.hasMoney(money)) {
						player.sendMessage(guild.getTag() + "§cLa Guilde ne détient pas assez d'argent !");
						return true;
					}
					
					guild.removeMoney(money);
					account.addMoney(money);
					player.sendMessage(guild.getTag() + "§aVous venez de récupérer " + money + " " + EconManager.devise);
					return true;
				}
				
				
			} else {
				player.sendMessage(Plugin.prefix + "§cErreur, La commande s'écrit : /" + label.toLowerCase() + " bank <add|get> <nombre>");
			}
			
			return true;
		}
		
		player.sendMessage(Plugin.prefix + "§cCette commande n'existe pas !");
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
	
	private Boolean hasInvite(UUID uuid, Guild guild) {
		GuildManager guildManager = Plugin.getInstance().getGuildManager();
		
		if (!guildManager.invites.containsKey(uuid)) return false;
		
		for (Invitation invite : guildManager.invites.get(uuid)) {
			if (guild.getName().equals(invite.getGuild().getName())) {
				return true;
			}
		}
		
		return false;
	}
	
	private void addInvite(UUID uuid, Guild guild) {
		GuildManager guildManager = Plugin.getInstance().getGuildManager();
		
		if (!guildManager.invites.containsKey(uuid)) guildManager.invites.put(uuid, new ArrayList<Invitation>());
		
		Invitation invite = new Invitation(uuid, guild);
		invite.runTaskTimer(Plugin.getInstance(), 0, 20);
		
		guildManager.invites.get(uuid).add(invite);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label,  String[] args) {
			 
		List<String> completion = new ArrayList<String>();
			
		if (args.length == 1) {
				
			for (String string : Arrays.asList("invite", "leave", "bank", "kick", "give", "create", "disband")) {
						
				if (args[0].equals("") || string.toLowerCase().startsWith(args[0].toLowerCase())) {
					completion.add(string);
				}
			}
				
			Collections.sort(completion);
		}
			
		if (args.length == 2) {
				
			if (args[0].equalsIgnoreCase("bank")) {
					
				for (String string : Arrays.asList("add", "get")) {
						
					if (args[1].equals("") || string.toLowerCase().startsWith(args[1].toLowerCase())) {
						completion.add(string);
					}
				}
					
				Collections.sort(completion);
			}
			
			if (args[0].equalsIgnoreCase("join")) {
				
				List<String> guilds_name = new ArrayList<String>();
				Plugin.getInstance().getGuildManager().getAllGuilds().forEach(g->guilds_name.add(g.getName()));
				
				for (String string : guilds_name) {
						
					if (args[1].equals("") || string.toLowerCase().startsWith(args[1].toLowerCase())) {
						completion.add(string);
					}
				}
					
				Collections.sort(completion);
			}
		}
			
		if (!completion.isEmpty()) return completion;
		
		return null;
	}
}
