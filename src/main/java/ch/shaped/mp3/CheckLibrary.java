package ch.shaped.mp3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.shaped.mp3.check.ArtworkCheck;
import ch.shaped.mp3.check.CheckState;
import ch.shaped.mp3.check.LibraryCheck;
import ch.shaped.mp3.library.MP3LibraryAlbum;
import ch.shaped.mp3.library.MP3LibraryItem;

/**
 * mp3 checker
 */
public class CheckLibrary {
	private static final Logger logger = LogManager.getLogger(CheckLibrary.class);
	
	private List<LibraryCheck> checks = new ArrayList<LibraryCheck>();
	private File source;
	
	public CheckLibrary(File source) {
		this.source = source;
	}
	
	public void run() {
		File[] albums = this.source.listFiles();
		int total = albums.length;
		int processed = 0;
		if(albums != null) {
			logger.info("Your music library contains "+albums.length+" albums");
			
			for (File album : albums) {
				
				if(processed % 10 == 0) {
					logger.info("Processed "+processed+"/"+total);
				}
				
				if(album.isFile()) {
					logger.error("Item "+album.getName()+" is a file. Directory expected.");
				} else {	
					File[] tracks = album.listFiles();
					MP3LibraryAlbum libraryAlbum = new MP3LibraryAlbum(album.getName(), album);
					for (File track : tracks) {
						if(track.isDirectory()) {
							logger.error("Item "+album+"/"+track.getName()+" is a directory. File expected.");
						} else {
							MP3LibraryItem albumItem = new MP3LibraryItem(track.getName(), track, libraryAlbum);
							libraryAlbum.addChild(albumItem);
						}
					}
					
					for (LibraryCheck check : this.checks) {
						CheckState s = check.run(libraryAlbum);
						
						switch (s) {
							case FAIL:
								logger.error(check.getName()+ " failed for album "+libraryAlbum.getName()+"");
								break;
							case UNKNOWN:
								logger.warn(check.getName()+ " could not be run for album "+libraryAlbum.getName()+"");
								break;
							case OK:
								logger.trace(check.getName()+ " passed for album "+libraryAlbum.getName()+"");
								break;
							default:
								break;
						}
					}
				}
				processed++;
			}
		}
	}
	
    public static void main( String[] args ) {
    	
        logger.info(CheckLibrary.class + " started...");
        
        if(args.length > 0 && args[0].length() > 0) {
        	File f = new File(args[0]);
        	if(f.exists()) {
        		CheckLibrary checker = new CheckLibrary(f);
        		checker.addCheck(new ArtworkCheck());
        		checker.run();
        	} else {
        		logger.error(CheckLibrary.class + " invalid source_path specified. File or Folder '"+ args[0] +"' does not exist");
        	}
        } else {
        	 	logger.info(CheckLibrary.class + " need param source_path");
        }
    }
    
    public void addCheck(LibraryCheck lc) {
    	this.checks.add(lc);
    }
    
   /* public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
      List<T> list = new ArrayList<T>(c);
      java.util.Collections.sort(list);
      return list;
    }*/
}
