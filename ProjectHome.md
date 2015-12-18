Point the app at a directory of pictures, movies, or audio files. It will create a web page (or pages) of thumbnails (or icons for media types), you can specify the number of rows and columns per page.

You can add captions,  EXIF data can be shown as an optional dropdown.  Don't like the page?  Delete the files in the dir, add new ones, regenerate - your old captions will be saved (they're saved as an XML file - delete that and they're lost :)  )

This is designed to be used for large numbers of files in non-public websites - where Flickr might not be appropriate, you have your own web server, and it's a pain to set up access lists.  Plus, it's a fun programming exercise.

Build is done via Gradle - no installation required beyond checking out this project and having Java in your path.

**Be forewarned - I'm converting a very old project, and it doesn't fully work yet - this is version 0.1**

It'll get better.  Trust me.