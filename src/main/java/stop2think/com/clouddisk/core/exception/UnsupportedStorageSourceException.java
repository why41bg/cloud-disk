package stop2think.com.clouddisk.core.exception;

/**
 * @author weihongyu
 */
public class UnsupportedStorageSourceException extends CloudDiskRuntimeException{

    public UnsupportedStorageSourceException(String message) {
        super(message);
    }

    public UnsupportedStorageSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
