package net.argus.plugin;

import java.io.File;
import java.io.IOException;

import net.argus.file.CardinalFile;
import net.argus.instance.Instance;

public abstract class PluginFile {
	
	private File file;
	
	public PluginFile(String fileName, String suff, String folder) {
		this(new File(Instance.currentInstance().getRootPath() + "/" + folder + "/" + fileName + "." + suff));
	}
	
	public PluginFile(File file) {
		this.file = file;
	}
	
	public abstract String[] getLines();
	
	public void writeFile() throws IOException {
		CardinalFile f = new CardinalFile(file);
		
		if(f.exists())
			return;
		
		f.createFile();
		
		String[] lines = getLines();
		if(lines != null && lines.length > 0)
			f.write(lines);
	}
	
	public File getFile() {return file;}

}
