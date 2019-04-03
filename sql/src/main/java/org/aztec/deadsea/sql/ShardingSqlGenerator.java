package org.aztec.deadsea.sql;

import org.aztec.deadsea.sql.impl.GenerationParam;

public interface ShardingSqlGenerator {
	
	public boolean accept(GenerationParameter param);
	public String generate(GenerationParameter param);
	
}
