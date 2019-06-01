package fr.aiidor.mch.realistic;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Temperature extends BukkitRunnable{
	
	private HashMap<UUID, Integer> temp = new HashMap<>();
	
	@Override
	public void run() {
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!temp.containsKey(p.getUniqueId())) {
				temp.put(p.getUniqueId(), 0);
			}
			
		}
	}
	
	public boolean HotBiome(Biome b) {
		if (b == Biome.DESERT || b == Biome.DESERT_HILLS || b == Biome.DESERT_LAKES || b == Biome.BADLANDS || b == Biome.BADLANDS_PLATEAU ||
			b == Biome.ERODED_BADLANDS || b == Biome.ERODED_BADLANDS || b == Biome.MODIFIED_BADLANDS_PLATEAU || 
			b == Biome.MODIFIED_WOODED_BADLANDS_PLATEAU || b == Biome.WOODED_BADLANDS_PLATEAU || b == Biome.SAVANNA ||
			b == Biome.SAVANNA_PLATEAU || b == Biome.SHATTERED_SAVANNA || b == Biome.SHATTERED_SAVANNA_PLATEAU) {
			return true;
		}
		return false;
	}
	
	public boolean coldBiome(Biome b) {
		if (b == Biome.COLD_OCEAN || b == Biome.DEEP_COLD_OCEAN || b == Biome.DEEP_FROZEN_OCEAN || b == Biome.FROZEN_RIVER || b == Biome.ICE_SPIKES ||
			b == Biome.SNOWY_BEACH || b == Biome.SNOWY_MOUNTAINS || b == Biome.SNOWY_TAIGA || b == Biome.SNOWY_TAIGA_HILLS|| b == Biome.SNOWY_TAIGA_HILLS ||
			b == Biome.SNOWY_TAIGA_MOUNTAINS || b == Biome.SNOWY_TUNDRA) {
			return true;
		}
		return false;
	}
}
