package org.aztec.deadsea.sql;

import org.aztec.deadsea.sql.impl.GenerationParam;

public interface ShardingSqlGenerator {
	
	public String generate(GenerationParameter param);
	
}
