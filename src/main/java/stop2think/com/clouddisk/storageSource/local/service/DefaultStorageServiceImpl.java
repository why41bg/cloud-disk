package stop2think.com.clouddisk.storageSource.local.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import stop2think.com.clouddisk.core.config.CloudDiskProperties;
import stop2think.com.clouddisk.core.model.S3Object;
import stop2think.com.clouddisk.core.model.enums.StorageSourceEnum;
import stop2think.com.clouddisk.core.storage.SimpleStorageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

/**
 * 使用服务器存储空间提供默认存储服务
 *
 * @author weihongyu
 */
@Slf4j
public class DefaultStorageServiceImpl implements SimpleStorageService {

    private final CloudDiskProperties cloudDiskProperties = CloudDiskProperties.getInstance();

    private final String localStoragePath = cloudDiskProperties.getLocalStoragePath();


    @Override
    public S3Object simpleDownloadObject(String bucket, String obj) throws IOException {
        return this.simpleDownloadObject(obj);
    }


    @Override
    public S3Object simpleDownloadObject(String obj) throws IOException {
        File file = Paths.get(localStoragePath, obj).toFile();
        Files.createDirectories(file.getParentFile().toPath());
        S3Object s3o = new S3Object();
        s3o.setStorageSource(StorageSourceEnum.DEFAULT);
        s3o.setKey(file.getName());
        s3o.setSize(file.length());
        s3o.setLastModified(file.lastModified());
        s3o.setPath(obj);
        s3o.setLocalFile(file);
        s3o.setObjectContent(new FileInputStream(file));
        return s3o;
    }


    @Override
    public S3Object simpleUploadObject(String bucket, String obj, InputStream inputStream) throws IOException {
        return this.simpleUploadObject(obj, inputStream);
    }


    @Override
    public S3Object simpleUploadObject(String obj, InputStream inputStream) throws IOException {
        Path dest = Paths.get(localStoragePath, obj);
        Files.createDirectories(dest.getParent());
        File destFile = dest.toFile();
        OutputStream outputStream = new FileOutputStream(destFile);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        S3Object s3o = new S3Object();
        s3o.setBucket(StorageSourceEnum.DEFAULT.getStorageSource());
        s3o.setKey(destFile.getName());
        s3o.setLastModified(destFile.lastModified());
        s3o.setSize(destFile.length());
        s3o.setPath(obj);
        s3o.setLocalFile(destFile);
        return s3o;
    }


    @Override
    public List<S3Object> listObjects(String bucket, String dirPath, Integer maxKeys) {
        return this.listObjects(dirPath, maxKeys);
    }


    @Override
    public List<S3Object> listObjects(String dirPath, Integer maxKeys) {
        Path dir = Paths.get(localStoragePath, dirPath);
        log.info("List objects in {}", dir);
        Collection<File> files = FileUtils.listFiles(dir.toFile(), null, true);
        return files.stream().map(file -> {
            S3Object s3o = new S3Object();
            s3o.setStorageSource(StorageSourceEnum.DEFAULT);
            s3o.setSize(file.length());
            s3o.setKey(file.getName());
            s3o.setLastModified(file.lastModified());
            s3o.setPath(Paths.get(dirPath, file.getName()).toString());
            s3o.setLocalFile(file);
            return s3o;
        }).toList();
    }

}
