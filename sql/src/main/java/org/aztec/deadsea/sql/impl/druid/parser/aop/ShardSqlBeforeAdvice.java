package org.aztec.deadsea.sql.impl.druid.parser.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ShardSqlBeforeAdvice {

	public ShardSqlBeforeAdvice() {
		// TODO Auto-generated constructor stub
	}

	@Before("execution(* org.aztec.deadsea.sql.impl.druid.parser.*.parse()) ")
	public void extractShardKeyWord(ProceedingJoinPoint pjp) {
		System.out.println(pjp.getArgs()[0]);
	}
}
