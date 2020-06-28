package fr.aiidor.hardcore.tasks;

import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Phantom;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Phantom_Boost extends BukkitRunnable {
	
	private Phantom p;
	private int Cooldown = 3;
	private int boom;
	
	public Phantom_Boost(Phantom p, Integer boom) {
		this.p = p;
		this.boom = boom;
	}

	@Override
	public void run() {
		
		if (p != null && !p.isDead() && p.isValid()) {
			
			
			if (Cooldown < 0) Cooldown = 0;
			if (Cooldown > 0) Cooldown --;
				
			p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 15, 0.1, 0.1, 0.1, 0.1);
			
			if (Cooldown == 0) {
					
				if (p.getTarget() != null) {
						
					Entity ent = p.getTarget();
						
					if (p.getWorld().equals(ent.getWorld())) {
							
						if (p.getLocation().distance(ent.getLocation()) > 10) {
								
							Vector vector = ent.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
							vector.multiply(0.05);
							
					        Fireball fire = (Fireball) p.getWorld().spawn(p.getLocation(), Fireball.class);
					         
					        fire.setDirection(vector);
					        fire.setShooter(p);
					        fire.setYield(boom);
					        Cooldown = 3;
						}
					}
				}
			}		
			
		} else {
			cancel();
		}
	}
}
