package fr.aiidor.mch.events;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerEvent implements Listener {
	
	@EventHandler
	public void onBreackBlock(BlockBreakEvent e) {
		if (e.getBlock().getType() == Material.COBWEB) {
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			
			return;
		}
		
		if (e.getBlock().getType() == Material.STONE) {
			Random ran = new Random();
			int choice = ran.nextInt(200);
			
			if (choice == 0) {
				e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.SILVERFISH);
				return;
			}
			return;
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.getDrops().clear();
		e.setDroppedExp(0);
		e.setDeathMessage("§f[§cMORT§f] §6" + e.getDeathMessage());
		
		e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
	}
}
