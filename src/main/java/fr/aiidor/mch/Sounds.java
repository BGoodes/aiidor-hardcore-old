package fr.aiidor.mch;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class Sounds {
	
	private Player player;
	
	public Sounds(Player p) {
		player = p;
	}
	
	public void PlaySound(Sound s, SoundCategory category) {
		player.playSound(player.getLocation(), s, category, 1, 1);
	}
	
	public void PlaySound(Sound s, SoundCategory category, float volume, float pitch) {
		player.playSound(player.getLocation(), s, category, volume, pitch);
	}
}
