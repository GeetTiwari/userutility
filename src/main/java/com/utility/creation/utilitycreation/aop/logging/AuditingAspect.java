package com.utility.creation.utilitycreation.aop.logging;

import com.utility.creation.utilitycreation.model.entity.CommonEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Aspect
@Component
public class AuditingAspect {

    @Before("(execution(* com.utility.creation.utilitycreation.service.*.save*(..)) || execution(* com.utility.creation.utilitycreation.service.*.add*(..))) && args(entity,..)")
    public void setCreatedTimestamp(CommonEntity entity) {
        if (entity != null && entity.getCreatedDate() == null && entity.getCreatedTime() == null) {
            entity.setCreatedDate(LocalDate.now());
            entity.setCreatedTime(LocalTime.now());
        }
    }

    @Before("execution(* com.utility.creation.utilitycreation.service.*.update*(..)) && args(.., entity)")
    public void setModifiedTimestamp(CommonEntity entity) {
        System.out.println("AOP Intercepted Update for: " + entity);
        if (entity != null) {
            entity.setModifiedDate(LocalDate.now());
            entity.setModifiedTime(LocalTime.now());
        }
    }

    @Around("execution(* com.utility.creation.utilitycreation.repository.*.save(..))")
    public Object setTimestampsOnSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof CommonEntity) {
            CommonEntity entity = (CommonEntity) args[0];

            // Handle creation timestamp
            if (entity.getCreatedDate() == null || entity.getCreatedTime() == null) {
                entity.setCreatedDate(LocalDate.now());
                entity.setCreatedTime(LocalTime.now());
            }

            // Handle modification timestamp
            entity.setModifiedDate(LocalDate.now());
            entity.setModifiedTime(LocalTime.now());
        }

        return joinPoint.proceed(args); // Proceed with the actual save operation
    }
}
