package net.argus.plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.argus.file.CardinalFile;
import net.argus.instance.Instance;
import net.argus.plugin.annotation.PluginInfo;
import net.argus.system.AnnotationManager;
import net.argus.system.UserSystem;
import net.argus.util.Error;
import net.argus.util.Version;
import net.argus.util.Version.State;
import net.argus.util.debug.Debug;
import net.argus.util.debug.Info;

public class InitializationPlugin {
	
	public static void register() {
		String cp = UserSystem.getProperty("class");
		if(cp != null) {
			String[] mainClasses = cp.split(";");
			
			for(String mainClass : mainClasses)
				register(mainClass);
			
			checkPlugin();
			
			createFile();
		}
	}
	
	private static void checkPlugin() {
		List<PluginInfo> infos = PluginRegister.getInfos();
		
  loop:	 for(PluginInfo info : infos) {
			String[] requesteds = info.pluginRequested();
			for(String requested : requesteds)
				if(requested != null && !requested.equals("") && !PluginRegister.isExist(requested)) {
					PluginRegister.removePlugin(infos.indexOf(info));
					Debug.log("Plugin \"" + info.pluginId() + "\" was deleted because plugin \"" + requested + "\" was not registered", Info.ERROR);
					continue loop;
				}
		}
	}
	
	@SuppressWarnings("deprecation")
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
			
			boolean sameVersion = createManifest(plug);
			
			List<PluginFile> files = plug.getPluginFiles();
			if(files == null || files.size() == 0)
				continue;
			for(PluginFile f : files) {
				try {f.writeFile(sameVersion);}
				catch(IOException e) {Debug.log("Error: Unable to write in file \"" + f.getFile().getAbsolutePath() + "\"", Info.ERROR);}
			}
		}
		Instance.setThreadInstance(main);
	}
	
	private static boolean createManifest(Plugin plug) {
		File man = new File(Instance.currentInstance().getRootPath() + "/plugin.info");
		boolean exist = man.exists();
		
		Version ver = PluginRegister.getVersion(plug);
		Version currentVersion = null;
		
		PluginFile manFile = new PluginFile("plugin", "info", "/", true) {
			
			@Override
			public String[] getLines() {
				return new String[] {ver.toString()};
			}
		};
		
		if(exist) {
			try {currentVersion = new Version(new CardinalFile(man).read());}
			catch(IOException e1) {Error.createErrorFileLog(e1); Debug.log("Error on read plugin manifest file", Info.ERROR);}
			if(ver.getState(currentVersion) == State.SUPERIOR) {
				try {manFile.writeFile(false);}
				catch(IOException e) {Debug.log("Error: Unable to write on \"plugin.info\" file", Info.ERROR);}
				return false;
			}
			return true;
		}else {
			try {manFile.writeFile(false);}
			catch(IOException e) {Debug.log("Error: Unable to write the manifest file", Info.ERROR);}
		}
		return false;	
	}

}
