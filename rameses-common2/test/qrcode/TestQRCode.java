/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qrcode;

import com.rameses.util.Base64Cipher;
import junit.framework.TestCase;

/**
 *
 * @author elmonazareno
 */
public class TestQRCode extends TestCase {
    
    public TestQRCode(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        //QRCodeUtil.saveQRCode("https://www.filipizen.com",  "/Users/elmonazareno/Desktop/test.png");
        //byte[] bytes = QRCodeUtil.generateQRCode("https://www.filipizen.com");
        //String s = QRCodeUtil.readQrcode(bytes);
        //System.out.println("output string is " + s);
        
        //String str = "qrcode:The quick brown fox jumped over the \"dog\"";
        //System.out.println(">"+str.substring(7));
        
        String s = "Elmo Nazareno";
        Base64Cipher cp = new Base64Cipher();
        System.out.println( "" + cp.isEncoded(s));
    }
}
