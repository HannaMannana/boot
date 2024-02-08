package com.clever.boot.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class HouseHistoryServiceAspect {

    @Pointcut(value = "execution(* com.clever.boot.service.HouseHistoryService.*(..))")
    public void pointCutMethod() {
    }

    @Before("pointCutMethod()")
    public void beforeCallAtMethod(JoinPoint jp) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(","));
        log.info("before: {}, args=[{}]", jp.toShortString(), args);
    }

    @AfterReturning(pointcut="pointCutMethod()", returning="retVal")
    public void afterReturningCallAt(JoinPoint jp,Object retVal) {
        log.info("after: {}, result: {}", jp.toShortString(), retVal.toString());

    }

    @AfterThrowing(pointcut = "pointCutMethod()", throwing = "exception")
    public void afterThrowingHouseHistoryService(Throwable exception) {
        log.info("afterThrowingHouseHistoryService: exception message {}",exception.getMessage());
    }
}
