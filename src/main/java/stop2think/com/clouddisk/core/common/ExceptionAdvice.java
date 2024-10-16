package stop2think.com.clouddisk.core.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import stop2think.com.clouddisk.core.model.response.ResultResponse;

/**
 * 统一异常处理器
 *
 * @author weihongyu
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResultResponse<Void> handleException(Exception ex, HttpServletRequest request) {
        log.warn("Request to [{}] throws exception: [{}]", request.getRequestURL(), ex.getMessage());
        return ResultResponse.failure(ex.getMessage());
    }

}
