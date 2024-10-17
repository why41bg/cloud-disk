package stop2think.com.clouddisk.core.storage.baseService;

import stop2think.com.clouddisk.core.model.S3Object;

import java.io.IOException;
import java.util.List;

/**
 * @author weihongyu
 */
public interface SimpleList {

    /**
     * 列出指定 bucket 下, 指定目录下的文件
     *
     * @param bucket  存储空间唯一标识
     * @param dir     目录路径
     * @param maxKeys 最大返回数量
     * @return 指定目录路径下的文件列表
     */
    List<S3Object> listObjects(String bucket, String dir, Integer maxKeys) throws IOException;

    /**
     * 列出默认 bucket 下, 指定目录下的文件
     *
     * @param dir     目录路径
     * @param maxKeys 最大返回数量
     * @return        默认 bucket 下指定目录路径下的文件列表
     */
    List<S3Object> listObjects(String dir, Integer maxKeys) throws IOException;
}
