package fr.aiidor.hardcore.tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.scheduler.BukkitRunnable;

public class Spawn extends BukkitRunnable {

	@Override
	public void run() {
		
		if (!Bukkit.getOnlinePlayers().isEmpty()) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				
				if (p.getGameMode() != GameMode.SPECTATOR) {
					World world = p.getWorld();
					
					if (world.getEnvironment() == Environment.NORMAL) {
						
						if (world.getTime() >= 13800 && world.getTime() <= 22200) {
							
							Random ran = new Random();
							//SPAWN
							if (p.getLocation().getY() >= 125) {
								
								if (ran.nextInt(60) == 0) {
									
									Vex vex = (Vex) world.spawnEntity(getVexSpawnLocation(p.getLocation()), EntityType.VEX);
									
									if (p.getGameMode() == GameMode.SURVIVAL) vex.setTarget(p);
										
									vex.setCanPickupItems(true);
									vex.setCharging(true);
								}
							}
						}
					}
				}
			}
		}
		
		
		//STORM EFFECTS
		
	}
	
	private Location getVexSpawnLocation(Location location) {
		
		Location loc = location.clone();
		
		int valeurMin = - 128;
		int valeurMax = 128;
		
		Random r = new Random();
		int x = valeurMin + r.nextInt(valeurMax - valeurMin);
		int z = valeurMin + r.nextInt(valeurMax - valeurMin);
		
		int y = -30 + r.nextInt(valeurMax + 30);
		loc.add(x, y, z);
		
		return loc;
		
	}
}
