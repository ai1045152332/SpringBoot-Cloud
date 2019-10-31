package com.example.demo.aspect;

import com.example.demo.model.LoginInfo;
import com.example.demo.model.OperationLogEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


/**
 * 操作日志注解
 *
 * @author zhaojy
 * @date 2019/10/17
 */
@Component
@Aspect
public class OperationLogAspect extends AbstractOperationLogAspect {

//@Autowired
//private ser

    @Override
    public boolean isSuccess(Object proceed) {
        if (proceed instanceof Object) {
            Object value = (Object) proceed;
//            String status = value.get("status").toString();
//            if (Integer.parseInt(status) >= 300) {
//                return false;
//            }
        }
        return true;
    }

    @Override
    public String description(ProceedingJoinPoint joinPoint) {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        if (requestAttributes instanceof ServletRequestAttributes) {
//            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
//            HttpServletRequest request = attributes.getRequest();
//            HttpServletResponse response = attributes.getResponse();
//            // ACCESS [2019-05-29T14:01:26.731Z] "POST /dong.php HTTP/1.1" 400 - 19 131 2 1 "-" "Mozilla/5.0 (X11; Linux x86_64; rv:28.0) Gecko/20100101 Firefox/28.0" "bb49ebef-4f2c-427b-aef3-c407634e8d0e" "39.97.8.31" "-"
//            String log = "ACCESS ";
//            Date curDate = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//            String curDateStr = dateFormat.format(curDate);
//            log += "[" + curDateStr + "]";
//            String path = request.getServletPath() + "?" + request.getQueryString();
//            String method = request.getMethod();
//            String protocol = request.getProtocol();
//            log += "\"" + method + " " + path + " " + protocol + "\"";
//            int status = response.getStatus();
//            log += " " + status;
//            String userAgent = request.getHeader("User-Agent");
//            log += " \"" + userAgent + "\"";
//            String ip = request.getRemoteAddr();
//            log += " " + ip;
//            return log;
//        }
        return null;
    }

    @Override
    @NonNull
    public LoginInfo loginInfo() {
        LoginInfo loginInfo = new LoginInfo();
//        LoginEntity loginEntity = UserContext.get();
//
//        if (loginEntity != null) {
//            loginInfo.setAccountLoginName(loginEntity.getAccountLoginName());
//            loginInfo.setOperator(loginEntity.getUsername());
//        }

        return loginInfo;
    }

    @Override
    public void saveLog(OperationLogEntity operationLogEntity) {
//        operateLogService.log(operationLogEntity);
    }
}
