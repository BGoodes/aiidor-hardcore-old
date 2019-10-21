package fr.aiidor.mch.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

public class Zombie_Miner extends BukkitRunnable {
	
	private Zombie z;
	
	public Zombie_Miner(Zombie z) {
		this.z = z;
	}
	
	@Override
	public void run() {
		
		if (z != null && !z.isDead() && z.isValid()) {
			if (!getPlayersNext().isEmpty()) {
				
				if (z.getTarget() != null) {
					if (canMineBotom()) {
						Block block = z.getWorld().getBlockAt(z.getLocation().clone().add(0, -1, 0));
						
						if (block != null) Break(block);
						return;
					}
					
					Float yaw = (float) Math.round(z.getLocation().getYaw() / 90);
					
				     BlockFace bf = null;
				     if(yaw == -4 || yaw == 0 || yaw == 4) bf = BlockFace.SOUTH;
				     if(yaw == -1 || yaw == 3) bf = BlockFace.EAST;
				     if(yaw == -2 || yaw == 2) bf = BlockFace.NORTH;
				     if(yaw == -3 || yaw == 1) bf = BlockFace.WEST;
				     
				     if (yaw != null && bf != null) {
				    	 
					     Block frontBlock = z.getLocation().getBlock().getRelative(bf, 1);
					     Block frontBlockTop = z.getWorld().getBlockAt(frontBlock.getLocation().clone().add(0, 1, 0));
					     
					    if (frontBlock != null) Break(frontBlock);
					    if (frontBlockTop != null) Break(frontBlockTop);
					    
				     }
				}  else {
					List<Player> players = getPlayersNext();
					z.setTarget(players.get(new Random().nextInt(players.size())));
					return;
				}
				
			}
		} else {
			cancel();
		}
	}
	
	private Boolean canMineBotom() {
		
		if (z != null && z.getTarget() != null) {
			if (z.getWorld().equals(z.getTarget().getWorld())) {
				if (z.getTarget().getLocation().getY() + 3 <= z.getLocation().getY() && z.getTarget().getLocation().getBlockY() <= 60) {
					
					double distance = Math.sqrt(Math.pow((z.getLocation().getBlockX()-z.getTarget().getLocation().getBlockX()), 2) + Math.pow((z.getLocation().getBlockZ()-z.getTarget().getLocation().getBlockZ()), 2));
					
					if (distance <= 10) {
						return true;
					}
				}
			}

		}

		return false;
	}
	
	private void Break(Block b) {
		if (b != null) {
		     if (b.getType() != Material.AIR && b.getType() != Material.BEDROCK && b.getType() != Material.OBSIDIAN && b.getType() != Material.WATER && b.getType() != Material.LAVA) {
			    	 
			       b.getLocation().getWorld().playEffect(b.getLocation().add(0.5,0,0.5), Effect.STEP_SOUND, b.getType());
			       b.setType(Material.AIR);
		     } 
		}

	}
	
	private List<Player> getPlayersNext() {
		
		List<Player> player = new ArrayList<Player>();
		
		if (Bukkit.getOnlinePlayers().size() == 0) return player;
		for (Player p : Bukkit.getOnlinePlayers()) {
			
			if (p.getWorld().equals(z.getWorld()) && p.getGameMode() == GameMode.SURVIVAL) {
				
				if (p.getLocation().distance(z.getLocation()) <= 64) {
					player.add(p);
				}
			}

		}
		return player;
	}
}
