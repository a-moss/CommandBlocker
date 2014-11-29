package com.mydeblob.commandblocker;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandBlocker extends JavaPlugin implements Listener{

	public void onEnable() {
		File config = new File(getDataFolder(), "config.yml");
		if(!config.exists()){
			saveDefaultConfig();
			getLogger().info("[CommandBlocker] Generating a new config.yml");
		}
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("cbreload").setExecutor(this);
	}
	
	public void onDisable() {
		
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		String msg = e.getMessage();
		for(String s:getConfig().getStringList("blocked")){
			if(s.contains("*")){ //Wildcard support
				String[] split = s.split("\\*");
				if(msg.startsWith(split[0].replaceAll("\\s+$", ""))){
					e.setCancelled(true);
				}
			}else{
				if(msg.contains(s)){
					e.setCancelled(true);
				}
			}
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("cbreload")) {
			if(sender.isOp() || sender.hasPermission("cb.admin")){
				reloadConfig();
				sender.sendMessage(ChatColor.GREEN + "[CommandBlocker] Successfully reloaded the config.yml");
				return true;
			}
		}
		return false;
	}
}
