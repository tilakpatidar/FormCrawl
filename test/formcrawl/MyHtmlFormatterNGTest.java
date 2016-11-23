/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package formcrawl;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author tilak
 */
public class MyHtmlFormatterNGTest {
    
    public MyHtmlFormatterNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of format method, of class MyHtmlFormatter.
     */
    @Test
    public void testFormat() {
        System.out.println("format");
        LogRecord rec = null;
        MyHtmlFormatter instance = new MyHtmlFormatter();
        String expResult = "";
        String result = instance.format(rec);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHead method, of class MyHtmlFormatter.
     */
    @Test
    public void testGetHead() {
        System.out.println("getHead");
        Handler h = null;
        MyHtmlFormatter instance = new MyHtmlFormatter();
        String expResult = "";
        String result = instance.getHead(h);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTail method, of class MyHtmlFormatter.
     */
    @Test
    public void testGetTail() {
        System.out.println("getTail");
        Handler h = null;
        MyHtmlFormatter instance = new MyHtmlFormatter();
        String expResult = "";
        String result = instance.getTail(h);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
