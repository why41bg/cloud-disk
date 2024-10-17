package stop2think.com.clouddisk.core.storage.baseService;

import stop2think.com.clouddisk.core.model.S3Object;

import java.io.IOException;

/**
 * @author weihongyu
 */
public interface SimpleDownload {

    /**
     * 从指定的 bucket 获取文件
     *
     * @param bucket 存储空间唯一标识
     * @param obj    文件绝对路径
     */
    S3Object simpleDownloadObject(String bucket, String obj) throws IOException;

    /**
     * 从默认的 bucket 简单下载文件
     *
     * @param obj 存储空间文件名
     * @return    下载文件元数据
     */
    S3Object simpleDownloadObject(String obj) throws IOException;
}
