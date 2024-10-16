package stop2think.com.clouddisk.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import stop2think.com.clouddisk.core.model.enums.StorageSourceEnum;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * @author weihongyu
 */
@Data
@NoArgsConstructor
public class S3Object {

    /**
     * 文件存储源
     */
    private StorageSourceEnum storageSource;

    /**
     * 文件所在 bucket 唯一标识
     * 当存储源为默认时, 使用本地默认目录作为 bucket
     */
    private String bucket;

    /**
     * 文件名
     */
    private String key;

    /**
     * 实体标签, 用于标识文件的唯一性
     */
    private String eTag;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件最新修改时间
     */
    private long lastModified;

    /**
     * 文件在 bucket 中的路径
     * example: dir/file.txt  -> dir 目录下的 file.txt 文件
     *          dir/          -> dir 目录
     *          file.txt      -> file.txt
     */
    private String path;

    /**
     * 当使用默认存储源时, 该字段作为文件的本地抽象
     */
    private File localFile;

    /**
     * 文件输入流
     */
    private InputStream objectContent;

    /**
     * 文件扩展字段
     */
    private Map<String, Object> ext;
}
