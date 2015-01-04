MusicLibraryChecker
===================
[![Build Status](https://travis-ci.org/christofluethi/MusicLibraryChecker.svg?branch=master)](https://travis-ci.org/christofluethi/MusicLibraryChecker)

Simple command line tool to check your music library

## Music Library Format
The LibraryChecker needs the following specific directory structure:

```
%{artist} - %{album}/%{track} %{artist} - %{title}
```

there is no artist folder (note for iTunes compatibility).

## Configuration
use a yaml configuration file to configure the checks to be run. 

A sample configuration looks like this:

```yaml
version: 1.0
logLevel: info
checks:
    - ArtworkCheck
    - ID3v2TagCheck
    - TrackNumberCheck
```

You have to list the LibraryChecks which should be run in the checks section.

## How to use it

 ```
java -jar MusicLibraryChecker.jar config.yml ~/MusicLibrary/

03:11:18.431 [main] INFO  ch.shaped.mp3.CheckLibrary - class ch.shaped.mp3.CheckLibrary started...
03:11:18.442 [main] INFO  ch.shaped.mp3.CheckLibrary - Your music library contains 875 albums
03:11:18.442 [main] INFO  ch.shaped.mp3.CheckLibrary - Processed 0/875
03:11:18.442 [main] ERROR ch.shaped.mp3.CheckLibrary - Item .DS_Store is a file. Directory expected.
03:11:18.442 [main] ERROR ch.shaped.mp3.CheckLibrary - Item .iTunes Preferences.plist is a file. Directory expected.
03:11:18.687 [main] ERROR ch.shaped.mp3.CheckLibrary - ArtworkCheck failed for album 2 Unlimited - No Limits
03:11:20.078 [main] INFO  ch.shaped.mp3.CheckLibrary - Processed 10/875
...
```
  
## Available Checks
* ArtworkCheck - Check if every file has the id3 cover tag set
* TrackNumberCheck - Check if every file has the tracknumbers set
* ID3v2Check - Check if every file has the id3v2 tags set
