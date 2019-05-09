package org.aztec.deadsea.sql;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.aztec.autumn.common.utils.security.CodeCipher;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public static void main(String[] args) {
		System.out.println(Integer.toBinaryString(172));
		System.out.println(Integer.toBinaryString(16));
		System.out.println(Integer.toBinaryString(3));
		System.out.println(Integer.toBinaryString(34));
		//Long.mva
		String testContent = "18##172.16.3.34";
		CodeCipher cipher = new CodeCipher();
		try {
			String md5Sub = cipher.getMD5Substract(testContent, "UTF-8");
			System.out.println(md5Sub);
			byte[] bytes = md5Sub.getBytes("UTF-8");
			System.out.println(bytes.length);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
