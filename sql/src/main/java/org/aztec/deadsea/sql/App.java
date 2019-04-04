package org.aztec.deadsea.sql;

import java.util.Formatter;

import org.aztec.deadsea.sql.impl.druid.DruidMetaDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.Banner.Mode;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@SpringBootConfiguration
@ComponentScan(basePackages= {"org.aztec.deadsea.sql.impl.druid.parser",
		"org.aztec.deadsea.sql"})
public class App implements ApplicationRunner
{
	@Autowired
	SqlGeneratorBuilder builder;
	
	
    public static void main( String[] args )
    {
        SpringApplication sa = new SpringApplication(App.class);
        sa.setBannerMode(Mode.OFF);
        sa.run(args);
    }

	public void run(ApplicationArguments args) throws Exception {
		try {
			String sql = "create table `lmDb1`.`lmTest`(id int primary key auto_increment)engine='InnoDB'";
			GenerationParameter gp = builder.getGenerationParam(sql);
			ShardingSqlGenerator sqlGen = builder.build(gp);
			System.out.println(sqlGen);
			
			System.out.println(String.format(SQLTemplates.CREATE_DATABASE, new Object[] {"lmtest","utf8","utf8_unicode_ci"}));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
