/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.rameses.util.ConfigProperties;
import java.io.InputStream;
import junit.framework.TestCase;

/**
 *
 * @author wflores
 */
public class TestConfigProperties extends TestCase {
    
    public TestConfigProperties(String testName) {
        super(testName);
    }

    public void test1() throws Exception {
        InputStream inp = null; 
        try {
            inp = getClass().getResourceAsStream("test-env.conf");
            ConfigProperties p = new ConfigProperties(inp); 
            System.out.println( p.getProperties()); 
        } 
        finally {
            try { inp.close(); }catch(Throwable t){;} 
        }
    }
}
