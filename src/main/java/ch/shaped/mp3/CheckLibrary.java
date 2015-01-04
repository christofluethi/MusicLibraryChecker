package ch.shaped.mp3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.yaml.snakeyaml.Yaml;

import ch.shaped.mp3.check.CheckState;
import ch.shaped.mp3.check.LibraryCheck;
import ch.shaped.mp3.config.CheckConfiguration;
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
        
        if(args.length > 1 && args[0].length() > 0 && args[1].length() > 0) {
        	File f = new File(args[1]);
        	if(f.exists()) {
        		CheckLibrary checker = new CheckLibrary(f);
        		Yaml yaml = new Yaml();  

            	try {
            		InputStream in = Files.newInputStream(Paths.get(args[0]));

            		CheckConfiguration configuration = yaml.loadAs(in, CheckConfiguration.class);
            		logger.info(configuration);
            		String level = configuration.getLogLevel().toUpperCase();
            		if(level != null && !level.isEmpty()) {
            			LoggerContext ctx = (LoggerContext)LogManager.getContext(false);
                		Configuration config = ctx.getConfiguration();
                		LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME); 
                		loggerConfig.setLevel(Level.getLevel(level));
                		ctx.updateLoggers(); 
                		
                		logger.info("Set LogLevel to "+level);
            		}
            		
            		List<LibraryCheck> checks = configuration.getLibraryCheck();
            		
            		for (LibraryCheck libraryCheck : checks) {
            			logger.info("Adding check "+libraryCheck.getName());
						checker.addCheck(libraryCheck);
					}
            	} catch(IOException e) {
            		logger.error(CheckLibrary.class + " invalid 'config' specified. File '"+ args[1] +"' does not exist");
            	}
        		checker.run();
        	} else {
        		logger.error(CheckLibrary.class + " invalid 'source_path' specified. File or Folder '"+ args[1] +"' does not exist");
        	}
        } else {
        	 	logger.info(CheckLibrary.class + " need param 'config' and 'source_path'");
        }
    }
    
    public void addCheck(LibraryCheck lc) {
    	this.checks.add(lc);
    }
}
