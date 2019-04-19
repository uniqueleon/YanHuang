package org.aztec.deadsea.metacenter;

import org.aztec.deadsea.metacenter.conf.BaseInfo;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	public static void main(String[] args) {
		try {
			BaseInfo bi = BaseInfo.getInstance();
			
			//rsi.setTotalNum(1);
			//rsi.setType("db");
			//rsi.save();
			System.out.println(bi.getDbSize());
			System.out.println(String.format(MetaCenterConst.ZkConfigPaths.SHARDING_AGE_INFO, new Object[] {"lm_test",1}));
			//System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
