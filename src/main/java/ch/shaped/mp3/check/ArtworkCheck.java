package ch.shaped.mp3.check;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.shaped.mp3.library.MP3LibraryAlbum;
import ch.shaped.mp3.library.MP3LibraryItem;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class ArtworkCheck extends AbstractLibraryCheck {
	public static final String NAME = "ArtworkCheck";
	public static final String DESCRIPTION = "Each item in the album must have the ID3v2 artwork tag set";
	
	private static final Logger logger = LogManager.getLogger(ArtworkCheck.class);
	
	@Override
	public CheckState run(MP3LibraryAlbum album) {
		if(album != null) {
			for (MP3LibraryItem item : album.getChilds()) {

				File f = item.getItem();
				if(FilenameUtils.isExtension(f.getName(), "mp3")) {
					try {
						Mp3File mp3file = new Mp3File(item.getItem());

						if(mp3file.hasId3v2Tag()) {
							ID3v2 id3v2Tag = mp3file.getId3v2Tag();
							if(id3v2Tag != null) {
								byte[] albumImageData = id3v2Tag.getAlbumImage();
								if(albumImageData == null || albumImageData.length == 0) {
									logger.trace(item.getName() +" has no Album Artwork set");
									return CheckState.FAIL;
								}
							}
						}
					} catch (UnsupportedTagException e) {
						logger.debug(item.getName() +" UnsupportedTagException");
						return CheckState.FAIL;
					} catch (InvalidDataException e) {
						logger.debug(item.getName() +" InvalidDataException");
						return CheckState.FAIL;
					} catch (IOException e) {
						logger.debug(item.getName() +" IOException");
						return CheckState.FAIL;
					}
				}
			}
			
			return CheckState.OK;
		} else {
			return CheckState.UNKNOWN;
		}
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
}
