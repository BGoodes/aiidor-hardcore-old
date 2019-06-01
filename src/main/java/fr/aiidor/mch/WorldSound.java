package fr.aiidor.mch;

import org.bukkit.Location;
import org.bukkit.Sound;

public class WorldSound {
	
	private Location loc;
	
	public WorldSound(Location loc) {
		this.loc = loc;
	}
	
	public void PlaySound(Sound s) {
		
		loc.getWorld().playSound(loc, s, 1, 1);
	}
}
