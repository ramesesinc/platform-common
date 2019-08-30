/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.common;

import com.rameses.util.Service;
import java.util.Iterator;

/**
 *
 * @author elmonazareno
 */
public abstract class ClientNotification {
    //implementation
    private static ClientNotification instance;
    
    public static ClientNotification getInstance() {
        if(instance==null) {
            Iterator e = Service.providers(ClientNotification.class,ClientNotification.class.getClassLoader());
            if(e.hasNext()) {
                instance = (ClientNotification)e.next();
            }
        }
        return instance;
    }
    
    public abstract void register( String eventName, Object handler );
    public abstract void unregister( Object handler );
    
}
