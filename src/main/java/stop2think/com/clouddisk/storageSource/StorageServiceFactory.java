package stop2think.com.clouddisk.storageSource;

import stop2think.com.clouddisk.core.exception.UnsupportedStorageSourceException;
import stop2think.com.clouddisk.core.model.enums.StorageSourceEnum;
import stop2think.com.clouddisk.storageSource.local.service.DefaultStorageServiceImpl;
import stop2think.com.clouddisk.core.storage.SimpleStorageService;
import stop2think.com.clouddisk.storageSource.oss.service.OssStorageServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储服务工厂类
 *
 * @author weihongyu
 */
public class StorageServiceFactory {

    private static final Map<String, SimpleStorageService> SIMPLE_STORAGE_SERVICE_MAP = new HashMap<>();

    public static SimpleStorageService getSimpleStorageService(String storageSource) {
        StorageSourceEnum storageSourceEnum = StorageSourceEnum.getStorageSourceEnum(storageSource);
        if (storageSourceEnum == null) {
            throw new UnsupportedStorageSourceException("Unsupported storage source type: " + storageSource);
        }
        if (SIMPLE_STORAGE_SERVICE_MAP.containsKey(storageSource)) {
            return SIMPLE_STORAGE_SERVICE_MAP.get(storageSource);
        }
        SimpleStorageService storageService;
        switch (storageSourceEnum) {
            case ALIYUN-> storageService = new OssStorageServiceImpl();
            default -> storageService = new DefaultStorageServiceImpl();
        }
        SIMPLE_STORAGE_SERVICE_MAP.put(storageSource, storageService);
        return storageService;
    }
}
