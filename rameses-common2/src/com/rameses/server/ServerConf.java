/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.server;

import com.rameses.util.ConfigProperties;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wflores
 */
public final class ServerConf { 
    
    private final static Object LOCKED = new Object();
    
    private static ConfigProperties conf = null; 
    
    static void load( String resourcePath ) {
        synchronized (LOCKED) {
            conf = new ConfigProperties( resourcePath );
        } 
    }
    
    public static Map<String, Map> getGroups() {
        synchronized (LOCKED) {
            if ( conf == null ) { 
                System.out.println("ServerConf.load must be called first");
                return new HashMap();
            } 
            return conf.getGroups(); 
        }
    }
    
    public static Map<String, Map> getGroup( String name ) {
        synchronized (LOCKED) {
            if ( conf == null ) { 
                System.out.println("ServerConf.load must be called first");
                return new HashMap();
            } 
            
            if ( name == null || name.trim().length() == 0 ) {
                return new HashMap();
            }
            
            Map map = null; 
            if ( name.startsWith("@@")) {
                String skey = name.substring(2).split(":")[0]; 
                map = conf.getGroups().get( skey ); 
            }
            else {
                map = conf.getGroups().get( name ); 
            }
            return (map == null ? new HashMap() : map); 
        }        
    }
}
