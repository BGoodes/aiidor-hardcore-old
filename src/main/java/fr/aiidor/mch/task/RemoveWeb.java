package fr.aiidor.mch.task;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.mch.Main;

public class RemoveWeb extends BukkitRunnable {

	private int Timer = 0;
	private int end = 0;
	
	private Block web;
	
	private Main main;
	
	public RemoveWeb(Main main, Block web) {
		this.web = web;
		this.main = main;
		
		int min = 5;
		int max = 30;
		
		end = min + new Random().nextInt(max - min);
	}
	
	@Override
	public void run() {
		Timer++;
		
		if (Timer == end) {
			
			removeWeb();
			if (main.webTasks.contains(this)) main.webTasks.remove(this);
			
			cancel();
			return;
		}
	}
	
	public Block getCobweb() {
		return web;
	}
	
	public void removeWeb() {
		if (web != null && web.getType() == Material.COBWEB) {
			web.setType(Material.AIR);
			web.getWorld().playEffect(web.getLocation(), Effect.STEP_SOUND, Material.COBWEB);
		}
	}

}
