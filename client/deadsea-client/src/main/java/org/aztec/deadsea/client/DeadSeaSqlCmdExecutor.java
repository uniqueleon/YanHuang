package org.aztec.deadsea.client;

import java.util.List;

import org.aztec.autumn.common.utils.BasePropertiesConfig;
import org.aztec.deadsea.common.sql.SQLTemplates;
import org.aztec.deadsea.sql.ShardSqlExecutor;
import org.aztec.deadsea.sql.ShardSqlExecutor.ExecuteMode;
import org.aztec.deadsea.sql.ShardingSqlException;
import org.aztec.deadsea.sql.SqlExecuteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.beust.jcommander.internal.Lists;

/**
 * Hello world!
 *
 */

@SpringBootConfiguration
@EnableAspectJAutoProxy
@ComponentScan(basePackages= {
		"org.aztec.deadsea.common.impl",
		"org.aztec.deadsea.xa.impl",
		"org.aztec.deadsea.xa.impl.redis",
		"org.aztec.deadsea.sql.impl",
		"org.aztec.deadsea.sql.impl.druid",
		"org.aztec.deadsea.sql.impl.druid.parser",
		"org.aztec.deadsea.sql.impl.executor",
		"org.aztec.deadsea.sql.impl.generator",
		"org.aztec.deadsea.sql.impl.xa",
		"org.aztec.deadsea.metacenter.impl",
		"org.aztec.deadsea.sql",})
public class DeadSeaSqlCmdExecutor implements ApplicationRunner
{
	@Autowired
	ShardSqlExecutor executor;
	
	
    public static void main( String[] args )
    {
        try {
        	if(args.length > 1) {
				System.setProperty(BasePropertiesConfig.DEFAUTL_SYSTEM_PROPERTY_FILE,args[1]);
			}
			SpringApplication sa = new SpringApplication(DeadSeaSqlCmdExecutor.class);
			sa.setBannerMode(Mode.OFF);
			sa.run(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public void run(ApplicationArguments args) throws Exception {
		try {
			//String sql = "create table `lmDb1`.`lmTest`(id int primary key auto_increment)engine='InnoDB'";
			//String sql = "create table `lmDb1`.`lmTest`(id int primary key auto_increment)engine='shardDB'";
			//String sql = "SELECT * FROM base_item_0001 WHERE id IN (4266266,4266264)";
			//String sql = "create shard(11) database `lmDb`";
			if(args.getOptionValues("conf") != null ) {
				List<String> confFiles = args.getOptionValues("conf");
				if(confFiles.size() > 0) {
					System.setProperty(BasePropertiesConfig.DEFAUTL_SYSTEM_PROPERTY_FILE,confFiles.get(0));
				}
			}
			Long curTime = System.currentTimeMillis();
			String sql = "insert into `lmDb`.`account`(`name`) values('liming2')";
			//String sql = "create shard(13) table lmDb.account (id int primary key auto_increment,name varchar(20))engine='InnoDB'";
			List<Thread> threads = Lists.newArrayList();
			for(int i = 0;i < 3;i++) {
				Thread execThread = new Thread(new MultiExecThread(sql,ExecuteMode.SINGLE));
				execThread.setName("Main test-" + i);
				execThread.start();
				threads.add(execThread);
			}
			for(Thread thrd : threads) {
				thrd.join();
			}
			System.out.println(String.format(SQLTemplates.CREATE_DATABASE, new Object[] {"lmtest","utf8","utf8_unicode_ci"}));
			System.out.println("USE TIME:" + (System.currentTimeMillis() - curTime));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {

			System.exit(0);
		}
	}
	
	public class MultiExecThread implements Runnable{
		String targetSql;
		ExecuteMode mode;

		public MultiExecThread(String targetSql,ExecuteMode mode) {
			super();
			this.targetSql = targetSql;
			this.mode = mode;
		}
		
		public void run() {

			try {
				//SqlExecuteResult sResult = executor.execute(targetSql, mode);
				SqlExecuteResult sResult = executor.executeInsert(targetSql, mode);
				System.out.println(sResult.isSuccess());
			} catch (ShardingSqlException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
