import com.rameses.http.BasicHttpClient;
import com.rameses.io.IOStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import junit.framework.*;
/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on October 26, 2010, 11:59 AM
 */

/**
 *
 * @author ms
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }
    
    public void test1() throws Exception {
        String spath = "https://pg-sandbox.paymaya.com/checkout/v1/checkouts";
        
        InputStream inps = getClass().getResourceAsStream("jsondata"); 
        Object data = IOStream.toByteArray(inps); 
        
        Map headers = new HashMap();
        headers.put("Authorization", "Basic cGstZW80c0wzOTNDV1U1S212ZUpVYVc4VjczMFRUZWkyelk4ekU0ZEhKRHhrRjo=");
        headers.put("Content-Type", "application/json; charset=UTF-8");            
        
        BasicHttpClient c = new BasicHttpClient();
        Object res = c.post(spath, data, headers ); 
        System.out.println( res );
    }

    
    
    public void xtestPost() throws Exception {
        String spath = "https://pg-sandbox.paymaya.com/checkout/v1/checkouts";
        
        InputStream inps = getClass().getResourceAsStream("jsondata"); 
        Object data = IOStream.toByteArray(inps); 
        System.out.println( data);
        
        HttpURLConnection conn = null;
        
        try { 
            URL url = new URL( spath ); 
            conn = (HttpURLConnection) url.openConnection(); 
            if (conn instanceof HttpsURLConnection) {
                HttpsURLConnection httpsc = (HttpsURLConnection)conn; 
                bypassSSLSecurityCheck(httpsc); 
            }
            
            conn.setRequestMethod("POST"); 
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setReadTimeout( 60000 );
            conn.setConnectTimeout( 5000 );
            
            byte[] bytes = null; 
            if ( data instanceof byte[] ) {
                bytes = (byte[]) data; 
            }
            else {
                bytes = ( data == null ? "" : data.toString()).getBytes("UTF-8"); 
            }

            conn.setRequestProperty("Authorization", "Basic cGstZW80c0wzOTNDV1U1S212ZUpVYVc4VjczMFRUZWkyelk4ekU0ZEhKRHhrRjo=");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");            
            conn.setRequestProperty("Content-Length", ""+ bytes.length);

            // sends data 
            OutputStream out = null; 
            try {
                out = conn.getOutputStream();
                out.write( bytes ); 
            } 
            finally {
                try { out.close(); }catch(Throwable t){;} 
            }
            
            // read response 
            BufferedReader reader = null; 
            InputStreamReader inp = null;
            try {
                inp = new InputStreamReader(conn.getInputStream(), "UTF-8"); 
                reader = new BufferedReader( inp );

                String readLine = null;
                StringBuilder buff = new StringBuilder();
                while ((readLine = reader.readLine()) != null) {
                    buff.append( readLine ).append("\n");
                }
                System.out.println("result -> "+ buff );
            } 
            finally {
                try { inp.close(); }catch(Throwable t){;} 
                try { reader.close(); }catch(Throwable t){;} 
            }
        }
        finally {
            try { conn.disconnect(); }catch(Throwable t){;} 
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
