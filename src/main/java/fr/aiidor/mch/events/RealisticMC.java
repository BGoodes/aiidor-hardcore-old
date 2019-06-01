package fr.aiidor.mch.events;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class RealisticMC implements Listener {
	
	@EventHandler
	public void onPlantGrowth(BlockGrowEvent e) {
		
		if (HotBiome(e.getBlock().getBiome())) {
			Material mat = e.getBlock().getType();
			
			if (isCrops(mat) && mat != Material.MELON_STEM) {
				e.setCancelled(true);
				if (new Random().nextBoolean() && new Random().nextBoolean()) {
					e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().add(0, -1 , 0)).setType(Material.DIRT);
				} else {
					e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().add(0, -1 , 0)).setType(Material.COARSE_DIRT);
				}
				
				e.getBlock().setType(Material.AIR);
				if (new Random().nextInt(9) == 0) {
					e.getBlock().setType(Material.DEAD_BUSH);
				}
			}
		}
		
		if (coldBiome(e.getBlock().getBiome()) || e.getBlock().getLocation().getBlockY() >= 125) {
			Material mat = e.getBlock().getType();
			
			if (isCrops(mat)) {
				e.setCancelled(true);
				if (new Random().nextBoolean() && new Random().nextBoolean()) {
					e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().add(0, -1 , 0)).setType(Material.DIRT);
				} else {
					e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().add(0, -1 , 0)).setType(Material.COARSE_DIRT);
				}
				
				e.getBlock().setType(Material.AIR);
				if (new Random().nextInt(9) == 0) {
					e.getBlock().setType(Material.SNOW);
				}
			}
		}
	}
	
	public boolean isCrops(Material mat) {
		if (mat == Material.CARROTS || mat == Material.POTATOES || mat == Material.BEETROOTS 
				|| mat == Material.PUMPKIN_STEM || mat == Material.WHEAT || mat == Material.MELON_STEM) {
			return true;
		}
		return false;
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
