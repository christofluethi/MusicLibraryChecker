package ch.shaped.mp3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileCrawler {

	private List<File> files = new ArrayList<File>();
	private File source;
	
	public FileCrawler(File s) {
		this.source = s;
	}
	
	/**
	 * Scan directory and get files
	 * @param file Start File/Folder for the recursive scan
	 */
	private void walkDir(File file) {
		File[] children = file.listFiles();
		if(children != null) {
			for (File child : children) {
				if(child.isFile()) {
					if(child.canRead() && child.length() > 0) {
						files.add(child);
					} 
				} else {	
					this.walkDir(child);
				}
			}
		}
	}
	
	public List<File> scan() {
		this.files.clear();
		this.walkDir(source);
		return this.files;
	}
}
