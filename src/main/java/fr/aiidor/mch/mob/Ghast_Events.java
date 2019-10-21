package fr.aiidor.mch.mob;

import java.util.Random;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ghast_Events implements Listener {
	
	@EventHandler
	public void onMobSpawn(EntitySpawnEvent e) {
		
		if (e.getEntity() instanceof Ghast) {
			
			Random r = new Random();
			Ghast ghast = (Ghast) e.getEntity();
			
			if (r.nextInt(30) == 0) {
				ghast.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
			}
			
			return;
		}
		
		if (e.getEntity().getType() == EntityType.FIREBALL) {
			Fireball ball = (Fireball) e.getEntity();
			
			if (ball.getShooter() != null && ball.getShooter() instanceof Ghast) {
				
				Ghast ghast = (Ghast) ball.getShooter();
				
				if (!ghast.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
					
					ball.setYield(4);
					ball.setVelocity(ball.getDirection().multiply(5));
				}
			}
			
			return;
		}
	}
	
	
	@EventHandler
	public void onMobAttack(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() instanceof Arrow) {
			if (e.getEntity() instanceof Ghast) {
				
				Arrow arrow = (Arrow) e.getDamager();
				
				if (!arrow.getScoreboardTags().contains("ballon_piercer")) e.setCancelled(true);
			}
		}
	}
}
