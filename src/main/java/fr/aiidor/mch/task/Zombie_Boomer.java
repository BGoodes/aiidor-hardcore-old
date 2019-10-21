package fr.aiidor.mch.task;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Zombie_Boomer extends BukkitRunnable {
	
	private Zombie z;
	
	public Zombie_Boomer(Zombie z) {
		this.z = z;
	}

	@Override
	public void run() {
		
		if (z != null && !z.isDead() && z.isValid()) {
			
			z.getWorld().playSound(z.getLocation(), Sound.ENTITY_TNT_PRIMED,SoundCategory.HOSTILE, 1, 1);
			particle(z.getLocation().clone().add(-0.5, 1.9 ,-0.5));
			
		} else {
			cancel();
		}
	}
	
	private void particle(Location loc) {
		
		loc.add(new Vector(0.5, 0, 0.5));

		for (int i = 0; i != 10; i ++) {
			loc.add(new Vector(0, 0.07, 0));
			loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc,  1, 0, 0, 0, 0);
		}
	}
}
