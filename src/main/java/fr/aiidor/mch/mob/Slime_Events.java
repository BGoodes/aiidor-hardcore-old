package fr.aiidor.mch.mob;

import java.util.Random;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Slime_Events implements Listener {
	
	@EventHandler
	public void mobAttackEvent(EntityDamageByEntityEvent e) {
		
		if (e.getDamager().getType() == EntityType.SLIME) {
			
			Slime slime = (Slime) e.getDamager();
			e.getEntity().setVelocity(slime.getLocation().getDirection().multiply(slime.getSize() - 2).setY(1.1));
		}
				
		if (e.getDamager().getType() == EntityType.MAGMA_CUBE) {
			e.getEntity().setFireTicks((new Random().nextInt(27) + 3) * 20);
		}
	}
	
	@EventHandler
	public void mobDamageEvent(EntityDamageEvent e) {
		if (e.getEntity() instanceof Slime) {
			if (e.getCause() == DamageCause.FALL) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Slime) {
			if (e.getDamager() instanceof Projectile) e.setCancelled(true);
		}
	}
}
