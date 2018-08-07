package com.sharex.token.api.aop;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.enums.CodeEnum;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Log logger = LogFactory.getLog(GlobalExceptionHandler.class);

//    // 违反约束，javax扩展定义
//    @ExceptionHandler(value = { ConstraintViolationException.class })
//    public RESTful handleConstraintViolation(ConstraintViolationException e) {
//        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
//        StringBuilder strBuilder = new StringBuilder();
//        for (ConstraintViolation<?> violation : violations) {
//            strBuilder.append(violation.getInvalidValue() + " " + violation.getMessage() + "\n");
//        }
//        String result = strBuilder.toString();
//        return RESTful.Fail(CodeEnum.ParameterError, "ConstraintViolation:" + result);
//    }
//
//    // 绑定失败，如表单对象参数违反约束
//    @ExceptionHandler(value = { BindException.class })
//    public RESTful handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return RESTful.Fail(CodeEnum.ParameterError,"BindException:" + buildMessages(ex.getBindingResult()));
//    }
//
//    // 参数无效，如JSON请求参数违反约束
//    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
//    public RESTful handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
//        return RESTful.Fail(CodeEnum.ParameterError,"MethodArgumentNotValid:" + buildMessages(e.getBindingResult()));
//    }
//
//    // 参数缺失
//    @ExceptionHandler(value = { MissingServletRequestParameterException.class })
//    public RESTful handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
//                                                                       HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return RESTful.Fail(CodeEnum.ParameterError,"ParamMissing:" + ex.getMessage());
//    }
//
//    // 参数类型不匹配
//    @ExceptionHandler(value = { TypeMismatchException.class })
//    public RESTful handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
//                                         HttpStatus status, WebRequest request) {
//        return RESTful.Fail(CodeEnum.ParameterError,"TypeMissMatch:" + ex.getMessage());
//    }
//
//    private String buildMessages(BindingResult result) {
//        StringBuilder resultBuilder = new StringBuilder();
//
//        List<ObjectError> errors = result.getAllErrors();
//        if (errors != null && errors.size() > 0) {
//            for (ObjectError error : errors) {
//                if (error instanceof FieldError) {
//                    FieldError fieldError = (FieldError) error;
//                    String fieldName = fieldError.getField();
//                    String fieldErrMsg = fieldError.getDefaultMessage();
//                    resultBuilder.append(fieldName).append(" ").append(fieldErrMsg).append(";");
//                }
//            }
//        }
//        return resultBuilder.toString();
//    }

    @ExceptionHandler(Exception.class)
    public RESTful handleException(Exception e) {

        logger.error(ExceptionUtils.getStackTrace(e));

        return RESTful.Fail(CodeEnum.ParameterError);
    }
}
