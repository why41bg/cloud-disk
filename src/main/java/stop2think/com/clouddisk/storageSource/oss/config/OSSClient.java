package stop2think.com.clouddisk.storageSource.oss.config;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import stop2think.com.clouddisk.storageSource.oss.constant.OssConstant;
import stop2think.com.clouddisk.storageSource.oss.model.enums.OSSErrorCodeEnum;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author weihongyu
 */
@Slf4j
public class OSSClient {

    private final OSS ossClientProxy;


    public PutObjectResult upload(PutObjectRequest request) {
        return this.ossClientProxy.putObject(request);
    }


    public OSSObject download(String bucket, String obj) {
        return this.ossClientProxy.getObject(bucket, obj);
    }

    public ObjectListing list(ListObjectsRequest request) {
        return this.ossClientProxy.listObjects(request);
    }


    private OSSClient() {
        this.ossClientProxy = initOssClient(getOssClientProperties());
        createDefaultBucketIfAbsent();
    }


    private static class OSSClientHolder {
        static OSSClient instance = new OSSClient();
    }


    public static OSSClient getInstance() {
        return OSSClientHolder.instance;
    }


    private Properties getOssClientProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/application.properties"));
        } catch (IOException e) {
            log.warn("Failed to load oss client properties.", e);
        }
        return properties;
    }


    private OSS initOssClient(Properties ossProperties) {
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(
                ossProperties.getProperty(OssConstant.ACCESS_ID_KEY),
                ossProperties.getProperty(OssConstant.ACCESS_SECRET_KEY));
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        return OSSClientBuilder.create()
                .endpoint(ossProperties.getProperty(OssConstant.ENDPOINT_KEY))
                .region(ossProperties.getProperty(OssConstant.REGION_KEY))
                .clientConfiguration(clientBuilderConfiguration)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    private void createDefaultBucketIfAbsent() {
        if (!isDefaultBucketExist()) {
            try {
                this.ossClientProxy.createBucket(OssConstant.DEFAULT_BUCKET_NAME);
                log.info("Default oss bucket created successfully: {}", OssConstant.DEFAULT_BUCKET_NAME);
            } catch (OSSException ossException) {
                String errorCode = ossException.getErrorCode();
                OSSErrorCodeEnum ossErrorCodeEnum = OSSErrorCodeEnum.getOssErrorCodeEnum(errorCode);
                log.warn("Failed to create default oss bucket: {}, error code: {}, description: {}.",
                        OssConstant.DEFAULT_BUCKET_NAME,
                        ossErrorCodeEnum.toString(),
                        ossErrorCodeEnum.getDesc(),
                        ossException);
            } catch (ClientException clientException) {
                log.warn("OSS client exception.", clientException);
            }
        }
    }

    private boolean isDefaultBucketExist() {
        return this.ossClientProxy.doesBucketExist(OssConstant.DEFAULT_BUCKET_NAME);
    }

}
