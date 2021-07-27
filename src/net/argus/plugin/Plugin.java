package net.argus.plugin;

import java.util.List;

import net.argus.instance.Instance;

public abstract class Plugin {
	
	private Instance instance;
	
	public abstract void preInit(PluginEvent e);
	public abstract void init(PluginEvent e);
	public abstract void postInit(PluginEvent e);
	
	public List<PluginFile> getPluginFiles() {
		return null;
	}
	
	public void setInstance(Instance instance) {this.instance = instance;}
	public Instance getInstance() {return instance;}
		
}
