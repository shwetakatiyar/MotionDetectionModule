/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lokesh.motion.usbcam;

import com.lokesh.mailing.service.MailHelper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lokesh Katiyar
 */
public class MotionEventHandler implements Runnable {

    /**
     * the watchService that is passed in from above
     */
    private WatchService watcherService;

    MotionEventHandler(WatchService myWatcher) {
        this.watcherService = myWatcher;
    }

    @Override
    public void run() {
        try {
            // get the first event before looping
            WatchKey key = watcherService.take();
            while (key != null) {
                // we have a polled event, now we traverse it and 
                // receive all the states from it
                for (WatchEvent event : key.pollEvents()) {
                    if (StandardWatchEventKinds.ENTRY_CREATE == event.kind()) {
                        // Send Mail and remove file
                        // A new Path was created 
                        Path filePath = (((WatchEvent<Path>) event).context());
                        
                        System.out.println(Files.probeContentType(filePath));
                        System.out.println(filePath.getFileName());
                        String path = MotionMain.DIRECTORY_TO_WATCH + java.io.File.separator + filePath.getFileName();
                        java.io.File fileToDelete = new java.io.File(path);
                        Boolean flag = false;
                        if ("application/x-shockwave-flash".equals(Files.probeContentType(filePath)) || filePath.getFileName().toString().contains("swf")) {
                            flag = MailHelper.sendMail(filePath.toString());
                        } else {
                            flag = true;
                        }
                        System.out.println("New path created: " + filePath);
                        if (flag) {
                            fileToDelete.delete();
                        } else {
                            // move to another folder for sending mail

                        }
                    } else {

                        System.out.printf("Received %s event for file: %s\n",
                                event.kind(), event.context());
                    }
                }
                key.reset();
                key = watcherService.take();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(MotionEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Stopping thread");
    }
}
