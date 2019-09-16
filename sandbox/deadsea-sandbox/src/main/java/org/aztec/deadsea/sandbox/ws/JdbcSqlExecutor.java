package org.aztec.deadsea.sandbox.ws;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.aztec.autumn.common.utils.JsonUtils;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.aztec.autumn.common.utils.jdbc.DBManager;
import org.aztec.deadsea.sandbox.conf.JDBCConfig;
import org.aztec.deadsea.sandbox.ws.dto.ResponseDTO;
import org.springframework.stereotype.Component;

@Component
@Path("/real/")
public class JdbcSqlExecutor {
	
	private JsonUtils jsonUtil = UtilsFactory.getInstance().getJsonUtils();

	public JdbcSqlExecutor() {
		// TODO Auto-generated constructor stub
	}


	@GET
	@Path("index")
	
	public String welcome() {
		return "welcome to sandbox environment!";
	}
	
	@POST
	@Path("query")
	@Produces(value = {MediaType.APPLICATION_JSON})
	public String query(@FormParam("sql")String sql) {

		JDBCConfig config = new JDBCConfig();
		try {
			DBManager manager = DBManager.getManager(config.getConnectionUrl(), config.getUsername(), config.getPassword(), "mysql");
			List<Map<String,String>> mapData = manager.getQueryExecutor().getQueryResultAsMap(sql);
			return jsonUtil.object2Json(new ResponseDTO(true, mapData));
		}  catch (Exception e) {
			return "{\"success\":false}";
		}
	}
	
	@POST
	@Path("execute")
	@Produces(value = {MediaType.APPLICATION_JSON})
	public String retText(@FormParam("sql")String sql) {

		JDBCConfig config = new JDBCConfig();
		try {
			DBManager manager = DBManager.getManager(config.getConnectionUrl(), config.getUsername(), config.getPassword(), "mysql");
			int updateCount = manager.getQueryExecutor().executeUpdate(sql);
			return jsonUtil.object2Json(new ResponseDTO(true, updateCount));
		}  catch (Exception e) {
			return "{\"success\":false}";
		}
	}
	
	@POST
	@Path("prepare")
	@Produces(value = {MediaType.APPLICATION_JSON})
	public String injectResult(@FormParam("sql")String testSql,@FormParam("result") String result) {
		
		return "{\"success\":false,\"errorMsg\":\"unsupported!\"}";
	}
}
