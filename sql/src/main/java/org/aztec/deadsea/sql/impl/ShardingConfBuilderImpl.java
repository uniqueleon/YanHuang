package org.aztec.deadsea.sql.impl;

import org.aztec.deadsea.sql.ShardingConfiguration;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.meta.SqlMetaData;
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

	public ShardingConfiguration getConfiguration(SqlMetaData metaData) throws ShardingSqlException {
		try {
			return new LocalShardingConfiguration();
		} catch (Exception e) {
			throw new ShardingSqlException(ShardingSqlException.ErrorCodes.SHARDING_CONFIGURATION_UNAVAILABLE);
		}
	}

	@Override
	public void setBeanFactory(BeanFactory arg0) throws BeansException {
		// TODO Auto-generated method stub
		this.factory = arg0;
	}


}
