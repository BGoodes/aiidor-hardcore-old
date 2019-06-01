package fr.aiidor.mch.events;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.mch.WorldSound;

public class ZombieBreak extends BukkitRunnable {
	
	private Zombie z;
	
	public ZombieBreak(Zombie z) {
		this.z = z;
	}
	
	@Override
	public void run() {
		
		if (!z.isDead() && z != null) {
			if (PlayerNext()) {
				
				if (z.getTarget() != null) {
					if (canMine()) {
						Block block = z.getWorld().getBlockAt(z.getLocation().add(0, -1, 0));
						
						if (block != null) Break(block);
						return;
					}
				}
				
				
				Float yaw = (float) Math.round(z.getLocation().getYaw() / 90);
			     BlockFace bf = null;
			     if(yaw == -4 || yaw == 0 || yaw == 4) bf = BlockFace.SOUTH;
			     if(yaw == -1 || yaw == 3) bf = BlockFace.EAST;
			     if(yaw == -2 || yaw == 2) bf = BlockFace.NORTH;
			     if(yaw == -3 || yaw == 1) bf = BlockFace.WEST;
			     
			     if (yaw != null) {
			    	 
				     Block frontBlock = z.getLocation().getBlock().getRelative(bf, 1);
				     Block frontBlockTop = z.getWorld().getBlockAt(frontBlock.getLocation().add(0, 1, 0));
				     
				     new WorldSound(frontBlock.getLocation()).PlaySound(Sound.BLOCK_STONE_BREAK);
				     
				    if (frontBlock != null) Break(frontBlock);
				    if (frontBlockTop != null) Break(frontBlockTop);
			     }
			     
			} else {
				z.setHealth(0);
				cancel();
			}
		} else {
			cancel();
		}
	}
	
	private Boolean canMine() {
		if (z.getWorld().equals(z.getTarget().getWorld())) {
			return false;
		}
		if (z != null && z.getTarget() != null) {
			if (z.getTarget().getLocation().getY() + 3 <= z.getLocation().getY() && z.getTarget().getLocation().getBlockY() < 60) {
				double distance = Math.sqrt(Math.pow((z.getLocation().getX()-z.getTarget().getLocation().getBlockX()), 2) 
						
						+ Math.pow((z.getLocation().getZ()-z.getTarget().getLocation().getBlockZ()), 2));
				if (distance <= 20) {
					return true;
				}
			}
		}

		return false;
	}
	
	private void Break(Block b) {
		if (b != null) {
		     if (b.getType() != Material.AIR
			    	 && b.getType() != Material.BEDROCK && b.getType() != Material.OBSIDIAN
			         && b.getType() != Material.WATER && b.getType() != Material.LAVA){
			    	 
			       b.setType(Material.AIR);
			       b.getLocation().getWorld().playEffect(b.getLocation().add(0.5,0,0.5), Effect.STEP_SOUND, b.getType());
		     } 
		}

	}
	
	private boolean PlayerNext() {
		
		if (Bukkit.getOnlinePlayers().size() == 0) return false;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getWorld().equals(z.getWorld())) {
				if (p.getLocation().distance(z.getLocation()) <= 64) {
					return true;
				}
			}

		}
		return false;
	}
}
