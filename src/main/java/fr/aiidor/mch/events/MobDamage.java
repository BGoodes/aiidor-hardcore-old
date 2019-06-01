package fr.aiidor.mch.events;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobDamage implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		
		if (e.getEntity() instanceof Zombie) {
			if (e.getCause() == DamageCause.FIRE_TICK || e.getCause() == DamageCause.FIRE) {
				e.setCancelled(true);
				e.getEntity().setFireTicks(0);
				return;
			}
		}
		
		//GHAST
		if (e.getEntity().getType() == EntityType.GHAST) {
			if (e.getCause() == DamageCause.PROJECTILE) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		
		if (e.getEntity() instanceof Spider) {
			
			for (int x = e.getEntity().getLocation().getBlockX()-4; x <= e.getEntity().getLocation().getBlockX() + 4; x++) {
				for (int z = e.getEntity().getLocation().getBlockZ()-4; z <= e.getEntity().getLocation().getBlockZ() + 4; z++) {
					
					if (new Random().nextInt(3) == 0) {
						
						int choose = new Random().nextInt(2);
						if (new Random().nextBoolean()) {
							if (e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() + choose , z).getType() == Material.AIR) {
								e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() + choose , z).setType(Material.COBWEB);
							}
							
						}
						else {
							if (e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() - choose , z).getType() == Material.AIR) {
								e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() - choose , z).setType(Material.COBWEB);
							}
						}
						
					}
				}
			}
		}
	}
}
