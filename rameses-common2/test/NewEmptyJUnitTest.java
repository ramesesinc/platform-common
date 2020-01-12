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
        String name = "@@";
        if ( name.startsWith("@@")) {
            name = name.substring(2).split(":")[0]; 
        }
        System.out.println( name );
    }
}
