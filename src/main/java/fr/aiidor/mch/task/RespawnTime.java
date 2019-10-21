package fr.aiidor.mch.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnTime extends BukkitRunnable {

	private Player player;
	
	public RespawnTime(Player player) {
		this.player = player;
	}
	
	int time = 10;
	
	@Override
	public void run() {
		
		time --;
		
		if (time == 0) {
			cancel();
		}
	}

	public Player getPlayer() {
		return player;
	}
	
	public Boolean isPlayer(Player player) {
		return this.player.equals(player);
	}
}
