package stop2think.com.clouddisk.core.config;

import lombok.Data;

import java.nio.file.Paths;

/**
 * @author weihongyu
 */
@Data
public class CloudDiskProperties {

    /**
     * 用户根目录
     */
    private String userHome = System.getProperty("user.home");

    /**
     * 使用服务器存储空间作为存储源, 服务器文件存储路径
     */
    private String localStoragePath = Paths.get(userHome, "cloud-disk", "storage").toString();

    /**
     * 本地数据库文件目录路径
     */
    private String localDbDirPath = Paths.get(userHome, ".cloud-disk", "db").toString();

    /**
     * 系统日志文件目录路径
     */
    private String logsDirPath = Paths.get(userHome, ".cloud-disk", "logs").toString();

    /**
     * 调试模式
     */
    private Boolean debug = false;


    private CloudDiskProperties() {
    }

    private static class CloudDiskPropertiesHolder {
        static CloudDiskProperties instance = new CloudDiskProperties();
    }

    public static CloudDiskProperties getInstance() {
        return CloudDiskPropertiesHolder.instance;
    }

}
