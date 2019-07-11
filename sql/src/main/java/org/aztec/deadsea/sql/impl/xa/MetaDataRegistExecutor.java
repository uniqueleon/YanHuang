package org.aztec.deadsea.sql.impl.xa;

import org.aztec.deadsea.common.MetaDataRegister;
import org.aztec.deadsea.common.ServerRegister;
import org.aztec.deadsea.common.xa.XAContext;
import org.aztec.deadsea.common.xa.XAException;
import org.aztec.deadsea.common.xa.XAExecutor;
import org.aztec.deadsea.common.xa.XAResponse;
import org.aztec.deadsea.sql.ShardingConfigurationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetaDataRegistExecutor implements XAExecutor {


	@Autowired
	protected ServerRegister serverRegister;
	@Autowired
	protected MetaDataRegister metaRegister;
	@Autowired
	protected ShardingConfigurationFactory confFactory;
	
	public MetaDataRegistExecutor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canHandle(XAContext context) throws XAException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public XAResponse prepare(XAContext context) throws XAException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XAResponse commit(XAContext context) throws XAException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XAResponse rollback(XAContext context) throws XAException {
		// TODO Auto-generated method stub
		return null;
	}

}
