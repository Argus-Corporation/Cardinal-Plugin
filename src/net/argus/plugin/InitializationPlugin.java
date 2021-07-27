package net.argus.plugin;

import java.io.IOException;
import java.util.List;

import net.argus.instance.Instance;
import net.argus.plugin.annotation.PluginInfo;
import net.argus.system.AnnotationManager;
import net.argus.system.UserSystem;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

public class InitializationPlugin {
	
	public static void register() {
		String cp = UserSystem.getProperty("class");
		if(cp != null) {
			String[] mainClasses = cp.split(";");
			
			for(String mainClass : mainClasses)
				register(mainClass);
			
			createFile();
		}
	}
	
	public static void register(String classPath) {
		try {
			Class<?> cl = InitializationPlugin.class.getClassLoader().loadClass(classPath);
			PluginInfo info = AnnotationManager.getAnnotaion(cl, PluginInfo.class);
			
			PluginRegister.addPlugin((Plugin) cl.newInstance(), info);	
			
			Debug.log("Plugin \"" + info.pluginId() + "\" version \"" + info.version() + "\" is loaded");
		}catch(InstantiationException | IllegalAccessException | ClassNotFoundException | ClassCastException | PluginException e) {
			if(e instanceof ClassNotFoundException) Debug.log(classPath + " not found", Info.ERROR);
			if(e instanceof ClassCastException) Debug.log(classPath + " not extends \"" + Plugin.class + "\"", Info.ERROR);
		}
	}
	
	public static void createFile() {
		Instance main = Instance.currentInstance();
		for(Plugin plug : PluginRegister.getPlugins()) {
			Instance.setThreadInstance(plug.getInstance());
			
			List<PluginFile> files = plug.getPluginFiles();
			if(files == null || files.size() == 0)
				continue;
			for(PluginFile f : files) {
				try {f.writeFile();}
				catch(IOException e) {Debug.log("Error: Unable to write in file \"" + f.getFile().getAbsolutePath() + "\"", Info.ERROR);}
			}
		}
		Instance.setThreadInstance(main);
	}

}
