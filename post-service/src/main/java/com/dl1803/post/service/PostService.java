package com.dl1803.post.service;

import com.dl1803.post.dto.request.PostRequest;
import com.dl1803.post.dto.response.PageResponse;
import com.dl1803.post.dto.response.PostResponse;
import com.dl1803.post.entity.Post;
import com.dl1803.post.mapper.PostMapper;
import com.dl1803.post.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    DateTimeFormatter dateTimeFormatter;

    public PostResponse createPost(PostRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("User ID from Token: {}", authentication != null ? authentication.getName() : "NULL");

        Post post = Post.builder()
                .content(request.getContent())
                .userId(authentication.getName())  // sub Jwt
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();

        post = postRepository.save(post);
        return  postMapper.toPostResponse(post);
    }

    public PageResponse<PostResponse> getMyPosts(int page, int size){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        // tạo cấu hình sắp xếp và phân trang để query data từ db thông qua repo
        Sort  s = Sort.by("createdDate").descending(); // yêu cầu db sort post theo attr createdDate và giảm dần
        Pageable pageable = PageRequest.of(page - 1,size, s); // tạo obj mô tả cách phân trang và sx data(trang X chứa bao nhiêu ptu, sx theo cách nào..)
        // 3 tham số : trang cần lấy(FE thường bắt đầu từ 1 mà BE Spring Data thường bắt đầu từ 0 => page - 1, số ptu mỗi trang, cách sx

        var pageData = postRepository.findAllByUserId(userId, pageable); // Page

        var postList = pageData.getContent().stream().map(post -> {
            var postResponse = postMapper.toPostResponse(post);
            postResponse.setCreated(dateTimeFormatter.format(post.getCreatedDate()));
            return postResponse;
        }).toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(postList)// getContent trả về List<Post>
                .build();
    }
}
