import com.rameses.util.Base64Cipher;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import junit.framework.*;
/*
 * Test1.java
 * JUnit based test
 *
 * Created on January 29, 2011, 9:39 AM
 */

/**
 *
 * @author ms
 */
public class Test1 extends TestCase {
    
    public Test1(String testName) {
        super(testName);
    }
    
    public void testHello() {
        Map data = new LinkedHashMap();
        data.put("First Name", "Jayrome");
        data.put("Last Name", "MaÑana");
        
        Map addr = new HashMap();
        addr.put("Address 1", "cebu city");
        addr.put("Address 2", "negros oriental");
        data.put("Address", addr);
        
        String s = "{lastname:'MaÑana', firstname: 'Jayrome'}";
        
        Base64Cipher bc = new Base64Cipher();
        String b = bc.encode(s);
        System.out.println(b);
        
    }
    
}
