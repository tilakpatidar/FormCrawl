/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package formcrawl;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author tilak
 */
public class MyHtmlFormatterTest {
    
    public MyHtmlFormatterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
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
        assertEquals(expResult, result);
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
        assertEquals(expResult, result);
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
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
