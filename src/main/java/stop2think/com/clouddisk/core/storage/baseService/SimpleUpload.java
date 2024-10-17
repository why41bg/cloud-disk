package stop2think.com.clouddisk.core.storage.baseService;

import stop2think.com.clouddisk.core.model.S3Object;
import stop2think.com.clouddisk.core.model.request.ObjectUploadRequest;

import java.io.IOException;

/**
 * 简单上传文件、图片、视频等资源. 适用于文件大小不超过 5 GB,
 * 且对并发上传性能要求不高的场景.
 *
 * @author weihongyu
 */
public interface SimpleUpload {

    /**
     * 上传文件流至指定 bucket
     *
     * @param request 上传请求对象
     * @return        上传结果
     */
    S3Object simpleUploadObject(ObjectUploadRequest request) throws IOException;

}
