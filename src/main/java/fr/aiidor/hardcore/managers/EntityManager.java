package fr.aiidor.hardcore.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import fr.aiidor.hardcore.tasks.RemoveWeb;

public class EntityManager {
	
	public List<RemoveWeb> webTasks = new ArrayList<>();
	public ArrayList<Player> vanish = new ArrayList<>();
	
	public void unload() {
		for (RemoveWeb webTask : webTasks) {
			webTask.removeWeb();
		}
	}
	
	public static void giveEffect(LivingEntity ent, PotionEffect pe) {
		if (ent.hasPotionEffect(pe.getType())) {
			if (ent.getPotionEffect(pe.getType()).getDuration() < pe.getDuration() && ent.getPotionEffect(pe.getType()).getAmplifier() <= pe.getAmplifier()) {
				ent.removePotionEffect(pe.getType());
				ent.addPotionEffect(pe);
			}
		} else {
			ent.addPotionEffect(pe);
		}
		
	}
}
