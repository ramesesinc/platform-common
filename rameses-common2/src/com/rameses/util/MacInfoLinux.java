/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.StringReader;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 *
 * @author wflores
 */
final class MacInfoLinux {
    
    MacInfoLinux() {
    }
    
    public String getMachAddress() {
        AbstractMacInfo[] impls = new AbstractMacInfo[]{
            new MacInfoImpl_1(), 
            new MacInfoImpl_2(),
            new MacInfoImpl_3()
        }; 
        
        for ( AbstractMacInfo o : impls ) {
            try {
                String mac = o.getMachAddress(); 
                if ( mac != null && mac.trim().length() > 0 ) {
                    return mac; 
                }
            }
            catch(Throwable t) { 
                System.out.println(" ");
                t.printStackTrace(); 
            }
        }
        return null; 
    }
    
    abstract class AbstractMacInfo {
        
        abstract String getMachAddress();
        
        String exec(String command) throws Exception {
            Process p = Runtime.getRuntime().exec(command);
            String error = toString( p.getErrorStream()); 
            if ( error != null && error.trim().length() > 0 ) {
                System.err.println( error ); 
                return null; 
            }
            
            return toString( p.getInputStream()); 
        }
        
        String toString(InputStream inp) throws Exception {
            if ( inp == null ) { 
                return null;
            } 
            
            int read = 0; 
            byte[] bytes = new byte[1024];
            StringBuilder sb = new StringBuilder(); 
            try {
                while ((read = inp.read(bytes)) != -1) { 
                    sb.append(new String(bytes, 0, read)); 
                }
                return sb.toString(); 
            }
            finally {
                try { inp.close(); }catch(Throwable t){;} 
                
                bytes = null; 
                sb = null; 
            }
        }
    }
    
    class MacInfoImpl_1 extends AbstractMacInfo {
        
        String getCommand() {
            return "ip link ls up"; 
        }
        
        public String getMachAddress() {
            String text = null;  
            try { 
                text = exec( getCommand());
            } 
            catch (Throwable t) {
                System.out.println(" ");
                t.printStackTrace(); 
            }
            
            if ( text == null || text.length() == 0 ) {
                return null; 
            }
            
            String mac = null; 
            String readLine = null; 
            BufferedReader reader = null; 
            try {
                reader = new BufferedReader(new StringReader(text)); 
                while ((readLine = reader.readLine()) != null) {
                    String[] arr = readLine.trim().split(" "); 
                    if ( !"link/ether".equals( arr[0])) {
                        continue; 
                    }
                    
                    arr[1] = arr[1].toUpperCase(); 
                    if ( arr[1].matches("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$")) {
                        mac = arr[1]; 
                        break; 
                    }
                }
                
                return mac; 
            }
            catch (Throwable t) {
                System.out.println(" ");
                t.printStackTrace(); 
            }
            finally {
                try { reader.close(); }catch(Throwable t){;} 
            }
            
            return null; 
        }
    }

    class MacInfoImpl_2 extends MacInfoImpl_1 {
        String getCommand() {
            return "ip a"; 
        }
    }

    class MacInfoImpl_3 extends AbstractMacInfo {
        public String getMachAddress() {
            String text = null;  
            try { 
                return exec("ifconfig");
            } 
            catch (Throwable t) {
                System.out.println(" ");
                t.printStackTrace(); 
            }
            
            if ( text == null || text.length() == 0 ) {
                return null; 
            }
            
            String mac = null; 
            String readLine = null; 
            BufferedReader reader = null; 
            try {
                reader = new BufferedReader(new StringReader(text)); 
                while ((readLine = reader.readLine()) != null) {
                    String[] arr = readLine.trim().split(" "); 
                    if ( !"ether".equals( arr[0])) {
                        continue; 
                    }
                    
                    arr[1] = arr[1].toUpperCase(); 
                    if ( arr[1].matches("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$")) {
                        mac = arr[1]; 
                        break; 
                    }
                }
                
                return mac; 
            }
            catch (Throwable t) {
                System.out.println(" ");
                t.printStackTrace(); 
            }
            finally {
                try { reader.close(); }catch(Throwable t){;} 
            }
            
            return null; 
        }
    }
}
