package com.sharex.token.api.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 请求参数切入日志
 */
@Aspect // 定义一个切面类
@Component // 加入spring容器管理
public class LogInterceptor {

    private static final Log logger = LogFactory.getLog(LogInterceptor.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    // 切入点 execution: 切入点语法
    @Pointcut("execution(public * com.sharex.token.api.controller..*(..))")
    public void myPointcut() {};

    @Before("myPointcut()")
    public void before(JoinPoint joinPoint) {

        try {

            if (logger.isInfoEnabled()) {

                StringBuffer sb = new StringBuffer();

                Signature signature = joinPoint.getSignature();
                sb.append("方法名: " + signature.getDeclaringTypeName() + "\r\n");
                sb.append("参数: " + "\r\n");

                Object[] args = joinPoint.getArgs();
                if (args != null) {

                    for (Object arg:args) {

                        // 是否是基本类型
                        if (arg.getClass().isPrimitive()) {

                            sb.append(arg.getClass().getName() + ": " + arg.toString() + "\r\n");
                        } else {

                            sb.append(arg.getClass().getName() + ": " + objectMapper.writeValueAsString(arg) + "\r\n");
                        }
                    }
                }

                logger.info("before: \r\n" + sb.toString());
            }
        } catch (Exception e) {

            logger.error(e.getMessage(), e);
        }
    }

    @After("myPointcut()")
    public void after(JoinPoint joinPoint) {

        if (logger.isInfoEnabled()) {

            logger.info("after: " + joinPoint);
        }
    }

    // @AfterReturning 不配置 pointcut 报异常: Must set property 'expression' before attempting to match
    @AfterReturning("myPointcut()")
    public void afterReturning(JoinPoint joinPoint) {

        if (logger.isInfoEnabled()) {

            logger.info("afterReturning: " + joinPoint);
        }
    }

    //
    @AfterThrowing(value = "myPointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {

        if (logger.isInfoEnabled()) {

            logger.info("afterThrowing: " + joinPoint + "\r\n" + "ex: " + ex.getMessage());
        }
    }
}
