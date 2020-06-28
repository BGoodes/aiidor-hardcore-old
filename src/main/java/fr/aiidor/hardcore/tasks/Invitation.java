package fr.aiidor.hardcore.tasks;

import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.managers.GuildManager.Guild;

public class Invitation extends BukkitRunnable {
	
	private int timer = 60;
	
	private Guild guild;
	private UUID uuid;
	
	public Invitation(UUID uuid, Guild guild) {
		this.guild = guild;
		this.uuid = uuid;
	}

	
	@Override
	public void run() {
		
		timer --;
		
		if (timer == 0) {
			cancel();
			
			if (Plugin.getInstance().getGuildManager().invites.containsKey(uuid)) {
				Plugin.getInstance().getGuildManager().invites.get(uuid).remove(this);
			}

			return;
		}
	}
	
	
	
	public Guild getGuild() {
		return guild;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public Boolean isExpired() {
		return isCancelled();
	}
}
