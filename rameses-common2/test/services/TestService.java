/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.rameses.service.DefaultScriptServiceProxy;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author ramesesinc
 */
public class TestService extends TestCase {
    
    public TestService(String testName) {
        super(testName);
    }

    public void test1() throws Exception {
        Map m = new HashMap();
        m.put("app.host", "192.168.254.120"); 
        m.put("app.cluster", "cloud-server"); 
        m.put("debug", true); 
                
        DefaultScriptServiceProxy p = new DefaultScriptServiceProxy("obo/OnlineBuildingPermitDownloadService", m, null); 
        
        Map param = new HashMap();
        param.put("appid", "137-D55KC2YR");
        param.put("orgcode", "137");
        Object res = p.invoke("getDownloadInfo", new Object[]{ param }); 
        System.out.println( res );
    }
}
