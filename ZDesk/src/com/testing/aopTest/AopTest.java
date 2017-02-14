package com.testing.aopTest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import com.zinglabs.base.core.frame.AppLogImpl;

@Aspect 
public class AopTest {
	@Pointcut("execution(* com.zinglabs.apps..action..*(..) )")
	private void anyMethod(){}
	
	@Before("anyMethod()")  
    public void doAccessCheck(JoinPoint jp){
		System.out.println(jp.getSignature().getName());
		Object obj=jp.getTarget();
		AppLogImpl ai=null;
		if(obj instanceof AppLogImpl){
			ai=(AppLogImpl)jp.getTarget();
		}
		if(ai!=null){
			System.out.println(ai.getMod());
		}
        System.out.println("前置通知" );  
    }
	
	@AfterReturning("anyMethod()")  
    public void doAfter(){  
        System.out.println("后置通知");
    }  
      
    @After("anyMethod()")
    public void after(){
        System.out.println("最终通知");  
    }  
      
    @AfterThrowing("anyMethod()")  
    public void doAfterThrow(){  
        System.out.println("例外通知");  
    }  
      
    @Around("anyMethod()")  
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable{  
        System.out.println("进入环绕通知");  
        Object object = pjp.proceed();//执行该方法
        System.out.println("退出方法");  
        return object;  
    } 
}
