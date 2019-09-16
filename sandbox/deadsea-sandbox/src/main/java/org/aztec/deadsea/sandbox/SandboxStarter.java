package org.aztec.deadsea.sandbox;

import org.aztec.autumn.common.utils.server.RESTServer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;

/**
 * Hello world!
 *
 */
@SpringBootConfiguration
public class SandboxStarter implements ApplicationRunner 
{
	

	private static void setLog4j2Path(){
		String dir = System.getProperty("user.dir");
		System.setProperty("catalina.home", dir);
	}
	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        setLog4j2Path();
        SpringApplication.run(SandboxStarter.class, args);
    }

	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(args.getOptionValues("host").get(0));
		System.out.println(args.getOptionValues("port").get(0));
		HttpServer server = RESTServer.startServer("http://" + args.getOptionValues("host").get(0) + ":" + args.getOptionValues("port").get(0), new String[] {"org.aztec.deadsea.sandbox.ws"});
		server.start();
	}
}
