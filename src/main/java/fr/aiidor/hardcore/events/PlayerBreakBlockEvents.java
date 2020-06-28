package fr.aiidor.hardcore.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.aiidor.hardcore.managers.LockManager;

public class PlayerBreakBlockEvents implements Listener {
	
	@EventHandler
	public void playerBreakBlockEvent(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Block b = e.getBlock();
		
		LockManager lock = new LockManager(b, player);
		
		if (!lock.canOpen()) {
			e.setCancelled(true);
			return;
		}
		
		if (lock.hasLockBlock()) {
			Block c = lock.getLockBlock();
			lock = new LockManager(c, player);
			
			e.setCancelled(true);
			
			if (lock.canOpen() || player.hasPermission("lock.bypass")) {
				lock.removeSigns();
			}
		}
	}
}
