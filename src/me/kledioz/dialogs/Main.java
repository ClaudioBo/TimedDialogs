package me.kledioz.dialogs;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.kledioz.dialogs.utils.ConfigManager;

public class Main extends JavaPlugin {

	Main pl;
	HashMap<String, HashMap<String, List<String>>> dialogos;
	HashMap<Player, BukkitTask> tasks;
	ConfigManager configs;
	boolean placeholderApiHook = false;

	public void onEnable() {
		pl = this;
		dialogos = new HashMap<String, HashMap<String, List<String>>>();
		tasks = new HashMap<Player, BukkitTask>();
		configs = new ConfigManager(this);

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			placeholderApiHook = true;
			getLogger().info("PlaceholderAPI hookeado");
		}

		saveDefaultConfig();
		reloadConfig();
		getCommand("tdialogs").setExecutor(new DialogCommand(this));
		loadDialogs();
	}

	public void loadDialogs() {
		dialogos.clear();
		configs.getConfigs().clear();
		if (!new File(getDataFolder(), "dialogs/").exists()) {
			new File(getDataFolder(), "dialogs/").mkdir();
			configs.loadConfig("example.yml", "dialogs/");
			configs.getConfigs().clear();
		}
		for (File f : new File(getDataFolder(), "dialogs/").listFiles()) {
			if (f.getName().toLowerCase().endsWith(".yml")) {
				FileConfiguration i = configs.loadConfig(f);
				if (i != null) {
					HashMap<String, List<String>> list = new HashMap<String, List<String>>();
					for (String name : i.getKeys(false)) {
						list.put(name.toLowerCase(), i.getStringList(name));
					}
					dialogos.put(f.getName().toLowerCase(), list);
				}
			}
		}

	}

	public void onDisable() {
		for (BukkitTask task : tasks.values()) {
			task.cancel();
		}
		tasks = null;
	}

}
