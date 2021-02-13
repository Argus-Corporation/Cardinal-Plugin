package net.argus.plugin;

public class PluginEvent {
	
	private Object parent;
	
	public PluginEvent(Object parent) {
		this.parent = parent;
	}
	
	public Object getParent() {return parent;}

}
