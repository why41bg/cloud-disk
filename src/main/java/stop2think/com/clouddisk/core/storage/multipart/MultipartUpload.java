package stop2think.com.clouddisk.core.storage.multipart;

import stop2think.com.clouddisk.core.model.S3Object;
import stop2think.com.clouddisk.core.model.request.ObjectUploadRequest;

import java.io.IOException;

/**
 * @author weihongyu
 */
public interface MultipartUpload {

    /**
     * 数据流分片上传至指定 bucket
     *
     * @param request@return 上传成功后的对象信息
     */
    S3Object multipartUploadObject(ObjectUploadRequest request) throws IOException;

}
