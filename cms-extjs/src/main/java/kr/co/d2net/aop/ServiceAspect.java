package kr.co.d2net.aop;

import java.util.Arrays;

import javax.persistence.EntityNotFoundException;
import javax.persistence.QueryTimeoutException;

import kr.co.d2net.exception.ConnectionTimeOutException;
import kr.co.d2net.exception.DaoNonRollbackException;
import kr.co.d2net.exception.DataNotFoundException;
import kr.co.d2net.exception.ServiceException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



@Aspect
@Component
public class ServiceAspect {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Pointcut("execution(* kr.co.d2net.service.*.*(..))")  
	protected void loggingOperation() {}

	@Before("loggingOperation()")
	@Order(1)
	public void logJoinPoint(JoinPoint joinPoint) {
		logger.info("Join point kind : " + joinPoint.getKind());  
		logger.info("Signature declaring type : "+ joinPoint.getSignature().getDeclaringTypeName());  
		logger.info("Signature name : " + joinPoint.getSignature().getName());  
		logger.info("Arguments : " + Arrays.toString(joinPoint.getArgs()));  
		logger.info("Target class : "+ joinPoint.getTarget().getClass().getName());  
		logger.info("This class : " + joinPoint.getThis().getClass().getName());  
	}

	@AfterReturning(pointcut="loggingOperation()", returning = "result")
	@Order(2)
	public void logAfter(JoinPoint joinPoint, Object result) {
		logger.info("Exiting from Method :"+joinPoint.getSignature().getName());
		logger.info("Return value :"+result);
	}

	@AfterThrowing(pointcut="execution(* kr.co.d2net.service.*.*(..))", throwing = "e")
	@Order(3)
	public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {

		String message = null;
		if(e instanceof ServiceException){		
			ServiceException serviceException = (ServiceException)e;
			if(serviceException instanceof ConnectionTimeOutException){
				ConnectionTimeOutException ex = (ConnectionTimeOutException)e;
				message = messageSource.getMessage("error."+ex.getErrorCode(), null, null);
				logger.error(ex.getErrorCode()+": "+message, e);
			}else if(serviceException instanceof DataNotFoundException){
				DataNotFoundException ex = (DataNotFoundException)e;
				message = messageSource.getMessage("error."+ex.getErrorCode(), null, null);
				logger.error(ex.getErrorCode()+": "+message, e);		
			}else if(serviceException instanceof DaoNonRollbackException){
				DaoNonRollbackException ex = (DaoNonRollbackException)e;
				message = messageSource.getMessage("error."+ex.getErrorCode(), null, null);
				logger.error(ex.getErrorCode()+": "+message, e);		
			}else if(e instanceof EntityNotFoundException){
				logger.error("005(EntityNotFoundException)", e);
				throw new EntityNotFoundException();
			}else if(e instanceof QueryTimeoutException){
				logger.error("006(QueryTimeoutException)", e);
			}
		}else{
			logger.error(message,e);
		}


	}

	@Around("execution(* kr.co.d2net.service.*.*(..))")
	@Order(4)
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.info("The method " + joinPoint.getSignature().getName()+ "() begins with " + Arrays.toString(joinPoint.getArgs()));
		try {
			Object result = joinPoint.proceed();
			logger.info("The method " + joinPoint.getSignature().getName()+ "() ends with " + result);
			return result;
		} catch (IllegalArgumentException e) {
			logger.error("Illegal argument "+ Arrays.toString(joinPoint.getArgs()) + " in "+ joinPoint.getSignature().getName() + "()");
			throw e;
		}
	}
}
