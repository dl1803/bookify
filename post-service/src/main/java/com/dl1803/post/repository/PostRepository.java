package com.dl1803.post.repository;

import com.dl1803.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    // tìm post với userId với cách phân trang và sắp xếp pageable
    // Page là obj chứa data và cả info phân trang (content(ds posts), size, totalElements,totalPages,...)
    Page<Post> findAllByUserId(String userId, Pageable pageable);

}
