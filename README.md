MusicLibraryChecker
===================

Command line tool to check a given music library folder for consistency

  java -jar MusicLibraryChecker.jar ~/MusicLibrary/

  03:11:18.431 [main] INFO  ch.shaped.mp3.CheckLibrary - class ch.shaped.mp3.CheckLibrary started...
  03:11:18.442 [main] INFO  ch.shaped.mp3.CheckLibrary - Your music library contains 875 albums
  03:11:18.442 [main] INFO  ch.shaped.mp3.CheckLibrary - Processed 0/875
  03:11:18.442 [main] ERROR ch.shaped.mp3.CheckLibrary - Item .DS_Store is a file. Directory expected.
  03:11:18.442 [main] ERROR ch.shaped.mp3.CheckLibrary - Item .iTunes Preferences.plist is a file. Directory expected.
  03:11:18.687 [main] ERROR ch.shaped.mp3.CheckLibrary - ArtworkCheck failed for album 2 Unlimited - No Limits
  03:11:20.078 [main] INFO  ch.shaped.mp3.CheckLibrary - Processed 10/875
  ...
