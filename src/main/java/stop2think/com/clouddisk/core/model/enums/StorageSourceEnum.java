package stop2think.com.clouddisk.core.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统支持的存储源枚举类
 *
 * @author weihongyu
 */
@AllArgsConstructor
@Getter
public enum StorageSourceEnum {

    ALIYUN("aliyun", "阿里云 OSS"),

    TENCENT("tencent", "腾讯云 COS"),

    DEFAULT("default", "使用服务器存储空间作为默认存储源");

    private static final Map<String, StorageSourceEnum> STORAGE_SOURCE_ENUM_MAP = new HashMap<>();

    private final String storageSource;

    private final String desc;

    static {
        for (StorageSourceEnum sse : StorageSourceEnum.values()) {
            STORAGE_SOURCE_ENUM_MAP.put(sse.getStorageSource(), sse);
        }
    }

    public static StorageSourceEnum getStorageSourceEnum(String dataSource) {
        return STORAGE_SOURCE_ENUM_MAP.get(dataSource);
    }

}
