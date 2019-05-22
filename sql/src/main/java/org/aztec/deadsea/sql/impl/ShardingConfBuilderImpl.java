package org.aztec.deadsea.sql.impl;

import java.util.concurrent.Callable;

import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

@Component
public class ShardingConfBuilderImpl implements ShardingConfigurationFactory,BeanFactoryAware {
	
	private BeanFactory factory;
	

	public ShardingConfBuilderImpl() {
		// TODO Auto-generated constructor stub
	}

	public ShardingConfiguration getConfiguration() throws ShardingSqlException {
		try {
			if(factory.getBean(DEFAULT_CONFIGURATION_BEAN_NAME) == null) {
				
			}
			return (ShardingConfiguration) factory.getBean(DEFAULT_CONFIGURATION_BEAN_NAME);
		} catch (Exception e) {
			throw new ShardingSqlException(ShardingSqlException.ErrorCodes.SHARDING_CONFIGURATION_UNAVAILABLE);
		}
	}
	
	public static class AsyncConfigurationFetcher implements Callable{

		@Override
		public Object call() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	@Override
	public void setBeanFactory(BeanFactory arg0) throws BeansException {
		this.factory = arg0;
	}


}
