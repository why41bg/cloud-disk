package stop2think.com.clouddisk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import stop2think.com.clouddisk.core.config.CloudDiskProperties;
import stop2think.com.clouddisk.storageSource.local.service.DefaultStorageServiceImpl;
import stop2think.com.clouddisk.core.storage.StorageService;
import stop2think.com.clouddisk.storageSource.StorageServiceFactory;
import stop2think.com.clouddisk.storageSource.oss.service.OssStorageServiceImpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author weihongyu
 */
@SpringBootTest
public class CoreTests {

    @Test
    void StorageServiceFactoryTest() {
        StorageService ossStorageService = StorageServiceFactory.getSimpleStorageService("aliyun");
        assert Objects.requireNonNull(ossStorageService).getClass() == OssStorageServiceImpl.class;

        StorageService defaultStorageService = StorageServiceFactory.getSimpleStorageService("default");
        assert Objects.requireNonNull(defaultStorageService).getClass() == DefaultStorageServiceImpl.class;
    }

    @Test
    void CloudDiskPropertiesTest() {
        CloudDiskProperties properties1 = CloudDiskProperties.getInstance();
        System.out.println("properties1.debug = " + properties1.getDebug());

        properties1.setDebug(true);
        CloudDiskProperties properties2 = CloudDiskProperties.getInstance();
        System.out.println("properties2.debug = " + properties2.getDebug());
        properties2.setDebug(false);

        System.out.println("properties1.debug = " + properties1.getDebug());
    }

    @Test
    void demo() {
        CloudDiskProperties properties = CloudDiskProperties.getInstance();
        Path path = Paths.get(properties.getLocalStoragePath(), "..");
        System.out.println(path);
    }
}
