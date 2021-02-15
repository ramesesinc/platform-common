/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.io;

import java.io.File;
import java.io.OutputStream;

/**
 *
 * @author wflores
 */
public abstract class FileTransferSession implements Runnable {

    private File file;
    private long offset;
    private String targetName;
    private String locationConfigId;
    private boolean cancelled;
    
    private FileTransferSession.Handler handler; 
    
    private OutputStream out;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public String getLocationConfigId() {
        return locationConfigId;
    }

    public void setLocationConfigId(String locationConfigId) {
        this.locationConfigId = locationConfigId;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }
    

    public FileTransferSession.Handler getHandler() { return handler; } 
    public void setHandler( FileTransferSession.Handler handler ) {
        this.handler = handler; 
    }    
    
    public void setUserObject( Object userObject ) { 
        // for override for extended properties or configuration 
    }
    
    public OutputStream getOutputStream() {
        return out; 
    }
    public void setOutputStream( OutputStream out ) {
        this.out = out; 
    }
    
    public static interface Handler { 
        void ontransfer( long bytesprocessed ); 
        void ontransfer( long filesize, long bytesprocessed ); 
        void oncomplete(); 
    } 
}
