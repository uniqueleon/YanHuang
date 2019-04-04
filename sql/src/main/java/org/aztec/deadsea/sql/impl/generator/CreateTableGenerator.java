package org.aztec.deadsea.sql.impl.generator;

import java.util.List;

import org.aztec.deadsea.sql.GenerationParameter;
import org.aztec.deadsea.sql.ShardingSqlGenerator;
import org.aztec.deadsea.sql.SqlType;

public class CreateTableGenerator implements ShardingSqlGenerator {

	public CreateTableGenerator() {
		// TODO Auto-generated constructor stub
	}

	public boolean accept(GenerationParameter param) {
		// TODO Auto-generated method stub
		return param.getSqlMetaData().getSqlType().equals(SqlType.CREAT_TABLE);
	}

	public String generate(GenerationParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

	public String generateSingle(GenerationParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> generateMulti(GenerationParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

}
