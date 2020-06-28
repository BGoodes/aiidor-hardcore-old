package fr.aiidor.hardcore.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.plugin.PluginManager;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.events.mobs.Blaze_Events;
import fr.aiidor.hardcore.events.mobs.Creeper_Events;
import fr.aiidor.hardcore.events.mobs.Ender_Dragon_Events;
import fr.aiidor.hardcore.events.mobs.Ghast_Events;
import fr.aiidor.hardcore.events.mobs.Phantom_Events;
import fr.aiidor.hardcore.events.mobs.Piglin_Events;
import fr.aiidor.hardcore.events.mobs.Random_Events;
import fr.aiidor.hardcore.events.mobs.Shulker_Events;
import fr.aiidor.hardcore.events.mobs.Silverfish_Events;
import fr.aiidor.hardcore.events.mobs.Skeleton_Events;
import fr.aiidor.hardcore.events.mobs.Slime_Events;
import fr.aiidor.hardcore.events.mobs.Spider_Events;
import fr.aiidor.hardcore.events.mobs.Witch_Events;
import fr.aiidor.hardcore.events.mobs.Wither_Events;
import fr.aiidor.hardcore.events.mobs.Wolf_Events;
import fr.aiidor.hardcore.events.mobs.Zombie_Events;
import fr.aiidor.hardcore.managers.EconManager;
import fr.aiidor.hardcore.managers.EconManager.Account;
import fr.aiidor.hardcore.rework.Anvil_Events;
import fr.aiidor.hardcore.rework.Enchant_Table_Events;

public class EventsManager implements Listener {
	
	public void register() {
		
		PluginManager pm = Bukkit.getPluginManager();
		Plugin pl = Plugin.getInstance();
		
		pm.registerEvents(this, pl);
		pm.registerEvents(new PlayerConnexionEvent(), pl);
		pm.registerEvents(new ChatEvents(), pl);
		pm.registerEvents(new PlayerInteractEvents(), pl);
		pm.registerEvents(new InventoryEvents(), pl);
		pm.registerEvents(new PlayerBreakBlockEvents(), pl);
		pm.registerEvents(new DamageEvents(), pl);
		pm.registerEvents(new FoodEvents(), pl);
		pm.registerEvents(new CraftEvents(), pl);
		pm.registerEvents(new DeathEvents(), pl);
		
		pm.registerEvents(new Enchant_Table_Events(), pl);
		pm.registerEvents(new Anvil_Events(), pl);
		
		pm.registerEvents(new Blaze_Events(), pl);
		pm.registerEvents(new Creeper_Events(), pl);
		pm.registerEvents(new Ender_Dragon_Events(), pl);
		pm.registerEvents(new Ghast_Events(), pl);
		pm.registerEvents(new Phantom_Events(), pl);
		pm.registerEvents(new Piglin_Events(), pl);
		pm.registerEvents(new Random_Events(), pl);
		pm.registerEvents(new Shulker_Events(), pl);
		pm.registerEvents(new Silverfish_Events(), pl);
		pm.registerEvents(new Skeleton_Events(), pl);
		pm.registerEvents(new Slime_Events(), pl);
		pm.registerEvents(new Spider_Events(), pl);
		pm.registerEvents(new Witch_Events(), pl);
		pm.registerEvents(new Wither_Events(), pl);
		pm.registerEvents(new Wolf_Events(), pl);
		pm.registerEvents(new Zombie_Events(), pl);
	}
	
	@EventHandler
	public void onAchievement(PlayerAdvancementDoneEvent e) {
		Player player = e.getPlayer();
		EconManager econManager = Plugin.getInstance().getEconManager();
		
		if (econManager.hasAccount(player)) {
			Account account = econManager.getAccount(player);
			
			Double money = 10.0;
			String key = e.getAdvancement().getKey().toString();
			
			if (key.startsWith("minecraft:recipes")) {
				return;
			}
			
			if (key.startsWith("minecraft:adventure")) {
				money = 50.0;
				if (key.contains("kill_all_mobs")) money = 500.0;
			}
			
			if (key.startsWith("minecraft:nether")) {
				money = 75.0;
				if (key.contains("create_full_beacon")) money = 250.0;
			}
			
			if (key.startsWith("minecraft:end")) {
				money = 100.0;
				if (key.contains("kill_dragon")) money = 500.0;
			}
			
			if (key.startsWith("minecraft:story")) {
				if (key.contains("mine_diamond")) money = 100.0;
				if (key.contains("enter_the")) money = 100.0;
			}
			
			account.addMoney(money);
			
			final Double m = money;
			final UUID id = player.getUniqueId();
			Bukkit.getScheduler().runTaskLater(Plugin.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Player p = Bukkit.getPlayer(id);
					if (p != null) p.sendMessage("§a+ §7" + m + " §e" + EconManager.devise);
				}
			}, 1);
		}
	}
}
