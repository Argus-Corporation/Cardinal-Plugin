package net.argus.plugin;

import net.argus.instance.Instance;

public abstract class Plugin {
	
	private Instance instance;
	
	public abstract void preInit(PluginEvent e);
	public abstract void init(PluginEvent e);
	public abstract void postInit(PluginEvent e);
	
	public void setInstance(Instance instance) {this.instance = instance;}
	public Instance getInstance() {return instance;}
		
}
