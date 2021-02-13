package net.argus.plugin;

public abstract class Plugin {
	
	public abstract void preInit(PluginEvent e);
	public abstract void init(PluginEvent e);
	public abstract void postInit(PluginEvent e);
		
}
