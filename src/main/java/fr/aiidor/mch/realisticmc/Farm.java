package fr.aiidor.mch.realisticmc;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class Farm implements Listener {
	
	@EventHandler
	public void onPlantGrowth(BlockGrowEvent e) {
		Block b = e.getBlock();
		
		if (isCrops(b.getType())) {
			
			//IL FAIT CHAUD
			if (b.getTemperature() >= 1 && b.getType() != Material.MELON_STEM) {
				
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
				
				return;
			}
			
			//IL FAIT FROID
			if (b.getTemperature() <= 0.16 || b.getBiome() == Biome.FROZEN_OCEAN || b.getLocation().getBlockY() >= 150 ) {
				
				e.setCancelled(true);
				if (new Random().nextBoolean()) {
					e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().add(0, -1 , 0)).setType(Material.DIRT);
				} else {
					e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().add(0, -1 , 0)).setType(Material.COARSE_DIRT);
				}
				
				e.getBlock().setType(Material.AIR);
				
				if (new Random().nextInt(9) == 0) {
					e.getBlock().setType(Material.SNOW);
				}
				
				return;
			}
		}
	}
	
	public boolean isCrops(Material mat) {
		if (mat == Material.CARROTS || mat == Material.POTATOES || mat == Material.BEETROOTS ||
			mat == Material.PUMPKIN_STEM || mat == Material.WHEAT || mat == Material.MELON_STEM) {
			
			return true;
		}
		return false;
	}
}
