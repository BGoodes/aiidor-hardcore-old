package fr.aiidor.hardcore.events.mobs;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class Witch_Events implements Listener {
	
	
	@EventHandler
	public void entitySpawnEvent(EntitySpawnEvent e) {
		if (e.getEntity() instanceof Witch) {
			Witch witch = (Witch) e.getEntity();
			
			witch.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
			witch.setHealth(witch.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
			
			Random r = new Random();
			
			//PHANTOM
			if (r.nextInt(30) == 0) {
				e.setCancelled(true);
				
				Location loc = e.getLocation();
				loc.setY(loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockY()) + 20);
				
				Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
				
				phantom.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
				
				witch.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
				phantom.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
				
				phantom.addPassenger(witch);
				return;
			}
		}
	}
	
	@EventHandler
	public void splashPotionEvent(PotionSplashEvent e) {
		if (e.getEntity().getShooter() instanceof Witch) {
			
			Random r = new Random();
			
			if (r.nextInt(10) == 0) {
				e.getAffectedEntities().forEach(en->en.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 0)));
			}
			
			if (r.nextInt(10) == 0) {
				e.getAffectedEntities().forEach(en->en.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 0)));
			}
		}
	}
	
	@EventHandler
	public void launchSplashPotionEvent(ProjectileLaunchEvent e) {
		if (e.getEntity().getShooter() instanceof Witch) {
			ProjectileSource s = e.getEntity().getShooter();
			
			if (e.getEntity() instanceof ThrownPotion) {
				
				ThrownPotion p = (ThrownPotion) e.getEntity();
				Witch ent = (Witch) e.getEntity().getShooter();
				
				Random r = new Random();
				
				if (p.getLocation().distance(ent.getLocation()) > 1.5 && r.nextInt(4) == 0) {
					e.setCancelled(true);
					s.launchProjectile(Fireball.class, ent.getLocation().getDirection().multiply(3));
					
				} else if (r.nextInt(4) == 0) {
					
					e.setCancelled(true);
					s.launchProjectile(SmallFireball.class, ent.getLocation().getDirection().multiply(3));
					
				} else if (r.nextInt(3) == 0) {
					
					e.setCancelled(true);
					s.launchProjectile(Arrow.class, ent.getLocation().getDirection());
					
				} else if (r.nextInt(8) == 0) {
					
					ent.getWorld().spawnParticle(Particle.SPELL_WITCH, ent.getLocation(), 10, 0.5, 0.5, 0.5, 0.5);
					ent.getWorld().spawnEntity(ent.getLocation(), EntityType.ZOMBIE);
					e.setCancelled(true);
				}
			}
		}
	}
}
