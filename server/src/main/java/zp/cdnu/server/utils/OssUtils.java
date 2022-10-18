package zp.cdnu.server.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class OssUtils {
    String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    String accessKeyId = "LTAI5tRfMdXq4ADPnxHbbqMT";
    String accessKeySecret = "egh4xBTXfCnfJGfUphdnnpR0OrN4Y2";

    public OSS getOssClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    public String uploadAvatar(MultipartFile avatar) throws IOException {
        String bucketName = "hire-avatar";
        return this.uploadFile(avatar, bucketName);
    }


    public String uploadResume(MultipartFile resume) throws IOException {
        String bucketName = "hire-resume";
        return this.uploadFile(resume, bucketName);
    }

    public void deleteAvatar(String filename) {
        if ("default.png".equals(filename)) {
            return;
        }
        OSS ossClient = getOssClient();
        ossClient.deleteObject("hire-avatar", filename);
    }

    public void deleteResume(String filename) {
        if (StringUtils.hasLength(filename)) {
            OSS ossClient = getOssClient();
            ossClient.deleteObject("hire-resume", filename);
        }
    }

    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf('.'));
        OSS ossClient = getOssClient();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getInputStream().available());
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setHeader("Pragma", "no-cache");
        objectMetadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
        objectMetadata.setContentDisposition("inline;filename=" + fileName);
        ossClient.putObject(bucketName, fileName, file.getInputStream(), objectMetadata);
        file.getInputStream().close();
        ossClient.shutdown();
        return fileName;
    }

    public String getContentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
                || filenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (filenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

}
