package org.aztec.deadsea.sql.impl;

import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.meta.SqlMetaData;

public class GenerationParam implements GenerationParameter{
	
	private SqlMetaData metaData;

	public GenerationParam(SqlMetaData metaData) {
		super();
		this.metaData = metaData;
	}

	public SqlMetaData getSqlMetaData() {
		return metaData;
	}

	public <T> T get(String key) {
		return null;
	}

	
}
