package fr.aiidor.mch.mob;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Shulker_Events implements Listener {
	
	@EventHandler
	public void ProjectileBoom(ProjectileHitEvent e) {
		
		if (e.getEntityType() == EntityType.SHULKER_BULLET) {
			
			ShulkerBullet bullet = (ShulkerBullet) e.getEntity();
			bullet.getWorld().createExplosion(bullet.getLocation(), 3);
		}
	}
	
}
