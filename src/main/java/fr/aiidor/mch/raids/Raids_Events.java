package fr.aiidor.mch.raids;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Raids_Events implements Listener {
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		
		if (e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equals("§cHunter")) {
			if (e.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getDamager();
				
				if (arrow.getShooter() != null && arrow.getShooter() instanceof Skeleton) {
					Skeleton skeleton = (Skeleton) arrow.getShooter();
					
					if (skeleton.getCustomName().equals("§cHunter")) {
						e.setCancelled(true);
					}
				}
			}
		}
	}
}
