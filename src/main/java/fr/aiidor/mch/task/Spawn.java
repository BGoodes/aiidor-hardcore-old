package fr.aiidor.mch.task;

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
					
					Random r = new Random();
					
					if (world.getEnvironment() == Environment.NORMAL) {
						//NIGHT
						if (world.getTime() >= 13800 && world.getTime() <= 22200) {
							
							
							//SPAWN
							if (p.getLocation().getY() >= 125) {
								
								if (r.nextInt(60) == 0) {
									Vex vex = (Vex) world.spawnEntity(getMobLocation(p.getLocation()), EntityType.VEX);
									
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
	}
	
	private Location getMobLocation(Location location) {
		
		Location loc = location.clone();
		
		int valeurMin = - 128;
		int valeurMax = 128;
		
		Random r = new Random();
		int x = valeurMin + r.nextInt(valeurMax - valeurMin);
		int z = valeurMin + r.nextInt(valeurMax - valeurMin);
		int y = valeurMin + r.nextInt(valeurMax - valeurMin);
		
		//int y = -10 + r.nextInt(valeurMax + 10);
		location.add(x, y, z);
		
		return loc;
		
	}
}
