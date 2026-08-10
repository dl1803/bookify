package com.dl1803.profile.repository.httpClient;

import com.dl1803.profile.configuration.AuthenticationRequestInterceptor;
import com.dl1803.profile.dto.response.ApiResponse;
import com.dl1803.profile.dto.response.FileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "file-service", url = "${app.service.file}",
        configuration = {AuthenticationRequestInterceptor.class}
)
public interface FileClient {
    // consumes(sử dụng cho FeignClient) : báo cho Feign biết sẽ đóng gói file này thành dạng multipart/form-data để gửi request đi, nếu không Feign sẽ cố đóng gói thành Json -> văng lỗi
    // dối với Spring :  k cần thiết vì nó có thể tự động ngầm cấu hình dựa trên KDL của request
    // Đối với Feign, cần dùng @RequestPart thay vì @RequestParam để gửi file, vì Feign không hỗ trợ @RequestParam cho MultipartFile. @RequestPart sẽ giúp Feign hiểu rằng đây là một phần của multipart request và sử dụng SpringFormEncoder để encode cho request hiện tại
    @PostMapping(value = "file/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<FileResponse> uploadMedia(@RequestPart("file")MultipartFile file);
}
