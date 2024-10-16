package stop2think.com.clouddisk.core.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stop2think.com.clouddisk.core.model.S3Object;
import stop2think.com.clouddisk.core.model.response.ResultResponse;
import stop2think.com.clouddisk.core.storage.SimpleStorageService;
import stop2think.com.clouddisk.storageSource.StorageServiceFactory;

import java.io.*;
import java.util.List;
import java.util.Objects;

/**
 * @author weihongyu
 */
@Slf4j
@RestController()
@RequestMapping("/api")
public class CloudDiskController {

    @PostMapping("/upload/{storageSource}")
    public ResultResponse<String> simpleUploadFile(@RequestParam MultipartFile file,
                                                   @RequestParam String bucket,
                                                   @RequestParam String obj,
                                                   @PathVariable String storageSource)
            throws IOException {
        SimpleStorageService storageService = StorageServiceFactory.getSimpleStorageService(storageSource);
        if (StringUtils.isNotEmpty(bucket)) {
            storageService.simpleUploadObject(bucket, obj, file.getInputStream());
        } else {
            storageService.simpleUploadObject(obj, file.getInputStream());
        }
        return ResultResponse.success("上传文件成功", obj);
    }


    @GetMapping("/download/{storageSource}")
    public ResultResponse<Void> simpleDownloadFile(@RequestParam String bucket,
                                   @RequestParam String obj,
                                   @PathVariable String storageSource,
                                   HttpServletResponse response)
            throws IOException {
        SimpleStorageService storageService = StorageServiceFactory.getSimpleStorageService(storageSource);
        S3Object s3o;
        if (StringUtils.isEmpty(bucket)) {
            s3o = storageService.simpleDownloadObject(obj);
        } else {
            s3o = storageService.simpleDownloadObject(bucket, obj);
        }
        if (Objects.isNull(s3o)) {
            return ResultResponse.failure("下载文件失败");
        }
        response.setHeader("Content-Type", "application/octet-stream,charset=utf-8");
        response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Accept-Ranges");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + s3o.getKey());
        try (BufferedInputStream is = new BufferedInputStream(s3o.getObjectContent());
             OutputStream os = response.getOutputStream()) {
            int len;
            byte[] buffer = new byte[1024];
            while (-1 != (len = is.read(buffer, 0, buffer.length))) {
                os.write(buffer, 0, len);
                os.flush();
            }
        }
        return ResultResponse.success("下载文件成功", null);
    }


    @GetMapping("/{storageSource}")
    public ResultResponse<List<S3Object>> listObjects(@RequestParam String bucket,
                                                      @RequestParam String obj,
                                                      @RequestParam Integer maxKeys,
                                                      @PathVariable String storageSource)
    throws IOException{
        SimpleStorageService storageService = StorageServiceFactory.getSimpleStorageService(storageSource);
        List<S3Object> s3Objects;
        if (StringUtils.isEmpty(bucket)) {
            s3Objects = storageService.listObjects(obj, maxKeys);
        } else {
            s3Objects = storageService.listObjects(bucket, obj, maxKeys);
        }
        return ResultResponse.success("获取文件列表成功", s3Objects);
    }
}
