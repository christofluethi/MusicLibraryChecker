package ch.shaped.mp3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * mp3 checker
 */
public class ID3v2ArtworkChecker {
	private static final Logger logger = LogManager.getLogger(ID3v2ArtworkChecker.class);
	
	private Set<String> failFolders = new HashSet<String>();
	private File source;
	
	public ID3v2ArtworkChecker(File source) {
		this.source = source;
	}
	
	public void run() {
		FileCrawler fc = new FileCrawler(source);
    	List<File> files = fc.scan();
    	
    	for (File file : files) {
    		Mp3File mp3file;
			try {
				mp3file = new Mp3File(file.getAbsoluteFile());
				if(mp3file.hasId3v2Tag()) {
					ID3v2 id3v2Tag = mp3file.getId3v2Tag();
        			if(id3v2Tag != null) {
        				byte[] albumImageData = id3v2Tag.getAlbumImage();
	        			if(albumImageData == null || albumImageData.length == 0) {
	        				this.failFolders.add(file.getAbsoluteFile().getParentFile().getName());
	        				logger.trace(file.getAbsoluteFile() +" has no Album Artwork set");
	        			}
        			}
        		}
			} catch (UnsupportedTagException e) {
				logger.error(file.getName() +" UnsupportedTagException");
			} catch (InvalidDataException e) {
				// logger.error(file.getName() +" is not in mp3 format");
			} catch (IOException e) {
				// logger.error(file.getName() +" IOException");
			}
		}
    	
    	logger.info("Artwork for "+this.failFolders.size() +" folders is missing");
    	List<String> list = ID3v2ArtworkChecker.asSortedList(failFolders);
    	
    	for (String s : list) {
			logger.warn("No Album Artwork for: "+s);
		}
	}
	
    public static void main( String[] args ) {
    	
        logger.info(ID3v2ArtworkChecker.class + " started...");
        
        if(args.length > 0 && args[0].length() > 0) {
        	File f = new File(args[0]);
        	if(f.exists()) {
        		ID3v2ArtworkChecker checker = new ID3v2ArtworkChecker(f);
        		checker.run();
        	} else {
        		logger.error(ID3v2ArtworkChecker.class + " invalid source_path specified. File or Folder '"+ args[0] +"' does not exist");
        	}
        } else {
        	 	logger.info(ID3v2ArtworkChecker.class + " need param source_path");
        }
    }
    
    
    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
      List<T> list = new ArrayList<T>(c);
      java.util.Collections.sort(list);
      return list;
    }
}
