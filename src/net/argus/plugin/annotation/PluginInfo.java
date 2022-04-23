package net.argus.plugin.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PluginInfo {
	
	public String pluginId();
	public String version();
	public String[] pluginRequested();
	
	public String name(); 
	public String[] authors();
	
	public String description();
	
}
