package com.dl1803.file.repository;

import com.dl1803.file.dto.FileInfo;
import com.dl1803.file.entity.FileMgmt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Repository
public class FileRepository {

    @Value("${app.file.storage-dir}")
    String storageDir;

    @Value("${app.file.download-prefix}")
    String urlPrefix;

    public FileInfo store(MultipartFile file) throws IOException {
        Path folder = Paths.get(storageDir);

        // file.getOriginalFilename() : trả về tên gốc của file (abc.jpg)
        // file.getFilenameExtension() : lấy phần mở rộng sau dấu . cuối cùng (jpg)
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = Objects.isNull(fileExtension)
                ? UUID.randomUUID().toString()
                : UUID.randomUUID() + "." + fileExtension;

        // resolve(f): nối path khác vào cuối path hiện tại
        // normalize(): chuẩn hóa đường dẫn, loại bỏ các ký tự không cần thiết như ./ hoặc ../
        // toAbsolutePath(): chuyển đổi đường dẫn thành đường dẫn tuyệt đối
        Path filePath = folder.resolve(fileName).normalize().toAbsolutePath();

        // file.getInputStream() đọc từng byte data của file vào filePath với điều kiện nếu file đã tồn tại thì ghi đè
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return FileInfo.builder()
                .name(fileName)
                .contentType(file.getContentType())
                .size(file.getSize())
                .md5Checksum(DigestUtils.md5DigestAsHex(file.getInputStream()))
                .path(filePath.toString())
                .url(urlPrefix + fileName)
                .build();
    }


    public Resource read(FileMgmt fileMgmt)
            throws IOException {
        // Path.of : chuyển chuối path thành obj đường dẫn mà hdh có thể hiểu
        var data = Files.readAllBytes(Path.of(fileMgmt.getPath())); // byte[]
        return new ByteArrayResource(data); // chuyển byte[] thành Resource
    }
}
