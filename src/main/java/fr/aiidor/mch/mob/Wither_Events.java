package fr.aiidor.mch.mob;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Wither_Events implements Listener {
	
	@EventHandler
	public void entitySpawnEvent(EntitySpawnEvent e) {
		
		if (e.getEntityType() == EntityType.WITHER) {
			Wither wither = (Wither) e.getEntity();
			
			Double health = wither.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 4;
			wither.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
			wither.setHealth(health);
		}
	}
	
	@EventHandler
	public void witherExplode(EntityExplodeEvent e) {
		
		if (e.getEntityType() == EntityType.WITHER) {
			
			Location loc = e.getEntity().getLocation();
			
			for (int x = -2; x != 3; x ++) {
				for (int y = 0; y != 3; y ++) {
					for (int z = -2; z != 3; z ++) {
						
						Block b = loc.clone().add(x, y, z).getBlock();
						
						if (b != null && b.getType() != Material.AIR) {
							
							if (b.getType() == Material.BEDROCK || b.getType() == Material.OBSIDIAN) {
								
								b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
								b.setType(Material.AIR);
							}
						}
					}
				}
			}
		}
	}
	
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		
		if (e.getCause() == DamageCause.ENTITY_EXPLOSION || e.getCause() == DamageCause.BLOCK_EXPLOSION)  {
			if (e.getEntityType() == EntityType.WITHER || e.getEntityType() == EntityType.WITHER_SKELETON) {
				e.setDamage(0);
			}
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		
		if (e.getEntityType() == EntityType.WITHER_SKULL) {
			WitherSkull skull = (WitherSkull) e.getEntity();
			
			if (e.getEntity().getShooter() != null && e.getEntity().getShooter()  instanceof Wither) {
				skull.getWorld().createExplosion(skull.getLocation(), 2);
			}
		}
	}
}
