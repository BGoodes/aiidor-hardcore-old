package fr.aiidor.mch.mob;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class Random_Events implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntitySpawn(EntitySpawnEvent e) {
		
		if (e.getEntity() instanceof Rabbit) {
			if (new Random().nextInt(100) <= 4) {
				
				Rabbit rabbit = (Rabbit) e.getEntity();
				rabbit.setRabbitType(Type.THE_KILLER_BUNNY);
				rabbit.setCustomName("Killer Bunny");
			}
			return;
		}
		
		
		Location loc = e.getLocation();
		
		if (e.getEntity() instanceof Bat) {
			if (new Random().nextInt(100) <= 4) {
				
				e.setCancelled(true);
				loc.getWorld().spawnEntity(loc, EntityType.VEX);
				
			}
			return;
		}
		
		if (e.getEntity() instanceof Squid) {
			if (new Random().nextInt(100) <= 4) {
				
				e.setCancelled(true);
				
				if (new Random().nextInt(100) <= 5) {
					loc.getWorld().spawnEntity(loc, EntityType.ELDER_GUARDIAN);
				} else {
					loc.getWorld().spawnEntity(loc, EntityType.GUARDIAN);
				}
				
			}
			return;
		}
		
		if (e.getEntity() instanceof Cow) {
			if (new Random().nextInt(800) == 0) {
				
				e.setCancelled(true);
				loc.getWorld().spawnEntity(loc, EntityType.RAVAGER);
				
			}
			return;
		}
		
		Biome biome = loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		
		if (biome == Biome.DARK_FOREST || biome == Biome.DARK_FOREST_HILLS) {
			
			if (e.getEntity() instanceof Zombie) {
				
				if (new Random().nextInt(10) <= 2) {
					e.setCancelled(true);
						
					loc.getWorld().spawnEntity(loc, EntityType.VINDICATOR);
				}
				return;
			}
			
			if (e.getEntity() instanceof Skeleton) {
					
				if (new Random().nextInt(10) <= 1) {
					e.setCancelled(true);
						
					loc.getWorld().spawnEntity(loc, EntityType.ILLUSIONER);
				}
				return;
			}
		}

	}
	
	@EventHandler
	public void onBreakBlock(BlockBreakEvent e) {

		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			if (e.getBlock().getType() == Material.END_STONE) {
				Random r = new Random();
				
				int choice = r.nextInt(400);
				
				if (choice == 0) {
					e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.ENDERMITE);
					return;
				}
				return;
			}
		}
	}
}
