package fr.aiidor.hardcore.events.mobs;

import java.util.Random;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.tasks.Phantom_Boost;

public class Phantom_Events implements Listener {
	
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() instanceof Phantom) {
			
			if (e.getEntity() instanceof LivingEntity) {
				LivingEntity ent = (LivingEntity) e.getEntity();
				
				giveEffect(ent, new PotionEffect(PotionEffectType.BLINDNESS, (20 + new Random().nextInt(40)) * 20, 0));
			}
		}
		
		if (e.getEntity() instanceof Phantom) {
			
			Phantom phantom = (Phantom) e.getEntity();
			
			if (!phantom.getPassengers().isEmpty()) {
				
				if (e.getDamager() instanceof Arrow) {
						
					Arrow arrow = (Arrow) e.getDamager();
					
					if (arrow.getShooter() != null) {
						if (arrow.getShooter() != null && arrow.getShooter() instanceof Entity) {
							Entity entity = (Entity) arrow.getShooter();
								
							if (phantom.getPassengers().contains(entity)) {
								
								e.setCancelled(true);
							}
						}
					}
				}
			}

			
			if (e.getDamager() instanceof Fireball) {
				
				Fireball fire = (Fireball) e.getDamager();
				
				if (fire.getShooter() != null && fire.getShooter().equals(phantom)) {
					e.setCancelled(true);
				}
			}
		}
		
		if (e.getDamager() instanceof Fireball) {
			
			Fireball fire = (Fireball) e.getDamager();
			
			if (fire.getShooter() != null && fire.getShooter() instanceof Phantom) {
				
				Phantom phantom = (Phantom) fire.getShooter();
				
				if (phantom.getPassengers().contains(e.getEntity())) {
					e.setCancelled(true);	
				}
			}

		}
	}
	
	@EventHandler
	public void onSpawn(EntitySpawnEvent e) {
		
		if (e.getEntity() instanceof Phantom) {
			Phantom phantom = (Phantom) e.getEntity();
			
			phantom.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(10);
			phantom.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
			phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
			
			phantom.setSize(4);
			
			Random r = new Random();
			
			if (r.nextInt(3) == 0) {
				phantom.setSize(6);
			}
			
			if (r.nextInt(300) == 0) {
				
				phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
				new Phantom_Boost(phantom, 3).runTaskTimer(Plugin.getInstance(), 0, 20);
				
			} else if (r.nextInt(400) == 0) {
				
				phantom.setSize(30);
				
				phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000);
				phantom.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(200);
				new Phantom_Boost(phantom, 5).runTaskTimer(Plugin.getInstance(), 0, 20);
				phantom.addScoreboardTag("Légendaire");
			}
			
			phantom.setHealth(phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		}
	}
	
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		
		if (e.getEntity() instanceof Phantom) {
			if (e.getCause() == DamageCause.POISON) e.setCancelled(true);
			if (e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.FIRE_TICK) {
				
				e.setDamage(e.getDamage() * 3);
			}
		}
	}
	
	@EventHandler
	public void onCombust(EntityCombustEvent e) {
		if (e.getEntity() instanceof Phantom) {
			Phantom phantom = (Phantom) e.getEntity();
			
			if (!phantom.getPassengers().isEmpty()) {
				e.setCancelled(true);
			}
		}
	}
	
	private void giveEffect(LivingEntity ent, PotionEffect pe) {
		if (ent.hasPotionEffect(pe.getType())) {
			if (ent.getPotionEffect(pe.getType()).getDuration() < pe.getDuration() && ent.getPotionEffect(pe.getType()).getAmplifier() <= pe.getAmplifier()) {
				ent.removePotionEffect(pe.getType());
				ent.addPotionEffect(pe);
			}
		} else {
			ent.addPotionEffect(pe);
		}
	}
}
