package org.aztec.deadsea.sandbox.ws;

import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.Maps;

@Path(value = "/sandbox/sql")
public class SandBoxSqlExecutor {
	
	public static final Map<String,String> sqlResult = Maps.newConcurrentMap();

	public SandBoxSqlExecutor() {
		// TODO Auto-generated constructor stub
	}
	
	@GET
	@Path("index")
	
	public String welcome() {
		return "welcome to sandbox environment!";
	}
	
	@POST
	@Path("execute")
	@Produces(value = {MediaType.APPLICATION_JSON})
	public String retText(@FormParam("sql")String sql) {
		
		return sqlResult.containsKey(sql) ? sqlResult.get(sql) : "{success:false}";
	}
	
	@POST
	@Path("prepare")
	@Produces(value = {MediaType.APPLICATION_JSON})
	public String injectResult(@FormParam("sql")String testSql,@FormParam("result") String result) {
		sqlResult.put(testSql,result);
		return result;
	}

}
