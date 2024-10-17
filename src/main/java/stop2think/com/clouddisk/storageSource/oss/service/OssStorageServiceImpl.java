package stop2think.com.clouddisk.storageSource.oss.service;

import com.aliyun.oss.model.*;
import com.aliyuncs.utils.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import stop2think.com.clouddisk.core.model.S3Object;
import stop2think.com.clouddisk.core.model.enums.StorageSourceEnum;
import stop2think.com.clouddisk.core.model.request.ObjectUploadRequest;
import stop2think.com.clouddisk.core.storage.StorageService;
import stop2think.com.clouddisk.storageSource.oss.config.OSSClient;
import stop2think.com.clouddisk.storageSource.oss.constant.OssConstant;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author weihongyu
 */
@Slf4j
public class OssStorageServiceImpl implements StorageService{

    @Resource
    private OSSClient ossClient = OSSClient.getInstance();


    @Override
    public S3Object simpleUploadObject(ObjectUploadRequest request) throws IOException {
        String bucket = StringUtils.isEmpty(request.getBucket()) ? OssConstant.DEFAULT_BUCKET_NAME : request.getBucket();
        String obj = request.getObj();
        InputStream content = request.getContent().getInputStream();
        PutObjectResult uploadResult = this.simpleUploadObject(new PutObjectRequest(bucket, obj, content));
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
    public S3Object simpleDownloadObject(String bucket, String obj) throws IOException {
        try (OSSObject ossObject = this.ossClient.getObject(bucket, obj)) {
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
    public List<S3Object> listObjects(String bucket, String dir, Integer maxKeys) {
        ListObjectsRequest request = new ListObjectsRequest();
        request.setBucketName(bucket);
        request.setPrefix(dir);
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
    public List<S3Object> listObjects(String dir, Integer maxKeys) {
        return this.listObjects(OssConstant.DEFAULT_BUCKET_NAME, dir, maxKeys);
    }


    private PutObjectResult simpleUploadObject(PutObjectRequest request) {
        try {
            return ossClient.putObject(request);
        } catch (Exception e) {
            log.warn("Failed to upload file to OSS.", e);
        }
        return null;
    }


    private ObjectListing listObjects(ListObjectsRequest request) {
        return ossClient.listObjects(request);
    }


    @Override
    public S3Object multipartUploadObject(ObjectUploadRequest request) throws IOException {
        String bucket = StringUtils.isEmpty(request.getBucket()) ? OssConstant.DEFAULT_BUCKET_NAME : request.getBucket();
        String obj = request.getObj();
        MultipartFile file = request.getContent();
        // 初始化分片
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucket, obj);
        InitiateMultipartUploadResult initiateMultipartUploadResult = ossClient.initiateMultipartUpload(initiateMultipartUploadRequest);
        String multipartUploadId = initiateMultipartUploadResult.getUploadId();
        List<PartETag> partETags = new ArrayList<>();
        InputStream is = file.getInputStream();
        long fileLength = is.available();
        int partCount = (int) Math.ceil((double) fileLength / OssConstant.PART_SIZE);
        for (int i = 0; i < partCount; i++) {
            long startPos = i * OssConstant.PART_SIZE;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : OssConstant.PART_SIZE;
            try (ByteArrayOutputStream byteArrayOutputStream = cloneInputStream(is);
                 InputStream stream1 = new ByteArrayInputStream(byteArrayOutputStream.toByteArray())) {
                stream1.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucket);
                uploadPartRequest.setKey(obj);
                uploadPartRequest.setUploadId(multipartUploadId);
                uploadPartRequest.setInputStream(stream1);
                uploadPartRequest.setPartSize(curPartSize);
                uploadPartRequest.setPartNumber( i + 1);
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                partETags.add(uploadPartResult.getPartETag());
            }
        }
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucket, obj, multipartUploadId, partETags);
        CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        S3Object s3o = new S3Object();
        s3o.setStorageSource(StorageSourceEnum.ALIYUN);
        s3o.setBucket(completeMultipartUploadResult.getBucketName());
        s3o.setPath(completeMultipartUploadResult.getKey());
        s3o.setETag(completeMultipartUploadResult.getETag());
        s3o.setKey(completeMultipartUploadResult.getKey().split("/")[completeMultipartUploadResult.getKey().split("/").length - 1]);
        return s3o;
    }


    private static ByteArrayOutputStream cloneInputStream(InputStream input) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        return os;
    }

}
