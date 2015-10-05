package kr.co.d2net.task.system;

import java.io.File;
import java.io.IOException;

import com.teamdev.filewatch.FileEvent;
import com.teamdev.filewatch.FileEventsListener;
import com.teamdev.filewatch.FileWatcher;

public class ManualWatcher {

	//final static Logger logger = Logger.getLogger(ManualWatcher.class.getName());

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
        //File tempFile = File.createTempFile("tmp", "tmp");
        //tempFile.deleteOnExit();
        //File watchingFolder = tempFile.getParentFile();

        FileWatcher watcher = FileWatcher.create(new File("X:/"));

        watcher.addFileEventsListener(new FileEventsListener() {
            public void fileAdded(FileEvent.Added e) {
                System.out.println(e.getEventId()+": "+e.toString());
            }

            public void fileDeleted(FileEvent.Deleted e) {
            	System.out.println(e.getEventId()+": "+e.toString());
            }

            public void fileChanged(FileEvent.Changed e) {
            }

            public void fileRenamed(FileEvent.Renamed e) {
            	System.out.println(e.getEventId()+": "+e.getOldFile().getAbsolutePath()+"->"+e.getFile().getAbsolutePath());
            	//System.out.println(e.toString());
            }
        });

        watcher.start();
/*
        System.out.println("File watcher started. Press 'Enter' to terminate applicaiton.");
        System.in.read();

        watcher.stop();
        System.out.println("File watcher stopped.");
        */
	}


}
