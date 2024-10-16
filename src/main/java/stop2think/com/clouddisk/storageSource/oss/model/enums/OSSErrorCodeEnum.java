package stop2think.com.clouddisk.storageSource.oss.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * OSS 错误码枚举类
 *
 * @author weihongyu
 */
@AllArgsConstructor
@Getter
public enum OSSErrorCodeEnum {

    AccessDenied(403, "拒绝访问"),

    BucketAlreadyExists(409, "存储空间已经存在"),

    BucketNotEmpty(409, "存储空间非空"),

    EntityTooLarge(400, "实体过大"),

    EntityTooSmall(400, "实体过小"),

    FileGroupTooLarge(400, "文件组过大"),

    FilePartNotExist(400, "文件分片不存在"),

    FilePartStale(400, "文件分片过时"),

    InvalidArgument(400, "参数格式错误"),

    InvalidAccessKeyId(403, "AccessKey ID不存在"),

    InvalidBucketName(400, "无效的存储空间名称"),

    InvalidDigest(400, "无效的摘要"),

    InvalidObjectName(400, "无效的文件名称"),

    InvalidPart(400, "无效的分片"),

    InvalidPartOrder(400, "无效的分片顺序"),

    InvalidTargetBucketForLogging(400, "Logging操作中有无效的目标存储空间"),

    InternalError(500, "OSS内部错误"),

    MalformedXML(400, "XML格式非法"),

    MethodNotAllowed(405, "不支持的方法"),

    MissingArgument(411, "缺少参数"),

    MissingContentLength(411, "缺少内容长度"),

    NoSuchBucket(404, "存储空间不存在"),

    NoSuchKey(404, "文件不存在"),

    NoSuchUpload(404, "分片上传ID不存在"),

    NotImplemented(501, "无法处理的方法"),

    PreconditionFailed(412, "预处理错误"),

    RequestTimeTooSkewed(403, "客户端本地时间和OSS服务器时间相差超过15分钟"),

    RequestTimeout(400, "请求超时"),

    SignatureDoesNotMatch(403, "签名错误"),

    InvalidEncryptionAlgorithmError(400, "指定的熵编码加密算法错误");

    private static final Map<String, OSSErrorCodeEnum> OSS_ERROR_CODE_ENUM_MAP = new HashMap<>();

    private final Integer httpCode;

    private final String desc;

    static {
        for (OSSErrorCodeEnum ecEnum : OSSErrorCodeEnum.values()) {
            OSS_ERROR_CODE_ENUM_MAP.put(ecEnum.toString(), ecEnum);
        }
    }

    public static OSSErrorCodeEnum getOssErrorCodeEnum(String errorCode) {
        return OSS_ERROR_CODE_ENUM_MAP.get(errorCode);
    }

    public static boolean isOssErrorCode(String errorCode) {
        return OSS_ERROR_CODE_ENUM_MAP.containsKey(errorCode);
    }
}
