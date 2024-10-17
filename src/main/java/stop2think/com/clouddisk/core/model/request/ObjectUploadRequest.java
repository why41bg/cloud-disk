package stop2think.com.clouddisk.core.model.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author weihongyu
 */
@Data
@Builder
public class ObjectUploadRequest {

    /**
     * 存储空间唯一标识
     */
    private String bucket;

    /**
     * 对象唯一标识
     */
    private String obj;

    /**
     * 上传文件内容
     */
    private MultipartFile content;
}
