package fr.aiidor.mch.mob;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

public class Blaze_Events implements Listener {
	
	@EventHandler
	public void ProjectileBoom(ProjectileHitEvent e) {
		
		if (e.getEntityType() == EntityType.SMALL_FIREBALL) {
			
			SmallFireball fire = (SmallFireball) e.getEntity();
			
			if (fire.getShooter() != null && fire.getShooter() instanceof Blaze) {
				
				
				int number = 5 + new Random().nextInt(15);
				
				for (int i = 0; i != number; i ++) {
					float x = -2.0F + (float)(Math.random() * 2.0D + 1.0D);

					float y = -3.0F + (float)(Math.random() * 3.0D + 1.0D);

					float z = -2.0F + (float)(Math.random() * 2.0D + 1.0D);
					
					BlockData mt = Material.FIRE.createBlockData();
					
					FallingBlock fallingBlock = e.getEntity().getWorld().spawnFallingBlock(e.getEntity().getLocation(), mt);
					fallingBlock.setDropItem(false);
					fallingBlock.setVelocity(new Vector(x, y, z));
					
				}
			}
		}
	}
}
