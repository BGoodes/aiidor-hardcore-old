package fr.aiidor.hardcore.events.mobs;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Silverfish_Events implements Listener {
	
	private ArrayList<PotionEffect> effect = new ArrayList<PotionEffect>();
	
	public Silverfish_Events() {
		effect.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3));
		effect.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
		effect.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
		effect.add(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
	}
	
	@EventHandler
	public void onMobSpawn(EntitySpawnEvent e) {
		
		
		if (e.getEntityType() == EntityType.SILVERFISH) {
			
			Silverfish sv = (Silverfish) e.getEntity();
			
			Random r = new Random();
			
			if (r.nextInt(12) == 0) {
				
				sv.addPotionEffect(effect.get(r.nextInt(effect.size())));
				
				if (r.nextInt(5) == 0) {
					sv.addPotionEffect(effect.get(r.nextInt(effect.size())));
				}
			}
		}
	}
	
	@EventHandler
	public void onSilverFishBurrow(EntityChangeBlockEvent e) {
		
		if(e.getEntity().getType().equals(EntityType.SILVERFISH)) {
			
			if (isInfested(e.getTo())) {
				
				for (int x = e.getEntity().getLocation().getBlockX()-3; x <= e.getEntity().getLocation().getBlockX() + 3; x++) {
					for (int z = e.getEntity().getLocation().getBlockZ()-3; z <= e.getEntity().getLocation().getBlockZ() + 3; z++) {
						
						if (new Random().nextInt(60) == 0) {
							
							int choose = new Random().nextInt(2);
							if (new Random().nextBoolean()) {
								if (e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() + choose , z).getType() == Material.STONE) {
									e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() + choose , z).setType(Material.INFESTED_STONE);
								}
								
							}
							else {
								if (e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() - choose , z).getType() == Material.STONE) {
									e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() - choose , z).setType(Material.INFESTED_STONE);
								}
							}
							
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBreakBlock(BlockBreakEvent e) {

		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			if (e.getBlock().getType() == Material.STONE) {
				Random r = new Random();
				
				int choice = r.nextInt(800);
				
				if (choice == 0) {
					e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.SILVERFISH);
					return;
				}
				return;
			}
		}
	}
	
	private Boolean isInfested(Material mat) {
		if (mat.toString().contains("INFESTED")) return true;
		return false;
	}
}
