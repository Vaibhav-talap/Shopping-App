package com.psl.product.service.AOP;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
@Aspect
@Component
public class LoggingAOP {
    Logger log = LoggerFactory.getLogger(LoggingAOP.class);
    @Pointcut(value = "execution(* com.psl.product.service.*.*.*(..) )")
    public void myPointcut() {
    }
    @Around("myPointcut()")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String methodName = pjp.getSignature().getName();
        String className = pjp.getSignature().getDeclaringTypeName();
        Object[] args = pjp.getArgs();
        // Modify args to handle MultipartFile
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) args[i];
                // Replace MultipartFile with a more log-friendly format
                args[i] = "[MultipartFile: " + file.getOriginalFilename() + ", Size: " + file.getSize() + " bytes]";
            }
        }
        // Log method invocation with arguments
        try {
            log.info("Method invoked: {}.{}() Arguments: {}", className, methodName, mapper.writeValueAsString(args));
        } catch (JsonProcessingException e) {
            log.error("Error logging arguments for {}.{}()", className, methodName, e);
        }
        // Proceed with the actual method execution
        Object result = pjp.proceed();
        // Log the method's response
        try {
            log.info("Method Response: {}.{}() Response: {}", className, methodName, mapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            log.error("Error logging response for {}.{}()", className, methodName, e);
        }
        return result;
    }
}