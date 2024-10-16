package stop2think.com.clouddisk.storageSource.oss.service;

import com.aliyun.oss.model.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import stop2think.com.clouddisk.core.model.S3Object;
import stop2think.com.clouddisk.core.model.enums.StorageSourceEnum;
import stop2think.com.clouddisk.core.storage.SimpleStorageService;
import stop2think.com.clouddisk.storageSource.oss.config.OSSClient;
import stop2think.com.clouddisk.storageSource.oss.constant.OssConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * @author weihongyu
 */
@Slf4j
public class OssStorageServiceImpl implements SimpleStorageService {

    @Resource
    private OSSClient ossClient = OSSClient.getInstance();


    @Override
    public S3Object simpleUploadObject(String bucket, String obj, InputStream inputStream) {
        PutObjectResult uploadResult = this.simpleUploadObject(new PutObjectRequest(bucket, obj, inputStream));
        S3Object s3o = new S3Object();
        s3o.setPath(obj);
        s3o.setBucket(bucket);
        if (Objects.isNull(uploadResult)) {
            return s3o;
        }
        s3o.setETag(uploadResult.getETag());
        return s3o;
    }


    @Override
    public S3Object simpleUploadObject(String obj, InputStream inputStream) {
        return this.simpleUploadObject(OssConstant.DEFAULT_BUCKET_NAME, obj, inputStream);
    }


    @Override
    public S3Object simpleDownloadObject(String bucket, String obj) throws IOException {
        try (OSSObject ossObject = this.ossClient.download(bucket, obj)) {
            S3Object s3o = new S3Object();
            s3o.setBucket(bucket);
            s3o.setPath(obj);
            s3o.setStorageSource(StorageSourceEnum.ALIYUN);
            s3o.setObjectContent(ossObject.getObjectContent());
            s3o.setLastModified(ossObject.getObjectMetadata().getLastModified().getTime());
            s3o.setSize(ossObject.getObjectMetadata().getContentLength());
            s3o.setETag(ossObject.getObjectMetadata().getETag());
            s3o.setKey(obj.split("/")[obj.split("/").length - 1]);
            return s3o;
        }
    }


    @Override
    public S3Object simpleDownloadObject(String obj) throws IOException {
        return this.simpleDownloadObject(OssConstant.DEFAULT_BUCKET_NAME, obj);
    }


    @Override
    public List<S3Object> listObjects(String bucket, String dirPath, Integer maxKeys) {
        ListObjectsRequest request = new ListObjectsRequest();
        request.setBucketName(bucket);
        request.setPrefix(dirPath);
        request.setMaxKeys(maxKeys);
        ObjectListing objectsList = this.listObjects(request);
        return objectsList.getObjectSummaries().stream().map(objectSummary -> {
            S3Object s3o = new S3Object();
            s3o.setBucket(bucket);
            s3o.setPath(objectSummary.getKey());
            s3o.setStorageSource(StorageSourceEnum.ALIYUN);
            s3o.setLastModified(objectSummary.getLastModified().getTime());
            s3o.setSize(objectSummary.getSize());
            s3o.setETag(objectSummary.getETag());
            s3o.setKey(objectSummary.getKey().split("/")[objectSummary.getKey().split("/").length - 1]);
            return s3o;
        }).toList();
    }


    @Override
    public List<S3Object> listObjects(String dirPath, Integer maxKeys) {
        return this.listObjects(OssConstant.DEFAULT_BUCKET_NAME, dirPath, maxKeys);
    }


    private PutObjectResult simpleUploadObject(PutObjectRequest request) {
        try {
            return ossClient.upload(request);
        } catch (Exception e) {
            log.warn("Failed to upload file to OSS.", e);
        }
        return null;
    }


    private ObjectListing listObjects(ListObjectsRequest request) {
        return ossClient.list(request);
    }
}
