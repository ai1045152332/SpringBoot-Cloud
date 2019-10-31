package com.example.demo.aspect;


import com.example.demo.annotation.OperationLog;
import com.example.demo.core.OperationLogSuccess;
import com.example.demo.model.LoginInfo;
import com.example.demo.model.OperationLogEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;


/**
 * 操作日志注解
 *
 * @author zhaojy
 * @date 2019/10/17
 */
public abstract class AbstractOperationLogAspect {

    private static ExpressionParser parser = new SpelExpressionParser();

    protected Logger log = LoggerFactory.getLogger(AbstractOperationLogAspect.class);
    private String START_TIME ="qieieie";

    @Pointcut(value = "@annotation(operationLog)", argNames = "operationLog")
    protected void operationLogMethod(OperationLog operationLog) {
        System.out.println("==-=-=-=-=-=");
    }



    @Before("operationLogMethod(operationLog)")
    public void logBefore(JoinPoint joinPoint, OperationLog operationLog){


        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        log.info("【请求 URL】：{}", request.getRequestURL());
        log.info("【请求 IP】：{}", request.getRemoteAddr());
        log.info("【请求类名】：{}，【请求方法名】：{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("【请求参数】：{}，", parameterMap.toString());
        Long start = System.currentTimeMillis();
        request.setAttribute(START_TIME, start);



        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        if (args!=null) {
            for (Object arg : args) {
                sb.append(sb.append(arg+" "));
            }
        }
        log.info(String.format("开始反射 [%s], params = [%s]", getMethodName(joinPoint), sb.toString()));
    }

    /**
     *
     * @param joinPoint
     * @param operationLog
     */
    @After("operationLogMethod(operationLog)")
    public void logAfter(JoinPoint joinPoint, OperationLog operationLog){

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        Long start = (Long) request.getAttribute(START_TIME);
        Long end = System.currentTimeMillis();
        log.info("【请求耗时】：{}毫秒", end - start);

        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        log.info("【浏览器类型】：{}，【操作系统】：{}，【原始User-Agent】：{}", userAgent.getBrowser().toString(), userAgent.getOperatingSystem().toString(), header);



        log.info(String.format("完成反射 [%s]", getMethodName(joinPoint)));
    }

    @AfterThrowing(value = "operationLogMethod(operationLog)", throwing = "ex")
    public void logAfterThrowingException(JoinPoint joinPoint, OperationLog operationLog,Exception ex){
        try {
            log.error(String.format("反射出错 [%s]",getMethodName(joinPoint)), ex);
        }catch (Exception e){
            System.out.println("===");
        }
    }


    /**
     * 最先进入
     * @param joinPoint
     * @param operationLog
     * @return
     * @throws Throwable
     */
    @Around(value = "operationLogMethod(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        log.info("进入环绕 --");
        String operateObject = (String) parseExpression(joinPoint, operationLog.operateObject());

        OperationLogEntity logEntity = new OperationLogEntity();
        LoginInfo loginInfo = loginInfo();
        logEntity.setAccountLoginName(loginInfo.getAccountLoginName());
        logEntity.setOperator(loginInfo.getOperator());

        logEntity.setModule(operationLog.module());
        logEntity.setOperateObject(operateObject);
        logEntity.setOperateTime(System.currentTimeMillis());
        logEntity.setOperateType(operationLog.operationType().name());
        logEntity.setDescription(description(joinPoint));

        try {
            final Object proceed = joinPoint.proceed();
            if (isSuccess(proceed)) {
                logEntity.setSuccess(OperationLogSuccess.SUCCESS.getMsg());
            } else {
                logEntity.setSuccess(OperationLogSuccess.FAILURE.getMsg());
            }
            return proceed;
        } catch (Throwable throwable) {
            logEntity.setSuccess(OperationLogSuccess.FAILURE.getMsg());
            throw throwable;
        } finally {
//            operationLogExecutor.execute(() -> {
//                saveLog(logEntity);
//            });
        }
    }

    /**
     * 解析Spring EL表达式，获取到参数值
     *
     * @param joinPoint
     * @param expression
     * @return
     */
    private Object parseExpression(JoinPoint joinPoint, String expression) {
        Object obj = null;
        try {
            Object[] objects = joinPoint.getArgs();
            EvaluationContext context = new StandardEvaluationContext();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String[] names = methodSignature.getParameterNames();
            for (int i = 0; i < names.length; i++) {
                context.setVariable(names[i], objects[i]);
            }
            obj = parser.parseExpression(expression, new TemplateParserContext()).getValue(context);
            if (obj != null && obj.getClass().isArray()) {
                try {
                    obj = joinIntArrayToStr((Object[]) obj);
                } catch (Exception e) {

                    log.error("parseExpression解析参数失败", e);
                }
            }
        } catch (Exception e) {
            log.error("SpEL表达式解析失败", e);
        }
        return obj;
    }

    /**
     * 将数组对象转换为字符串对象
     *
     * @param a
     * @return
     */
    private static String joinIntArrayToStr(Object[] a) {
        StringBuilder sb = new StringBuilder();
        for (Object o : a) {
            sb.append(o).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 判断当前操作是否成功
     * @param proceed
     * @return 从业务逻辑上判断是否成功
     */
    public abstract boolean isSuccess(Object proceed);

    /**
     * 自定义描述信息
     * @return
     * @param joinPoint
     */
    public abstract String description(ProceedingJoinPoint joinPoint);

    /**
     * 获取当前登录用户信息，这个地方
     * @return
     */
    @NonNull
    public abstract LoginInfo loginInfo();

    /**
     * 保存日志，各模块根据自己的情况将日志保存到数据库、ES或控制台，该方法已经异步，实现的时候不需要再自己实现线程池。
     * @param operationLogModel 已经填充好的操作日志实体类，直接保存就可以了
     */
    public abstract void saveLog(OperationLogEntity operationLogModel);


    private String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }
    private String getFullClassName(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getName();
    }
}
