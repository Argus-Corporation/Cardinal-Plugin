package net.argus.plugin;

import net.argus.instance.Instance;

public class PluginEvent {
	
	private Object parent;
	
	private Instance parentInstance;
	
	public PluginEvent(Object parent) {
		this.parent = parent;
		this.parentInstance = Instance.currentInstance();
	}
		
	public Object getParent() {return parent;}
	public Instance getParentInstance() {return parentInstance;}

}
