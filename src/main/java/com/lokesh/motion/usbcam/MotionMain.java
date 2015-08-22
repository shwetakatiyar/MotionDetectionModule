/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lokesh.motion.usbcam;

import com.lokesh.encrypt.util.Config;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lokesh
 */

public class MotionMain {

    public static final String DIRECTORY_TO_WATCH = Config.MOTION_CAPTURE_FILE_PATH.getValue();

    public static void main(String[] args) {

        try {
            // get the directory we want to watch, using the Paths singleton class
            Path toWatch = Paths.get(DIRECTORY_TO_WATCH);
            if (toWatch == null) {
                throw new UnsupportedOperationException("Directory not found");
            }

        // make a new watch service that we can register interest in 
            // directories and files with.
            WatchService myWatcher = toWatch.getFileSystem().newWatchService();

            // start the file watcher thread below
            MotionEventHandler fileWatcher = new MotionEventHandler(myWatcher);
            Thread th = new Thread(fileWatcher, "FileWatcher");
            th.start();

            // register a file
            toWatch.register(myWatcher, ENTRY_CREATE, ENTRY_MODIFY);
            th.join();
            
            // start another thread to keep sending mail abt the ip address.
            
            
            
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(MotionMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
