package ch.shaped.mp3.check;
import ch.shaped.mp3.library.MP3LibraryAlbum;

public interface LibraryCheck {
	CheckState run(MP3LibraryAlbum album);
	String getName();
	String getDescription();
}
