package fr.aiidor.hardcore.tools;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class WorldSound {
	
	private Location loc;
	
	public WorldSound(Location loc) {
		this.loc = loc;
	}
	
	public void PlaySound(Sound s, SoundCategory cat) {
		loc.getWorld().playSound(loc, s, cat, 1, 1);
	}
	
	public void PlaySound(Sound s, SoundCategory cat, Float volume) {
		loc.getWorld().playSound(loc, s, cat, volume, 1);
	}
	
	
}
