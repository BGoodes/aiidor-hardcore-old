package fr.aiidor.hardcore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import fr.aiidor.hardcore.commands.CommandBaltop;
import fr.aiidor.hardcore.commands.CommandBroadcast;
import fr.aiidor.hardcore.commands.CommandEcon;
import fr.aiidor.hardcore.commands.CommandEnchant;
import fr.aiidor.hardcore.commands.CommandFly;
import fr.aiidor.hardcore.commands.CommandGuild;
import fr.aiidor.hardcore.commands.CommandInvsee;
import fr.aiidor.hardcore.commands.CommandMessage;
import fr.aiidor.hardcore.commands.CommandMoney;
import fr.aiidor.hardcore.commands.CommandPay;
import fr.aiidor.hardcore.commands.CommandTitle;
import fr.aiidor.hardcore.commands.CommandVanish;
import fr.aiidor.hardcore.events.EventsManager;
import fr.aiidor.hardcore.files.MCFile;
import fr.aiidor.hardcore.managers.CraftsManager;
import fr.aiidor.hardcore.managers.EconManager;
import fr.aiidor.hardcore.managers.EnchantManager;
import fr.aiidor.hardcore.managers.EntityManager;
import fr.aiidor.hardcore.managers.GuildManager;
import fr.aiidor.hardcore.rework.FastRunnable;
import fr.aiidor.hardcore.tasks.Spawn;

public class Plugin extends JavaPlugin {
	
	private static Plugin instance;
	
	private World world;
	
	private EconManager econManager;
	private GuildManager guildManager;
	private EnchantManager enchantManager;
	private EntityManager entityManager;
	
	public static String prefix;
	
	@Override
	public void onEnable() {
		instance = this;
		
		//FILES
		for (MCFile file : MCFile.values()) {
			file.create(getLogger());
		}

		prefix = ChatColor.translateAlternateColorCodes('&', MCFile.CONFIG.getYamlConfig().getString("serveur-tag"));
		
		this.econManager = new EconManager();
		this.econManager.load();
		
		this.guildManager = new GuildManager();
		this.guildManager.load();
		
		this.enchantManager = new EnchantManager();
		this.enchantManager.load();
		
		this.entityManager = new EntityManager();
		
		new EventsManager().register();
		new CraftsManager().load();
		
		String no_permission = prefix + "§cVous n'avez pas la permission pour effectuer cette commande !";
		
		CommandMessage cmd_msg = new CommandMessage();
		getCommand("msg").setExecutor(cmd_msg);
		getCommand("tell").setExecutor(cmd_msg);
		
		getCommand("money").setExecutor(new CommandMoney());
		getCommand("pay").setExecutor(new CommandPay());
		
		CommandEnchant cmd_enchant = new CommandEnchant();
		getCommand("enchant").setExecutor(cmd_enchant);
		getCommand("enchant").setPermissionMessage(no_permission);
		
		getCommand("vanish").setExecutor(new CommandVanish());
		getCommand("vanish").setPermissionMessage(no_permission);
		
		getCommand("fly").setExecutor(new CommandFly());
		getCommand("fly").setPermissionMessage(no_permission);
		
		getCommand("alert").setExecutor(new CommandBroadcast());
		getCommand("alert").setPermissionMessage(no_permission);
		getCommand("broadcast").setExecutor(new CommandBroadcast());
		getCommand("broadcast").setPermissionMessage(no_permission);
		getCommand("title").setExecutor(new CommandTitle());
		getCommand("title").setPermissionMessage(no_permission);
		
		getCommand("invsee").setExecutor(new CommandInvsee());
		getCommand("invsee").setPermissionMessage(no_permission);
		
		CommandEcon cmd_econ = new CommandEcon();
		
		getCommand("econ").setExecutor(cmd_econ);
		getCommand("econ").setTabCompleter(cmd_econ);
		getCommand("econ").setPermissionMessage(no_permission);
		
		CommandGuild cmd_guild = new CommandGuild();
		
		getCommand("guild").setExecutor(cmd_guild);
		getCommand("guild").setTabCompleter(cmd_guild);
		
		getCommand("g").setExecutor(cmd_guild);
		getCommand("g").setTabCompleter(cmd_guild);
		
		getCommand("baltop").setExecutor(new CommandBaltop());
		
		String worldname = MCFile.CONFIG.getYamlConfig().getString("world");
		
		if (Bukkit.getWorld(worldname) != null) {
			World world = Bukkit.getWorld(worldname);
			
			world.setGameRule(GameRule.NATURAL_REGENERATION, false);
			world.setGameRule(GameRule.UNIVERSAL_ANGER, true);
			
			world.setMonsterSpawnLimit(80);
			world.setDifficulty(Difficulty.HARD);
			
			this.world = world;
		}
		
		if (Bukkit.getWorld(worldname + "_nether") != null) {
			World world = Bukkit.getWorld(worldname + "_nether");
			
			world.setGameRule(GameRule.NATURAL_REGENERATION, false);
			world.setGameRule(GameRule.UNIVERSAL_ANGER, true);
			
			world.setMonsterSpawnLimit(80);
			world.setDifficulty(Difficulty.HARD);
		}
		
		if (Bukkit.getWorld(worldname + "_the_end") != null) {
			World world = Bukkit.getWorld(worldname + "_the_end");
			
			world.setGameRule(GameRule.NATURAL_REGENERATION, false);
			world.setGameRule(GameRule.UNIVERSAL_ANGER, true);
			
			world.setDifficulty(Difficulty.HARD);
		}
		
		//RUNNABLE
		new FastRunnable().runTaskTimer(this, 0, 1);
		new Spawn().runTaskTimer(this, 0, 20);
	}
	
	@Override
	public void onDisable() {
		this.econManager.save();
		this.guildManager.save();
		this.enchantManager.unload();
		this.entityManager.unload();
	}
	
	public static Plugin getInstance() {
		return instance;
	}
	
	public World getWorld() {
		return world;
	}
	
	public EconManager getEconManager() {
		return econManager;
	}
	
	public GuildManager getGuildManager() {
		return guildManager;
	}
	
	public EnchantManager getEnchantManager() {
		return enchantManager;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
