package stop2think.com.clouddisk.core.model.response;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author weihongyu
 */
@Data
public class ResultResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Integer SUCCESS_CODE = 200;

    private static final Integer FAILURE_CODE = 500;

    private Integer code;

    private String message;

    private T data;

    public static <T> ResultResponse<T> success(String message, T data) {
        ResultResponse<T> response = new ResultResponse<>();
        response.setCode(SUCCESS_CODE);
        if (StringUtils.isNotBlank(message)) {
            response.setMessage(message);
        }
        if (null != data) {
            response.setData(data);
        }
        return response;
    }


    public static <T> ResultResponse<T> failure(String message) {
        ResultResponse<T> response = new ResultResponse<>();
        response.setCode(FAILURE_CODE);
        if (StringUtils.isNotBlank(message)) {
            response.setMessage(message);
        }
        return response;
    }
}
