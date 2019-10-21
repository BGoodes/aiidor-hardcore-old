package fr.aiidor.mch;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.aiidor.mch.events.Dead;
import fr.aiidor.mch.events.Listeners;
import fr.aiidor.mch.events.MoreDamage;
import fr.aiidor.mch.mob.Blaze_Events;
import fr.aiidor.mch.mob.Creeper_Events;
import fr.aiidor.mch.mob.Ender_Dragon_Events;
import fr.aiidor.mch.mob.Enderman_Events;
import fr.aiidor.mch.mob.Ghast_Events;
import fr.aiidor.mch.mob.Phantom_Events;
import fr.aiidor.mch.mob.Random_Events;
import fr.aiidor.mch.mob.Silverfish_Events;
import fr.aiidor.mch.mob.Skeleton_Events;
import fr.aiidor.mch.mob.Slime_Events;
import fr.aiidor.mch.mob.Spider_Events;
import fr.aiidor.mch.mob.Wither_Events;
import fr.aiidor.mch.mob.Zombie_Events;
import fr.aiidor.mch.raids.Raids_Events;
import fr.aiidor.mch.realisticmc.Farm;
import fr.aiidor.mch.task.RemoveWeb;
import fr.aiidor.mch.task.Spawn;

public class Main extends JavaPlugin {
	
	public Spawn spawnTask;
	public List<RemoveWeb> webTasks = new ArrayList<>();
	
	@Override
	public void onEnable() {
		
		saveDefaultConfig();
		//REGISTER EVENTS
		Bukkit.getPluginManager().registerEvents(new MoreDamage(), this);
		Bukkit.getPluginManager().registerEvents(new Farm(), this);
		Bukkit.getPluginManager().registerEvents(new Dead(this), this);
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		
		Bukkit.getPluginManager().registerEvents(new Raids_Events(), this);
		
		Bukkit.getPluginManager().registerEvents(new Zombie_Events(this), this);
		Bukkit.getPluginManager().registerEvents(new Skeleton_Events(this), this);
		Bukkit.getPluginManager().registerEvents(new Spider_Events(this), this);
		Bukkit.getPluginManager().registerEvents(new Creeper_Events(), this);
		Bukkit.getPluginManager().registerEvents(new Random_Events(), this);
		Bukkit.getPluginManager().registerEvents(new Slime_Events(), this);
		Bukkit.getPluginManager().registerEvents(new Ghast_Events(), this);
		Bukkit.getPluginManager().registerEvents(new Blaze_Events(), this);
		Bukkit.getPluginManager().registerEvents(new Enderman_Events(), this);
		Bukkit.getPluginManager().registerEvents(new Phantom_Events(this), this);
		Bukkit.getPluginManager().registerEvents(new Ender_Dragon_Events(), this);
		Bukkit.getPluginManager().registerEvents(new Silverfish_Events(), this);
		Bukkit.getPluginManager().registerEvents(new Wither_Events(), this);
		
		spawnTask = new Spawn();
		spawnTask.runTaskTimer(this, 0, 20);
	}
	

	@Override
	public void onDisable() {
		if (!webTasks.isEmpty()) {
			for (RemoveWeb webTask : webTasks) {
				webTask.removeWeb();
			}
		}
	}
}
