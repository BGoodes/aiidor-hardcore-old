package fr.aiidor.mch;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class WorldSound {
	
	private Location loc;
	
	public WorldSound(Location loc) {
		this.loc = loc;
	}
	
	public void PlaySound(Sound s, SoundCategory category) {
		
		loc.getWorld().playSound(loc, s, category, 1, 1);
	}
}
