/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.url;

import com.rameses.http.BasicHttpClient;
import com.rameses.http.HttpClient;
import java.util.HashMap;
import junit.framework.TestCase;

/**
 *
 * @author ramesesinc
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }

    public void Xtest1() throws Exception {
        String host = "pg-sandbox.paymaya.com";
        
        HttpClient c = new HttpClient( host ); 
        c.setProtocol("https"); 
        c.setContentType("application/json"); 
        
        Object res = c.get("checkout/v1/checkouts"); 
        System.out.println("res -> "+ res);
    }

    public void test2() throws Exception {
        String url = "https://pg-sandbox.paymaya.com/checkout/v1/checkouts";
        
        BasicHttpClient c = new BasicHttpClient(); 
        Object res = c.post(url, "{}", new HashMap()); 
        System.out.println("res -> "+ res);
    }
}
