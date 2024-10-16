package stop2think.com.clouddisk.core.exception;

/**
 * @author weihongyu
 */
public class CloudDiskRuntimeException extends RuntimeException{

    public CloudDiskRuntimeException(String message) {
        super(message);
    }

    public CloudDiskRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
