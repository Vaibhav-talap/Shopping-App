package com.psl.user.service.AOP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAop {

    Logger log = LoggerFactory.getLogger(LoggingAop.class);

    @Pointcut(value="execution(* com.psl.user.service.Services..(..) )")
    public void myPointcut() {

    }
    @Around("myPointcut()")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {

        Object result = pjp.proceed();
        if (result instanceof PasswordEncoder) {
            return result;
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String methodName = pjp.getSignature().getName();
        String className = pjp.getSignature().getClass().toString();
        Object[] array = pjp.getArgs();

        log.info("method invoked "+className+":"+methodName+"()"+"arguments :"+mapper.writeValueAsString(array));
        log.info(className + " : " + methodName + "()" + "Response : "
                + mapper.writeValueAsString(result));
        return result;
    }
}