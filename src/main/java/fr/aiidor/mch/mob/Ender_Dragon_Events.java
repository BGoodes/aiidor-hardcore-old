package fr.aiidor.mch.mob;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Ender_Dragon_Events implements Listener {
	
	@EventHandler
	public void changePhase(EnderDragonChangePhaseEvent e) {
		
		EnderDragon dragon = e.getEntity();

		if (e.getNewPhase() == Phase.CHARGE_PLAYER || e.getNewPhase() == Phase.LAND_ON_PORTAL || e.getNewPhase() == Phase.CIRCLING || e.getNewPhase() == Phase.LEAVE_PORTAL) {
			
			int number = 10 + new Random().nextInt(40);
			
			for (int i = 0; i != number; i ++) {
				
				float x = -2.0F + (float)(Math.random() * 2.0D + 2.0D);

				float y = -3.0F + (float)(Math.random() * 3.0D + 2.0D);

				float z = -2.0F + (float)(Math.random() * 2.0D + 2.0D);
				
				BlockData mt = Material.FIRE.createBlockData();
				
				FallingBlock fallingBlock = dragon.getWorld().spawnFallingBlock(dragon.getLocation(), mt);
				fallingBlock.setDropItem(false);
				fallingBlock.setVelocity(new Vector(x, y, z));
			}
		}
	}
	
	
	@EventHandler
	public void onLaunch(ProjectileLaunchEvent e) {
		
		if (e.getEntityType() == EntityType.DRAGON_FIREBALL) {
			DragonFireball ball = (DragonFireball) e.getEntity();
			
			if (e.getEntity().getShooter() != null && e.getEntity().getShooter()  instanceof EnderDragon) {	
				ball.setYield(4);
				ball.setIsIncendiary(true);
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() instanceof EnderDragon) {
			if (e.getEntity() instanceof LivingEntity) {
				LivingEntity ent = (LivingEntity) e.getEntity();
				
				giveEffect(ent, new PotionEffect(PotionEffectType.BLINDNESS, (10 + new Random().nextInt(10)) * 20, 0));
				
				for (Entity mob : ent.getWorld().getNearbyEntities(ent.getLocation(), 30, 10, 30)) {
					
					if (mob instanceof Enderman) {
						
						Enderman enderman = (Enderman) mob;
						enderman.setTarget(ent);
						
					} else if (mob instanceof Endermite) {
						
						Endermite endermite = (Endermite) mob;
						endermite.setTarget(ent);
					}
				}
			}
		}
	}

	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		
		if (e.getEntityType() == EntityType.DRAGON_FIREBALL) {
			DragonFireball ball = (DragonFireball) e.getEntity();
			
			if (e.getEntity().getShooter() != null && e.getEntity().getShooter()  instanceof EnderDragon) {
				ball.getWorld().createExplosion(ball.getLocation(), 5);
				
				int number = 3 + new Random().nextInt(7);
				
				for (int i = 0; i != number; i ++) {
					float x = -2.0F + (float)(Math.random() * 2.0D + 1.0D);

					float y = -3.0F + (float)(Math.random() * 3.0D + 1.0D);

					float z = -2.0F + (float)(Math.random() * 2.0D + 1.0D);
					
					BlockData mt = Material.FIRE.createBlockData();
					
					FallingBlock fallingBlock = ball.getWorld().spawnFallingBlock(ball.getLocation(), mt);
					fallingBlock.setDropItem(false);
					fallingBlock.setVelocity(new Vector(x, y, z));
				}
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
