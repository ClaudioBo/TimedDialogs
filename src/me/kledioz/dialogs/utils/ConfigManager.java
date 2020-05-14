package me.kledioz.dialogs.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

	private JavaPlugin main;
	private HashMap<String, Config> configs;

	public ConfigManager(JavaPlugin main) {
		this.configs = new HashMap<String, Config>();
		this.main = main;
	}

	public FileConfiguration loadConfig(String nombre, String path) {
		if (configs.containsKey(nombre)) {
			configs.remove(nombre);
		}
		if (createConfig(nombre, path)) {
			Config cf = new Config(main, nombre, path);
			configs.put(nombre, cf);
			return cf.config;
		} else {
			return null;
		}
	}

	public FileConfiguration loadConfig(File f) {
		if (configs.containsKey(f.getName())) {
			configs.remove(f.getName());
		}
		if (f.exists()) {
			Config cf = new Config(main, f);
			configs.put(f.getName(), cf);
			return cf.config;
		} else {
			return null;
		}
	}

	public void saveConfig(String nombre) {
		if (configs.containsKey(nombre)) {
			try {
				configs.get(nombre).config.save(configs.get(nombre).fileConfig);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public FileConfiguration getConfig(String nombre) {
		if (configs.containsKey(nombre)) {
			return configs.get(nombre).config;
		}
		return null;
	}

	public HashMap<String, Config> getConfigs() {
		return configs;
	}

	private boolean createConfig(String nombre, String path) {
		File fileConfig = new File(main.getDataFolder(), path + "/" + nombre);
		if (!fileConfig.exists()) {
			try {
				FileUtils.copyInputStreamToFile(main.getResource("example.yml"), fileConfig);
				return true;
			} catch (IOException e) {
				try {
					fileConfig.createNewFile();
					return true;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				return false;
			}
		} else {
			return true;
		}
	}

	public class Config {
		public String nombre;
		public File fileConfig;
		public FileConfiguration config;

		public Config(JavaPlugin main, String nombre, String path) {
			this.nombre = nombre;
			fileConfig = new File(main.getDataFolder(), path + "/" + nombre);
			config = YamlConfiguration.loadConfiguration(fileConfig);
		}

		public Config(JavaPlugin main, File f) {
			this.nombre = f.getName();
			fileConfig = f;
			config = YamlConfiguration.loadConfiguration(fileConfig);
		}

	}

}