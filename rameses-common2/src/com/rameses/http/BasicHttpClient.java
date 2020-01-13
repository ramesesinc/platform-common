/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.http;

import com.rameses.io.IOStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author wflores
 */
public class BasicHttpClient {
    
    private int readTimeout;
    private int connectTimeout;
    private String contentType;
    
    public BasicHttpClient() {
    }

    public int getReadTimeout() { return readTimeout; } 
    public void setReadTimeout( int timeout ) {
        this.readTimeout = timeout; 
    }
    
    public int getConnectTimeout() { return connectTimeout; }
    public void setConnectTimeout( int timeout ) {
        this.connectTimeout = timeout;
    }

    public String getContentType() { return contentType; } 
    public void setContentType( String contentType ) {
        this.contentType = contentType; 
    }

    private String encodeValue( String value ) {
        try { 
            if ( value == null ) {
                return "";
            } 
            else if ( value.length() == 0 ) {
                return value; 
            }
            else {
                return URLEncoder.encode( value, StandardCharsets.UTF_8.toString());
            }
        } 
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException( ex );
        }
    }
    
    public Object get( String urlPath, Object data ) {
        return get( urlPath, data, null ); 
    }
    public Object get( String urlPath, Object data, Map headers ) {
        StringBuilder sb = new StringBuilder();
        if ( data instanceof Map ) {
            Iterator itr = ((Map) data).entrySet().iterator(); 
            while (itr.hasNext()) {
                Map.Entry me = (Map.Entry) itr.next(); 
                String name = encodeValue( me.getKey().toString());
                String value = encodeValue(me.getValue() == null ? null : me.getValue().toString()); 
                
                if ( sb.length() > 0 ) {
                    sb.append("&");
                }
                sb.append( name ).append("=").append( value ); 
            }
        }
        else {
            sb.append( data == null ? "" : data.toString()); 
        }
        
        StringBuilder pathbuff = new StringBuilder( urlPath );
        if ( sb.length() > 0 ) { 
            if ( urlPath.indexOf('?') < 0 ) { 
                pathbuff.append("?"); 
            }
            pathbuff.append( sb.toString()); 
        } 
        return invoke( "GET", pathbuff.toString(), null, headers ); 
    }
    
    public Object post( String urlPath, Object data ) {
        return post( urlPath, data, null ); 
    }
    public Object post( String urlPath, Object data, Map headers ) {
        return invoke( "POST", urlPath, data, headers ); 
    }
    
    private Object invoke( String reqMethod, String urlPath, Object data, Map headers ) {
        HttpURLConnection conn = null; 
        try {
            URL url = new URL( urlPath );
            conn = (HttpURLConnection) url.openConnection(); 
            if (conn instanceof HttpsURLConnection) {
                HttpsURLConnection httpsc = (HttpsURLConnection)conn; 
                bypassSSLSecurityCheck(httpsc); 
            }

            if ( this.readTimeout > 0 ) {
                conn.setReadTimeout( readTimeout );
            }            
            if ( connectTimeout > 0 ) {
                conn.setConnectTimeout( connectTimeout );
            }
            
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod( reqMethod );
            
            String conntype = getContentType();
            if ( conntype != null && conntype.length() > 0 ) {
                conn.setRequestProperty("Content-Type", conntype); 
            }
                        
            if ( headers != null ) {
                Iterator itr = headers.entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry me = (Map.Entry) itr.next();
                    String name = (me.getKey() == null ? null : me.getKey().toString()); 
                    if ( name != null && name.length() > 0 ) {
                        String value = (me.getValue() == null ? "" : me.getValue().toString()); 
                        conn.setRequestProperty(name, value);
                    }
                }
            }
            
            OutputStream out = null; 
            try {
                byte[] bytes = null; 
                if ( data instanceof byte[] ) {
                    bytes = (byte[]) data; 
                    conn.setRequestProperty("Content-Length", ""+ bytes.length);
                }
                else {
                    bytes = ( data == null ? "" : data.toString()).getBytes("utf-8"); 
                    conn.setRequestProperty("Content-Length", ""+ bytes.length);
                }
                
                out = conn.getOutputStream();
                out.write( bytes, 0, bytes.length); 
            } finally {
                try { out.close(); }catch(Throwable t){;} 
            }
          
            BufferedReader reader = null; 
            InputStreamReader inp = null;

            try {
                inp = new InputStreamReader(conn.getInputStream(), "utf-8"); 
                reader = new BufferedReader( inp );
                
                String readLine = null;
                StringBuilder buff = new StringBuilder();
                while ((readLine = reader.readLine()) != null) {
                    buff.append( readLine ).append("\n");
                }
                return buff.toString(); 
            } 
            finally {
                try { inp.close(); }catch(Throwable t){;} 
                try { reader.close(); }catch(Throwable t){;} 
            }
        } 
        catch (RuntimeException re) {
            throw re;
        } 
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    
    
    private void bypassSSLSecurityCheck(HttpsURLConnection conn) {
        SSLSocketFactory sslsf = getSSLSocketFactory();
        if (sslsf == null) return;
        
        // Ignore differences between given hostname and certificate hostname
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) { 
                return true; 
            }
        };        
        conn.setSSLSocketFactory(sslsf); 
        conn.setHostnameVerifier(hv); 
    }
    
    private SSLSocketFactory sslSocketFactory;
    private SSLSocketFactory getSSLSocketFactory() {
        if (sslSocketFactory == null) {
            sslSocketFactory = createSSLSocketFactory();
        }
        return sslSocketFactory;             
    }
    
    private SSLSocketFactory createSSLSocketFactory() {
        try { 
            TrustManager[] trustAllCerts = new TrustManager[] { 
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { 
                        return new X509Certificate[0]; 
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
            };

            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance( "SSL" );
            sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
            // Create an ssl socket factory with our all-trusting manager
            return sslContext.getSocketFactory();  
        } catch(Throwable t) {
            return null; 
        }
    }
}
