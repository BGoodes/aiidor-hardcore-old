package fr.aiidor.hardcore.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.files.MCFile;
import fr.aiidor.hardcore.tools.UUIDFetcher;

public class EconManager {

	private List<Account> accounts = new ArrayList<EconManager.Account>();
	private Double start_balance;
	
	public static String devise;
	
	public EconManager() {
		
		devise = MCFile.CONFIG.getYamlConfig().getString("Money.devise");
		start_balance = MCFile.CONFIG.getYamlConfig().getDouble("Money.start-balance");
	}
	
	public void load() {
		FileConfiguration c = MCFile.MONEY.getYamlConfig();
		c.getKeys(false).forEach(k -> accounts.add(new Account(UUID.fromString(k), c.getDouble(k))));
	}
	
	public void save() {
		FileConfiguration c = MCFile.MONEY.getYamlConfig();
		c.getKeys(false).forEach(k -> c.set(k, null));
		accounts.forEach(a -> c.set(a.getOwnerId().toString(), a.getBalance()));
		MCFile.MONEY.save(c);
	}
	
	public Account getAccount(UUID uuid) {
		try {
			return accounts.stream().filter(a-> a.getOwnerId().equals(uuid)).findFirst().get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}
	
	public List<Account> getAllAccounts() {
		return accounts;
	}
	
	public Account getAccount(Player player) {
		return getAccount(player.getUniqueId());
	}
	
	public Account getAccount(String name) {
		return getAccount(UUIDFetcher.getUUID(name));
	}
	
	public Boolean hasAccount(UUID uuid) {
		return getAccount(uuid) != null;
	}
	
	public Boolean hasAccount(Player player) {
		return getAccount(player.getUniqueId()) != null;
	}
	
	public Boolean hasAccount(String name) {
		return getAccount(name) != null;
	}
	
	public void createAccount(Player player) {
		createAccount(player.getUniqueId());
	}
	
	public void createAccount(UUID uuid) {
		accounts.add(new Account(uuid, start_balance));
	}
	
	public void destroyAccount(Account account) {
		accounts.remove(account);
	}
	
	public void destroyAccount(String name) {
		accounts.remove(getAccount(name));
	}
	
	public void destroyAccount(UUID uuid) {
		accounts.remove(getAccount(uuid));
	}
	
	public void destroyAccount(Player player) {
		destroyAccount(player.getUniqueId());
	}
	
	public class Account {
		private UUID uuid;
		private Double balance;
		private String name;
		
		public Account(UUID uuid, Double money) {
			this.uuid = uuid;
			this.balance = money;
			name = UUIDFetcher.getName(uuid);
		}
		
		public UUID getOwnerId() {
			return uuid;
		}
		
		public String getOwnerName() {
			return name;
		}
		
		public Double getBalance() {
			balance = (double) Math.round(balance * 100d) / 100d;
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
