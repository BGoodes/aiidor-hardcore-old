package fr.aiidor.hardcore.events;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.hardcore.managers.EntityManager;
import fr.aiidor.hardcore.tools.DurabilityManager;

public class DamageEvents implements Listener {
	
	@EventHandler
	public void damage(EntityDamageEvent e) {
		if (e.getEntity() instanceof LivingEntity) {
			
			LivingEntity entity = (LivingEntity) e.getEntity();
			
			if (e.getCause() == DamageCause.ENTITY_EXPLOSION || e.getCause() == DamageCause.BLOCK_EXPLOSION) {
				EntityManager.giveEffect(entity, new PotionEffect(PotionEffectType.BLINDNESS, (int) (e.getFinalDamage() * 20 * 0.7), 0, false, false, false));
				EntityManager.giveEffect(entity, new PotionEffect(PotionEffectType.SLOW, (int) (e.getFinalDamage() * 1.8 * 20), 1, false, false, true));
				return;
			}
			
			if (e.getCause() == DamageCause.FALL) {
				EntityManager.giveEffect(entity, new PotionEffect(PotionEffectType.SLOW, (int) (e.getFinalDamage() * 2 * 20), 2, false, false, true));
				return;
			}
			
			if (e.getCause() == DamageCause.HOT_FLOOR) {
				entity.setFireTicks(new Random().nextInt(180));
				return;
			}
			
			if (e.getCause() == DamageCause.STARVATION) {
				EntityManager.giveEffect(entity, new PotionEffect(PotionEffectType.SLOW, 100, 1, false, false, true));
				EntityManager.giveEffect(entity, new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1, false, false, true));
				return;
			}
		}
	}
	
	@EventHandler 
	public void onEntityHit(EntityDamageByEntityEvent e) {
		Entity damaged = e.getEntity();
		Entity damager = e.getDamager();
		
		if (damager instanceof Egg || damager instanceof Snowball) {
			if (e.getDamage() == 0) e.setDamage(0.1);
		}
		
		if (damaged instanceof LivingEntity) {
			LivingEntity damaged_ent = (LivingEntity) damaged;
			
			
			if (damager.getType() == EntityType.SPECTRAL_ARROW) {
				EntityManager.giveEffect(damaged_ent, new PotionEffect(PotionEffectType.GLOWING, 1200, 0));
			}
			
			
			//SLIME STICK
			if (damager instanceof LivingEntity) {
				LivingEntity damager_ent = (LivingEntity) damager;
				
				if (damager_ent.getEquipment() != null && damager_ent.getEquipment().getItemInMainHand() != null) {
					
					ItemStack hand = damager_ent.getEquipment().getItemInMainHand();
					
					if (hand.getType() == Material.STONE_SWORD && hand.getItemMeta().hasCustomModelData()) {
							
						if (hand.getItemMeta().getCustomModelData() == 1) {
								
							damager_ent.getWorld().spawnParticle(Particle.SLIME, e.getEntity().getLocation().add(0, e.getEntity().getHeight() / 2, 0), 15, 0.5, 0.5, 0.5, 0.5);
							damager_ent.getWorld().playSound(damager_ent.getLocation(), Sound.ENTITY_SLIME_HURT, SoundCategory.HOSTILE, 0.6f, 1);
							e.setDamage(0);
							
							if (damager_ent instanceof Player) {
								
								Player player = (Player) damager_ent;
								if (player.getGameMode() != GameMode.CREATIVE) {
									DurabilityManager durability = new DurabilityManager(hand);
									durability.removeDurability();
									
									if (durability.getDurabiliy() <= 0) durability.breakItem(player);
									
								}
							}		
						}
					}
				}
			}
		}
	}
	
}
