package net.argus.plugin;

import net.argus.plugin.annotation.PluginInfo;
import net.argus.system.AnnotationManager;
import net.argus.system.Launcher;
import net.argus.system.UserSystem;
import net.argus.util.debug.Debug;

public class InitializationPlugin {
	
	public static void register() {
		String cp = UserSystem.getProperty("class");
		if(cp != null && cp.contains(Launcher.getSeperator())) {
			String[] mainClasses = cp.split(";");
			
			for(String mainClass : mainClasses) {
				try {
					Class<?> cl = InitializationPlugin.class.getClassLoader().loadClass(mainClass);
					PluginInfo info = AnnotationManager.getAnnotaion(cl, PluginInfo.class);
					
					PluginRegister.addPlugin((Plugin) cl.newInstance(), info);	
					
					Debug.log("Plugin \"" + info.pluginId() + "\" version \"" + info.version() + "\" is loaded");
				}catch(InstantiationException | IllegalAccessException | ClassNotFoundException | ClassCastException | PluginException e) {
					if(e instanceof ClassNotFoundException) Debug.log(mainClass + " not found");
					if(e instanceof ClassCastException) Debug.log(mainClass + " not extends \"" + Plugin.class + "\"");
				}
			}
		}
	}

}
