package net.argus.plugin;

import java.io.File;
import java.io.IOException;

import net.argus.file.CardinalFile;
import net.argus.instance.Instance;

public abstract class PluginFile {
	
	private File file;
	private boolean overwritable;
	
	public PluginFile(String fileName, String suff, String folder, boolean overwritable) {
		this(new File(Instance.currentInstance().getRootPath() + "/" + folder + "/" + fileName + "." + suff), overwritable);
	}
	
	public PluginFile(File file, boolean overwritable) {
		this.file = file;
		this.overwritable = overwritable;
	}
	
	public abstract String[] getLines();
	
	public void writeFile(boolean sameVersion) throws IOException {
		CardinalFile f = new CardinalFile(file);
		
		if(f.exists() && (!overwritable || sameVersion))
			return;
		
		f.createFile();
		
		String[] lines = getLines();
		if(lines != null && lines.length > 0)
			f.write(lines);
	}
	
	public File getFile() {return file;}
	
	public boolean isOverwritable() {return overwritable;}

}
