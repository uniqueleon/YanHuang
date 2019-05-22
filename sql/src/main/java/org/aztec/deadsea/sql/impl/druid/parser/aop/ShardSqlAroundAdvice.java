package org.aztec.deadsea.sql.impl.druid.parser.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.BeforeAdvice;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ShardSqlAroundAdvice implements BeforeAdvice{

	public ShardSqlAroundAdvice() {
		// TODO Auto-generated constructor stub
	}

	@Around("execution(* org.aztec.deadsea.sql.impl.druid.parser.[Create*|Drop*].parse(..))")
	public Object extractShardKeyWord(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println(pjp.getArgs()[0]);
		Object retObj = pjp.proceed(pjp.getArgs());
		
		return retObj;
	}
}
