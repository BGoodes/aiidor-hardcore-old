package fr.aiidor.hardcore.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.aiidor.hardcore.Plugin;

public enum MCFile {
	
	CONFIG("config.yml"),
	MONEY("money.yml", "Data"),
	GUILDS("guilds.yml", "Data");
	
	private final String fileName;
	private final File dataFolder;
	
	private MCFile(String fileName) {
		this.fileName = fileName;
		this.dataFolder = Plugin.getInstance().getDataFolder();
	}
	
	private MCFile(String fileName, String path) {
		this.fileName = fileName;
		this.dataFolder = new File(Plugin.getInstance().getDataFolder().getPath() + File.separator + path);
	}
	
	private MCFile(String fileName, String path, Boolean pluginsFolder) {
		
		this.fileName = fileName;
		
		if (pluginsFolder) this.dataFolder = new File(Plugin.getInstance().getDataFolder().getParent() + File.separator + path);
		else this.dataFolder = new File(Plugin.getInstance().getDataFolder().getPath() + File.separator + path);
	}
	
	public File getFile() { return new File(dataFolder, fileName); }
	
	//YAML
	public Boolean isYamlConfiguration() { return fileName.toLowerCase().endsWith(".yml"); }
	public FileConfiguration getYamlConfig() { return YamlConfiguration.loadConfiguration(getFile()); }
	public void save(FileConfiguration config) {
		try {
			config.save(getFile());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//JSON
	public Boolean isJSONConfiguration() { return fileName.toLowerCase().endsWith(".json"); }
	public JSONObject getJSONConfig() { 
		try {
			
			JSONParser jsonParser = new JSONParser();
			Object parsed = jsonParser.parse(new FileReader(getFile().getPath()));
			JSONObject jsonObject = (JSONObject) parsed;
			
			return jsonObject;
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	public JSONObject getJSONObject(String path) { 
		JSONObject object = getJSONConfig();
		
		for (String objectPath : path.split("/")) {
			object = (JSONObject) object.get(objectPath);
		}
		
		return object;
	}
	
	public void create(Logger logger) {
		if (fileName == null || fileName.isEmpty()) {
			throw new IllegalArgumentException("Ressource name cannot be null or empty");
		}
		
		InputStream in = Plugin.getInstance().getResource(getFileName());
		
		if (in == null) {
			
			if (!getFile().exists()) {
				
				if (!createDirectory()) {
					logger.severe("Failed to make directory");
					return;
				}
				
				logger.info("The file " + getFileName() + " was not found, creation in progress...");
				try {
					getFile().createNewFile();
					logger.info("The file " + getFileName() + " has been created.");
					
				} catch (IOException e) {
					e.printStackTrace();
					
					logger.severe("Unable to create file.");
					return;
				}
			}
			
			return;
		} 
		
		if (!createDirectory()) {
			logger.severe("Failed to make directory");
			return;
		}
		
		File outFile = getFile();
		
		try {
			if (!outFile.exists()) {
				logger.info("The file " + getFileName() + " was not found, creation in progress...");
				
				OutputStream out = new FileOutputStream(outFile);
				byte[] buf = new byte[1024];
				int n;
				
				while ((n = in.read(buf)) >= 0) {
					out.write(buf, 0, n);
				}
				
				out.close();
				in.close();
				
				if (!outFile.exists()) {
					logger.severe("Unable to copy file");
				} else {
					logger.info("The file " + getFileName() + " has been created.");
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public Boolean createDirectory() {
		if (!dataFolder.exists() && !dataFolder.mkdir()) {
			return false;
		}
		
		return true;
	}

	
	public String getFileName() { return fileName; }
}
