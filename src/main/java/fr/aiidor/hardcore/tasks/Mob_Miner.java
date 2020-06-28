package fr.aiidor.hardcore.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Mob_Miner extends BukkitRunnable {
	
	private Mob m;
	
	public Mob_Miner(Mob m) {
		this.m = m;
	}
	
	@Override
	public void run() {
		
		if (m != null && !m.isDead() && m.isValid()) {
			if (!getPlayersNext().isEmpty()) {
				
				if (m.getTarget() != null) {
					if (canMineBotom()) {
						Block block = m.getWorld().getBlockAt(m.getLocation().clone().add(0, -1, 0));
						
						if (block != null) Break(block);
						return;
					}
					
					Float yaw = (float) Math.round(m.getLocation().getYaw() / 90);
					
				     BlockFace bf = null;
				     if(yaw == -4 || yaw == 0 || yaw == 4) bf = BlockFace.SOUTH;
				     if(yaw == -1 || yaw == 3) bf = BlockFace.EAST;
				     if(yaw == -2 || yaw == 2) bf = BlockFace.NORTH;
				     if(yaw == -3 || yaw == 1) bf = BlockFace.WEST;
				     
				     if (yaw != null && bf != null) {
				    	 
					     Block frontBlock = m.getLocation().getBlock().getRelative(bf, 1);
					     Block frontBlockTop = m.getWorld().getBlockAt(frontBlock.getLocation().clone().add(0, 1, 0));
					     
					    if (frontBlock != null) Break(frontBlock);
					    if (frontBlockTop != null) Break(frontBlockTop);
					    
				     }
				}  else {
					List<Player> players = getPlayersNext();
					m.setTarget(players.get(new Random().nextInt(players.size())));
					return;
				}
				
			}
		} else {
			cancel();
		}
	}
	
	private Boolean canMineBotom() {
		
		if (m != null && m.getTarget() != null) {
			if (m.getWorld().equals(m.getTarget().getWorld())) {
				if (m.getTarget().getLocation().getY() + 3 <= m.getLocation().getY() && m.getTarget().getLocation().getBlockY() <= 60) {
					
					double distance = Math.sqrt(Math.pow((m.getLocation().getBlockX()-m.getTarget().getLocation().getBlockX()), 2) + Math.pow((m.getLocation().getBlockZ()-m.getTarget().getLocation().getBlockZ()), 2));
					
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
		
		if (Bukkit.getOnlinePlayers().isEmpty()) return player;
		for (Player p : Bukkit.getOnlinePlayers()) {
			
			if (p.getWorld().equals(m.getWorld()) && p.getGameMode() == GameMode.SURVIVAL) {
				
				if (p.getLocation().distance(m.getLocation()) <= 64) {
					player.add(p);
				}
			}

		}
		return player;
	}
}
