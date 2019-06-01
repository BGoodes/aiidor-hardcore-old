package fr.aiidor.mch;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.aiidor.mch.events.MobAttack;
import fr.aiidor.mch.events.MobDamage;
import fr.aiidor.mch.events.MobSpawn;
import fr.aiidor.mch.events.MoreDamage;
import fr.aiidor.mch.events.PlayerEvent;
import fr.aiidor.mch.events.RealisticMC;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		
		//REGISTER EVENTS
		Bukkit.getPluginManager().registerEvents(new MobSpawn(this), this);
		Bukkit.getPluginManager().registerEvents(new MobAttack(), this);
		Bukkit.getPluginManager().registerEvents(new MobDamage(), this);
		
		Bukkit.getPluginManager().registerEvents(new MoreDamage(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerEvent(), this);
		
		Bukkit.getPluginManager().registerEvents(new RealisticMC(), this);
	}
}
