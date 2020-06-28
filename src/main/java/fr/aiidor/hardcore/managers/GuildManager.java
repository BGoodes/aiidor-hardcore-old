package fr.aiidor.hardcore.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.files.MCFile;
import fr.aiidor.hardcore.tasks.Invitation;
import fr.aiidor.hardcore.tools.UUIDFetcher;

public class GuildManager {

	private List<Guild> guilds;
	public Double guild_price;
	
	public HashMap<UUID, List<Invitation>> invites = new HashMap<>();
	
	public GuildManager() {
		guild_price = MCFile.CONFIG.getYamlConfig().getDouble("Guilds.guild-price");
		guilds = new ArrayList<Guild>();
	}
	
	public void load() {
		FileConfiguration c = MCFile.GUILDS.getYamlConfig();
		
		for (String k : c.getKeys(false)) {
			
			UUID master = UUID.fromString(c.getString(k + ".master"));
			Double balance = c.getDouble(k + ".balance");
			
			List<UUID> members = new ArrayList<UUID>();
			c.getStringList(k + ".members").forEach(s->members.add(UUID.fromString((String) s)));
			
			guilds.add(new Guild(k, master, members, balance));
		}
	}
	
	public void save() {
		FileConfiguration c = MCFile.GUILDS.getYamlConfig();
		c.getKeys(false).forEach(k -> c.set(k, null));
		
		for (Guild g : guilds) {
			List<String> members = new ArrayList<String>();
			g.getMembers().forEach(m-> members.add(m.toString()));
			
			c.set(g.getName() + ".master", g.getMaster().toString());
			c.set(g.getName() + ".balance", g.getBalance());
			c.set(g.getName() + ".members", members);
		}
		
		MCFile.GUILDS.save(c);
	}
	
	public List<Guild> getAllGuilds() {
		return guilds;
	}
	
	public void createGuild(String name, UUID master) {
		guilds.add(new Guild(name, master, new ArrayList<UUID>(), 0.0));
	}
	
	public void destroyGuild(Guild guild) {
		guilds.remove(guild);
	}
	
	public Guild getGuild(String name) {
		try {
			return guilds.stream().filter(g->g.getName().equals(name)).findFirst().get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public Guild getPlayerGuild(UUID uuid) {
		try {
			return guilds.stream().filter(g->g.getMembers().contains(uuid)).findFirst().get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public Guild getPlayerGuild(Player player) {
		return getPlayerGuild(player.getUniqueId());
	}
	
	public Guild getPlayerGuild(String name) {
		return getPlayerGuild(UUIDFetcher.getUUID(name));
	}
	
	public Boolean hasGuild(UUID uuid) {
		return getPlayerGuild(uuid) != null;
	}
	
	public Boolean hasGuild(String name) {
		return getPlayerGuild(name) != null;
	}
	
	public Boolean hasGuild(Player player) {
		return getPlayerGuild(player.getUniqueId()) != null;
	}
	
	public Boolean isGuild(String name) {
		return getGuild(name) != null;
	}
	
	public class Guild {
		
		private String name;
		
		private UUID master;
		private List<UUID> members;

		private Double balance;
		
		public Guild(String name, UUID master, List<UUID> members, Double balance) {
			this.setName(name); 
			
			this.setMaster(master);
			
			this.members = members;
			if (!members.contains(master)) this.members.add(master);
			
			this.setBalance(balance);
		}

		public String getName() {
			return name;
		}
		
		public String getTag() {
			return "§f[§d" + getName() + "§f] §r";
		}
		
		public void setName(String name) {
			this.name = name;
		}

		public UUID getMaster() {
			return master;
		}

		public void setMaster(UUID master) {
			this.master = master;
		}

		public List<UUID> getMembers() {
			return members;
		}
		
		public Boolean isMaster(UUID uuid) {
			return master.equals(uuid);
		}
		
		public Boolean isMember(UUID uuid) {
			return getMembers().contains(uuid);
		}
		
		public Boolean isMember(String name) {
			return isMember(UUIDFetcher.getUUID(name));
		}
		
		public void broadcast(String message) {
			getMembers().stream().filter(id->Bukkit.getPlayer(id) != null).forEach(id->Bukkit.getPlayer(id).sendMessage(message));
		}
		
		public String getMemberName(UUID uuid) {
			return UUIDFetcher.getName(uuid);
		}
		
		public void addMember(UUID uuid) {
			members.add(uuid);
		}
		
		public void addMember(String name) {
			members.add(UUIDFetcher.getUUID(name));
		}
		public void addMember(Player player) {
			members.add(player.getUniqueId());
		}
		
		public void removeMember(UUID uuid) {
			members.remove(uuid);
			if (master.equals(uuid)) master = null;
		}
		
		public void removeMember(String name) {
			removeMember(UUIDFetcher.getUUID(name));
		}
		
		public void removeMember(Player player) {
			removeMember(player.getUniqueId());
		}
		
		public Double getBalance() {
			balance = (double)Math.round(balance * 100d) / 100d;
			return balance;
		}

		public void setBalance(Double balance) {
			balance = (double)Math.round(balance * 100d) / 100d;
			this.balance = balance;
		}
		
		public void addMoney(Double money) {
			setBalance(this.balance + money);
		}
		
		public void removeMoney(Double money) {
			setBalance(this.balance - money);
		}
		
		public Boolean hasMoney(Double money) {
			return balance >= money;
		}
	}
}
