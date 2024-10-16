package stop2think.com.clouddisk.core.storage.baseService;

import stop2think.com.clouddisk.core.model.S3Object;

import java.io.IOException;
import java.io.InputStream;

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
     * @param bucket      存储空间唯一标识
     * @param obj         存储空间文件名
     * @param inputStream 文件流
     * @return 上传结果
     */
    S3Object simpleUploadObject(String bucket, String obj, InputStream inputStream) throws IOException;

    /**
     * 上传文件流至默认 bucket
     *
     * @param obj         存储空间文件名
     * @param inputStream 文件流
     * @return 上传结果
     */
    S3Object simpleUploadObject(String obj, InputStream inputStream) throws IOException;
}
