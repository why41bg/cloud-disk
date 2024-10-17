package stop2think.com.clouddisk.core.storage;

import stop2think.com.clouddisk.core.storage.baseService.SimpleDownload;
import stop2think.com.clouddisk.core.storage.baseService.SimpleList;
import stop2think.com.clouddisk.core.storage.baseService.SimpleUpload;
import stop2think.com.clouddisk.core.storage.multipart.MultipartUpload;

/**
 * @author weihongyu
 */
public interface StorageService extends SimpleUpload, SimpleList, SimpleDownload, MultipartUpload {
}
