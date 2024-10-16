package stop2think.com.clouddisk.core.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author weihongyu
 */
@Slf4j
@Configuration
public class SystemConfigInit {

    private final CloudDiskProperties cloudDiskProperties = CloudDiskProperties.getInstance();

    private final String localDbDirPath = cloudDiskProperties.getLocalDbDirPath();

    private final String logsDirPath = cloudDiskProperties.getLogsDirPath();

    private final String localStoragePath = cloudDiskProperties.getLocalStoragePath();

    public SystemConfigInit() {
        createDbDirIfAbsent();
        createLocalStorageDirIfAbsent();
        createLogsDirIfAbsent();
    }

    private void createDbDirIfAbsent() {
        createDirIfAbsent(localDbDirPath);
    }

    private void createLocalStorageDirIfAbsent() {
        createDirIfAbsent(localStoragePath);
    }

    private void createLogsDirIfAbsent() {
        createDirIfAbsent(logsDirPath);
    }

    private void createDirIfAbsent(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            log.info("Folder not exist, creating automatically. {}", dirPath);
            try {
                FileUtils.forceMkdir(dir);
            } catch (Exception e) {
                log.warn("Failed to create folder automatically.", e);
            }
            return;
        }
        log.info("Folder exist: {}. Skip auto-creation.", dirPath);
    }
}
