package com.clever.boot.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class PersonControllerAspect {

    @Pointcut(value = "execution(* com.clever.boot.controller.UserController.*(..))")
    public void pointCutMethod() {
    }

    @Before("pointCutMethod()")
    public void beforeCallAtMethod(JoinPoint jp) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(","));
        log.info("before: {}, args=[{}]", jp.toShortString(), args);
    }
}
