package net.argus.plugin;

import java.util.ArrayList;
import java.util.List;

import net.argus.instance.Instance;
import net.argus.plugin.annotation.PluginInfo;
import net.argus.util.Version;
import net.argus.util.debug.Debug;

public class PluginRegister {
	
	private static List<PluginRegister> plugins = new ArrayList<PluginRegister>();
	
	private Plugin plugin;
	private PluginInfo info;
	
	public PluginRegister(Plugin plugin, PluginInfo info) {
		this.plugin = plugin;
		this.info = info;
		
		plugin.setInstance(new Instance("plugin/" + info.pluginId()).setRootFolder(""));
	}
	
	public Plugin getPlugin() {return plugin;}
	public PluginInfo getInfo() {return info;}
	public Instance getInstance() {return plugin.getInstance();}
	
	public Version getVersion() {return new Version(info.version());}
	
	
	/**--STATIC--**/
	public static void preInit(PluginEvent e) {
		for(PluginRegister plug : plugins) {
			Instance instance = Instance.currentInstance();
			
			Instance.setThreadInstance(plug.getInstance());
			plug.getPlugin().preInit(e);
			Instance.setThreadInstance(instance);
		}
	}
	
	public static void init(PluginEvent e) {
		for(PluginRegister plug : plugins) {
			Instance instance = Instance.currentInstance();
						
			Instance.setThreadInstance(plug.getInstance());
			plug.getPlugin().init(e);
			Instance.setThreadInstance(instance);
		}
	}
	
	public static void postInit(PluginEvent e) {
		for(PluginRegister plug : plugins) {
			Instance instance = Instance.currentInstance();
						
			Instance.setThreadInstance(plug.getInstance());
			plug.getPlugin().postInit(e);
			Instance.setThreadInstance(instance);
		}
	}
	
	public static void addPlugin(Plugin plugin, PluginInfo info) throws PluginException {
		if(isPluginValid(info)) {
			plugins.add(new PluginRegister(plugin, info));
		}else
			throw new PluginException();
	}
	
	private static boolean isPluginValid(PluginInfo info) {
		boolean valid = true;
		if(info != null) {
			for(PluginRegister plug : plugins)
				if(info.pluginId().equals(plug.getInfo().pluginId())) {
					Debug.log("The plugin \"" + info.pluginId() + "\" already exists");
					valid = false;
					break;
				}
		}else {
			Debug.log(PluginInfo.class + " not found");
			valid = false;
		}
		
		return valid;
	}
	
	
	/**--GETTERS--**/
	public static int length() {return plugins.size();}
	
	public static Plugin getPlugin(int index) {return plugins.get(index).getPlugin();}
	public static PluginInfo getInfo(int index) {return plugins.get(index).getInfo();}
	
	public static Version getVersion(int index) {return plugins.get(index).getVersion();}
	
	public static List<Plugin> getPlugins() {
		List<Plugin> plugs = new ArrayList<Plugin>();
		
		for(PluginRegister rp : plugins)
			plugs.add(rp.getPlugin());
		
		return plugs;
	}
	
	public static List<PluginInfo> getInfos() {
		List<PluginInfo> info = new ArrayList<PluginInfo>();
		
		for(PluginRegister rp : plugins)
			info.add(rp.getInfo());
		
		return info;
	}

}
