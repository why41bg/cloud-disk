package stop2think.com.clouddisk.core.storage;

import stop2think.com.clouddisk.core.storage.baseService.SimpleDownload;
import stop2think.com.clouddisk.core.storage.baseService.SimpleList;
import stop2think.com.clouddisk.core.storage.baseService.SimpleUpload;

/**
 * @author weihongyu
 */
public interface SimpleStorageService extends SimpleUpload, SimpleList, SimpleDownload {
}
