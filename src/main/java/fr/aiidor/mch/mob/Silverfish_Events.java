package fr.aiidor.mch.mob;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class Silverfish_Events implements Listener {
	
	@EventHandler
	public void onSilverFishBurrow(EntityChangeBlockEvent e) {
		
		if(e.getEntity().getType().equals(EntityType.SILVERFISH)) {
			
			if (isInfested(e.getTo())) {
				
				for (int x = e.getEntity().getLocation().getBlockX()-3; x <= e.getEntity().getLocation().getBlockX() + 3; x++) {
					for (int z = e.getEntity().getLocation().getBlockZ()-3; z <= e.getEntity().getLocation().getBlockZ() + 3; z++) {
						
						if (new Random().nextInt(40) == 0) {
							
							int choose = new Random().nextInt(2);
							if (new Random().nextBoolean()) {
								if (e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() + choose , z).getType() == Material.STONE) {
									e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() + choose , z).setType(Material.INFESTED_STONE);
								}
								
							}
							else {
								if (e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() - choose , z).getType() == Material.STONE) {
									e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() - choose , z).setType(Material.INFESTED_STONE);
								}
							}
							
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBreakBlock(BlockBreakEvent e) {

		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			if (e.getBlock().getType() == Material.STONE) {
				Random r = new Random();
				
				int choice = r.nextInt(400);
				
				if (choice == 0) {
					e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.SILVERFISH);
					return;
				}
				return;
			}
		}
	}
	
	private Boolean isInfested(Material mat) {
		if (mat.toString().contains("INFESTED")) return true;
		return false;
	}
}
