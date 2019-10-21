package fr.aiidor.mch.mob;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTeleportEvent;

import fr.aiidor.mch.WorldSound;

public class Enderman_Events implements Listener {
	
	@EventHandler
	public void enderManSpawn(EntitySpawnEvent e) {
		if (e.getEntity() instanceof Enderman) {
			
			Enderman enderman = (Enderman) e.getEntity();
			enderman.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(18);
			enderman.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.5);
		}
	}
	
	@EventHandler
	public void endermanTeleportEvent(EntityTeleportEvent e) {
		if (e.getEntity() instanceof Enderman) {
			Enderman enderman = (Enderman) e.getEntity();
			
			if (enderman.getTarget() != null)  {
				
				Random r = new Random();
				Location loc = e.getTo();
				
				if (r.nextInt(10) == 0) {
					
					int nombre = r.nextInt(3) + 3;
					
					for (int i = nombre; i != 0; i --) {
						Endermite endermite = (Endermite) loc.getWorld().spawnEntity(loc, EntityType.ENDERMITE);
						endermite.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
						endermite.setTarget(enderman.getTarget());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void EnderManHit(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Enderman) {
			
			Enderman enderman = (Enderman) e.getEntity();
			Entity damager = e.getDamager();
			
			Location loc = enderman.getLocation();
			
			Random r = new Random();
			
			if (r.nextInt(5) == 0) {
				new WorldSound(loc).PlaySound(Sound.ENTITY_ENDERMAN_SCREAM, SoundCategory.HOSTILE);
				
				for (int x = -1; x != 2; x ++) {
					for (int y = 0; y != 4; y ++) {
						for (int z = -1; z != 2; z ++) {
							
							Block b = loc.clone().add(x, y, z).getBlock();
							
							if (b != null && b.getType() != Material.AIR) {
								
								if (b.getType() != Material.BEDROCK && b.getType() != Material.BARRIER) {
									b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
									b.setType(Material.AIR);
								}
							}
						}
					}
				}
				return;
			}
			
			if (r.nextInt(10) == 0) {
				new WorldSound(loc).PlaySound(Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE);
				damager.teleport(loc);
				return;
			}
		}
	}
}
